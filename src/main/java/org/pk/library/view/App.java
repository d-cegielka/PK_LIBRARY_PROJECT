package org.pk.library.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX Library App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Settings appSettings = Settings.getInstance();
        Scene scene = new Scene(loadFXML());
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        stage.setScene(scene);
        stage.setTitle("Biblioteka");
        try {
            stage.getIcons().add(new Image(appSettings.getIconPath()));
            stage.setTitle(appSettings.getAppTitle());
        } catch (Exception e) {
            stage.getIcons().add(new Image(String.valueOf(App.class.getResource("icon.png"))));
            stage.setTitle("Biblioteka");
        }
        stage.show();
    }

    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main" + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args){
        if(args.length > 0 && args[0].equals("no-gui")) {
            TUI tui = new TUI();
        } else{
            launch();
        }
    }

}