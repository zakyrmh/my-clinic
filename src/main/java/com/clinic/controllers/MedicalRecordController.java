// package com.clinic.controllers;

// import com.clinic.manager.UserSession;
// import com.clinic.models.MedicalRecord;
// import com.clinic.utils.DatabaseUtil;

// import javafx.collections.ObservableList;
// import javafx.fxml.FXML;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.cell.PropertyValueFactory;

// public class MedicalRecordController {
//     @FXML private TableView<MedicalRecord> tableView;
//     @FXML private TableColumn<MedicalRecord, Integer> no;
//     @FXML private TableColumn<MedicalRecord, String> namePatient;
//     @FXML private TableColumn<MedicalRecord, String> nameDoctor;
//     @FXML private TableColumn<MedicalRecord, String> visitDate;
//     @FXML private TableColumn<MedicalRecord, Void> action;

//     @FXML
//     public void initialize() {
//         no.setCellValueFactory(cellData -> {
//             int index = tableView.getItems().indexOf(cellData.getValue()) + 1;
//             return javafx.beans.binding.Bindings.createObjectBinding(() -> index);
//         });
//         no.setSortable(false);
//         namePatient.setCellValueFactory(new PropertyValueFactory<>("namePatient"));
//         nameDoctor.setCellValueFactory(new PropertyValueFactory<>("nameDoctor"));
//         visitDate.setCellValueFactory(new PropertyValueFactory<>("visitDate"));

//         configureActionColumn();

//         if (UserSession.getInstance().isLoggedIn()) {
//             loadMedicalRecordData();
//         }
//     }

//     private void loadMedicalRecordData() {
//         ObservableList<MedicalRecord> medicalRecords = FXCollections.observableArrayList();

//         try (Connection conn = DatabaseUtil.getConnection();
//              Statement stmt = conn.createStatement();
//              ResultSet rs = stmt.executeQuery("SELECT * FROM medical_records")) {

//                 while (rs.next()) {
//                     MedicalRecord medicalRecord = new MedicalRecord(
//                         // query select
//                     )
//                 }
//              }
//     }
// }
