package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

    @FXML void onRegisterButtonClick() throws Exception {
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

        if (created_at.matches("[0-9]+") == false) {
            label_Error.setVisible(true);
            label_Error.setText("La fecha debe de estar puesta con numeros.");
            return;
        }

        String updated_at = field_UpdatedAt.getText();

        if (updated_at.matches("[0-9]+") == false) {
            label_Error.setVisible(true);
            label_Error.setText("La fecha debe de estar puesta con numeros.");
            return;
        }
        String query = "INSERT INTO researchs (name, category, created_at, updated_at, theme, subtitle, autor, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

        onRegisterClick();

    }

    protected void onRegisterClick() throws  Exception {
        Parent registerScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Register.getScene().getWindow();
        window.setScene(new Scene(registerScene));
    }

}
