package home.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import home.modal.StudentMarks;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import marking.assistant.database.DatabaseHandler;

public class MainController implements Initializable {


    static List<StudentMarks> studentMarks = new ArrayList<>();
    @FXML
    private  Text sIDField;
    @FXML
    private  Text Ass1Field;
    @FXML
    private  Text Ass2Field;
    @FXML
    private  Text examField;
    @FXML
    private  Text totalField;
    @FXML
    private  Text gradeFiled;
    @FXML
    private  Text marksIndex;
    @FXML
    private  Button editButton;
    @FXML
    private  Button deleteButton;
    DatabaseHandler databaseHandler;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField idSearchInput;
    @FXML
    private TextField gradeSearchId;
    @FXML
    private HBox searchBlock;
    @FXML
    private TextField startMarksInput;

    @FXML
    private TextField endMarksInput;


    @FXML
    private TableView<StudentMarks> tableView;

    @FXML
    private TableColumn<StudentMarks, String> studentIdCol;

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

    // function to set current view single data
     void setData(StudentMarks studentMarks) {
        sIDField.setText(studentMarks.getStudentId());
        Ass1Field.setText(Integer.toString(studentMarks.getAssignment1()));
        Ass2Field.setText(Integer.toString(studentMarks.getAssignment2()));
        examField.setText(Integer.toString(studentMarks.getExam()));
        totalField.setText(Integer.toString(studentMarks.getTotal()));
        gradeFiled.setText(studentMarks.getGrade());
        editButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    // open add marks stage
    @FXML
    void openAddMarks(ActionEvent event) {
        this.loadWindow("/home/view/AddStudent.fxml", "Add New Marks");
    }

    //open view all marks list stage
    @FXML
    void openViewMarks(ActionEvent event) {
        this.loadWindow("/home/view/MarksList.fxml", "Marks List");
    }

    //clear search from scene
    @FXML
    void clearMarksSearch(ActionEvent event) {
        this.startMarksInput.setText("");
        this.endMarksInput.setText("");
         this.loadStudentMarksDataList(false);
    }

    //refresh all data of scene
    @FXML
    void refreshData(ActionEvent event) {
        this.loadStudentMarksData();
        this.loadStudentMarksDataList(false);
        int loadIndex = Integer.parseInt(marksIndex.getText());
        setData(studentMarks.get(loadIndex));
    }

    // performs delete operation for student marks
    @FXML
    void onDeleteMarks(ActionEvent event) {
        deleteEntryMarks();
    }

    // performs edit operation for student marks
    @FXML
    void onEditMarks(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/home/view/AddStudent.fxml"));
        Parent studentAdder = loader.load();
        //access the  controller and call method to set data
        StudentMarks studentMarks = getCurrentData();
        AddStudentController addStudentController = loader.getController();
        addStudentController.setUpdateData(studentMarks);
        addStudentController.isUpdateOperation = true;
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Update Student");
        stage.setScene(new Scene(studentAdder));
        stage.setOnCloseRequest(e -> {
            this.refreshData(new ActionEvent());
        });
        stage.show();

    }

    // function to search student mark by id
    @FXML
    void searchById(ActionEvent event) {
        String sID = idSearchInput.getText();
        idSearchInput.setText("");
        if (sID.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter ID To Search");
            alert.showAndWait();
        }
        String qu = "SELECT * FROM MARKS WHERE STUDENTID = '" + sID + "'";
        ResultSet rs = databaseHandler.execQuery(qu);
        boolean hasData = false;
        try {
            while (rs.next()) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!hasData) {
            resetData();
        }
    }

    // main method to initialize scene
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        initColumn();
        this.loadStudentMarksData();
        if(studentMarks.size() > 0){
            this.setData(studentMarks.get(0));
        } else {
            this.resetData();
        }
        marksIndex.setText("0");
        this.loadStudentMarksDataList(false);
    }

    // generalize method to open stage window
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.setOnCloseRequest(e -> {
                System.out.println("here");
                this.refreshData(new ActionEvent());
            });
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // function to delete student marks
    void deleteEntryMarks() {
        String sID = sIDField.getText();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are your sure you want to delete entry with id: " + sID);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String qu = "Delete from Marks where STUDENTID ='" + sID + "'";
            System.out.println(qu);
            if (databaseHandler.execAction(qu)) {
                studentMarks.remove(Integer.parseInt(marksIndex.getText()));
                Alert alert1 = new Alert(AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Successfully Deleted");
                alert1.showAndWait();
                if(studentMarks.size() > 0){
                    this.setData(studentMarks.get(0));
                } else {
                    this.searchById(null);
                }
            } else {
                Alert alert1 = new Alert(AlertType.ERROR);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed To  Delete Marks");
                alert1.showAndWait();
            }
        }

    }

