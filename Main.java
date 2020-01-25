package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Rename files");
        primaryStage.setScene(new Scene(root, 750, 900));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.ico")));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(true);
        primaryStage.show();
        root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
