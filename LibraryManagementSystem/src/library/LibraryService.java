package library;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final Map<String, IssueRecord> issues = new LinkedHashMap<>();
    private static final long FINE_PER_DAY = 2;

    public Collection<Book> getBooks() { return books.values(); }
    public Collection<Member> getMembers() { return members.values(); }
    public Collection<IssueRecord> getIssues() { return issues.values(); }

    public void addBook(Book b) {
        if (books.containsKey(b.getId())) throw new IllegalArgumentException("Book ID already exists.");
        books.put(b.getId(), b);
    }
    public void addMember(Member m) {
        if (members.containsKey(m.getId())) throw new IllegalArgumentException("Member ID already exists.");
        members.put(m.getId(), m);
    }
    public List<Book> searchBooks(String query) {
        String q = query.toLowerCase();
        return books.values().stream().filter(b ->
            b.getId().toLowerCase().contains(q) ||
            b.getTitle().toLowerCase().contains(q) ||
            b.getAuthor().toLowerCase().contains(q) ||
            b.getCategory().toLowerCase().contains(q)
        ).collect(Collectors.toList());
    }
    public void issueBook(String bookId, String memberId) {
        Book b = books.get(bookId);
        if (b == null) throw new IllegalArgumentException("Book not found.");
        if (!members.containsKey(memberId)) throw new IllegalArgumentException("Member not found.");
        if (b.getAvailableCopies() < 1) throw new IllegalArgumentException("No available copy.");
        b.setAvailableCopies(b.getAvailableCopies() - 1);
        String id = "ISS" + (issues.size() + 1);
        LocalDate today = LocalDate.now();
        issues.put(id, new IssueRecord(id, bookId, memberId, today, today.plusDays(14), null));
    }
    public long returnBook(String issueId) {
        IssueRecord r = issues.get(issueId);
        if (r == null) throw new IllegalArgumentException("Issue record not found.");
        if (r.isReturned()) throw new IllegalArgumentException("This book has already been returned.");
        r.markReturned();
        Book b = books.get(r.getBookId());
        if (b != null) b.setAvailableCopies(Math.min(b.getTotalCopies(), b.getAvailableCopies() + 1));
        return r.calculateFine(FINE_PER_DAY);
    }
    public long getFinePerDay() { return FINE_PER_DAY; }

    void loadBook(Book b) { books.put(b.getId(), b); }
    void loadMember(Member m) { members.put(m.getId(), m); }
    void loadIssue(IssueRecord i) { issues.put(i.getId(), i); }
}
