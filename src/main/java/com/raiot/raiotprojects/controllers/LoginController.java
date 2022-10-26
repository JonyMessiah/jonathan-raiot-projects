package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;

public class LoginController {

    @FXML
    Button btn_Login;

    @FXML
    Hyperlink hyperlink_Register;

    @FXML
    protected void onLoginButtonClick() throws Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Login.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }

    @FXML
    protected void onRegisterClick() throws  Exception {
        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("register.fxml"));
        Stage window = (Stage) hyperlink_Register.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }
}
