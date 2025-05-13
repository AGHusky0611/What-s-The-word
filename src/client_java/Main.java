package client_java;

import client_java.controller.login.LoginController;
import client_java.model.login.LoginModel;
import client_java.view.login.Login;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Create the view
            Login view = new Login();

            // Create the model
            LoginModel model = new LoginModel();

            // Create the controller and connect it to view and model
            new LoginController(view, model);

            // The view is already set to visible in its constructor
        });
    }
}