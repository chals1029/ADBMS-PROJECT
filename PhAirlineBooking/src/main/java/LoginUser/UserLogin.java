package LoginUser;

public class UserLogin {

    private String membershipId, email, firstName, middleName, lastName, phoneNumber, password;
    private int points;
    private static UserLogin currentUser = null;

    // Default constructor
    public UserLogin() {}

    // Constructor with parameters
    public UserLogin(String membershipId, String email, String firstName, String middleName, String lastName, String phoneNumber, String password, int points) {
        this.membershipId = membershipId;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.points = points;
    }

    // Getters
    public String getMembershipId() { return membershipId; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }
    public int getPoints() { return points; }

    // Setters
    public void setMembershipId(String membershipId) { this.membershipId = membershipId; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPassword(String password) { this.password = password; }
    public void setPoints(int points) { this.points = points; }

    // Get the current logged-in user
    public static UserLogin getCurrentUser() {
        return currentUser;
    }

    // Set the current logged-in user
    public static void setCurrentUser(UserLogin user) {
        currentUser = user;
    }

    // Get logged-in user's email
    public static String getLoggedInEmail() {
        return (currentUser != null) ? currentUser.getEmail() : null;
    }

    // You can add more methods as needed for validation or other functionality
}
