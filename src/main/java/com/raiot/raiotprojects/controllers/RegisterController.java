package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    Label label_Error;

    @FXML
    Button btn_BackLogin;

    @FXML
    Button btn_Register;

    @FXML
    Hyperlink hyperlink_Register;

    @FXML
    TextField field_Name;

    @FXML
    TextField field_LastName;
    @FXML
    TextField field_Email;

    @FXML
    TextField field_Password;

    @FXML
    TextField field_Age;

    protected void onBackLoginButtonClick() throws Exception {

        Parent loginScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("login.fxml"));
        Stage window = (Stage) btn_BackLogin.getScene().getWindow();
        window.setScene(new Scene(loginScene));
    }

    }


