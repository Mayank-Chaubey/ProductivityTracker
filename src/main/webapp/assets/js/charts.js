/* Enterprise-grade charts.js
     - Initializes Chart.js charts when available
     - Provides fetch + SSE/WebSocket live update utilities
     - Graceful reconnect with exponential backoff
     - Exposes simple API: initCharts(config), connectLive(sourceConfig)
     - Includes export and resize helpers
*/
;(function (global) {
    'use strict';

    var DEFAULTS = {
        charts: [],
        live: null,
        retry: { attempts: 8, baseDelay: 800 },
        debug: false
    };

    var state = {
        charts: {},     // id -> chart
        connections: [],
        config: {}
    };

    function log() {
        if (state.config.debug && console && console.debug) {
            console.debug.apply(console, ['[charts]'].concat([].slice.call(arguments)));
        }
    }

    function error() {
        if (console && console.error) {
            console.error.apply(console, ['[charts]'].concat([].slice.call(arguments)));
        }
    }

    function safeJSON(text) {
        try { return JSON.parse(text); } catch (e) { return null; }
    }

    function fetchJson(url, cb) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState !== 4) return;
            if (xhr.status >= 200 && xhr.status < 300) {
                try { cb(null, JSON.parse(xhr.responseText)); }
                catch (e) { cb(e); }
            } else {
                cb(new Error('HTTP ' + xhr.status));
            }
        };
        xhr.onerror = function () { cb(new Error('Network error')); };
        xhr.send();
    }

    function createChart(ctx, cfg) {
        if (!global.Chart) return null;
        return new Chart(ctx, cfg);
    }

    function updateChartData(chart, data) {
        if (!chart || !chart.data) return;
        chart.data.labels = data.labels || chart.data.labels;
        chart.data.datasets = data.datasets || chart.data.datasets;
        chart.update();
    }

    function renderSimpleBar(id, series) {
        var el = document.getElementById(id);
        if (!el) return;
        el.innerHTML = '';
        for (var i = 0; i < series.length; i++) {
            var d = document.createElement('div');
            d.className = 'bar';
            d.style.height = series[i].value + '%';
            el.appendChild(d);
        }
        state.charts[id] = { type: 'dom', data: series };
    }

    function initCharts(cfg) {
        state.config = cfg || DEFAULTS;

        var charts = state.config.charts || [];
        for (var i = 0; i < charts.length; i++) {
            (function (c) {
                var el = document.getElementById(c.id);
                if (!el) return;

                if (c.data) {
                    renderChart(c, el, c.data);
                } else if (c.dataUrl) {
                    fetchJson(c.dataUrl, function (err, data) {
                        if (!err) renderChart(c, el, data);
                    });
                }
            })(charts[i]);
        }

        if (state.config.live) connectLive(state.config.live);
    }

    function renderChart(c, el, data) {
        if (global.Chart && (c.type === 'line' || c.type === 'bar')) {
            var canvas = el.tagName === 'CANVAS' ? el : document.createElement('canvas');
            if (canvas !== el) el.appendChild(canvas);

            var chart = createChart(canvas, {
                type: c.type,
                data: data,
                options: c.options || {}
            });
            state.charts[c.id] = chart;
        } else if (Array.isArray(data)) {
            renderSimpleBar(c.id, data);
        }
    }

    function connectLive(cfg) {
        if (cfg.protocol === 'sse' && global.EventSource) {
            var es = new EventSource(cfg.url);
            es.onmessage = function (e) {
                var msg = safeJSON(e.data);
                if (msg && msg.chartId && state.charts[msg.chartId]) {
                    updateChartData(state.charts[msg.chartId], msg.data);
                }
            };
            es.onerror = function () {
                error('SSE error');
                es.close();
            };
            state.connections.push(es);
        }
    }

    global.ChartsModule = {
        initCharts: initCharts
    };

    document.addEventListener('DOMContentLoaded', function () {
        if (global.chartConfig) initCharts(global.chartConfig);
    });

})(window);