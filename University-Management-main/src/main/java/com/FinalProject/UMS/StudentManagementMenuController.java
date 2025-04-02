package com.FinalProject.UMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentManagementMenuController {

    @FXML private Label welcomeText;
    @FXML private Button backButton;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField studentIDField;
    @FXML private TextField semesterField;

    @FXML
    protected void onOpenSecondPageButtonClick() {
        loadPage("StudentManagementAdminaura.fxml", "Student Information");
    }

    @FXML
    protected void onOpenProfileButtonClick() {
        loadPage("profile-view.fxml", "Profile");
    }

    @FXML
    protected void onOpenCoursesButtonClick() {
        loadPage("courses-view.fxml", "Enrolled Courses");
    }

    @FXML
    protected void onOpenGradesButtonClick() {
        loadPage("grades-view.fxml", "Grades");
    }

    @FXML
    protected void onOpenTuitionButtonClick() {
        loadPage("tuition-view.fxml", "Tuition Status");
    }

    @FXML
    protected void onOpenAdminDashboardButtonClick() {
        loadPage("admin-student-view.fxml", "Admin Dashboard");
    }

    @FXML
    private void handleBackButton() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("menu-view.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("University Management System - Main Screen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setStudentData(Student student) {

        studentIDField.setText(student.getId());
    }

    private void loadPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/FinalProject/UMS/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void addStudent(ActionEvent event) {}
    @FXML public void editStudent(ActionEvent event) {}
    @FXML public void deleteStudent(ActionEvent event) {}
    @FXML public void viewProfile(ActionEvent event) {}
    @FXML public void manageEnrollments(ActionEvent event) {}
    @FXML public void trackProgress(ActionEvent event) {}
    @FXML public void manageTuition(ActionEvent event) {}
}
