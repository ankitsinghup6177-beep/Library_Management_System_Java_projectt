package library;

public class Book {
    private String id, title, author, category;
    private int totalCopies, availableCopies;

    public Book(String id, String title, String author, String category,
                int totalCopies, int availableCopies) {
        this.id = id; this.title = title; this.author = author;
        this.category = category; this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public void setTitle(String v) { title = v; }
    public void setAuthor(String v) { author = v; }
    public void setCategory(String v) { category = v; }
    public void setTotalCopies(int v) { totalCopies = v; }
    public void setAvailableCopies(int v) { availableCopies = v; }
}
