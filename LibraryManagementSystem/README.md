# Library Management System (Java)

A desktop mini-project built with Java Swing. It supports book management, member registration,
book search, issue/return transactions, 14-day loan periods, and overdue fine calculation.

## Requirements
- JDK 8 or later
- No external libraries or database installation required

## Run
Open a terminal in the project folder and run:

```bash
javac -d out src/library/*.java
java -cp out library.LibraryManagementApp
```

The application creates a `data` folder automatically. It stores data in:
- `data/books.csv`
- `data/members.csv`
- `data/issues.csv`

Keep the `data` folder with the application between runs.

## Demo workflow
1. Open **Books** and add a book (e.g. B101, Java Basics, Herbert Schildt, Programming, 3).
2. Open **Members** and register a member (e.g. M101, Rahul, 9876543210).
3. Open **Issue / Return**, issue book B101 to member M101.
4. Note the generated Issue ID (for example ISS1).
5. Return using that Issue ID. Fine is calculated at Rs. 2 per day after the due date.

## Main concepts demonstrated
- Classes and objects: Book, Member, IssueRecord
- Encapsulation: private fields and public getters/setters
- Collections: LinkedHashMap
- Java 8 streams: book search
- Exception handling and validation
- Date API: LocalDate and ChronoUnit
- GUI: Swing, JFrame, JTabbedPane, JTable, JOptionPane
- File handling: CSV persistence

## Limitations / possible enhancements
This is an educational single-user prototype. CSV storage is suitable for a small demonstration,
not concurrent multi-user deployment. A production system should use a relational database
such as MySQL/SQLite with transactions, authentication, backups, and stronger input handling.
