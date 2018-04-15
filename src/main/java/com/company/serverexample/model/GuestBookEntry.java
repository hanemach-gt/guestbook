package com.company.serverexample.model;

import java.util.Date;

public class GuestBookEntry {

    private String authorName;
    private String message;
    private Date dateAdded;

    public GuestBookEntry(String authorName, String message, Date dateAdded) {

        this.authorName = authorName;
        this.message = message;
        this.dateAdded = dateAdded;
    }

    public GuestBookEntry(String authorName, String message) {

        this(authorName, message, new Date());
    }

    public String getAuthorName() { return this.authorName; }

    public String getMessage() { return this.message; }

    public Date getDateAdded() { return this.dateAdded; }
}
