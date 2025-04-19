package Admin;

public class AdminUser {
    private final int adminId;
    private final String username;

    public AdminUser(int adminId, String username) {
        if (adminId < 0) {
            throw new IllegalArgumentException("Admin ID cannot be negative");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.adminId = adminId;
        this.username = username.trim();
    }

    public int getAdminId() {
        return adminId;
    }

    public String getUsername() {
        return username;
    }
}