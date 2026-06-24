package com.digitalprimaryschool.digitalprimaryschool.dao;

import com.digitalprimaryschool.digitalprimaryschool.Database;
import com.digitalprimaryschool.digitalprimaryschool.model.Tranche;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrancheDAO {

    public void enregistrerTranche(Tranche tranche) throws SQLException {
        String sql = """
                INSERT INTO Tranche (idTranche, idTarifScolaire, libelleTranche, montantTranche, dateEcheance, estPaye)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tranche.getIdTranche());
            stmt.setString(2, tranche.getIdTarifScolaire());
            stmt.setString(3, tranche.getLibelleTranche());
            stmt.setDouble(4, tranche.getMontantTranche());
            stmt.setString(5, tranche.getDateEcheance());
            stmt.setInt(6, tranche.isEstPaye() ? 1 : 0);
            stmt.executeUpdate();
        }
    }

    public List<Tranche> listerParTarif(String idTarifScolaire) throws SQLException {
        String sql = "SELECT * FROM Tranche WHERE idTarifScolaire = ?";
        List<Tranche> tranches = new ArrayList<>();
        try (Connection conn = Database.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idTarifScolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tranche t = new Tranche();
                    t.setIdTranche(rs.getString("idTranche"));
                    t.setIdTarifScolaire(rs.getString("idTarifScolaire"));
                    t.setLibelleTranche(rs.getString("libelleTranche"));
                    t.setMontantTranche(rs.getDouble("montantTranche"));
                    t.setDateEcheance(rs.getString("dateEcheance"));
                    t.setEstPaye(rs.getInt("estPaye") == 1);
                    tranches.add(t);
                }
            }
        }
        return tranches;
    }
}