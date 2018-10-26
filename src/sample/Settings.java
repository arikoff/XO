package sample;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public final class Settings {

    private static Settings instance;
    public int size;

    @FXML
    private Button buttonSaveSettings;

    @FXML
    private TextField tfSize;

    @FXML
    private void SaveSettings(){
        if (instance == null) {
            instance = new Settings(Integer.parseInt(tfSize.getText()));
        }
        else {
            instance.size = Integer.parseInt(tfSize.getText());
        }
        Stage stage = (Stage) buttonSaveSettings.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onKeyTyped(KeyEvent k){
        String newValue = tfSize.getText();
        if (!newValue.matches("\\d*")) {
            tfSize.setText(newValue.replaceAll("[^\\d]", ""));
        }
    }

    public Settings() {
        this.size = 13;
    }

    private Settings(int size) {
        this.size = size;
    }

    public static Settings getInstance(){
        if (instance == null) {
            instance = new Settings(13);
        }
        return instance;
    }
}
