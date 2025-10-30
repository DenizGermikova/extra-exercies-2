package models;

public class DVD extends LibraryItem {
    private String director;
    private int duration; // in minutes

    public DVD(String id, String title, int year, String director, int duration) {
        super(id, title, year);
        this.director = director;
        this.duration = duration;
    }

    @Override
    public String getType() {
        return "DVD";
    }

    @Override
    public String[] toTableRow() {
        return new String[] {
                id,
                getType(),
                title,
                String.valueOf(year),
                "Режисер: " + director + ", Тривалість: " + duration + " хв",
                available ? "Доступний" : "Орендований",
                rentedBy != null ? rentedBy : "-"
        };
    }

    @Override
    public boolean matches(String query) {
        if (super.matches(query)) return true;
        if (query == null) return false;
        return director.toLowerCase().contains(query.toLowerCase());
    }

    public String getDirector() { return director; }
    public int getDuration() { return duration; }
}
