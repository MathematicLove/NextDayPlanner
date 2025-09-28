package ayzekssoft.nextday.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    public static void initialize() {
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS item (\n" +
                    "  id SERIAL PRIMARY KEY,\n" +
                    "  name VARCHAR(200) NOT NULL,\n" +
                    "  category VARCHAR(50) NOT NULL,\n" +
                    "  subtype VARCHAR(50),\n" +
                    "  image_path TEXT\n" +
                    ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS day_plan (\n" +
                    "  date DATE PRIMARY KEY,\n" +
                    "  tshirt_id INTEGER,\n" +
                    "  sweatshirt_id INTEGER,\n" +
                    "  jeans_id INTEGER,\n" +
                    "  shoes_id INTEGER,\n" +
                    "  jacket_id INTEGER,\n" +
                    "  home_tshirt_id INTEGER,\n" +
                    "  home_underwear_id INTEGER,\n" +
                    "  home_pants_id INTEGER,\n" +
                    "  underwear_panties_id INTEGER,\n" +
                    "  underwear_socks_id INTEGER,\n" +
                    "  undershirt_id INTEGER,\n" +
                    "  watch_strap_id INTEGER,\n" +
                    "  watch_face_id INTEGER,\n" +
                    "  wallpaper_iphone_id INTEGER,\n" +
                    "  wallpaper_macbook_id INTEGER,\n" +
                    "  wallpaper_ipad_id INTEGER,\n" +
                    "  cosmetic_shampoo_id INTEGER,\n" +
                    "  cosmetic_gel_id INTEGER,\n" +
                    "  cosmetic_deodorant_id INTEGER,\n" +
                    "  cosmetic_spray_id INTEGER,\n" +
                    "  cosmetic_antiperspirant_id INTEGER,\n" +
                    "  cosmetic_cream_id INTEGER,\n" +
                    "  cosmetic_hair_spray_id INTEGER\n" +
                    ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS day_pack_item (\n" +
                    "  date DATE NOT NULL,\n" +
                    "  item_id INTEGER NOT NULL,\n" +
                    "  PRIMARY KEY(date, item_id)\n" +
                    ")");

            // Migrations: ensure columns exist when upgrading existing installations
            String[] alterColumns = new String[] {
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS home_tshirt_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS home_underwear_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS home_pants_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS cosmetic_shampoo_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS cosmetic_gel_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS cosmetic_deodorant_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS cosmetic_spray_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS cosmetic_antiperspirant_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS cosmetic_cream_id INTEGER",
                    "ALTER TABLE day_plan ADD COLUMN IF NOT EXISTS cosmetic_hair_spray_id INTEGER"
            };
            for (String sql : alterColumns) {
                st.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize schema", e);
        }
    }
}


