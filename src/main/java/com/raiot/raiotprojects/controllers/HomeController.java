package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeController {

    @FXML
    Button btn_AddProject;

    @FXML
    Button btn_ModifyProject;

    @FXML
    Button btn_AddResearch;

    @FXML
    Label label_Count;
    @FXML
    public void initialize() throws Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String query = "SELECT COUNT(*) AS recordCount FROM projects WHERE  user_id = ?";

        PreparedStatement pstmt  = connection.prepareStatement(query);
        pstmt.setInt(1, RaiotProjectsApplication.user_id);
        ResultSet rs    = pstmt.executeQuery();
        rs.next();
        Integer total_projects = rs.getInt("recordCount");

        label_Count.setText("Existen " + total_projects + " proyecto(s) para tu usuario");
        label_Count.setVisible(true);

        if (total_projects == 0) {
            btn_ModifyProject.setVisible(false);
        }
    }


    @FXML
    protected void onAddProjectClick() throws IOException, SQLException {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        FXMLLoader registerScene = new FXMLLoader(RaiotProjectsApplication.class.getResource("project.fxml"));
        Parent root = registerScene.load();
        ProjectController projectController = registerScene.getController();
        projectController.setEdit(false);
        Stage window = (Stage) btn_AddProject.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    protected void onModifyProjectClick() throws IOException, SQLException {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        FXMLLoader registerScene = new FXMLLoader(RaiotProjectsApplication.class.getResource("project.fxml"));
        Parent root = registerScene.load();
        ProjectController projectController = registerScene.getController();
        projectController.setEdit(true);
        Stage window = (Stage) btn_AddProject.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    protected void  onAddResearchClick() throws Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("research-form.fxml"));
        Stage window = (Stage) btn_AddResearch.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }


}
