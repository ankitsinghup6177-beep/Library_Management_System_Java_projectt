package library;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;

public class DataStore {
    private final Path folder = Paths.get("data");
    private final Path booksFile = folder.resolve("books.csv");
    private final Path membersFile = folder.resolve("members.csv");
    private final Path issuesFile = folder.resolve("issues.csv");

    public void load(LibraryService service) throws IOException {
        Files.createDirectories(folder);
        if (Files.exists(booksFile)) {
            for (String line : Files.readAllLines(booksFile, StandardCharsets.UTF_8)) {
                String[] x = line.split(",", -1);
                if (x.length == 6) service.loadBook(new Book(x[0], x[1], x[2], x[3],
                    Integer.parseInt(x[4]), Integer.parseInt(x[5])));
            }
        }
        if (Files.exists(membersFile)) {
            for (String line : Files.readAllLines(membersFile, StandardCharsets.UTF_8)) {
                String[] x = line.split(",", -1);
                if (x.length == 3) service.loadMember(new Member(x[0], x[1], x[2]));
            }
        }
        if (Files.exists(issuesFile)) {
            for (String line : Files.readAllLines(issuesFile, StandardCharsets.UTF_8)) {
                String[] x = line.split(",", -1);
                if (x.length == 6) service.loadIssue(new IssueRecord(x[0], x[1], x[2],
                    LocalDate.parse(x[3]), LocalDate.parse(x[4]),
                    x[5].isEmpty() ? null : LocalDate.parse(x[5])));
            }
        }
    }
    public void save(LibraryService service) throws IOException {
        Files.createDirectories(folder);
        try (BufferedWriter w = Files.newBufferedWriter(booksFile, StandardCharsets.UTF_8)) {
            for (Book b : service.getBooks())
                w.write(String.join(",", clean(b.getId()), clean(b.getTitle()), clean(b.getAuthor()),
                    clean(b.getCategory()), String.valueOf(b.getTotalCopies()),
                    String.valueOf(b.getAvailableCopies())) + "\n");
        }
        try (BufferedWriter w = Files.newBufferedWriter(membersFile, StandardCharsets.UTF_8)) {
            for (Member m : service.getMembers())
                w.write(String.join(",", clean(m.getId()), clean(m.getName()), clean(m.getPhone())) + "\n");
        }
        try (BufferedWriter w = Files.newBufferedWriter(issuesFile, StandardCharsets.UTF_8)) {
            for (IssueRecord i : service.getIssues())
                w.write(String.join(",", clean(i.getId()), clean(i.getBookId()), clean(i.getMemberId()),
                    i.getIssueDate().toString(), i.getDueDate().toString(),
                    i.getReturnDate() == null ? "" : i.getReturnDate().toString()) + "\n");
        }
    }
    private String clean(String s) { return s.replace(",", " ").replace("\n", " ").trim(); }
}
