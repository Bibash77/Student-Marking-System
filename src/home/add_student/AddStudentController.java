package home.add_student;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import marking.assistant.database.DatabaseHandler;

public class AddStudentController implements Initializable {


    @FXML
    private TextField studentId;

    @FXML
    private TextField assignment1;

    @FXML
    private TextField assignment2;

    @FXML
    private TextField exam;

    @FXML
    private TextField total;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    @FXML
    private AnchorPane rootPane;

    DatabaseHandler databaseHandler;

    @FXML
    void addStudent(ActionEvent event) {
        String id  = studentId.getText();
        int assignment1 = Integer.parseInt(this.assignment1.getText());
        int assignment2 = Integer.parseInt(this.assignment2.getText());
        int exam = Integer.parseInt(this.exam.getText());
        int total = Integer.parseInt(this.total.getText());
        String grade = findGrade(total);

        if(id.isEmpty() || grade.isEmpty() || assignment1 < 0 || assignment2 < 0 || exam < 0
        || total < 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("please Enter in all fields");
            alert.showAndWait();
            return;
        }

        String qu = "INSERT INTO MARKS VALUES ( "
            + "'" + id + "'," +
            + assignment1 + "," +
              assignment2 + "," +
             exam + "," +
            total + ","+
            "'" + grade + "')" ;

        System.out.println(qu);

        if(databaseHandler.execAction(qu)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("successfully added student marks");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to add student marks");
            alert.showAndWait();
        }
        cancel(null);

    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void getData(){
        String qu = "SELECT * FROM MARKS";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()){
                System.out.println(rs.getString("STUDENTID"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        getData();
    }

    String findGrade  (int marks){
        String grade = "";
        if(marks >= 85 && marks <=100){
            grade = "HD";
        } else if(marks >= 75 && marks < 85){
            grade = "D";
        } else if(marks >= 65 && marks < 75){
            grade = "C";
        } else if(marks >= 50 && marks < 65){
            grade = "P";
        } else {
            if(marks >= 45 && marks <= 50){
                grade = "SA";
            } else {
                grade = "F";
            }
        }

        return grade;
    }

}

