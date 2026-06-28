package com.productivitytracker.model;

import java.sql.Date;
import java.sql.Timestamp;

public class JournalEntry {

    private long id;
    private int userId;
    private String title;
    private String content;
    private Integer moodScore;
    private Date entryDate;
    private Timestamp createdAt;

    public JournalEntry() {}

    public JournalEntry(long id, int userId, String title, String content, Integer moodScore,
                        Date entryDate, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.moodScore = moodScore;
        this.entryDate = entryDate;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getMoodScore() {
        return moodScore;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
