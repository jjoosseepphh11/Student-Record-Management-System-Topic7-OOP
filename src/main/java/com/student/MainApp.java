package com.student;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // The FXML file lives in src/main/resources, so it must be loaded from the classpath root.
        URL fxml = getClass().getResource("/main.fxml");
        if (fxml == null) {
            throw new IllegalStateException("Cannot find /main.fxml on the classpath.");
        }

        FXMLLoader loader = new FXMLLoader(fxml);
        Scene scene = new Scene(loader.load());

        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
