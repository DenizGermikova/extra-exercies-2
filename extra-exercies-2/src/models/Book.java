package models;

public class Book extends LibraryItem {
    private String author;
    private int pages;

    public Book(String id, String title, int year, String author, int pages) {
        super(id, title, year);
        this.author = author;
        this.pages = pages;
    }

    @Override
    public String getType() {
        return "Книга";
    }

    @Override
    public String[] toTableRow() {
        return new String[] {
                id,
                getType(),
                title,
                String.valueOf(year),
                "Автор: " + author + ", Сторінок: " + pages,
                available ? "Доступна" : "Орендована",
                rentedBy != null ? rentedBy : "-"
        };
    }

    @Override
    public boolean matches(String query) {
        if (super.matches(query)) return true;
        if (query == null) return false;
        return author.toLowerCase().contains(query.toLowerCase());
    }

    public String getAuthor() { return author; }
    public int getPages() { return pages; }
}