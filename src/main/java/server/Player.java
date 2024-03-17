package server;

public class Player {

    private static int counter = 0;

    private int id;

    private String username;

    public Player() {
        counter++;
        this.id = counter;
        this.username = "user_" + id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
