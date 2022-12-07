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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResearchController {
    @FXML
    Label label_Error;


    @FXML
    Button btn_Register;


    @FXML
    TextField field_Name;

    @FXML
    TextField field_Category;
    @FXML
    TextField field_CreatedAt;

    @FXML
    TextField field_Theme;

    @FXML
    TextField field_Subtitle;

    @FXML
    TextField field_Autor;

    @FXML
    TextField field_Title;

    @FXML
    TextField field_Content;


    @FXML
    TextField field_UpdatedAt;

    @FXML
    TextField field_Repository;

    Integer id;
    Boolean editing = false;

    @FXML
    ChoiceBox choices_Researches;

    @FXML
    Button btn_Delete;

    @FXML
    Button btn_Menu;

    @FXML
    Label label_Title_Register;

    String role;
    Integer project_id;

    @FXML
    ChoiceBox choices_Projects;

    @FXML
    Button btn_Approve;

    @FXML
    Label label_Status;

    void setEdit(Boolean edit) throws SQLException {

        label_Status.setText("Status: Pendiente");

        btn_Delete.setVisible(false);

        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();
        String queryProjects = "SELECT projects.*, project_users.role as role FROM projects JOIN project_users ON projects.id = project_users.project_id WHERE project_users.user_id = ?";

        PreparedStatement pstmtProjects  = connection.prepareStatement(queryProjects);
        pstmtProjects.setInt(1, RaiotProjectsApplication.user_id);
        ResultSet rsProjects = pstmtProjects.executeQuery();

        ObservableList<ChoiceClassUserOnProject> choicesProjects = FXCollections.observableArrayList();

        while (rsProjects.next()) {
            ChoiceClassUserOnProject item = new ChoiceClassUserOnProject(rsProjects.getInt("id"), rsProjects.getString("name"), new ChoiceClassString("", rsProjects.getString("role")));
            choicesProjects.add(item);
        }


        choices_Projects.setItems(choicesProjects);

        choices_Projects.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ChoiceClassUserOnProject>() {
                    public void changed(ObservableValue<? extends ChoiceClassUserOnProject> ov,
                                        ChoiceClassUserOnProject old_val, ChoiceClassUserOnProject new_val) {
                        project_id = (Integer) new_val.getValue();
                        role = new_val.getRole().getValue();
                    }
                });

        if (edit) {
            String query = "SELECT * FROM researches JOIN project_users ON project_users.project_id = researches.project_id WHERE project_users.user_id = ? AND (researches.user_id = ? OR (project_users.role = ? OR project_users.role = ?))";

            PreparedStatement pstmt  = connection.prepareStatement(query);
            pstmt.setInt(1, RaiotProjectsApplication.user_id);
            pstmt.setInt(2, RaiotProjectsApplication.user_id);
            pstmt.setString(3, "owner");
            pstmt.setString(4, "leader");
            ResultSet rs    = pstmt.executeQuery();

            editing = true;

            ObservableList<ChoiceClass> choices = FXCollections.observableArrayList();

            while (rs.next()) {
                ChoiceClass item = new ChoiceClass(rs.getString("name"), rs.getInt("id"));
                choices.add(item);
            }

            choices_Researches.setVisible(true);
            choices_Projects.setVisible(false);

            choices_Researches.setItems(choices);

            choices_Researches.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<ChoiceClass>() {
                        public void changed(ObservableValue<? extends ChoiceClass> ov,
                                            ChoiceClass old_val, ChoiceClass new_val) {
                            id = (Integer) new_val.getValue();
                            try {
                                String projectQuery = "SELECT researches.*, project_users.role as role FROM researches JOIN project_users ON project_users.project_id = researches.project_id WHERE researches.id = ? AND project_users.user_id = ?";
                                PreparedStatement pstmt  = null;
                                pstmt = connection.prepareStatement(projectQuery);
                                pstmt.setInt(1, (Integer) id);
                                pstmt.setInt(2, RaiotProjectsApplication.user_id);
                                ResultSet rs    = pstmt.executeQuery();
                                while (rs.next()) {
                                    label_Title_Register.setText(rs.getString("name"));
                                    field_Name.setDisable(false);
                                    field_Name.setText(rs.getString("name"));
                                    field_CreatedAt.setDisable(false);
                                    field_CreatedAt.setText(rs.getString("created_at"));
                                    field_UpdatedAt.setDisable(false);
                                    field_UpdatedAt.setText(rs.getString("updated_at"));
                                    field_Subtitle.setDisable(false);
                                    field_Subtitle.setText(rs.getString("subtitle"));
                                    field_Title.setDisable(false);
                                    field_Title.setText(rs.getString("title"));
                                    field_Theme.setDisable(false);
                                    field_Theme.setText(rs.getString("theme"));
                                    field_Autor.setDisable(false);
                                    field_Autor.setText(rs.getString("author"));
                                    field_Category.setDisable(false);
                                    field_Category.setText(rs.getString("category"));
                                    field_Content.setDisable(false);
                                    field_Content.setText(rs.getString("content"));
                                    role = rs.getString("role");
                                    Integer approved_by = rs.getInt("approved_by");
                                    if (rs.wasNull()) {
                                        label_Status.setText("Status: Pendiente");

                                        if (role.equals("owner") || role.equals("leader")) {
                                            btn_Approve.setVisible(true);
                                        } else {
                                            btn_Approve.setVisible(false);
                                        }
                                    } else {
                                        label_Status.setText("Status: Aprobada");
                                    }

                                }
                                btn_Delete.setVisible(true);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

            label_Title_Register.setText("Editar investigación");
            field_Name.setDisable(true);
            field_CreatedAt.setDisable(true);
            field_UpdatedAt.setDisable(true);
            field_Subtitle.setDisable(true);
            field_Theme.setDisable(true);
            field_Autor.setDisable(true);
            field_Category.setDisable(true);
            field_Title.setDisable(true);
            field_Content.setDisable(true);

        } else {
            editing = false;

            choices_Researches.setVisible(false);

            label_Title_Register.setText("Registrar Investigación");
            field_Name.setDisable(false);
            field_CreatedAt.setDisable(false);
            field_UpdatedAt.setDisable(false);
            field_Subtitle.setDisable(false);
            field_Theme.setDisable(false);
            field_Autor.setDisable(false);
            field_Category.setDisable(false);
            field_Title.setDisable(false);
            field_Content.setDisable(false);


        }
    }

    @FXML void onRegisterButtonClick() throws Exception {
        label_Error.setVisible(false);

        if (editing && id == null) {
            label_Error.setText("Necesitas elegir una investigación para editar");
            label_Error.setVisible(true);
            return;
        }

        label_Error.setText("Ha ingresado valores no validos, asegurese de rellenar todos los espacios como se le indica.");
        label_Error.setVisible(false);

        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String name = field_Name.getText();
        String theme = field_Theme.getText();
        String subtitle = field_Subtitle.getText();
        String autor = field_Autor.getText();


        String category = field_Category.getText();

        if (category.matches("^[a-zA-Z]*$") == false) {
            label_Error.setVisible(true);
            label_Error.setText("Solo puede utilizar letras en el nombre de la categoria.");
            return;
        }

        String created_at = field_CreatedAt.getText();

        String updated_at = field_UpdatedAt.getText();

        String title = field_Title.getText();

        String content = field_Content.getText();

        if (editing && id != null) {

            if (role.equals("owner") || role.equals("leader")) {
                String query = "UPDATE researches SET name = ?, category = ?, created_at = ?, updated_at = ?, theme = ?, subtitle = ?, author = ?, title = ?, content= ?, approved_by = ?  WHERE id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setString(2, category);
                pstmt.setString(3, created_at);
                pstmt.setString(4, updated_at);
                pstmt.setString(5, theme);
                pstmt.setString(6, subtitle);
                pstmt.setString(7, autor);
                pstmt.setString(8, title);
                pstmt.setString(9, content);
                pstmt.setInt(10, RaiotProjectsApplication.user_id);
                pstmt.setInt(11,  id);
                pstmt.executeUpdate();
            } else {
                String query = "UPDATE researches SET name = ?, category = ?, created_at = ?, updated_at = ?, theme = ?, subtitle = ?, author = ?, title = ?, content= ?, approved_by = NULL  WHERE id = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setString(2, category);
                pstmt.setString(3, created_at);
                pstmt.setString(4, updated_at);
                pstmt.setString(5, theme);
                pstmt.setString(6, subtitle);
                pstmt.setString(7, autor);
                pstmt.setString(8, title);
                pstmt.setString(9, content);
                pstmt.setInt(10,  id);
                pstmt.executeUpdate();
            }

        } else {

            if (role.equals("owner") || role.equals("leader")) {
                String query = "INSERT INTO researches (name, category, created_at, updated_at, theme, subtitle, author, title, content, user_id, project_id, approved_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setString(2, category);
                pstmt.setString(3, created_at);
                pstmt.setString(4, updated_at);
                pstmt.setString(5, theme);
                pstmt.setString(6, subtitle);
                pstmt.setString(7, autor);
                pstmt.setString(8, title);
                pstmt.setString(9, content);
                pstmt.setInt(10, RaiotProjectsApplication.user_id);
                pstmt.setInt(11, project_id);
                pstmt.setInt(12, RaiotProjectsApplication.user_id);
                pstmt.executeUpdate();
            } else {
                String query = "INSERT INTO researches (name, category, created_at, updated_at, theme, subtitle, author, title, content, user_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setString(2, category);
                pstmt.setString(3, created_at);
                pstmt.setString(4, updated_at);
                pstmt.setString(5, theme);
                pstmt.setString(6, subtitle);
                pstmt.setString(7, autor);
                pstmt.setString(8, title);
                pstmt.setString(9, content);
                pstmt.setInt(10, RaiotProjectsApplication.user_id);
                pstmt.setInt(11, project_id);
                pstmt.executeUpdate();
            }
        }

        onRegisterClick();

    }

    @FXML void onApproveClick() throws  Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String query = "UPDATE researches SET approved_by = ? WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, RaiotProjectsApplication.user_id);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();

        onRegisterClick();
    }

    protected void onRegisterClick() throws  Exception {
        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Register.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }

    @FXML void onDeleteButtonClick() throws  Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String query = "DELETE FROM researches WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);

        pstmt.executeUpdate();

        onRegisterClick();
    }

    @FXML
    protected void onBackLoginButtonClick() throws Exception {
        Parent loginScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Menu.getScene().getWindow();
        window.setScene(new Scene(loginScene));
    }

}
