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

public class HomeDialogController {
    @FXML private ChoiceBox<Category> categoryChoice;
    @FXML private TableView<Clothes> itemsTable;
    @FXML private TableColumn<Clothes, String> nameCol;
    @FXML private Label tshirtLabel;
    @FXML private Label underwearLabel;
    @FXML private Label pantsLabel;

    private final ItemDao itemDao = new ItemDao();
    private final DayPlanDao dayPlanDao = new DayPlanDao();
    private LocalDate date;

    @FXML
    public void initialize() {
        categoryChoice.setItems(FXCollections.observableArrayList(Category.HOME_TSHIRT, Category.HOME_UNDERWEAR, Category.HOME_PANTS));
        categoryChoice.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)-> refreshItems());
        nameCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(cd.getValue().getName()));
        categoryChoice.getSelectionModel().selectFirst();
    }

    public void setDate(LocalDate date) { this.date = date; refreshDay(); }

    private void refreshItems() {
        try { itemsTable.setItems(FXCollections.observableArrayList(itemDao.findByCategory(categoryChoice.getValue().name()))); }
        catch (SQLException e) { showError(e); }
    }

    private void refreshDay() {
        try {
            Day d = dayPlanDao.findOrCreate(date);
            tshirtLabel.setText(d.getHomeTshirtId()==null?"-":String.valueOf(d.getHomeTshirtId()));
            underwearLabel.setText(d.getHomeUnderwearId()==null?"-":String.valueOf(d.getHomeUnderwearId()));
            pantsLabel.setText(d.getHomePantsId()==null?"-":String.valueOf(d.getHomePantsId()));
        } catch (SQLException e) { showError(e); }
    }

    @FXML private void onAdd() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Добавить");
        dlg.setContentText("Имя:");
        dlg.showAndWait().ifPresent(name -> {
            try { itemDao.insert(name, categoryChoice.getValue().name(), null, null); refreshItems(); } catch (SQLException e) { showError(e);} });
    }
    @FXML private void onRemove() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem(); if (sel==null) return;
        try { itemDao.delete(sel.getId()); refreshItems(); refreshDay(); } catch (SQLException e) { showError(e);} }
    @FXML private void onWear() {
        Clothes sel = itemsTable.getSelectionModel().getSelectedItem(); if (sel==null) return;
        String col = switch (categoryChoice.getValue()) { case HOME_TSHIRT -> "home_tshirt_id"; case HOME_UNDERWEAR -> "home_underwear_id"; case HOME_PANTS -> "home_pants_id"; default -> null; };
        if (col==null) return;
        try (var conn = ayzekssoft.nextday.db.Database.getConnection(); var ps = conn.prepareStatement("UPDATE day_plan SET "+col+"=? WHERE date=?")) {
            ps.setLong(1, sel.getId()); ps.setDate(2, java.sql.Date.valueOf(date)); ps.executeUpdate(); refreshDay();
        } catch (Exception e) { showError(e); }
    }

    private void showError(Exception e) { new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait(); }
}

