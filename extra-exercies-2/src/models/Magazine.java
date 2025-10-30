package models;

public class Magazine extends LibraryItem {
    private int issueNumber;
    private String publisher;

    public Magazine(String id, String title, int year, int issueNumber, String publisher) {
        super(id, title, year);
        this.issueNumber = issueNumber;
        this.publisher = publisher;
    }

    @Override
    public String getType() {
        return "Журнал";
    }

    @Override
    public String[] toTableRow() {
        return new String[] {
                id,
                getType(),
                title,
                String.valueOf(year),
                "Випуск: " + issueNumber + ", Видавець: " + publisher,
                available ? "Доступний" : "Орендований",
                rentedBy != null ? rentedBy : "-"
        };
    }

    @Override
    public boolean matches(String query) {
        if (super.matches(query)) return true;
        if (query == null) return false;
        return publisher.toLowerCase().contains(query.toLowerCase());
    }

    public int getIssueNumber() { return issueNumber; }
    public String getPublisher() { return publisher; }
}