package repository;

import models.LibraryItem;

import java.util.ArrayList;
import java.util.List;

public class Repository<T extends LibraryItem> {
    private List<T> items;

    public Repository() {
        this.items = new ArrayList<>();
    }

    public void add(T item) {
        items.add(item);
    }

    public boolean remove(String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public T findById(String id) {
        for (T item : items) {
            if (item.getId().equals(id)) return item;
        }
        return null;
    }

    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    public List<T> search(String query) {
        List<T> results = new ArrayList<>();
        for (T item : items) {
            if (item.matches(query)) results.add(item);
        }
        return results;
    }
}
