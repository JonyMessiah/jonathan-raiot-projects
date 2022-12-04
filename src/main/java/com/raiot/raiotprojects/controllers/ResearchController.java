package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.classes.ChoiceClass;
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
    Label label_Title_Register;

    void setEdit(Boolean edit) throws SQLException {
        btn_Delete.setVisible(false);
        if (edit) {

            SQLiteDao sqlite = new SQLiteDao();
            Connection connection = sqlite.getConnection();
            String query = "SELECT * FROM researches WHERE  user_id = ?";

            PreparedStatement pstmt  = connection.prepareStatement(query);
            pstmt.setInt(1, RaiotProjectsApplication.user_id);
            ResultSet rs    = pstmt.executeQuery();

            editing = true;

            ObservableList<ChoiceClass> choices = FXCollections.observableArrayList();

            while (rs.next()) {
                ChoiceClass item = new ChoiceClass(rs.getString("name"), rs.getInt("id"));
                choices.add(item);
            }

            choices_Researches.setVisible(true);
            choices_Researches.setItems(choices);

            choices_Researches.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<ChoiceClass>() {
                        public void changed(ObservableValue<? extends ChoiceClass> ov,
                                            ChoiceClass old_val, ChoiceClass new_val) {
                            id = (Integer) new_val.getValue();
                            try {
                                String projectQuery = "SELECT * FROM researches WHERE id = ?";
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
                                    field_Subtitle.setDisable(false);
                                    field_Subtitle.setText(rs.getString("subtitle"));
                                    field_Theme.setDisable(false);
                                    field_Theme.setText(rs.getString("theme"));
                                    field_Autor.setDisable(false);
                                    field_Autor.setText(rs.getString("author"));
                                    field_Category.setDisable(false);
                                    field_Category.setText(rs.getString("category"));
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
        }
    }

    //boton register
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

        if (editing && id != null) {

            String query = "UPDATE researches SET name = ?, category = ?, created_at = ?, updated_at = ?, theme = ?, subtitle = ?, author = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, created_at);
            pstmt.setString(4, updated_at);
            pstmt.setString(5, theme);
            pstmt.setString(6, subtitle);
            pstmt.setString(7, autor);
            pstmt.setInt(8,  id);
            pstmt.executeUpdate();
        } else {

            String query = "INSERT INTO researches (name, category, created_at, updated_at, theme, subtitle, author, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, created_at);
            pstmt.setString(4, updated_at);
            pstmt.setString(5, theme);
            pstmt.setString(6, subtitle);
            pstmt.setString(7, autor);
            pstmt.setInt(8, RaiotProjectsApplication.user_id);
            pstmt.executeUpdate();
        }

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

}
