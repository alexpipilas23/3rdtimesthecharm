package com.FinalProject.UMS;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class WCourseController {

    @FXML
    private Button addCourseButton;  // Link the 'Add Course' button

    @FXML
    private Button deleteCourseButton;  // Link the 'Delete Course' button

    @FXML
    private TextField txtCourseCode;

    @FXML
    private TextField txtCourseName;

    @FXML
    private TextField txtSubject;

    @FXML
    private Spinner<Integer> spnSection;

    @FXML
    private TextField txtTeacher;

    @FXML
    private Spinner<Integer> spnCapacity;

    @FXML
    private DatePicker dpExamDate;

    @FXML
    private TextField txtLocation;

    @FXML
    private ListView<WCourse> courseListView;

    private String userRole;

    private ObservableList<WCourse> courseData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize Spinners
        SpinnerValueFactory<Integer> sectionValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        spnSection.setValueFactory(sectionValueFactory);

        SpinnerValueFactory<Integer> capacityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spnCapacity.setValueFactory(capacityValueFactory);

        this.userRole = LoginController.getCurrentUserRole();
        System.out.println("User Role: " + userRole);  // Debugging output

        courseData.addAll(
                new WCourse("CALC1200", "Calculus II", "MATH", "1", 30, "Mon/Wed 9-11 AM", "12/15/2025", "Room 101", "Dr. Alan Turing"),
                new WCourse("ENG1200", "Mechanics", "ENG", "1", 25, "Tue/Thu 10-12 PM", "12/16/2025", "Room 102", "Prof. Emily Bronte"),
                new WCourse("ENGG1420", "Java Programming", "CS", "1", 42, "Tue/Thu 12-2 PM", "12/16/2025", "Room 103", "Prof. Bahar Nozari"),
                new WCourse("CHEM101", "Intro to Chemistry", "CHEM", "1", 20, "Mon/Wed 3-4 PM", "12/14/2025", "Room 201", "Dr. Lucka Lucku"),
                new WCourse("ENGG1500", "Linear Algebra", "ENGG", "1", 22, "Tue/Thu 4:30-5:30", "12/13/2025", "Room 202", "Dr. Lakyn Copeland"),
                new WCourse("PHYS1150", "Physics", "PHYS", "1", 18, "Mon/Fri 9:00-10:30", "12/01/2025", "Room 203", "Dr. Albozr Ghanaba")
        );

        courseListView.setItems(courseData);
        adjustVisibilityBasedOnRole();

        // Set the cell factory to display course information in the ListView
        courseListView.setCellFactory(param -> new ListCell<WCourse>() {
            @Override
            protected void updateItem(WCourse course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null || course.getCourseName() == null) {
                    setText(null);
                } else {
                    setText(course.getCourseName() + " (" + course.getCourseCode() + ")");
                }
            }
        });
    }

    public void setUserRole(String role) {
        this.userRole = role;
        adjustVisibilityBasedOnRole();
    }

    private void adjustVisibilityBasedOnRole() {
        if ("USER".equals(userRole)) {
            // Disable text fields and spinners for users
            txtCourseCode.setDisable(true);
            txtCourseName.setDisable(true);
            txtSubject.setDisable(true);
            spnSection.setDisable(true);
            txtTeacher.setDisable(true);
            spnCapacity.setDisable(true);
            dpExamDate.setDisable(true);
            txtLocation.setDisable(true);

            // Disable buttons for users
            addCourseButton.setDisable(true);
            deleteCourseButton.setDisable(true);
        } else {
            // Enable buttons and fields for admins or other roles
            addCourseButton.setDisable(false);
            deleteCourseButton.setDisable(false);
        }
    }

    @FXML
    private void addCourse() {
        // Check if the user is not an admin
        if ("USER".equals(userRole)) {
            // Show an error message if the user is a regular user
            showAlert("Access Denied", "You do not have permission to add courses.");
            return; // Exit the method if the user is a regular user
        }

        try {
            String courseCode = txtCourseCode.getText();
            String courseName = txtCourseName.getText();
            String subjectCode = txtSubject.getText();
            String sectionNumber = String.valueOf(spnSection.getValue());
            int capacity = spnCapacity.getValue();
            LocalDate examDate = dpExamDate.getValue();
            String location = txtLocation.getText();
            String teacherName = txtTeacher.getText();

            if (courseCode.isEmpty() || courseName.isEmpty() || subjectCode.isEmpty() ||
                    sectionNumber.isEmpty() || location.isEmpty() || teacherName.isEmpty() || examDate == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            String examDateStr = examDate.toString(); // Format LocalDate to String

            WCourse newCourse = new WCourse(courseCode, courseName, subjectCode, sectionNumber, capacity, "To be scheduled", examDateStr, location, teacherName);
            courseData.add(newCourse);
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "An error occurred while adding the course.");
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteCourse() {
        // Retrieve the user role from LoginController
        userRole = LoginController.getCurrentUserRole();

        // Check if the user is not an admin
        if (userRole == null || "USER".equals(userRole)) {
            showAlert("Access Denied", "You do not have permission to delete courses.");
            return; // Exit the method if the user is a regular user
        }

        // Proceed with deleting the selected course
        WCourse selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            courseData.remove(selectedCourse);
            clearFields();
        } else {
            showAlert("Error", "Please select a course to delete.");
        }
    }


    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) courseListView.getScene().getWindow();
            Scene scene = new Scene(root, 1366, 768);
            stage.setScene(scene);
            stage.setTitle("Main Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void clearFields() {
        txtCourseCode.clear();
        txtCourseName.clear();
        txtSubject.clear();
        spnSection.getValueFactory().setValue(1);
        txtTeacher.clear();
        spnCapacity.getValueFactory().setValue(1);
        dpExamDate.setValue(null);
        txtLocation.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getter for courseListView (added for testing)
    public ListView<WCourse> getCourseListView() {
        return courseListView;
    }

    // Getter for userRole (added for testing)
    public String getUserRole() {
        return userRole;
    }

}

