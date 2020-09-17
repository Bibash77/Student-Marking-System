package home.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StudentMarks{
    private SimpleStringProperty StudentId;
    private SimpleIntegerProperty assignment1;
    private SimpleIntegerProperty assignment2;
    private SimpleIntegerProperty exam;
    private SimpleIntegerProperty total;
    private SimpleStringProperty grade;

    public StudentMarks(String studentId, Integer assignment1, Integer assignment2,
        Integer exam, Integer total, String grade) {
        StudentId = new SimpleStringProperty(studentId);
        this.assignment1 = new SimpleIntegerProperty(assignment1);
        this.assignment2 = new SimpleIntegerProperty(assignment2);
        this.exam = new SimpleIntegerProperty(exam);
        this.total = new SimpleIntegerProperty(total);
        this.grade = new SimpleStringProperty(grade);
    }

    public String getStudentId() {
        return StudentId.get();
    }

    public SimpleStringProperty studentIdProperty() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        this.StudentId.set(studentId);
    }

    public int getAssignment1() {
        return assignment1.get();
    }

    public SimpleIntegerProperty assignment1Property() {
        return assignment1;
    }

    public void setAssignment1(int assignment1) {
        this.assignment1.set(assignment1);
    }

    public int getAssignment2() {
        return assignment2.get();
    }

    public SimpleIntegerProperty assignment2Property() {
        return assignment2;
    }

    public void setAssignment2(int assignment2) {
        this.assignment2.set(assignment2);
    }

    public int getExam() {
        return exam.get();
    }

    public SimpleIntegerProperty examProperty() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam.set(exam);
    }

    public int getTotal() {
        return total.get();
    }

    public SimpleIntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
    }

    public String getGrade() {
        return grade.get();
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }
}
