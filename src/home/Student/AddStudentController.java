package home.Student;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import marking.assistant.database.DatabaseHandler;

public class AddStudentController implements Initializable {


    @FXML
    private TextField studentId;

    @FXML
    private TextField assignment1;

    @FXML
    private TextField assignment2;

    @FXML
    private TextField Exam;

    @FXML
    private TextField Grade;

    @FXML
    private TextField Total;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    DatabaseHandler databaseHandler;

    @FXML
    void addStudent(ActionEvent event) {

    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = new DatabaseHandler();
    }
}

