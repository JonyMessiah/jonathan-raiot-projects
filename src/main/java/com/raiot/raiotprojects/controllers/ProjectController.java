package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProjectController {
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
    TextField field_UpdatedAt;

    @FXML
    TextField field_Repository;

    @FXML void onRegisterButtonClick() throws Exception {
        label_Error.setText("Ha ingresado valores no validos, asegurese de rellenar todos los espacios como se le indica.");
        label_Error.setVisible(false);

        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        // TODO validate if email exists

        //TODO Validations, including age being a number

        String name = field_Name.getText();

        String category = field_Category.getText();

        String created_at = field_CreatedAt.getText();

        String updated_at = field_UpdatedAt.getText();

        String repository = field_Repository.getText();


        String age = field_CreatedAt.getText();
        if (created_at.matches("[0-9, /]+") == false) {
            label_Error.setVisible(true);
            label_Error.setText("Debe escribir la fecha de la siguiente manera: 19/10/2022. Asegurese de usar numeros.");
            return;
        }

        String query = "INSERT INTO users (name, category, created_at, updated_at, repository) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setString(2, category);
        pstmt.setString(3, created_at);
        pstmt.setString(4, updated_at);
        pstmt.setString(5, repository);

        pstmt.executeUpdate();

    }

}
