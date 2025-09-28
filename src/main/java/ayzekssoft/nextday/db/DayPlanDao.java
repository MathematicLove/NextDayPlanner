package ayzekssoft.nextday.db;

import ayzekssoft.nextday.dto.Day;

import java.sql.*;
import java.time.LocalDate;

public class DayPlanDao {
    public Day findOrCreate(LocalDate date) throws SQLException {
        Day day = find(date);
        if (day == null) {
            try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO day_plan(date) VALUES (?)")) {
                ps.setDate(1, Date.valueOf(date));
                ps.executeUpdate();
            }
            day = find(date);
        }
        return day;
    }

    public Day find(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM day_plan WHERE date=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Day d = new Day();
                    d.setDate(date);
                    d.setTshirtId(getLongOrNull(rs, "tshirt_id"));
                    d.setSweatshirtId(getLongOrNull(rs, "sweatshirt_id"));
                    d.setJeansId(getLongOrNull(rs, "jeans_id"));
                    d.setShoesId(getLongOrNull(rs, "shoes_id"));
                    d.setJacketId(getLongOrNull(rs, "jacket_id"));
                    d.setHomeTshirtId(getLongOrNull(rs, "home_tshirt_id"));
                    d.setHomeUnderwearId(getLongOrNull(rs, "home_underwear_id"));
                    d.setHomePantsId(getLongOrNull(rs, "home_pants_id"));
                    d.setUnderwearPantiesId(getLongOrNull(rs, "underwear_panties_id"));
                    d.setUnderwearSocksId(getLongOrNull(rs, "underwear_socks_id"));
                    d.setUndershirtId(getLongOrNull(rs, "undershirt_id"));
                    d.setWatchStrapId(getLongOrNull(rs, "watch_strap_id"));
                    d.setWatchFaceId(getLongOrNull(rs, "watch_face_id"));
                    d.setWallpaperIphoneId(getLongOrNull(rs, "wallpaper_iphone_id"));
                    d.setWallpaperMacbookId(getLongOrNull(rs, "wallpaper_macbook_id"));
                    d.setWallpaperIpadId(getLongOrNull(rs, "wallpaper_ipad_id"));
                    d.setCosmeticShampooId(getLongOrNull(rs, "cosmetic_shampoo_id"));
                    d.setCosmeticGelId(getLongOrNull(rs, "cosmetic_gel_id"));
                    d.setCosmeticDeodorantId(getLongOrNull(rs, "cosmetic_deodorant_id"));
                    d.setCosmeticSprayId(getLongOrNull(rs, "cosmetic_spray_id"));
                    d.setCosmeticAntiperspirantId(getLongOrNull(rs, "cosmetic_antiperspirant_id"));
                    d.setCosmeticCreamId(getLongOrNull(rs, "cosmetic_cream_id"));
                    d.setCosmeticHairSprayId(getLongOrNull(rs, "cosmetic_hair_spray_id"));
                    return d;
                }
            }
        }
        return null;
    }

    private Long getLongOrNull(ResultSet rs, String col) throws SQLException {
        long v = rs.getLong(col);
        return rs.wasNull() ? null : v;
    }
}


