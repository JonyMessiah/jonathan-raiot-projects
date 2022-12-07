package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.classes.ChoiceClass;
import com.raiot.raiotprojects.classes.ChoiceClassString;
import com.raiot.raiotprojects.classes.ChoiceClassUserOnProject;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;

public class ProjectController {
    @FXML
    Label label_Error;


    @FXML
    Button btn_Register;

    @FXML
    Button btn_Delete;

    @FXML
    Button btn_Users;


    @FXML
    TextField field_Name;

    @FXML
    TextField field_Category;
    @FXML
    TextField field_CreatedAt;

    @FXML
    Button btn_Menu;

    @FXML
    TextField field_UpdatedAt;

    @FXML
    TextField field_Repository;

    @FXML
    Label label_Title_Register;

    @FXML
    ChoiceBox choices_Projects;

    @FXML
    Label label_Most_Researches;

    @FXML
    Label label_Most_Approved_Researches;

    @FXML
    Label label_Biggest_Research;

    @FXML
    Pane pane_Stats;

    Integer id;
    Integer owner_id;
    String role;
    Boolean editing = false;

    void setEdit(Boolean edit) throws SQLException {
        btn_Delete.setVisible(false);
        btn_Users.setVisible(false);
        if (edit) {

            SQLiteDao sqlite = new SQLiteDao();
            Connection connection = sqlite.getConnection();
            String query = "SELECT projects.*, project_users.role as role FROM projects JOIN project_users ON projects.id = project_users.project_id WHERE project_users.user_id = ? AND (project_users.role = ? OR project_users.role = ?)";

            PreparedStatement pstmt  = connection.prepareStatement(query);
            pstmt.setInt(1, RaiotProjectsApplication.user_id);
            pstmt.setString(2, "owner");
            pstmt.setString(3, "leader");
            ResultSet rs    = pstmt.executeQuery();

            editing = true;

            ObservableList<ChoiceClassUserOnProject> choices = FXCollections.observableArrayList();

            while (rs.next()) {
                ChoiceClassUserOnProject item = new ChoiceClassUserOnProject(rs.getInt("id"), rs.getString("name"), new ChoiceClassString("", rs.getString("role")));
                choices.add(item);
            }

            choices_Projects.setVisible(true);
            choices_Projects.setItems(choices);

            choices_Projects.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<ChoiceClassUserOnProject>() {
                        public void changed(ObservableValue<? extends ChoiceClassUserOnProject> ov,
                                            ChoiceClassUserOnProject old_val, ChoiceClassUserOnProject new_val) {
                            id = (Integer) new_val.getValue();
                            role = new_val.getRole().getValue();
                            try {
                                String projectQuery = "SELECT * FROM projects WHERE id = ?";
                                PreparedStatement pstmt  = null;
                                pstmt = connection.prepareStatement(projectQuery);
                                pstmt.setInt(1, (Integer) id);
                                ResultSet rs    = pstmt.executeQuery();
                                while (rs.next()) {
                                    label_Title_Register.setText(rs.getString("name"));
                                    field_Name.setDisable(false);
                                    field_Name.setText(rs.getString("name"));
                                    field_CreatedAt.setDisable(false);
                                    field_CreatedAt.setText(rs.getString("created_at"));
                                    field_UpdatedAt.setDisable(false);
                                    field_UpdatedAt.setText(rs.getString("updated_at"));
                                    field_Category.setDisable(false);
                                    field_Category.setText(rs.getString("category"));
                                    field_Repository.setDisable(false);
                                    field_Repository.setText(rs.getString("repository"));

                                    pane_Stats.setVisible(true);

                                    String projectMostResearch = "SELECT researches.name as name, users.name as username, users.lastname as userlastname, COUNT(*) as total FROM researches JOIN users ON users.id = researches.user_id WHERE project_id = ? GROUP BY researches.user_id ORDER BY total DESC";
                                    PreparedStatement pstmtMostResearch  = null;
                                    pstmtMostResearch = connection.prepareStatement(projectMostResearch);
                                    pstmtMostResearch.setInt(1, (Integer) id);
                                    ResultSet rsMostResearch    = pstmtMostResearch.executeQuery();

                                    if (rsMostResearch.next()) {
                                        label_Most_Researches.setText(rsMostResearch.getString("username") + " " + rsMostResearch.getString("userlastname") + " (" + rsMostResearch.getString("total") + ")");
                                    } else {
                                        label_Most_Researches.setText("");
                                    }

                                    String projectMostApprovedResearch = "SELECT researches.name as name, users.name as username, users.lastname as userlastname, COUNT(*) as total FROM researches JOIN users ON users.id = researches.user_id WHERE project_id = ? AND researches.approved_by IS NOT NULL GROUP BY researches.user_id ORDER BY total DESC";
                                    PreparedStatement pstmtMostApprovedResearch  = null;
                                    pstmtMostApprovedResearch = connection.prepareStatement(projectMostApprovedResearch);
                                    pstmtMostApprovedResearch.setInt(1, (Integer) id);
                                    ResultSet rsMostApprovedResearch    = pstmtMostApprovedResearch.executeQuery();

                                    if (rsMostApprovedResearch.next()) {
                                        label_Most_Approved_Researches.setText(rsMostApprovedResearch.getString("username") + " " + rsMostApprovedResearch.getString("userlastname") + " (" + rsMostApprovedResearch.getString("total") + ")");
                                    } else {
                                        label_Most_Approved_Researches.setText("");
                                    }

                                    String projectBiggestResearch = "SELECT researches.name as name, users.name as username, users.lastname as userlastname FROM researches JOIN users ON users.id = researches.user_id WHERE project_id = ? AND researches.approved_by IS NOT NULL ORDER BY word_count_unique DESC";
                                    PreparedStatement pstmtBiggestResearch  = null;
                                    pstmtBiggestResearch = connection.prepareStatement(projectBiggestResearch);
                                    pstmtBiggestResearch.setInt(1, (Integer) id);
                                    ResultSet rsBiggestResearch    = pstmtBiggestResearch.executeQuery();

                                    if (rsBiggestResearch.next()) {
                                        label_Biggest_Research.setText(rsBiggestResearch.getString("username") + " " + rsBiggestResearch.getString("userlastname") + " (" + rsBiggestResearch.getString("name") + ")");
                                    } else {
                                        label_Biggest_Research.setText("");
                                    }



                                }

                                if (new_val.getRole().getValue().equals("owner")) {
                                    btn_Delete.setVisible(true);
                                    btn_Users.setVisible(true);
                                } else {
                                    btn_Delete.setVisible(false);
                                    btn_Users.setVisible(false);
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

            label_Title_Register.setText("Editar proyecto");
            field_Name.setDisable(true);
            field_CreatedAt.setDisable(true);
            field_UpdatedAt.setDisable(true);
            field_Category.setDisable(true);
            field_Repository.setDisable(true);
        } else {
            editing = false;

            choices_Projects.setVisible(false);

            label_Title_Register.setText("Registrar proyecto");
            field_Name.setDisable(false);
            field_CreatedAt.setDisable(false);
            field_UpdatedAt.setDisable(false);
            field_Category.setDisable(false);
            field_Repository.setDisable(false);
        }
    }

    @FXML void onDeleteButtonClick() throws  Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String query = "DELETE FROM projects WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);

        pstmt.executeUpdate();

        onRegisterClick();
    }

    @FXML void onUsersButtonClick() throws Exception {
        FXMLLoader scene = new FXMLLoader(RaiotProjectsApplication.class.getResource("project-users.fxml"));
        Parent root = scene.load();
        ProjectUsersController controller = scene.getController();
        controller.initialize(id);
        Stage window = (Stage) btn_Users.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML void onRegisterButtonClick() throws Exception {
        label_Error.setVisible(false);

        if (editing && id == null) {
            label_Error.setText("Necesitas elegir un proyecto para editar");
            label_Error.setVisible(true);
            return;
        }


        label_Error.setText("Ha ingresado valores no validos, asegurese de rellenar todos los espacios como se le indica.");
        label_Error.setVisible(false);

        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String name = field_Name.getText();

        String category = field_Category.getText();

        if (category.matches("^[a-zA-Z]*$") == false) {
            label_Error.setVisible(true);
            label_Error.setText("Solo puede utilizar letras en el nombre de la categoria.");
            return;
        }

        String created_at = field_CreatedAt.getText();

        String updated_at = field_UpdatedAt.getText();

        String repository = field_Repository.getText();


        if (editing && id != null) {

            if (role.equals("owner")) {
                String query = "UPDATE projects SET name = ?, category = ?, created_at = ?, updated_at = ?, repository =? WHERE id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setString(2, category);
                pstmt.setString(3, created_at);
                pstmt.setString(4, updated_at);
                pstmt.setString(5, repository);
                pstmt.setInt(6, id);

                pstmt.executeUpdate();
            } else {
                String query = "INSERT INTO project_changes (name, category, created_at, updated_at, repository, user_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setString(2, category);
                pstmt.setString(3, created_at);
                pstmt.setString(4, updated_at);
                pstmt.setString(5, repository);
                pstmt.setInt(6, RaiotProjectsApplication.user_id);
                pstmt.setInt(7, id);

                pstmt.executeUpdate();
            }
        } else {
            String query = "INSERT INTO projects (name, category, created_at, updated_at, repository, user_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, created_at);
            pstmt.setString(4, updated_at);
            pstmt.setString(5, repository);
            pstmt.setInt(6, RaiotProjectsApplication.user_id);

            pstmt.executeUpdate();


            String queryInserted = "SELECT last_insert_rowid()";

            PreparedStatement pstmtInserted  = connection.prepareStatement(queryInserted);

            ResultSet rsInserted = pstmtInserted.executeQuery();

            if (rsInserted.next()) {
                final Integer insertedID = rsInserted.getInt("last_insert_rowid()");
                String projectQuery = "INSERT INTO project_users(user_id, project_id, role) VALUES(?, ?, ?) \n" +
                        "ON CONFLICT(user_id, project_id) DO UPDATE SET role = ?;";
                PreparedStatement pstmtOwner  = null;
                pstmtOwner = connection.prepareStatement(projectQuery);
                pstmtOwner.setInt(1, RaiotProjectsApplication.user_id);
                pstmtOwner.setInt(2, insertedID);
                pstmtOwner.setString(3, "owner");
                pstmtOwner.setString(4, "owner");
                pstmtOwner.executeUpdate();
            }
        }

        onRegisterClick();

    }

    protected void onRegisterClick() throws  Exception {
        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Register.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }

    @FXML
    protected void onBackLoginButtonClick() throws Exception {
        Parent loginScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Menu.getScene().getWindow();
        window.setScene(new Scene(loginScene));
    }

}
