package client_java.model.admin;

public class CreatePlayerModel {
    private String username;
    private String password;

    public CreatePlayerModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty();
    }
}
