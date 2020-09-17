package home.list_student_marks;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import home.view.StudentMarks;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import marking.assistant.database.DatabaseHandler;

public class MarksListController implements Initializable {
    ObservableList<StudentMarks> studentMarks = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<StudentMarks> tableView;

    @FXML
    private TableColumn<StudentMarks , String> studentIdCol;

    @FXML
    private TableColumn<StudentMarks, Integer> assignment1Col;

    @FXML
    private TableColumn<StudentMarks, Integer> assignment2Col;

    @FXML
    private TableColumn<StudentMarks, Integer> examCol;

    @FXML
    private TableColumn<StudentMarks, Integer> totalCol;

    @FXML
    private TableColumn<StudentMarks, String> gradeCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumn();
        loadStudentMarksData();
    }

    private void initColumn(){
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        assignment1Col.setCellValueFactory(new PropertyValueFactory<>("assignment1"));
        assignment2Col.setCellValueFactory(new PropertyValueFactory<>("assignment2"));
        examCol.setCellValueFactory(new PropertyValueFactory<>("exam"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    private void loadStudentMarksData(){
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM MARKS";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()){
                String id = rs.getString("studentId");
                Integer assignment1 = rs.getInt("assignment1");
                Integer assignment2 = rs.getInt("assignment2");
                Integer exam = rs.getInt("exam");
                Integer total = rs.getInt("total");
                String grade = rs.getString("grade");
                studentMarks.add(new StudentMarks(id , assignment1 , assignment2 , exam , total , grade));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        tableView.getItems().setAll(studentMarks);
    }

}
