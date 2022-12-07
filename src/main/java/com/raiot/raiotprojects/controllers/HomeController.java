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
    Button btn_ApproveProject;

    @FXML
    Button btn_ApproveResearch;

    @FXML
    Button btn_AddResearch;

    @FXML
    Button btn_ModifyResearch;

    @FXML
    Button btn_Media;

    @FXML
    Label label_Count;
    @FXML
    public void initialize() throws Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String query = "SELECT COUNT(*) AS recordCount FROM projects JOIN project_users ON projects.id = project_users.project_id WHERE project_users.user_id = ? AND (project_users.role = ? OR project_users.role = ?)";

        PreparedStatement pstmt  = connection.prepareStatement(query);
        pstmt.setInt(1, RaiotProjectsApplication.user_id);
        pstmt.setString(2, "owner");
        pstmt.setString(3, "leader");
        ResultSet rs    = pstmt.executeQuery();
        rs.next();
        Integer total_projects = rs.getInt("recordCount");


        label_Count.setVisible(true);
        if (total_projects == 0) {
            btn_ModifyProject.setVisible(false);
        }

        String queryProjectsIn = "SELECT COUNT(*) AS recordCount FROM projects JOIN project_users ON projects.id = project_users.project_id WHERE project_users.user_id = ?";

        PreparedStatement pstmtProjectsIn  = connection.prepareStatement(queryProjectsIn);
        pstmtProjectsIn.setInt(1, RaiotProjectsApplication.user_id);
        ResultSet rsprojectin    = pstmtProjectsIn.executeQuery();
        rsprojectin.next();
        Integer total_projectsin= rsprojectin.getInt("recordCount");

        label_Count.setText("Existen " + total_projectsin + " proyecto(s) para tu usuario");

        if (total_projectsin == 0) {
            btn_AddResearch.setVisible(false);
        }

        String queryResearches = "SELECT COUNT(*) AS recordCount FROM researches WHERE  user_id = ?";

        PreparedStatement pstmtResearches  = connection.prepareStatement(queryResearches);
        pstmtResearches.setInt(1, RaiotProjectsApplication.user_id);
        ResultSet rsresearches    = pstmtResearches.executeQuery();
        rsresearches.next();
        Integer total_researches = rsresearches.getInt("recordCount");

        if (total_researches == 0 || total_projectsin == 0) {
            btn_ModifyResearch.setVisible(false);
        }

        String queryProjectsWithChanges = "SELECT COUNT(*) AS recordCount FROM projects JOIN project_users ON projects.id = project_users.project_id JOIN project_changes ON project_changes.project_id = projects.id WHERE project_changes.approved_by IS NULL AND project_users.user_id = ? AND project_users.role = ?";

        PreparedStatement pstmtProjectsWithChanges  = connection.prepareStatement(queryProjectsWithChanges);
        pstmtProjectsWithChanges.setInt(1, RaiotProjectsApplication.user_id);
        pstmtProjectsWithChanges.setString(2, "owner");
        ResultSet rsprojectwithchanges    = pstmtProjectsWithChanges.executeQuery();
        rsprojectwithchanges.next();
        Integer total_projectswithchanges= rsprojectwithchanges.getInt("recordCount");

        if (total_projectswithchanges == 0) {
            btn_ApproveProject.setVisible(false);
        }

        String queryInvestigationsToApprove = "SELECT COUNT(*) AS recordCount FROM projects JOIN project_users ON projects.id = project_users.project_id JOIN researches ON researches.project_id = projects.id WHERE researches.approved_by IS NULL AND project_users.user_id = ? AND (project_users.role = ? OR project_users.role = ?)";

        PreparedStatement pstmtInvestigationsToApprove  = connection.prepareStatement(queryInvestigationsToApprove);
        pstmtInvestigationsToApprove.setInt(1, RaiotProjectsApplication.user_id);
        pstmtInvestigationsToApprove.setString(2, "owner");
        pstmtInvestigationsToApprove.setString(3, "leader");
        ResultSet rsprojectresearchwithchanges   = pstmtInvestigationsToApprove.executeQuery();
        rsprojectresearchwithchanges.next();
        Integer total_research_with_changes= rsprojectresearchwithchanges.getInt("recordCount");

        if (total_research_with_changes == 0) {
            btn_ApproveResearch.setVisible(false);
        }





    }


    @FXML
    protected void onAddProjectClick() throws IOException, SQLException {
        FXMLLoader registerScene = new FXMLLoader(RaiotProjectsApplication.class.getResource("project.fxml"));
        Parent root = registerScene.load();
        ProjectController projectController = registerScene.getController();
        projectController.setEdit(false);
        Stage window = (Stage) btn_AddProject.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    protected void onModifyProjectClick() throws IOException, SQLException {
        FXMLLoader registerScene = new FXMLLoader(RaiotProjectsApplication.class.getResource("project.fxml"));
        Parent root = registerScene.load();
        ProjectController projectController = registerScene.getController();
        projectController.setEdit(true);
        Stage window = (Stage) btn_AddProject.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    protected void  onAddResearchClick() throws Exception {
        FXMLLoader scene = new FXMLLoader(RaiotProjectsApplication.class.getResource("research-form.fxml"));
        Parent root = scene.load();
        ResearchController controller = scene.getController();
        controller.setEdit(false);
        Stage window = (Stage) btn_ModifyResearch.getScene().getWindow();
        window.setScene(new Scene(root));
    }


    @FXML
    protected void  onEditResearchClick() throws Exception {
        FXMLLoader scene = new FXMLLoader(RaiotProjectsApplication.class.getResource("research-form.fxml"));
        Parent root = scene.load();
        ResearchController controller = scene.getController();
        controller.setEdit(true);
        Stage window = (Stage) btn_ModifyResearch.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    protected void  onMediaClick() throws Exception {
        Parent mediaScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("research-media.fxml"));
        Stage window = (Stage) btn_Media.getScene().getWindow();
        window.setScene(new Scene(mediaScene));
    }


}
