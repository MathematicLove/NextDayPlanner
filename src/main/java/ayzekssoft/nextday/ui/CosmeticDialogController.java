package ayzekssoft.nextday.ui;

import ayzekssoft.nextday.db.DayPlanDao;
import ayzekssoft.nextday.db.ItemDao;
import ayzekssoft.nextday.dto.Category;
import ayzekssoft.nextday.dto.Clothes;
import ayzekssoft.nextday.dto.Day;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CosmeticDialogController {
    @FXML private ChoiceBox<Category> categoryChoice;
    @FXML private TableView<Clothes> itemsTable;
    @FXML private TableColumn<Clothes, String> nameCol;
    @FXML private Label shampooLabel;
    @FXML private Label gelLabel;
    @FXML private Label deodorantLabel;
    @FXML private Label sprayLabel;
    @FXML private Label antiperspirantLabel;
    @FXML private Label creamLabel;
    @FXML private Label hairSprayLabel;

    private final ItemDao itemDao = new ItemDao();
    private final DayPlanDao dayPlanDao = new DayPlanDao();
    private LocalDate date;

    @FXML public void initialize() {
        categoryChoice.setItems(FXCollections.observableArrayList(
                Category.COSMETIC_SHAMPOO,
                Category.COSMETIC_GEL,
                Category.COSMETIC_DEODORANT,
                Category.COSMETIC_SPRAY,
                Category.COSMETIC_ANTIPERSPIRANT,
                Category.COSMETIC_CREAM,
                Category.COSMETIC_HAIR_SPRAY
        ));
        categoryChoice.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)-> refreshItems());
        nameCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getName()));
        categoryChoice.getSelectionModel().selectFirst();
    }

    public void setDate(LocalDate date) { this.date = date; refreshDay(); }

    private void refreshItems() {
        try { List<Clothes> list = itemDao.findByCategory(categoryChoice.getValue().name()); itemsTable.setItems(FXCollections.observableArrayList(list)); }
        catch (SQLException e) { showError(e); }
    }

    private void refreshDay() {
        try {
            Day d = dayPlanDao.findOrCreate(date);
            shampooLabel.setText(valueOf(d.getCosmeticShampooId()));
            gelLabel.setText(valueOf(d.getCosmeticGelId()));
            deodorantLabel.setText(valueOf(d.getCosmeticDeodorantId()));
            sprayLabel.setText(valueOf(d.getCosmeticSprayId()));
            antiperspirantLabel.setText(valueOf(d.getCosmeticAntiperspirantId()));
            creamLabel.setText(valueOf(d.getCosmeticCreamId()));
            hairSprayLabel.setText(valueOf(d.getCosmeticHairSprayId()));
        } catch (SQLException e) { showError(e); }
    }

    private String valueOf(Long id) { return id == null ? "-" : String.valueOf(id); }

    @FXML private void onAdd() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Добавить");
        dlg.setContentText("Имя:");
        dlg.showAndWait().ifPresent(name -> {
            try {
                String imagePath = null;
                Alert ask = new Alert(Alert.AlertType.CONFIRMATION, "Добавить фото?", ButtonType.YES, ButtonType.NO);
                ask.setHeaderText(null);
                ask.setTitle("Фото");
                var res = ask.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.YES) {
                    javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
                    fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
                    var file = fc.showOpenDialog(itemsTable.getScene().getWindow());
                    if (file != null) {
                        var stored = ayzekssoft.nextday.util.ImageStorage.storeImage(file.toPath());
                        imagePath = stored.toString();
                    }
                }
                itemDao.insert(name, categoryChoice.getValue().name(), null, imagePath);
                refreshItems();
            } catch (Exception e) { showError(e);} });
    }

    @FXML private void onRemove() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem(); if (sel == null) return;
        try { itemDao.delete(sel.getId()); refreshItems(); refreshDay(); } catch (SQLException e) { showError(e);} }

    @FXML private void onSelect() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem(); if (sel==null) return;
        String col = switch (categoryChoice.getValue()) {
            case COSMETIC_SHAMPOO -> "cosmetic_shampoo_id";
            case COSMETIC_GEL -> "cosmetic_gel_id";
            case COSMETIC_DEODORANT -> "cosmetic_deodorant_id";
            case COSMETIC_SPRAY -> "cosmetic_spray_id";
            case COSMETIC_ANTIPERSPIRANT -> "cosmetic_antiperspirant_id";
            case COSMETIC_CREAM -> "cosmetic_cream_id";
            case COSMETIC_HAIR_SPRAY -> "cosmetic_hair_spray_id";
            default -> null;
        };
        if (col==null) return;
        try (var conn = ayzekssoft.nextday.db.Database.getConnection(); var ps = conn.prepareStatement("UPDATE day_plan SET "+col+"=? WHERE date=?")) {
            ps.setLong(1, sel.getId()); ps.setDate(2, java.sql.Date.valueOf(date)); ps.executeUpdate(); refreshDay();
        } catch (Exception e) { showError(e); }
    }

    private void showError(Exception e) { new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait(); }
}


