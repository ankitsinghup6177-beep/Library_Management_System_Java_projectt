package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IssueRecord {
    private String id, bookId, memberId;
    private LocalDate issueDate, dueDate, returnDate;

    public IssueRecord(String id, String bookId, String memberId,
                       LocalDate issueDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id; this.bookId = bookId; this.memberId = memberId;
        this.issueDate = issueDate; this.dueDate = dueDate; this.returnDate = returnDate;
    }
    public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return returnDate != null; }
    public long calculateFine(long finePerDay) {
        LocalDate end = returnDate == null ? LocalDate.now() : returnDate;
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(dueDate, end));
        return lateDays * finePerDay;
    }
    public void markReturned() { returnDate = LocalDate.now(); }
}
