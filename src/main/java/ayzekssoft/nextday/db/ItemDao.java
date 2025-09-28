package ayzekssoft.nextday.db;

import ayzekssoft.nextday.dto.Clothes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    public long insert(String name, String category, String subtype, String imagePath) throws SQLException {
        String sql = "INSERT INTO item(name, category, subtype, image_path) VALUES(?,?,?,?) RETURNING id";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, subtype);
            ps.setString(4, imagePath);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM item WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public List<Clothes> findByCategory(String category) throws SQLException {
        String sql = "SELECT id, name, category, image_path FROM item WHERE category=? ORDER BY name";
        List<Clothes> list = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Clothes(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getString("image_path")
                    ));
                }
            }
        }
        return list;
    }

    public List<Clothes> findByCategoryAndSubtype(String category, String subtype) throws SQLException {
        String sql = "SELECT id, name, category, image_path FROM item WHERE category=? AND subtype=? ORDER BY name";
        List<Clothes> list = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, subtype);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Clothes(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getString("image_path")
                    ));
                }
            }
        }
        return list;
    }

    public String findNameById(long id) throws SQLException {
        String sql = "SELECT name FROM item WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        }
        return null;
    }
}


