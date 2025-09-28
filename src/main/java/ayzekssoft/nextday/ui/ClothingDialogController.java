package ayzekssoft.nextday.ui;

import ayzekssoft.nextday.db.DayPlanDao;
import ayzekssoft.nextday.db.ItemDao;
import ayzekssoft.nextday.dto.Category;
import ayzekssoft.nextday.dto.Clothes;
import ayzekssoft.nextday.dto.Day;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClothingDialogController {
    @FXML private ChoiceBox<Category> categoryChoice;
    @FXML private TableView<Clothes> itemsTable;
    @FXML private TableColumn<Clothes, String> nameCol;
    @FXML private Label tshirtLabel;
    @FXML private Label sweatshirtLabel;
    @FXML private Label jeansLabel;
    @FXML private Label shoesLabel;
    @FXML private Label jacketLabel;

    private final ItemDao itemDao = new ItemDao();
    private final DayPlanDao dayPlanDao = new DayPlanDao();
    private LocalDate date;

    public void setDate(LocalDate date) {
        this.date = date;
        refreshDay();
    }

    @FXML
    public void initialize() {
        categoryChoice.setItems(FXCollections.observableArrayList(
                Category.CLOTHING_TSHIRT,
                Category.CLOTHING_SWEATSHIRT,
                Category.CLOTHING_JEANS,
                Category.CLOTHING_SHOES,
                Category.CLOTHING_JACKET
        ));
        categoryChoice.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> refreshItems());
        nameCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getName()));
        categoryChoice.getSelectionModel().selectFirst();
    }

    private void refreshItems() {
        Category c = categoryChoice.getValue();
        if (c == null) return;
        try {
            List<Clothes> list = itemDao.findByCategory(c.name());
            itemsTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void refreshDay() {
        if (date == null) return;
        try {
            Day d = dayPlanDao.findOrCreate(date);
            tshirtLabel.setText(idToText(d.getTshirtId()));
            sweatshirtLabel.setText(idToText(d.getSweatshirtId()));
            jeansLabel.setText(idToText(d.getJeansId()));
            shoesLabel.setText(idToText(d.getShoesId()));
            jacketLabel.setText(idToText(d.getJacketId()));
        } catch (SQLException e) {
            showError(e);
        }
    }

    private String idToText(Long id) {
        return id == null ? "-" : String.valueOf(id);
    }

    @FXML
    private void onAdd() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Добавить");
        dlg.setHeaderText("Введите имя");
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
                    var file = fc.showOpenDialog(((Stage) itemsTable.getScene().getWindow()));
                    if (file != null) {
                        var stored = ayzekssoft.nextday.util.ImageStorage.storeImage(file.toPath());
                        imagePath = stored.toString();
                    }
                }
                itemDao.insert(name, categoryChoice.getValue().name(), null, imagePath);
                refreshItems();
            } catch (Exception e) {
                showError(e);
            }
        });
    }

    @FXML
    private void onRemove() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            itemDao.delete(sel.getId());
            refreshItems();
            refreshDay();
        } catch (SQLException e) {
            showError(e);
        }
    }

    @FXML
    private void onWear() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem();
        if (sel == null || date == null) return;
        String col = switch (categoryChoice.getValue()) {
            case CLOTHING_TSHIRT -> "tshirt_id";
            case CLOTHING_SWEATSHIRT -> "sweatshirt_id";
            case CLOTHING_JEANS -> "jeans_id";
            case CLOTHING_SHOES -> "shoes_id";
            case CLOTHING_JACKET -> "jacket_id";
            default -> null;
        };
        if (col == null) return;
        try (var conn = ayzekssoft.nextday.db.Database.getConnection();
             var ps = conn.prepareStatement("UPDATE day_plan SET " + col + "=? WHERE date=?")) {
            ps.setLong(1, sel.getId());
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.executeUpdate();
            refreshDay();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }
}


