package com.student;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.*;

import java.sql.*;

public class Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, String> colYear;

    private ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;

    @FXML
    public void initialize() {
        conn = DBConnection.connect();
        if (conn == null) {
            showError("Database Connection Error", "Could not connect to the PostgreSQL database.");
            return;
        }

        // Load Enum to ChoiceBox
        cbYear.getItems().setAll(YearLevel.values());

        // Table Columns Binding
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        loadData();

        // Selection listener is more reliable than a mouse-click handler.
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, s) -> {
            if (s == null) {
                return;
            }

            selectedId = s.getId();
            txtName.setText(s.getName());
            txtCourse.setText(s.getCourse());
            cbYear.setValue(YearLevel.fromDisplayText(s.getYearLevel()));
        });
    }

    private void loadData() {
        if (conn == null) {
            return;
        }

        list.clear();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY id")) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
            table.setItems(list);

        } catch (Exception e) {
            showError("Load Error", "Failed to load student records.");
            e.printStackTrace();
        }
    }

    @FXML
    private void addStudent() {
        if (!validateForm()) {
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)")) {
            pst.setString(1, txtName.getText());
            pst.setString(2, txtCourse.getText());
            pst.setString(3, cbYear.getValue().toString());

            pst.executeUpdate();
            loadData();
            clearFields();

        } catch (Exception e) {
            showError("Insert Error", "Failed to add the student record.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showError("Selection Required", "Select a student from the table before updating.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement(
                "UPDATE students SET name=?, course=?, year_level=? WHERE id=?")) {

            pst.setString(1, txtName.getText());
            pst.setString(2, txtCourse.getText());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);

            pst.executeUpdate();
            loadData();
            clearFields();

        } catch (Exception e) {
            showError("Update Error", "Failed to update the selected student.");
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showError("Selection Required", "Select a student from the table before deleting.");
            return;
        }

        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM students WHERE id=?")) {

            pst.setInt(1, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();

        } catch (Exception e) {
            showError("Delete Error", "Failed to delete the selected student.");
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        table.getSelectionModel().clearSelection();
        selectedId = -1;
    }

    private boolean validateForm() {
        if (txtName.getText().isBlank() || txtCourse.getText().isBlank() || cbYear.getValue() == null) {
            showError("Validation Error", "Name, course, and year level are required.");
            return false;
        }
        return true;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
