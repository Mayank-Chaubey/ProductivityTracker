package com.productivitytracker.service;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.productivitytracker.dto.ReportDTO;
import com.productivitytracker.util.Logger;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Export service for report downloads.
 */
public class ExportService {

    public byte[] exportReportCsv(ReportDTO report) {
        StringBuilder csv = new StringBuilder(512);
        csv.append("Metric,Value\n");
        csv.append("Productivity,").append(report.getProductivityPercentage()).append('\n');
        csv.append("Tasks Completed,").append(report.getCompletedTasks()).append('\n');
        csv.append("Total Tasks,").append(report.getTotalTasks()).append('\n');
        csv.append("Habits Done,").append(report.getCompletedHabitsToday()).append('\n');
        csv.append("Total Habits,").append(report.getTotalHabits()).append('\n');
        csv.append("Total Time Minutes,").append(report.getTotalTimeMinutes()).append('\n');
        csv.append('\n');
        csv.append("Day,Productivity,Tasks Completed,Time Logged\n");
        for (ReportDTO.DailyReportDTO day : report.getWeeklyReports()) {
            csv.append(day.getDay()).append(',')
                    .append(day.getProductivity()).append(',')
                    .append(day.getTasksCompleted()).append(',')
                    .append(day.getTimeLogged()).append('\n');
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportReportPdf(ReportDTO report, String periodLabel) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("Productivity Report (" + periodLabel + ")",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100f);
            addSummaryCell(summaryTable, "Productivity", report.getProductivityPercentage() + "%");
            addSummaryCell(summaryTable, "Tasks", report.getCompletedTasks() + "/" + report.getTotalTasks());
            addSummaryCell(summaryTable, "Habits", report.getCompletedHabitsToday() + "/" + report.getTotalHabits());
            addSummaryCell(summaryTable, "Total Time", report.getTotalTimeMinutes() + " min");
            document.add(summaryTable);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Daily Breakdown", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));
            document.add(new Paragraph(" "));

            PdfPTable dailyTable = new PdfPTable(4);
            dailyTable.setWidthPercentage(100f);
            addHeader(dailyTable, "Day");
            addHeader(dailyTable, "Productivity");
            addHeader(dailyTable, "Tasks");
            addHeader(dailyTable, "Time");
            for (ReportDTO.DailyReportDTO day : report.getWeeklyReports()) {
                dailyTable.addCell(day.getDay());
                dailyTable.addCell(day.getProductivity() + "%");
                dailyTable.addCell(String.valueOf(day.getTasksCompleted()));
                dailyTable.addCell(day.getTimeLogged() + " min");
            }
            document.add(dailyTable);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            Logger.logError("Failed to export report PDF", ex);
            return new byte[0];
        }
    }

    private void addSummaryCell(PdfPTable table, String key, String value) {
        table.addCell(new Phrase(key, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        table.addCell(new Phrase(value));
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        table.addCell(cell);
    }
}
