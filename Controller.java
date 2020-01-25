package sample;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.regex.Pattern;



public class Controller extends Main {

    @FXML
    public VBox vBox;

    @FXML
    public Button btnDirectoryChooser;

    @FXML
    public Label lblCopyright;

    @FXML
    public Button btnRename;

    @FXML
    public Button btnGithub;

    @FXML
    public Label lblDirectory;

    @FXML
    MenuItem menuBar;

    @FXML
    MenuItem menuBarMax;

    @FXML
    JFXComboBox<String> cBoxFileType;

    private String now = LocalDate.now().toString().replaceAll("-","");
    private Stage primaryStage;
    private boolean directoryChosen;
    private static final Pattern INVALID_CHARS_PATTERN =
            Pattern.compile("[^~#@*+%{}<>\\[\\]|/\"\\_^]*");
    private String[] fileTypes = {".txt",".png",".jpg",".jpeg",".gif"};


    public void initialize() {
        setPrimaryStage(primaryStage);
        addItemsToList();
        cBoxFileType.setValue(".txt");
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void closeApp() {
        Platform.exit();
    }

    private void addItemsToList(){
        for (String fileType : fileTypes) {
            cBoxFileType.getItems().add(fileType);
        }
    }

    public void maximize(){
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.setMaximized(true);
    }

    public void minimize(){
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.setMaximized(false);
    }

    public void browse() throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("https://github.com/Yannick-Driessens"));
        }
    }

    public void setDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        directoryChosen = true;

        if (selectedDirectory == null) {
            JOptionPane.showMessageDialog(null, "Je hebt geen directory gekozen!");
            directoryChosen = false;
        } else {
            lblDirectory.setText("Gekozen directory: " + selectedDirectory.getAbsolutePath());
        }
    }

    private boolean notContainsIllegals(String toExamine) {
        return INVALID_CHARS_PATTERN.matcher(toExamine).matches(); // returns a false value
    }

    @FXML
    private void renameFiles() throws Exception {
        if (directoryChosen) {
            String fileName = JOptionPane.showInputDialog("Voer de naam in van het bestand (Zonder leading zero's): ");

            int totalFiles = 0;
            int i = 0; // teller

            try {
                String path = lblDirectory.getText().substring(lblDirectory.getText().indexOf(':') + 2);
                if (!notContainsIllegals(fileName)) {
                    throw new Exception("Illegaal character is gebruikt. Controleer log file. ");
                }
                File dir = new File(path);
                File[] filesInDirectory = dir.listFiles();

                assert filesInDirectory != null;
                for (File file : filesInDirectory) {
                    if(file.toString().contains(cBoxFileType.getValue())) {
                        i++;
                        totalFiles++;
                        String newName = String.format("%s %03d", fileName, i) + cBoxFileType.getValue();
                        String newPath = path + "\\" + newName;
                        file.renameTo(new File(newPath));
                    }
                }

            } catch (Exception e) {
                File f = new File("error" + now + ".txt");
                if (!f.exists())
                    f.createNewFile();
                e.printStackTrace(new PrintStream(f));
                Thread.currentThread().interrupt();
                JOptionPane.showMessageDialog(null,e);
            }
            JOptionPane.showMessageDialog(null, "Aantal files hernoemd: " + totalFiles);
        } else {
            JOptionPane.showMessageDialog(null, "Je hebt geen directory gekozen. Kies een directory!");
        }
    }
}
