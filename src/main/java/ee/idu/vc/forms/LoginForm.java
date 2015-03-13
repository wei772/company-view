package ee.idu.vc.forms;

public class LoginForm {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LoginForm{username='" + username + '\'' + ", password=***}";
    }
}