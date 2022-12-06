package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.classes.ChoiceClass;
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
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectController {
    @FXML
    Label label_Error;


    @FXML
    Button btn_Register;

    @FXML
    Button btn_Delete;

    @FXML
    Button bnt_Users;

    @FXML
    TextField field_Name;

    @FXML
    TextField field_Category;
    @FXML
    TextField field_CreatedAt;

    @FXML
    TextField field_UpdatedAt;

    @FXML
    TextField field_Repository;

    @FXML
    Label label_Title_Register;

    @FXML
    ChoiceBox choices_Projects;


    Integer id;
    Boolean editing = false;

    void setEdit(Boolean edit) throws SQLException {
        btn_Delete.setVisible(false);
        bnt_Users.setVisible(false);
        if (edit) {

            SQLiteDao sqlite = new SQLiteDao();
            Connection connection = sqlite.getConnection();
            String query = "SELECT * FROM projects WHERE  user_id = ?";

            PreparedStatement pstmt  = connection.prepareStatement(query);
            pstmt.setInt(1, RaiotProjectsApplication.user_id);
            ResultSet rs    = pstmt.executeQuery();

            editing = true;

            ObservableList<ChoiceClass> choices = FXCollections.observableArrayList();

            while (rs.next()) {
                ChoiceClass item = new ChoiceClass(rs.getString("name"), rs.getInt("id"));
                choices.add(item);
            }

            choices_Projects.setVisible(true);
            choices_Projects.setItems(choices);

            choices_Projects.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<ChoiceClass>() {
                        public void changed(ObservableValue<? extends ChoiceClass> ov,
                                            ChoiceClass old_val, ChoiceClass new_val) {
                            id = (Integer) new_val.getValue();
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
                                }
                                btn_Delete.setVisible(true);
                                bnt_Users.setVisible(true);
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
            String query = "INSERT INTO projects (name, category, created_at, updated_at, repository, user_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, created_at);
            pstmt.setString(4, updated_at);
            pstmt.setString(5, repository);
            pstmt.setInt(6, RaiotProjectsApplication.user_id);

            pstmt.executeUpdate();
        }

        onRegisterClick();

    }

    protected void onRegisterClick() throws  Exception {
        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Register.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }

}
