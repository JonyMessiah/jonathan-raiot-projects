package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;

public class HomeController {

    @FXML
    Button btn_AddProject;
    @FXML
    protected void onAddProjectClick() throws Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("project.fxml"));
        Stage window = (Stage) btn_AddProject.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }
}
