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

public class UnderwearDialogController {
    @FXML private ChoiceBox<Category> categoryChoice;
    @FXML private TableView<Clothes> itemsTable;
    @FXML private TableColumn<Clothes, String> nameCol;
    @FXML private Label pantiesLabel;
    @FXML private Label socksLabel;
    @FXML private Label undershirtLabel;

    private final ItemDao itemDao = new ItemDao();
    private final DayPlanDao dayPlanDao = new DayPlanDao();
    private LocalDate date;

    @FXML
    public void initialize() {
        categoryChoice.setItems(FXCollections.observableArrayList(Category.UNDERWEAR_PANTIES, Category.UNDERWEAR_SOCKS, Category.UNDERSHIRT));
        categoryChoice.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)-> refreshItems());
        nameCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getName()));
        categoryChoice.getSelectionModel().selectFirst();
    }

    public void setDate(LocalDate date) { this.date = date; refreshDay(); }

    private void refreshItems() {
        try {
            List<Clothes> list = itemDao.findByCategory(categoryChoice.getValue().name());
            itemsTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) { showError(e); }
    }

    private void refreshDay() {
        try {
            Day d = dayPlanDao.findOrCreate(date);
            pantiesLabel.setText(d.getUnderwearPantiesId()==null?"-":String.valueOf(d.getUnderwearPantiesId()));
            socksLabel.setText(d.getUnderwearSocksId()==null?"-":String.valueOf(d.getUnderwearSocksId()));
            undershirtLabel.setText(d.getUndershirtId()==null?"-":String.valueOf(d.getUndershirtId()));
        } catch (SQLException e) { showError(e); }
    }

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
                itemDao.insert(name, categoryChoice.getValue().name(), null, imagePath); refreshItems();
            } catch (Exception e) { showError(e);} });
    }
    @FXML private void onRemove() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem(); if (sel==null) return;
        try { itemDao.delete(sel.getId()); refreshItems(); refreshDay(); } catch (SQLException e) { showError(e);} }
    @FXML private void onSelect() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem(); if (sel==null) return;
        String col = switch (categoryChoice.getValue()) { case UNDERWEAR_PANTIES -> "underwear_panties_id"; case UNDERWEAR_SOCKS -> "underwear_socks_id"; case UNDERSHIRT -> "undershirt_id"; default -> null; };
        if (col==null) return;
        try (var conn = ayzekssoft.nextday.db.Database.getConnection(); var ps = conn.prepareStatement("UPDATE day_plan SET "+col+"=? WHERE date=?")) {
            ps.setLong(1, sel.getId()); ps.setDate(2, java.sql.Date.valueOf(date)); ps.executeUpdate(); refreshDay();
        } catch (Exception e) { showError(e); }
    }

    private void showError(Exception e) { new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait(); }
}

