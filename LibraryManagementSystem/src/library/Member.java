package library;

public class Member {
    private String id, name, phone;
    public Member(String id, String name, String phone) {
        this.id = id; this.name = name; this.phone = phone;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
}
