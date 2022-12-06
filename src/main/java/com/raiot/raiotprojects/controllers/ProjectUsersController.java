package com.raiot.raiotprojects.controllers;

import com.raiot.raiotprojects.RaiotProjectsApplication;
import com.raiot.raiotprojects.classes.ChoiceClass;
import com.raiot.raiotprojects.classes.ChoiceClassString;
import com.raiot.raiotprojects.dao.SQLiteDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProjectUsersController implements Initializable {

    @FXML
    private ListView<ChoiceClass> listview_Users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<ChoiceClassString> choices = FXCollections.observableArrayList();
        ObservableList<ChoiceClass> users = FXCollections.observableArrayList();
        choices.add(new ChoiceClassString("Líder", "leader"));
        choices.add(new ChoiceClassString("Investigador", "researcher"));

        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();
        String query = "SELECT * FROM users";

        PreparedStatement pstmt  = null;
        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rs    = pstmt.executeQuery();
            while(rs.next()) {
                users.add(new ChoiceClass(rs.getString("name") + rs.getString("lastname"), rs.getInt("id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        listview_Users.getItems().addAll(users);

        listview_Users.setCellFactory(param -> new ListCell<ChoiceClass>() {

            private ChoiceBox choiceBox = new ChoiceBox<>(choices);
            private BorderPane bp = new BorderPane(choiceBox);
            @Override
            protected void updateItem(ChoiceClass item, boolean empty) {
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setGraphic(bp);
                }
            }
        });
    }
}
