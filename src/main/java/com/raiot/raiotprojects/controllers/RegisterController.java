package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

    @FXML void onRegisterButtonClick() throws Exception {
        label_Error.setText("Ha ingresado valores no validos, asegurese de rellenar todos los espacios como se le indica.");
        label_Error.setVisible(false);

        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        String name = field_Name.getText();

        if (name.matches("^[a-zA-Z]$") == true) {
            label_Error.setVisible(true);
            label_Error.setText("Solo puede utilizar letras en su nombre.");
            return;
        }

        String lastname = field_LastName.getText();

        if (lastname.matches("^[a-zA-Z]$") == true) {
            label_Error.setVisible(true);
            label_Error.setText("Solo puede utilizar letras en su apellido.");
            return;

        }

        String email = field_Email.getText();
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {


        } else {
            label_Error.setVisible(true);
            label_Error.setText("Por favor, ingrese una dirección de correo valida.");
            return;
        }


        String password = field_Password.getText();

        String age = field_Age.getText();
        if (age.matches("[0-9]+") == false) {
            label_Error.setVisible(true);
            label_Error.setText("Edad debe ser un numero");
            return;
        }

        String query = "INSERT INTO users (name, lastname, email, password, age) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setString(2, lastname);
        pstmt.setString(3, email);
        pstmt.setString(4, password);
        pstmt.setString(5, age);

        pstmt.executeUpdate();

        onBackLoginButtonClick();
    }

    @FXML
    protected void onBackLoginButtonClick() throws Exception {
        Parent loginScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("login.fxml"));
        Stage window = (Stage) btn_BackLogin.getScene().getWindow();
        window.setScene(new Scene(loginScene));
    }

}


