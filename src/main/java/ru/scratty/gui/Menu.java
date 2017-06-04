package ru.scratty.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Класс, стартующий Menu
 * @author scratty
 */

public class Menu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = new FXMLLoader().load(getClass().getResourceAsStream("/menu.fxml"));
        primaryStage.setTitle("Меню");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
