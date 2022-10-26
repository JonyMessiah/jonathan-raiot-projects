package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.dao.SQLiteDao;
import com.raiot.raiotprojects.models.UserModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {
    @FXML
    Label label_Error;

    @FXML
    Button btn_Login;

    @FXML
    Hyperlink hyperlink_Register;

    @FXML
    TextField field_Email;

    @FXML
    TextField field_Password;

    @FXML
    protected void onLoginButtonClick() throws Exception {

        label_Error.setVisible(false);
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String email = field_Email.getText();
        String password = field_Password.getText();

        // TODO validate if emails contains values, if not display label

        String query = "SELECT id, password, name, lastname FROM users WHERE  email = ?";

        PreparedStatement pstmt  = connection.prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs    = pstmt.executeQuery();

        boolean exists = false;
        System.out.println(email);
        while(rs.next()) {
            String user_password = rs.getString("password");
            if (user_password.equals(password)) {
                // login user
                RaiotProjectsApplication.user_id = rs.getInt("id");

                Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
                Stage window = (Stage) btn_Login.getScene().getWindow();
                window.setScene(new Scene(registerScene));
                exists = true;
            }
            break;
        }

        if (email==null || email==" ") {
            //Error
            label_Error.setVisible(true);
            return;

        }

        if (password.equals(null) || password.equals(" ")) {
            label_Error.setVisible(true);
            return;


        }


        if (!exists) {
            // Error
            label_Error.setVisible(true);
        }




    }

    @FXML
    protected void onRegisterClick() throws  Exception {
        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("register.fxml"));
        Stage window = (Stage) hyperlink_Register.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }
}
