package com.FinalProject.UMS;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * EventMenuController manages the event menu interface.
 * It allows users to navigate to either the Admin or User event views.
 */
public class EventMenuController {

    // Buttons for navigation
    @FXML private Button adminButton; // Button to access the admin event view
    @FXML private Button userButton;  // Button to access the user event view
    @FXML private Button returnButton;  // Button to return to the main menu

    /**
     * Handles the click event for the admin button.
     * Loads the admin-event-view.fxml file and switches the scene to the Admin View.
     */
    @FXML
    protected void onAdminButtonClick() {
        // Get the current user role (assumes you have a method to fetch the role)
        String currentUserRole = String.valueOf(LoginController.getCurrentUser()); // Fetch current user role from LoginController

        if ("ADMIN".equals(currentUserRole)) {
            // Proceed to load Admin Event View if the role is ADMIN
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-event-view.fxml"));
                Parent root = loader.load();

                // Retrieve the current stage (window) from the admin button
                Stage stage = (Stage) adminButton.getScene().getWindow();

                // Set a new scene with the admin event view layout
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Admin View");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle loading errors
            }
        } else {
            // Show an access denied message if the user is not an admin
            showPopup("Access Denied", "You do not have permission to access the Admin Event View.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Helper method to show a popup alert.
     */
    private void showPopup(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Handles the click event for the user button.
     * Loads the event-user-view.fxml file and switches the scene to the User View.
     */
    @FXML
    protected void onUserButtonClick() {
        try {
            // Load the User Event View from its FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("event-user-view.fxml"));
            Parent root = loader.load();

            // Retrieve the current stage (window) from the user button
            Stage stage = (Stage) userButton.getScene().getWindow();

            // Set a new scene with the user event view layout
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User View"); // Set the title for the new window
            stage.show();
        } catch (IOException e) {
            // Print error details if loading fails
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event for the back button.
     * Loads the main menu view (menu-view.fxml) and switches the scene to the Main Menu.
     */
    @FXML
    protected void handleBackButton() {
        try {
            // Load the Main Menu view from its FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Parent root = loader.load();

            // Retrieve the current stage (window) from the back button
            Stage stage = (Stage) returnButton.getScene().getWindow();

            // Set a new scene with the main menu layout
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("University Management System - Main Screen"); // Set the title for the new window
            stage.show();
        } catch (IOException e) {
            // Print error details if loading fails
            e.printStackTrace();
        }
    }
}
