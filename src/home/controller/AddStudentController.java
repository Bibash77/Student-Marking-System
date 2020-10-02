package home.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import home.modal.StudentMarks;
import javafx.application.Platform;
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

/**
 * This Controller handles all the adding and updating functions
 */

public class AddStudentController implements Initializable {


    public boolean isUpdateOperation = false;
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

    // function to add data and update data in data base
    @FXML
    void addStudent(ActionEvent event) {
        String id  = studentId.getText();
        try {
            int assignment1 = Integer.parseInt(this.assignment1.getText());
            int assignment2 = Integer.parseInt(this.assignment2.getText());
            int exam = Integer.parseInt(this.exam.getText());
            int total = Integer.parseInt(this.total.getText());
            String grade = findGrade(total , assignment1 , assignment2 , exam);


        if(id.isEmpty() || grade.isEmpty() || assignment1 < 0 || assignment2 < 0 || exam < 0
        || total < 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("please Enter in all fields");
            alert.showAndWait();
            return;
        }
        String qu = "";
        if (isUpdateOperation){
            qu = "UPDATE MARKS SET ASSIGNMENT1="+ assignment1+","
                + "ASSIGNMENT2=" + assignment2 + ","
                + "EXAM=" + exam + ","
                + "total=" + total + ","
                + "grade='"+ grade + "' where STUDENTID='"+ id +"'";
        } else {
             qu = "INSERT INTO MARKS VALUES ( "
                + "'" + id + "'," +
                + assignment1 + "," +
                assignment2 + "," +
                exam + "," +
                total + ","+
                "'" + grade + "')" ;
        }

        System.out.println(qu);

        if(databaseHandler.execAction(qu)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("successfully saved student marks");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to add student marks");
            alert.showAndWait();
        }
        }catch (Exception ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid data");
            alert.showAndWait();
        }
    }

    // close scene when cancel button is clicked
    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    // main method to initialize the stage
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        total.setEditable(false);
        exam.textProperty().addListener((observable, oldValue, newValue) -> {
           totalCalc(null);
        });
        assignment1.textProperty().addListener((observable, oldValue, newValue) -> {
           totalCalc(null);
        });
        assignment2.textProperty().addListener((observable, oldValue, newValue) -> {
           totalCalc(null);
        });
    }

    // method to find marks
    String findGrade  (int marks , int ass1 , int ass2 , int exam){
       if(ass1 <= 0 && ass2 <= 0 || marks == 0){
           return "AF";
       }
        if(marks >= 85 && marks <=100){
            return  "HD";
        } else if(marks >= 75 && marks < 85){
            return  "D";
        } else if(marks >= 65 && marks < 75){
            return  "C";
        } else if(marks >= 50 && marks < 65){
            return  "P";
        } else {
            if(marks < 50 && marks >= 45){
                if(ass1 < 10 || ass2 < 15){
                    return "SA";
                } else if(exam < 25){
                    return "SE";
                }
            }
            return "F";
        }
    }

    // this will set data if the scene is called as update with data
    public void setUpdateData(StudentMarks studentMarks){
        studentId.setText(studentMarks.getStudentId());
        studentId.setEditable(false);
        assignment1.setText(String.valueOf(studentMarks.getAssignment1()));
        assignment2.setText(String.valueOf(studentMarks.getAssignment2()));
        exam.setText(String.valueOf(studentMarks.getExam()));
        total.setText(String.valueOf(studentMarks.getTotal()));
    }

    // method to trigger calculation of all assignment
    @FXML
    void totalCalc(ActionEvent event) {
        try {

            int total = 0;
            int assignment1 = 0;
            int assignment2 = 0;
            int exam = 0;

            if(!this.assignment1.getText().equals("")){
                assignment1 = Integer.parseInt(this.assignment1.getText());
            }
            if(!this.assignment2.getText().equals(""))
            {
                assignment2 = Integer.parseInt(this.assignment2.getText());
            }
            if(!this.exam.getText().equals(""))
            {
                exam = Integer.parseInt(this.exam.getText());
            }
            this.total.setText(String.valueOf(assignment1 + assignment2 + exam));
        }catch (Exception ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid data");
            alert.showAndWait();
        }
    }
}

