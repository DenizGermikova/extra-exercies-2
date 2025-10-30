package repository;

import models.Book;
import models.DVD;
import models.LibraryItem;
import models.Magazine;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private Repository<Book> bookRepository;
    private Repository<Magazine> magazineRepository;
    private Repository<DVD> dvdRepository;

    public LibraryManager() {
        bookRepository = new Repository<>();
        magazineRepository = new Repository<>();
        dvdRepository = new Repository<>();
    }

    public void addItem(LibraryItem item) {
        if (item instanceof Book) {
            bookRepository.add((Book) item);
        } else if (item instanceof Magazine) {
            magazineRepository.add((Magazine) item);
        } else if (item instanceof DVD) {
            dvdRepository.add((DVD) item);
        }
    }

    public List<LibraryItem> getAllItems() {
        List<LibraryItem> allItems = new ArrayList<>();
        allItems.addAll(bookRepository.getAll());
        allItems.addAll(magazineRepository.getAll());
        allItems.addAll(dvdRepository.getAll());
        return allItems;
    }

    public List<LibraryItem> searchAll(String query) {
        List<LibraryItem> results = new ArrayList<>();
        results.addAll(bookRepository.search(query));
        results.addAll(magazineRepository.search(query));
        results.addAll(dvdRepository.search(query));
        return results;
    }

    public LibraryItem findById(String id) {
        LibraryItem item = bookRepository.findById(id);
        if (item != null) return item;
        item = magazineRepository.findById(id);
        if (item != null) return item;
        return dvdRepository.findById(id);
    }
}