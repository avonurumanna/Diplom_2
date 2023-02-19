package pojo;

public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials(String login, String password) {
        this.email = login;
        this.password = password;
    }


    public UserCredentials() {
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
