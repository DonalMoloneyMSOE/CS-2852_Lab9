/*
 *CS 2852 - 011
 *Fall 2017
 *Lab 9 - DNS Server
 *Name: Donal Moloney
 *Created: 11/1/2017
 */
package Moloneyda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Simulator extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /*
    * This method creates and shows the GUI
    * @throws IOException when encountering problems to create the GUI
    */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Lab 9 DNS Server");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600, 700));
        primaryStage.show();
    }

}