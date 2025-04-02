package com.FinalProject.UMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {

    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    @FXML
    private Button subjectManagementButton;

    @FXML
    private Button courseManagementButton;

    @FXML
    private Button studentManagementButton;

    @FXML
    private Button facultyManagementButton;

    @FXML
    private Button eventManagementButton;

    private String userRole;

    public void initialize() {
    }

    public void setUserRole(String role) {
        this.userRole = role;
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        loadScene("Dashboard-view.fxml", "Dashboard", event);
    }

    @FXML
    private void handleSubjectManagement(ActionEvent event) {
        LOGGER.log(Level.INFO, "Subject Management button clicked");
        loadScene("/com/FinalProject/UMS/SubjectManagement.fxml", "Subject Management", event);
    }

    @FXML
    private void handleAddSubject(ActionEvent event) {
        loadScene("addSubject.fxml", "Add Subject", event);
    }

    @FXML
    private void handleEditSubject(ActionEvent event) {
        loadScene("editSubject.fxml", "Edit Subject", event);
    }

    @FXML
    private void handleDeleteSubject(ActionEvent event) {
        loadScene("deleteSubject.fxml", "Delete Subject", event);
    }

    @FXML
    private void handleViewSubject(ActionEvent event) {
        loadScene("viewSubject.fxml", "View Subject", event);
    }

    @FXML
    private void handleCourseManagement(ActionEvent event) {
        loadScene("CourseManagement-view.fxml", "Course Management", event);
    }

    @FXML
    private void handleAddCourse(ActionEvent event) {
        loadScene("addCourse.fxml", "Add Course", event);
    }

    @FXML
    private void handleEditCourse(ActionEvent event) {
        loadScene("editCourse.fxml", "Edit Course", event);
    }

    @FXML
    private void handleDeleteCourse(ActionEvent event) {
        loadScene("deleteCourse.fxml", "Delete Course", event);
    }

    @FXML
    private void handleViewCourse(ActionEvent event) {
        loadScene("viewCourse.fxml", "View Course", event);
    }

    @FXML
    private void handleAssignFaculty(ActionEvent event) {
        loadScene("assignFaculty.fxml", "Assign Faculty", event);
    }

    @FXML
    private void handleManageEnrollments(ActionEvent event) {
        loadScene("manageEnrollments.fxml", "Manage Enrollments", event);
    }

    @FXML
    private void handleStudentManage(ActionEvent event) {
        loadScene("studentmanagecontroller.fxml", "Manage Enrollments", event);
    }

    @FXML
    private void handleFacultyManagement(ActionEvent event) {
        if (userRole == null || "USER".equals(userRole)) {
            showAlert("Access Denied", "You do not have permission to access Faculty Management.");
        } else {
            loadScene("faculty-view.fxml", "Faculty Management", event);
        }
    }

    @FXML
    private void handleAssignFacultyCourses(ActionEvent event) {
        if (userRole == null || "USER".equals(userRole)) {
            showAlert("Access Denied", "You do not have permission to assign faculty courses.");
        } else {
            loadScene("assignFacultyCourses.fxml", "Assign Faculty Courses", event);
        }
    }

    @FXML
    private void handleEventManagement(ActionEvent event) {
        loadScene("event-menu-view.fxml", "Event Management", event);
    }

    @FXML
    private void handleEditEvent(ActionEvent event) {
        loadScene("editEvent.fxml", "Edit Event", event);
    }

    @FXML
    private void handleDeleteEvent(ActionEvent event) {
        loadScene("deleteEvent.fxml", "Delete Event", event);
    }

    @FXML
    private void handleViewEvents(ActionEvent event) {
        loadScene("viewEvents.fxml", "View Events", event);
    }

    @FXML
    private void handleManageEventRegistrations(ActionEvent event) {
        loadScene("manageEventRegistrations.fxml", "Manage Event Registrations", event);
    }

    @FXML
    private void handleViewProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentManagementStudentaura.fxml"));
            Parent root = loader.load();

            StudentManagementMenuController controller = loader.getController();
            Student currentStudent = LoginController.getCurrentUser();
            controller.setStudentData(currentStudent);

            Stage stage = new Stage();
            stage.setTitle("View Profile");
            stage.setScene(new Scene(root));
            stage.show();

            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading profile view: " + e.getMessage(), e);
            showAlert("Error", "Failed to load profile view.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error during logout: " + e.getMessage(), e);
            showAlert("Error", "Failed to log out.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadScene(String fxmlFile, String title, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading " + fxmlFile + ": " + e.getMessage(), e);
            showAlert("Error", "Failed to load " + title + " window.");
        }
    }
}
