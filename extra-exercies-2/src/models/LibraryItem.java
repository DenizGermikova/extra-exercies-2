package models;

import interfaces.Displayable;
import interfaces.Rentable;
import interfaces.Searchable;

import java.time.LocalDate;

public abstract class LibraryItem implements Searchable, Rentable, Displayable {
    protected String id;
    protected String title;
    protected int year;
    protected boolean available;
    protected String rentedBy;
    protected LocalDate rentDate;

    public LibraryItem(String id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.available = true;
        this.rentedBy = null;
        this.rentDate = null;
    }

    @Override
    public boolean rent(String userName) {
        if (!available) return false;
        available = false;
        rentedBy = userName;
        rentDate = LocalDate.now();
        return true;
    }

    @Override
    public boolean returnItem() {
        if (available) return false;
        available = true;
        rentedBy = null;
        rentDate = null;
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public boolean matches(String query) {
        if (query == null) return false;
        String lowerQuery = query.toLowerCase();
        return id.toLowerCase().contains(lowerQuery) || title.toLowerCase().contains(lowerQuery);
    }

    public abstract String getType();

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getRentedBy() { return rentedBy; }
    public LocalDate getRentDate() { return rentDate; }
}
