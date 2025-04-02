package com.FinalProject.UMS;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


import java.io.IOException;


public class ProfileController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;


    @FXML
    protected void onSaveChangesButtonClick() {
        // Save changes logic
    }


    @FXML
    protected void onChangePasswordButtonClick() {
        // Change password logic
    }


    @FXML
    protected void onUploadPictureButtonClick() {
        // Upload picture logic
    }


    @FXML
    protected void onReturnButtonClick() {
        try {
            System.out.println("Return button clicked!");
            String fxmlFile = "/com/FinalProject/UMS/profile-view.fxml";  // Full resource path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Navigation successful to: " + fxmlFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        System.out.println("ProfileController initialized!");
        try {
            // Simulate data loading
            firstNameField.setText("John");
            lastNameField.setText("Doe");
            emailField.setText("john.doe@example.com");
            System.out.println("Profile data loaded!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading profile data: " + e.getMessage());
        }
    }


}




