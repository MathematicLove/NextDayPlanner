package ayzekssoft.nextday.db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BackpackDao {
    public void setItemsForDate(LocalDate date, List<Long> itemIds) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM day_pack_item WHERE date=?")) {
                del.setDate(1, Date.valueOf(date));
                del.executeUpdate();
            }
            try (PreparedStatement ins = conn.prepareStatement("INSERT INTO day_pack_item(date, item_id) VALUES (?,?)")) {
                for (Long itemId : itemIds) {
                    ins.setDate(1, Date.valueOf(date));
                    ins.setLong(2, itemId);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
    }

    public List<Long> getItemsForDate(LocalDate date) throws SQLException {
        String sql = "SELECT item_id FROM day_pack_item WHERE date=? ORDER BY item_id";
        List<Long> result = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getLong(1));
                }
            }
        }
        return result;
    }

    public List<String> getItemNamesForDate(LocalDate date) throws SQLException {
        String sql = "SELECT i.name FROM day_pack_item dpi JOIN item i ON i.id=dpi.item_id WHERE dpi.date=? ORDER BY i.name";
        List<String> result = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString(1));
                }
            }
        }
        return result;
    }
}


