//package com.digitalprimaryschool.digitalprimaryschool.dao;
//
//import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
//import com.digitalprimaryschool.digitalprimaryschool.model.SyncStatus;
//import com.google.gson.Gson;
//import java.sql.*;
//
//public class ClasseDAO {
//    private final Connection connection;
//    private final SyncOutboxDAO outboxDAO;
//    private final Gson gson = new Gson();
//
//    public ClasseDAO(Connection connection, SyncOutboxDAO outboxDAO) {
//        this.connection = connection;
//        this.outboxDAO = outboxDAO;
//    }
//
//    public void insert(Classe classe) throws SQLException {
//        classe.touch();
//        classe.setSyncStatus(SyncStatus.valueOf("PENDING_REMOTELY"));
//
//        // Note : idEcoleprimaire est requis par ta structure de clé primaire composite (idEcoleprimaire, idClasse)
//        String sqlClasse = "INSERT INTO Classe (idEcoleprimaire, idClasse, idTarifScolaire, nom, niveau, capaciteMax, section, sync_status, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        boolean defaultCommit = connection.getAutoCommit();
//        try {
//            connection.setAutoCommit(false);
//
//            try (PreparedStatement stmt = connection.prepareStatement(sqlClasse)) {
//                stmt.setString(1, "ECOLE-01"); // Valeur temporaire ou dynamique de ton contexte d'école
//                stmt.setString(2, "CLASSE-ID"); // Correspond au format texte SQLite ou liaison UUID
//                stmt.setInt(3, classe.getTarisScolaire() != null ? 1 : 0);
//                stmt.setString(4, classe.getNom());
//                stmt.setString(5, classe.getNiveau());
//                stmt.setInt(6, 50); // capaciteMax par défaut si non encapsulé
//                stmt.setString(7, classe.getNom());
//                stmt.setString(8, classe.getSyncStatus());
//                stmt.setLong(9, classe.getUpdatedAt());
//                stmt.executeUpdate();
//            }
//
//            outboxDAO.saveAction("CREATE", "CLASSE", "CLASSE-ID", gson.toJson(classe));
//
//            connection.commit();
//        } catch (SQLException e) {
//            connection.rollback();
//            throw e;
//        } finally {
//            connection.setAutoCommit(defaultCommit);
//        }
//    }
//
//    public void updateSyncStatus(String idClasse, String status) throws SQLException {
//        String sql = "UPDATE Classe SET sync_status = ? WHERE idClasse = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, status);
//            stmt.setString(2, idClasse);
//            stmt.executeUpdate();
//        }
//    }
//}