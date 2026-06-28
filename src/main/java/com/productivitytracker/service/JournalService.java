package com.productivitytracker.service;

import com.productivitytracker.dao.JournalDAO;
import com.productivitytracker.model.JournalEntry;
import com.productivitytracker.util.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class JournalService {

    private final JournalDAO journalDAO = new JournalDAO();

    public boolean addEntry(int userId, String title, String content, String moodScore, String entryDate) {
        try {
            if (userId <= 0 || title == null || title.isBlank() || content == null || content.isBlank()) {
                return false;
            }
            Integer parsedMood = parseMood(moodScore);
            Date parsedDate = entryDate == null || entryDate.isBlank()
                    ? Date.valueOf(LocalDate.now())
                    : Date.valueOf(LocalDate.parse(entryDate));
            return journalDAO.addEntry(userId, limit(title.trim(), 160), content.trim(), parsedMood, parsedDate);
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            Logger.logError("Invalid journal entry input", ex);
            return false;
        }
    }

    public List<JournalEntry> getEntries(int userId, String query) {
        if (userId <= 0) {
            return List.of();
        }
        return journalDAO.getEntries(userId, query);
    }

    public boolean deleteEntry(long entryId, int userId) {
        return entryId > 0 && userId > 0 && journalDAO.deleteEntry(entryId, userId);
    }

    private Integer parseMood(String moodScore) {
        if (moodScore == null || moodScore.isBlank()) {
            return null;
        }
        int mood = Integer.parseInt(moodScore);
        if (mood < 1 || mood > 5) {
            throw new IllegalArgumentException("Mood must be between 1 and 5");
        }
        return mood;
    }

    private String limit(String value, int maxLength) {
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