    //returns current data of view
    StudentMarks getCurrentData() {
        return new StudentMarks(sIDField.getText(), Integer.parseInt(Ass1Field.getText()),
            Integer.parseInt(Ass2Field.getText()), Integer.parseInt(examField.getText()),
            Integer.parseInt(totalField.getText()), gradeFiled.getText());
    }

    //reset data of current view
    void resetData() {
        sIDField.setText("No such Marks Detail Found");
        Ass1Field.setText("N/A");
        Ass2Field.setText("N/A");
        examField.setText("N/A");
        totalField.setText("N/A");
        gradeFiled.setText("N/A");
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    // loads all student data for list view in view all tab
    private void loadStudentMarksData() {
         studentMarks = new ArrayList<>();
        String qu = "SELECT * FROM MARKS";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String id = rs.getString("studentId");
                Integer assignment1 = rs.getInt("assignment1");
                Integer assignment2 = rs.getInt("assignment2");
                Integer exam = rs.getInt("exam");
                Integer total = rs.getInt("total");
                String grade = rs.getString("grade");
                studentMarks
                    .add(new StudentMarks(id, assignment1, assignment2, exam, total, grade));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // listen even for next button and loads next data
    @FXML
    void loadNextMarks(ActionEvent event) {
        int loadIndex = Integer.parseInt(marksIndex.getText()) + 1;
        StudentMarks studentMark;
        if (loadIndex >= studentMarks.size()) {
            loadIndex = 0;
        }
        studentMark = studentMarks.get(loadIndex);
        setData(studentMark);
        marksIndex.setText(String.valueOf(loadIndex));
    }

    // function to listen even for next button and loads next data
    @FXML
    void loadPrevMarks(ActionEvent event) {
        int loadIndex = Integer.parseInt(marksIndex.getText()) - 1;
        StudentMarks studentMark;
        if (loadIndex < 0) {
            loadIndex = studentMarks.size() - 1;
        }
        studentMark = studentMarks.get(loadIndex);
        setData(studentMark);
        marksIndex.setText(String.valueOf(loadIndex));
    }

    // function to set property for initial columns
    private void initColumn() {
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        assignment1Col.setCellValueFactory(new PropertyValueFactory<>("assignment1"));
        assignment2Col.setCellValueFactory(new PropertyValueFactory<>("assignment2"));
        examCol.setCellValueFactory(new PropertyValueFactory<>("exam"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    // loads student marks list according to range
    private void loadStudentMarksDataList(boolean isRangeSearch) {
        List<StudentMarks> marks = new ArrayList<>();
        String qu = "";
        if (isRangeSearch) {
            int startMarks = Integer.parseInt(startMarksInput.getText());
            int endMarks = Integer.parseInt(endMarksInput.getText());
            qu = "SELECT * FROM MARKS WHERE total >=" + startMarks + "and total <=" + endMarks + "ORDER BY TOTAL DESC";
        } else {
            qu = "SELECT * FROM MARKS ORDER BY TOTAL DESC";
        }
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String id = rs.getString("studentId");
                Integer assignment1 = rs.getInt("assignment1");
                Integer assignment2 = rs.getInt("assignment2");
                Integer exam = rs.getInt("exam");
                Integer total = rs.getInt("total");
                String grade = rs.getString("grade");
                marks.add(new StudentMarks(id, assignment1, assignment2, exam, total, grade));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tableView.getItems().setAll(marks);
    }

    // specified trigger function to search by grade
    @FXML
    void searchByGrades(ActionEvent event) {
        this.loadStudentMarksDataList(true);
    }

    // performs refresh updated and added data from outside the scene
     static void updateStudentListData(StudentMarks studentMark, String status) {
        if (status.equals("add")) {
            studentMarks.add(studentMark);
        }
        if (status.equals("edit")) {
            studentMarks.forEach(studentMarks1 -> {
                if (studentMarks1.getStudentId().equals(studentMark.getStudentId())) {
                    int i = studentMarks.indexOf(studentMarks1);
                    studentMarks.set(i, studentMark);
                }
            });
        }
        System.out.println("data updated");
        System.out.println(studentMarks);
    }
}
