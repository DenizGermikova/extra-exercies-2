package app;

import models.Book;
import models.DVD;
import models.LibraryItem;
import models.Magazine;
import repository.LibraryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagementApp extends JFrame {
    private LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterCombo;

    public LibraryManagementApp() {
        manager = new LibraryManager();
        initializeTestData();
        setupUI();
    }

    private void initializeTestData() {
        manager.addItem(new Book("B001", "Війна і мир", 1869, "Лев Толстой", 1225));
        manager.addItem(new Book("B002", "1984", 1949, "Джордж Орвелл", 328));
        manager.addItem(new Book("B003", "Майстер і Маргарита", 1967, "Михайло Булгаков", 470));

        manager.addItem(new Magazine("M001", "National Geographic", 2023, 145, "NG Society"));
        manager.addItem(new Magazine("M002", "Forbes", 2024, 102, "Forbes Media"));

        manager.addItem(new DVD("D001", "Матриця", 1999, "Вачовські", 136));
        manager.addItem(new DVD("D002", "Інтерстеллар", 2014, "Крістофер Нолан", 169));
    }

    private void setupUI() {
        setTitle("Система управління бібліотекою");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Пошук:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton searchBtn = new JButton("Шукати");
        topPanel.add(searchBtn);

        JButton clearSearchBtn = new JButton("Очистити");
        topPanel.add(clearSearchBtn);

        topPanel.add(new JLabel("Фільтр:"));
        filterCombo = new JComboBox<>(new String[]{"Усі", "Книги", "Журнали", "DVD", "Доступні", "Орендовані"});
        topPanel.add(filterCombo);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Тип", "Назва", "Рік", "Додатково", "Статус", "Орендовано"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton rentBtn = new JButton("Орендувати");
        JButton returnBtn = new JButton("Повернути");
        JButton addBtn = new JButton("Додати елемент");
        JButton refreshBtn = new JButton("Оновити");
        JButton statsBtn = new JButton("Статистика");

        bottomPanel.add(rentBtn);
        bottomPanel.add(returnBtn);
        bottomPanel.add(addBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(statsBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Обробники подій
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                refreshTable();
            } else {
                List<LibraryItem> results = manager.searchAll(query);
                filterTable(results);
            }
        });

        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            filterCombo.setSelectedIndex(0);
            refreshTable();
        });

        rentBtn.addActionListener(e -> rentSelectedItem());
        returnBtn.addActionListener(e -> returnSelectedItem());
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            filterCombo.setSelectedIndex(0);
            refreshTable();
        });
        addBtn.addActionListener(e -> showAddDialog());
        statsBtn.addActionListener(e -> showStatistics());
        searchField.addActionListener(e -> searchBtn.doClick());

        filterCombo.addActionListener(e -> {
            String selected = (String) filterCombo.getSelectedItem();
            List<LibraryItem> filtered = manager.getAllItems();
            List<LibraryItem> result = new ArrayList<>();

            if (selected.equals("Книги")) {
                for (LibraryItem item : filtered) if (item instanceof Book) result.add(item);
            } else if (selected.equals("Журнали")) {
                for (LibraryItem item : filtered) if (item instanceof Magazine) result.add(item);
            } else if (selected.equals("DVD")) {
                for (LibraryItem item : filtered) if (item instanceof DVD) result.add(item);
            } else if (selected.equals("Доступні")) {
                for (LibraryItem item : filtered) if (item.isAvailable()) result.add(item);
            } else if (selected.equals("Орендовані")) {
                for (LibraryItem item : filtered) if (!item.isAvailable()) result.add(item);
            } else {
                result = filtered;
            }
            filterTable(result);
        });

        refreshTable();
    }

    private void refreshTable() {
        List<LibraryItem> items = manager.getAllItems();
        filterTable(items);
    }

    private void filterTable(List<LibraryItem> items) {
        tableModel.setRowCount(0);
        for (LibraryItem item : items) {
            tableModel.addRow(item.toTableRow());
        }
    }

    private void rentSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Будь ласка, виберіть елемент для оренди", "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        LibraryItem item = manager.findById(id);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Елемент не знайдено!", "Помилка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!item.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Цей елемент вже орендовано користувачем: " + item.getRentedBy());
            return;
        }

        String userName = JOptionPane.showInputDialog(this, "Введіть ім'я користувача:", "Оренда елемента", JOptionPane.QUESTION_MESSAGE);
        if (userName != null && !userName.trim().isEmpty()) {
            if (item.rent(userName.trim())) {
                JOptionPane.showMessageDialog(this, "Елемент успішно орендовано!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Не вдалося орендувати елемент", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void returnSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Будь ласка, виберіть елемент для повернення", "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        LibraryItem item = manager.findById(id);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Елемент не знайдено!", "Помилка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (item.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Цей елемент не орендовано.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Повернути елемент, орендований користувачем: " + item.getRentedBy() + "?", "Підтвердження повернення", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (item.returnItem()) {
                JOptionPane.showMessageDialog(this, "Елемент успішно повернено!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Не вдалося повернути елемент", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // showAddDialog() та showStatistics() — ті ж, що були в твоєму шаблоні (залишені без змін)
    private void showAddDialog() {
        String[] types = {"Книга", "Журнал", "DVD"};
        String selectedType = (String) JOptionPane.showInputDialog(this,
                "Виберіть тип елемента:",
                "Додавання елемента",
                JOptionPane.QUESTION_MESSAGE,
                null,
                types,
                types[0]);

        if (selectedType == null) return;

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField yearField = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Назва:"));
        panel.add(titleField);
        panel.add(new JLabel("Рік:"));
        panel.add(yearField);

        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();

        switch (selectedType) {
            case "Книга":
                panel.add(new JLabel("Автор:"));
                panel.add(field1);
                panel.add(new JLabel("Кількість сторінок:"));
                panel.add(field2);
                break;
            case "Журнал":
                panel.add(new JLabel("Номер випуску:"));
                panel.add(field1);
                panel.add(new JLabel("Видавець:"));
                panel.add(field2);
                break;
            case "DVD":
                panel.add(new JLabel("Режисер:"));
                panel.add(field1);
                panel.add(new JLabel("Тривалість (хв):"));
                panel.add(field2);
                break;
        }

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Введіть дані елемента",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());

                if (id.isEmpty() || title.isEmpty()) {
                    throw new IllegalArgumentException("ID та назва обов'язкові");
                }

                LibraryItem newItem = null;

                switch (selectedType) {
                    case "Книга":
                        String author = field1.getText().trim();
                        int pages = Integer.parseInt(field2.getText().trim());
                        newItem = new Book(id, title, year, author, pages);
                        break;
                    case "Журнал":
                        int issueNumber = Integer.parseInt(field1.getText().trim());
                        String publisher = field2.getText().trim();
                        newItem = new Magazine(id, title, year, issueNumber, publisher);
                        break;
                    case "DVD":
                        String director = field1.getText().trim();
                        int duration = Integer.parseInt(field2.getText().trim());
                        newItem = new DVD(id, title, year, director, duration);
                        break;
                }

                manager.addItem(newItem);
                refreshTable();
                JOptionPane.showMessageDialog(this,
                        "Елемент успішно додано!",
                        "Успіх",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Невірний формат числових даних",
                        "Помилка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Помилка: " + ex.getMessage(),
                        "Помилка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showStatistics() {
        List<LibraryItem> allItems = manager.getAllItems();

        int totalCount = allItems.size();

        int bookCount = 0, magazineCount = 0, dvdCount = 0;
        for (LibraryItem item : allItems) {
            if (item instanceof Book) bookCount++;
            else if (item instanceof Magazine) magazineCount++;
            else if (item instanceof DVD) dvdCount++;
        }

        int rentedCount = 0;
        for (LibraryItem item : allItems) {
            if (!item.isAvailable()) rentedCount++;
        }
        double rentedPercentage = totalCount > 0 ? (rentedCount * 100.0 / totalCount) : 0;

        java.util.Map<Integer, Integer> yearFrequency = new java.util.HashMap<>();
        for (LibraryItem item : allItems) {
            int year = item.getYear();
            yearFrequency.put(year, yearFrequency.getOrDefault(year, 0) + 1);
        }

        int mostPopularYear = 0;
        int maxCount = 0;
        for (java.util.Map.Entry<Integer, Integer> entry : yearFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPopularYear = entry.getKey();
            }
        }

        StringBuilder stats = new StringBuilder();
        stats.append("=== СТАТИСТИКА БІБЛІОТЕКИ ===\n\n");
        stats.append("Всього елементів: ").append(totalCount).append("\n\n");
        stats.append("За типами:\n");
        stats.append("  - Книга: ").append(bookCount).append("\n");
        stats.append("  - Журнал: ").append(magazineCount).append("\n");
        stats.append("  - DVD: ").append(dvdCount).append("\n\n");
        stats.append("Орендовано: ").append(rentedCount)
                .append(" (").append(String.format("%.1f", rentedPercentage)).append("%)\n");
        stats.append("Доступно: ").append(totalCount - rentedCount).append("\n\n");

        if (maxCount > 0) {
            stats.append("Найпопулярніший рік видання: ")
                    .append(mostPopularYear)
                    .append(" (").append(maxCount).append(" елементів)");
        }

        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Статистика бібліотеки",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LibraryManagementApp app = new LibraryManagementApp();
            app.setLocationRelativeTo(null);
            app.setVisible(true);
        });
    }
}
