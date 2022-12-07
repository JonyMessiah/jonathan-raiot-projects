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
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProjectUsersController {

    Integer project_id;
    Integer owner_id;

    @FXML
    Button btn_Menu;

    @FXML
    private ListView<ChoiceClassUserOnProject> listview_Users;

    public void initialize(Integer project_id) {

        this.project_id = project_id;


        ObservableList<ChoiceClassString> choices = FXCollections.observableArrayList();
        ObservableList<ChoiceClassUserOnProject> users = FXCollections.observableArrayList();
        choices.add(new ChoiceClassString("Líder", "leader"));
        choices.add(new ChoiceClassString("Investigador", "researcher"));

        SQLiteDao sqlite = new SQLiteDao();
        Connection connection = sqlite.getConnection();
        String query = "SELECT users.*, project_users.role as role FROM users LEFT JOIN project_users ON (users.id = project_users.user_id AND project_users.project_id = ?)";

        PreparedStatement pstmt  = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, project_id);
            ResultSet rs    = pstmt.executeQuery();
            while(rs.next()) {
                String roleId = rs.getString("role");
                System.out.println(roleId);
                String roleName = "";
                if (roleId != null) {
                    if (roleId.equals("researcher")) {
                        roleName = "Investigador";
                    }
                    if (roleId.equals("leader")) {
                        roleName = "Líder";
                    }
                    if (roleId.equals("owner")) {
                        roleName = "Gestor";
                    }
                }
                users.add(new ChoiceClassUserOnProject(
                        rs.getInt("id"),
                        rs.getString("name") + " " + rs.getString("lastname"),
                        new ChoiceClassString(roleName, roleId)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        listview_Users.getItems().addAll(users);

        listview_Users.setCellFactory(param -> new ListCell<ChoiceClassUserOnProject>() {
            private ChoiceBox choiceBox = new ChoiceBox<ChoiceClassString>(choices);

            private Label ownerLabel = new Label("Gestor");
            private BorderPane bp = new BorderPane();
            @Override
            protected void updateItem(ChoiceClassUserOnProject item, boolean empty) {
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    if (item.getRole().getValue() == null || !item.getRole().getValue().equals("owner")) {
                        if (item.getRole().getValue() != null) {
                            choiceBox.setValue(item.getRole());
                        }
                        choiceBox.getSelectionModel().selectedItemProperty().addListener(
                                new ChangeListener<ChoiceClassString>() {
                                    public void changed(ObservableValue<? extends ChoiceClassString> ov,
                                                        ChoiceClassString old_val, ChoiceClassString new_val) {

                                        if (old_val != null && old_val.getValue() == new_val.getValue()) return;

                                        item.setRole(new_val);
                                        String id = new_val.getValue();
                                        System.out.println(id);
                                        try {
                                            String projectQuery = "INSERT INTO project_users(user_id, project_id, role) VALUES(?, ?, ?) \n" +
                                                    "ON CONFLICT(user_id, project_id) DO UPDATE SET role = ?;";
                                            PreparedStatement pstmt  = null;
                                            pstmt = connection.prepareStatement(projectQuery);
                                            pstmt.setInt(1, (Integer) item.getValue());
                                            pstmt.setInt(2, project_id);
                                            pstmt.setString(3, new_val.getValue());
                                            pstmt.setString(4, new_val.getValue());
                                            pstmt.executeUpdate();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                        bp.setRight(choiceBox);
                        setGraphic(bp);
                    } else {
                        bp.setRight(ownerLabel);
                        setGraphic(bp);
                    }
                }
            }
        });
    }

    @FXML
    protected void onBackLoginButtonClick() throws Exception {
        Parent loginScene = FXMLLoader.load(RaiotProjectsApplication.class.getResource("home.fxml"));
        Stage window = (Stage) btn_Menu.getScene().getWindow();
        window.setScene(new Scene(loginScene));
    }
}
