package home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import marking.assistant.database.DatabaseHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/home/view/main.fxml"));
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(new Scene(root, 685, 490));
        primaryStage.show();
        new Thread(DatabaseHandler::getInstance).start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
