package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class MediaController {

    @FXML
    Button btn_Add;

    @FXML
    Button btn_Menu;

    @FXML
    TextArea area_content;



    String created_at;

    String authorship;

    String archives;

    String content;

    @FXML
    void onAddButtonClick() throws Exception {
        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();

        content = area_content.getText();


    String query = "INSERT INTO research_media (archives, created_at, authorship, content) VALUES (?, ?, ?, ?)";
    PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, archives);
        pstmt.setString(2, created_at);
        pstmt.setString(3, authorship);
        pstmt.setString(4, content);

        pstmt.executeUpdate();

    onBackLoginButtonClick();
}

    @FXML
    protected void onBackLoginButtonClick() throws Exception {
        Parent loginScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Menu.getScene().getWindow();
        window.setScene(new Scene(loginScene));
    }


}
