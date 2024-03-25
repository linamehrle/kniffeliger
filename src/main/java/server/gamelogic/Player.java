package server.gamelogic;

public class Player {
    private String username;
    private int id;

    public Player(String username, int id){
        this.username = username;
        this.id = id;
    }

    /**
     * Method to access private field username;
     *
     * @return name of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method to access private field id;
     *
     * @return id of user
     */
    public int getId() {
        return id;
    }
}
