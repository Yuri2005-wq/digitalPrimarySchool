//package com.digitalprimaryschool.digitalprimaryschool.repository;
//import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
//import com.digitalprimaryschool.digitalprimaryschool.model.SyncStatus;
//import com.google.gson.Gson;
//
//import java.io.InputStream;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.sql.*;
//import java.util.UUID;
//import java.util.stream.Collector;
//import java.util.stream.Collectors;
//
//public class EleveDAO {
//    private final Connection connection;
//    private final Gson gson = new Gson();
//
//    public EleveDAO(Connection connection) {
//        this.connection = connection;
//    }
//    public void insert(Eleve eleve) throws SQLException {
//        eleve.touch();
//        eleve.setSyncStatus(SyncStatus.valueOf("PENDING_REMOTELY"));
//
//        String sqlEleve = "INSERT INTO Eleve ()";
//        String sqlOutbox = "";
//
//        boolean defaultCommit = connection.getAutoCommit();
//        try {
//            connection.setAutoCommit(false);
//            try(PreparedStatement stmt = connection.prepareStatement(sqlEleve)){
//                stmt.setString(1, eleve.getMatricule());
//                stmt.setString(2, eleve.getParent().toString());
//                stmt.setString(3, eleve.getNom());
//                stmt.setString(4, eleve.getPrenom());
//                stmt.setDate(5, (Date) eleve.getDateNaissance());
//                stmt.setString(6, eleve.getSexe());
//                stmt.setBoolean(7, eleve.getATerminerPension());
//                stmt.setString(8, eleve.getSyncStatus());
//                stmt.setLong(9, eleve.getUpdatedAt());
//                stmt.executeUpdate();
//            }
//            try(PreparedStatement stmt = connection.prepareStatement(sqlOutbox)){
//                stmt.setString(1, UUID.randomUUID().toString()); // ID unique de l'action
//                stmt.setString(2, "CREATE");                    // Type d'action
//                stmt.setString(3, "ELEVE");                     // Type d'entité
//                stmt.setString(4, eleve.getIdMatricule());       // ID de l'entité
//                stmt.setString(5, jsonPayload);                 // Données JSON
//                stmt.setLong(6, System.currentTimeMillis());
//                stmt.executeUpdate();
//            }
//            connection.commit();
//            System.out.println("Execution");
//        }catch (SQLException e){
//            connection.rollback();
//            throw e;
//        }finally {
//            connection.setAutoCommit(defaultCommit);
//        }
//    }
//    public void updateSyncStatus(String idEleve, String status) throws SQLException{
//        String sql = "UPDATE Eleve SET sync_status = ? WHERE idMatricule = ?";
//        try(PreparedStatement stmt = connection.prepareStatement(sql)){
//            stmt.setString(1, status);
//            stmt.setString(2, idEleve);
//            stmt.executeUpdate();
//        }
//    }
//}
