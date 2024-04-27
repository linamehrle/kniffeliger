package client.gui;

import javafx.collections.ObservableList;

public class PlayerGUImplementation {
    private String username;
    private ObservableList<EntrySheetGUImplementation> entrySheet;

    public PlayerGUImplementation (String username) {
        this.username = username;
        entrySheet = GameWindowHelper.makeEntrySheet();
    }

    public ObservableList<EntrySheetGUImplementation> getEntrySheet() {
        return entrySheet;
    }

    public void setEntrySheet(ObservableList<EntrySheetGUImplementation> entrySheet) {
        this.entrySheet = entrySheet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
