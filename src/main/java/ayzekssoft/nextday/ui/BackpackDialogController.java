package ayzekssoft.nextday.ui;

import ayzekssoft.nextday.db.BackpackDao;
import ayzekssoft.nextday.db.ItemDao;
import ayzekssoft.nextday.dto.Category;
import ayzekssoft.nextday.dto.Clothes;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BackpackDialogController {
    @FXML private TableView<Clothes> itemsTable;
    @FXML private TableColumn<Clothes, String> nameCol;
    @FXML private ListView<String> packList;

    private final ItemDao itemDao = new ItemDao();
    private final BackpackDao backpackDao = new BackpackDao();
    private LocalDate date;

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getName()));
        refreshItems();
    }

    public void setDate(LocalDate date) {
        this.date = date;
        refreshPack();
    }

    private void refreshItems() {
        try {
            var list = itemDao.findByCategory(Category.BACKPACK_ITEM.name());
            itemsTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) { showError(e); }
    }

    private void refreshPack() {
        if (date == null) return;
        try {
            packList.setItems(FXCollections.observableArrayList(backpackDao.getItemNamesForDate(date)));
        } catch (SQLException e) { showError(e); }
    }

    @FXML
    private void onAddItem() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Добавить предмет");
        dlg.setHeaderText(null);
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
                itemDao.insert(name, Category.BACKPACK_ITEM.name(), null, imagePath);
                refreshItems();
            } catch (Exception e) { showError(e); }
        });
    }

    @FXML
    private void onRemoveItem() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            itemDao.delete(sel.getId());
            refreshItems();
            refreshPack();
        } catch (SQLException e) { showError(e); }
    }

    @FXML
    private void onAddToPack() {
        var selected = itemsTable.getSelectionModel().getSelectedItems();
        if (selected == null || selected.isEmpty() || date == null) return;
        try {
            List<Long> ids = new ArrayList<>(backpackDao.getItemsForDate(date));
            for (Clothes c : selected) if (!ids.contains(c.getId())) ids.add(c.getId());
            backpackDao.setItemsForDate(date, ids);
            refreshPack();
        } catch (SQLException e) { showError(e); }
    }

    @FXML
    private void onRemoveFromPack() {
        String sel = packList.getSelectionModel().getSelectedItem();
        if (sel == null || date == null) return;
        try {
            // Rebuild without selected name
            List<String> names = new ArrayList<>(packList.getItems());
            names.remove(sel);
            // Convert names back to ids by querying; simplistic approach
            List<Long> ids = new ArrayList<>();
            for (Clothes c : itemDao.findByCategory(Category.BACKPACK_ITEM.name())) {
                if (names.contains(c.getName())) ids.add(c.getId());
            }
            backpackDao.setItemsForDate(date, ids);
            refreshPack();
        } catch (SQLException e) { showError(e); }
    }

    private void showError(Exception e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
    }
}


