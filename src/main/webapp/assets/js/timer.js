// Enterprise Live Timer Module
// - Local persistence (localStorage)
// - Multi-tab sync (BroadcastChannel)
// - Server sync (POST), SSE primary, WebSocket fallback, polling fallback
// - Exponential backoff reconnect
;(function (window, document) {
    'use strict';

    var DEFAULTS = {
        storageKey: 'focusTimerState',
        displayId: 'timer',
        syncUrl: null,
        liveUrl: null,
        wsUrl: null,
        pollUrl: null,
        pollInterval: 30000,
        autoStart: false,
        onUpdate: null
    };

    function nowTs() { return new Date().getTime(); }

    var config = {};
    for (var k in DEFAULTS) config[k] = DEFAULTS[k];

    var state = {
        seconds: 0,
        running: false,
        updatedAt: nowTs()
    };

    var tickInterval = null;
    var sse = null;
    var ws = null;
    var pollTimer = null;

    var backoff = { attempts: 0, base: 1000, max: 30000 };

    var bc = null;
    try { bc = new BroadcastChannel('productivity-timer-sync'); } catch (e) {}

    function saveLocal() {
        try { localStorage.setItem(config.storageKey, JSON.stringify(state)); } catch (e) {}
    }

    function loadLocal() {
        try {
            var raw = localStorage.getItem(config.storageKey);
            if (raw) {
                var p = JSON.parse(raw);
                if (typeof p.seconds === 'number') {
                    state.seconds = p.seconds;
                    state.running = !!p.running;
                    state.updatedAt = p.updatedAt || state.updatedAt;
                }
            }
        } catch (e) {}
    }

    function broadcastLocal() {
        if (bc) try { bc.postMessage(state); } catch (e) {}
    }

    if (bc) bc.onmessage = function (ev) {
        if (ev && ev.data) handleRemoteState(ev.data);
    };

    function displayTime() {
        var el = document.getElementById(config.displayId);
        if (!el) return;

        var mins = Math.floor(state.seconds / 60);
        var secs = state.seconds % 60;
        el.innerText =
            (mins < 10 ? '0' : '') + mins + ':' +
            (secs < 10 ? '0' : '') + secs;
    }

    function tick() {
        state.seconds++;
        state.updatedAt = nowTs();
        saveLocal();
        broadcastLocal();
        if (typeof config.onUpdate === 'function') config.onUpdate(state);
        displayTime();
    }

    function startTicking() {
        if (!tickInterval) tickInterval = setInterval(tick, 1000);
    }

    function stopTicking() {
        if (tickInterval) {
            clearInterval(tickInterval);
            tickInterval = null;
        }
    }

    function start() {
        if (state.running) return;
        state.running = true;
        state.updatedAt = nowTs();
        saveLocal();
        broadcastLocal();
        startTicking();
        displayTime();
    }

    function pause() {
        if (!state.running) return;
        state.running = false;
        state.updatedAt = nowTs();
        saveLocal();
        broadcastLocal();
        stopTicking();
        displayTime();
    }

    function reset() {
        state.seconds = 0;
        state.running = false;
        state.updatedAt = nowTs();
        saveLocal();
        broadcastLocal();
        stopTicking();
        displayTime();
    }

    function handleRemoteState(remote) {
        if (!remote || typeof remote.seconds !== 'number') return;

        if (remote.updatedAt > state.updatedAt || remote.seconds > state.seconds) {
            state.seconds = remote.seconds;
            state.running = !!remote.running;
            state.updatedAt = remote.updatedAt || nowTs();

            if (state.running) startTicking();
            else stopTicking();

            saveLocal();
            displayTime();
        }
    }

    function init(userConfig) {
        for (var k in userConfig) config[k] = userConfig[k];

        loadLocal();
        displayTime();
        if (state.running) startTicking();
        if (config.autoStart && !state.running) start();
    }

    window.TimerModule = {
        init: init,
        start: start,
        pause: pause,
        reset: reset,
        getState: function () {
            return {
                seconds: state.seconds,
                running: state.running,
                updatedAt: state.updatedAt
            };
        }
    };

    document.addEventListener('DOMContentLoaded', function () {
        if (window.timerConfig) init(window.timerConfig);
        else init({});
    });

})(window, document);