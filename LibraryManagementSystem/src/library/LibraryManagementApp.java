package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class LibraryManagementApp extends JFrame {
    private final LibraryService service = new LibraryService();
    private final DataStore store = new DataStore();
    private final DefaultTableModel bookModel = new DefaultTableModel(
        new String[]{"ID", "Title", "Author", "Category", "Total", "Available"}, 0);
    private final DefaultTableModel memberModel = new DefaultTableModel(
        new String[]{"Member ID", "Name", "Phone"}, 0);
    private final DefaultTableModel issueModel = new DefaultTableModel(
        new String[]{"Issue ID", "Book ID", "Member ID", "Issue Date", "Due Date", "Return Date"}, 0);

    public LibraryManagementApp() {
        super("Library Management System");
        try { store.load(service); } catch (Exception e) { showError("Could not load data: " + e.getMessage()); }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 570); setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Books", createBooksPanel());
        tabs.addTab("Members", createMembersPanel());
        tabs.addTab("Issue / Return", createIssuePanel());
        add(tabs);
        refreshTables();
    }
    private JPanel createBooksPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JTable table = new JTable(bookModel);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add Book");
        JButton search = new JButton("Search");
        JButton all = new JButton("Show All");
        add.addActionListener(e -> addBook());
        search.addActionListener(e -> {
            String q = JOptionPane.showInputDialog(this, "Enter title, author, category or ID:");
            if (q != null) showBooks(service.searchBooks(q));
        });
        all.addActionListener(e -> showBooks(service.getBooks()));
        actions.add(add); actions.add(search); actions.add(all);
        p.add(actions, BorderLayout.NORTH); p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }
    private JPanel createMembersPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JButton add = new JButton("Register Member");
        add.addActionListener(e -> addMember());
        p.add(add, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(memberModel)), BorderLayout.CENTER);
        return p;
    }
    private JPanel createIssuePanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton issue = new JButton("Issue Book");
        JButton ret = new JButton("Return Book");
        issue.addActionListener(e -> issueBook());
        ret.addActionListener(e -> returnBook());
        actions.add(issue); actions.add(ret);
        p.add(actions, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(issueModel)), BorderLayout.CENTER);
        return p;
    }
    private void addBook() {
        JTextField id = new JTextField(), title = new JTextField(), author = new JTextField(),
            category = new JTextField(), copies = new JTextField("1");
        Object[] fields = {"Book ID", id, "Title", title, "Author", author,
            "Category", category, "Number of copies", copies};
        if (JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION)
            == JOptionPane.OK_OPTION) {
            try {
                int n = Integer.parseInt(copies.getText().trim());
                if (n < 1 || id.getText().trim().isEmpty() || title.getText().trim().isEmpty())
                    throw new IllegalArgumentException("Enter valid ID, title and copies (minimum 1).");
                service.addBook(new Book(id.getText().trim(), title.getText().trim(),
                    author.getText().trim(), category.getText().trim(), n, n));
                saveAndRefresh();
            } catch (Exception ex) { showError(ex.getMessage()); }
        }
    }
    private void addMember() {
        JTextField id = new JTextField(), name = new JTextField(), phone = new JTextField();
        Object[] fields = {"Member ID", id, "Name", name, "Phone", phone};
        if (JOptionPane.showConfirmDialog(this, fields, "Register Member", JOptionPane.OK_CANCEL_OPTION)
            == JOptionPane.OK_OPTION) {
            try {
                if (id.getText().trim().isEmpty() || name.getText().trim().isEmpty())
                    throw new IllegalArgumentException("Member ID and name are required.");
                service.addMember(new Member(id.getText().trim(), name.getText().trim(), phone.getText().trim()));
                saveAndRefresh();
            } catch (Exception ex) { showError(ex.getMessage()); }
        }
    }
    private void issueBook() {
        JTextField book = new JTextField(), member = new JTextField();
        Object[] fields = {"Book ID", book, "Member ID", member};
        if (JOptionPane.showConfirmDialog(this, fields, "Issue Book", JOptionPane.OK_CANCEL_OPTION)
            == JOptionPane.OK_OPTION) {
            try { service.issueBook(book.getText().trim(), member.getText().trim()); saveAndRefresh();
                JOptionPane.showMessageDialog(this, "Book issued. Standard loan period: 14 days."); }
            catch (Exception ex) { showError(ex.getMessage()); }
        }
    }
    private void returnBook() {
        String id = JOptionPane.showInputDialog(this, "Enter Issue ID:");
        if (id != null) try {
            long fine = service.returnBook(id.trim()); saveAndRefresh();
            JOptionPane.showMessageDialog(this, "Book returned. Fine: Rs. " + fine);
        } catch (Exception ex) { showError(ex.getMessage()); }
    }
    private void refreshTables() {
        showBooks(service.getBooks());
        memberModel.setRowCount(0);
        for (Member m : service.getMembers())
            memberModel.addRow(new Object[]{m.getId(), m.getName(), m.getPhone()});
        issueModel.setRowCount(0);
        for (IssueRecord i : service.getIssues())
            issueModel.addRow(new Object[]{i.getId(), i.getBookId(), i.getMemberId(),
                i.getIssueDate(), i.getDueDate(), i.getReturnDate() == null ? "Not returned" : i.getReturnDate()});
    }
    private void showBooks(Iterable<Book> books) {
        bookModel.setRowCount(0);
        for (Book b : books) bookModel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(),
            b.getCategory(), b.getTotalCopies(), b.getAvailableCopies()});
    }
    private void saveAndRefresh() {
        try { store.save(service); refreshTables(); }
        catch (IOException e) { showError("Could not save data: " + e.getMessage()); }
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Library System", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementApp().setVisible(true));
    }
}
