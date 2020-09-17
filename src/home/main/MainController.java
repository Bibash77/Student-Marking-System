package home.main;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import marking.assistant.database.DatabaseHandler;

public class MainController implements Initializable {

    DatabaseHandler databaseHandler;

    @FXML
    private Text sIDField;

    @FXML
    private Text Ass1Field;

    @FXML
    private Text Ass2Field;

    @FXML
    private Text examField;

    @FXML
    private Text totalField;

    @FXML
    private Text gradeFiled;

    @FXML
    private TextField idSearchInput;

    @FXML
    private TextField gradeSearch;

    @FXML
    private HBox searchBlock;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;


    @FXML
    void openAddMarks(ActionEvent event) {
        this.loadWindow("/home/add_student/AddStudent.fxml", "Add New Marks");
    }

    @FXML
    void openViewMarks(ActionEvent event) {
        this.loadWindow("/home/list_student_marks/MarksList.fxml", "Marks List");
    }
    @FXML
    void onDeleteMarks(ActionEvent event) {

    }

    @FXML
    void onEditMarks(ActionEvent event) {

    }


    @FXML
    void searchByGrade(ActionEvent event) {

    }

    @FXML
    void searchById(ActionEvent event) {
        String sID = idSearchInput.getText();
        if(sID.isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter ID To Search");
            alert.showAndWait();
        }
        String qu = "SELECT * FROM MARKS WHERE STUDENTID = '"+ sID + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        boolean hasData = false;
       try {
           while (rs.next()){
               String id = rs.getString("studentId");
               int assignment1 = rs.getInt("assignment1");
               int assignment2 = rs.getInt("assignment2");
               int exam = rs.getInt("exam");
               int total = rs.getInt("total");
               String grade = rs.getString("grade");

               sIDField.setText(id);
               Ass1Field.setText(Integer.toString(assignment1));
               Ass2Field.setText(Integer.toString(assignment2));
               examField.setText(Integer.toString(exam));
               totalField.setText(Integer.toString(total));
               gradeFiled.setText(grade);
               editButton.setDisable(false);
               deleteButton.setDisable(false);
               hasData = true;
           }
       }catch (Exception ex ){
           ex.printStackTrace();
       }

       if(!hasData){
           sIDField.setText("No such Marks Detail Found");
           editButton.setDisable(true);
           deleteButton.setDisable(true);
       }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
