package client.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class PlayerGUImplementation {
    private String username;
    private ObservableList<EntrySheetGUImplementation> entrySheet;

    private ListView<EntrySheetGUImplementation> entrySheetListView;

    /*public PlayerGUImplementation (String username) {
        this.username = username;
        entrySheet = GameWindowHelper.makeEntrySheet();
    }*/

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


    public ListView<EntrySheetGUImplementation> getEntrySheetListView() {
        return entrySheetListView;
    }

    public void setEntrySheetListView(ListView<EntrySheetGUImplementation> entrySheetListView) {
        this.entrySheetListView = entrySheetListView;
    }
}
