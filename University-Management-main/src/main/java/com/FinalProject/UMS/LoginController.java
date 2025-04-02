package com.FinalProject.UMS;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Button loginButton;

    private Map<String, User> users;
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    private static Student currentUser;  // Added currentUser field

    public static Student getCurrentUser() {
        return currentUser;
    }

    private void setCurrentUser(Student student) {
        currentUser = student;
    }

    public void initialize() {
        users = ExcelDatabase.loadUsers();
        if (users != null) {
            LOGGER.log(Level.INFO, "Loaded {0} users", users.size());
        } else {
            LOGGER.log(Level.SEVERE, "Failed to load users from Excel");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        LOGGER.log(Level.INFO, "Attempting login for user: {0}", username);
        LOGGER.log(Level.INFO, "Entered password: {0}", password);
        LOGGER.log(Level.INFO, "Entered password (hex): {0}", toHexString(password));

        // Hardcoded admin and user for quick testing
        if ("a".equals(username) && "a".equals(password)) {
            LOGGER.info("Admin login successful");
            showPopup("Login Successful", "Welcome, Admin!", Alert.AlertType.INFORMATION);
            navigateToAdminDashboard(event);
            return;
        }

        if ("u".equals(username) && "u".equals(password)) {
            LOGGER.info("User login successful");
            showPopup("Login Successful", "Welcome, User!", Alert.AlertType.INFORMATION);
            navigateToUserDashboard(event);
            return;
        }

        if (users == null) {
            showPopup("Error", "User database not available!", Alert.AlertType.ERROR);
            return;
        }

        User user = users.get(username);
        if (user != null) {
            LOGGER.log(Level.INFO, "Stored password for user {0}: {1}", new Object[]{username, user.getPassword()});
            LOGGER.log(Level.INFO, "Stored password (hex): {0}", toHexString(user.getPassword()));
            if (user.authenticate(password)) {
                if (user instanceof Student) { // Check if user is a Student
                    setCurrentUser((Student) user);  // Safe casting
                } else {
                    LOGGER.warning("Authenticated user is not a student.");
                    showPopup("Login Failed", "This login is not for a student user.", Alert.AlertType.ERROR);
                    return;
                }
                LOGGER.log(Level.INFO, "User {0} authenticated successfully", username);
                showPopup("Login Successful", "Welcome, " + username + "!", Alert.AlertType.INFORMATION);
                navigateToUserDashboard(event);
            } else {
                LOGGER.warning("Incorrect password for user: " + username);
                errorMessageLabel.setText("Invalid password. Please try again.");
                showPopup("Login Failed", "Incorrect password", Alert.AlertType.ERROR);
            }
        } else {
            LOGGER.warning("User ID not found: " + username);
            errorMessageLabel.setText("User ID not found!");
            showPopup("Login Failed", "User ID not found", Alert.AlertType.ERROR);
        }
    }

    private void navigateToAdminDashboard(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading Admin Dashboard: " + e.getMessage(), e);
            showPopup("Error", "Failed to load admin dashboard.", Alert.AlertType.ERROR);
        }
    }

    private void navigateToUserDashboard(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading User Dashboard: " + e.getMessage(), e);
            showPopup("Error", "Failed to load user dashboard.", Alert.AlertType.ERROR);
        }
    }

    private void showPopup(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to convert a string to its hexadecimal representation.
    private String toHexString(String input) {
        if (input == null) return "";
        StringBuilder hex = new StringBuilder();
        for (char c : input.toCharArray()) {
            hex.append(String.format("%02X ", (int) c));
        }
        return hex.toString().trim();
    }
}
