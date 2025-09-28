package ayzekssoft.nextday;

import ayzekssoft.nextday.ui.ClothingDialogController;
import ayzekssoft.nextday.ui.BackpackDialogController;
import ayzekssoft.nextday.ui.WatchDialogController;
import ayzekssoft.nextday.ui.WallpaperDialogController;
import ayzekssoft.nextday.ui.UnderwearDialogController;
import ayzekssoft.nextday.ui.HomeDialogController;
import ayzekssoft.nextday.ui.CosmeticDialogController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class ApplicationController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button clothingButton;
    @FXML
    private Button backpackButton;
    @FXML
    private Button watchButton;
    @FXML
    private Button wallpapersButton;
    @FXML
    private Button underwearButton;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        refreshSummary();
    }

    @FXML
    protected void onDateChanged() {
        refreshSummary();
    }

    @FXML
    protected void onClothingClick() {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Одежда");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Application.class.getResource("dialogs/clothing-dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            ClothingDialogController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            dialog.getDialogPane().setContent(root);
            applyStyles(dialog);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
            refreshSummary();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onBackpackClick() {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Рюкзак");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Application.class.getResource("dialogs/backpack-dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            BackpackDialogController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            dialog.getDialogPane().setContent(root);
            applyStyles(dialog);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
            refreshSummary();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onWatchClick() {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Часы");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Application.class.getResource("dialogs/watch-dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            WatchDialogController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            dialog.getDialogPane().setContent(root);
            applyStyles(dialog);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
            refreshSummary();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onWallpapersClick() {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Обои");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Application.class.getResource("dialogs/wallpaper-dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            WallpaperDialogController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            dialog.getDialogPane().setContent(root);
            applyStyles(dialog);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
            refreshSummary();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onUnderwearClick() {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Нижнее бельё");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Application.class.getResource("dialogs/underwear-dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            UnderwearDialogController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            dialog.getDialogPane().setContent(root);
            applyStyles(dialog);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
            refreshSummary();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onAddImageClick() {
        // deprecated
    }

    @FXML
    protected void onShowSelectionClick() {
        LocalDate date = datePicker.getValue();
        try {
            var dayDao = new ayzekssoft.nextday.db.DayPlanDao();
            var itemDao = new ayzekssoft.nextday.db.ItemDao();
            var backpackDao = new ayzekssoft.nextday.db.BackpackDao();
            var day = dayDao.findOrCreate(date);
            StringBuilder sb = new StringBuilder();
            // Одежда
            appendGroup(sb, "Одежда", new String[]{
                    labelOf("Футболка", day.getTshirtId(), itemDao),
                    labelOf("Свитшот", day.getSweatshirtId(), itemDao),
                    labelOf("Джинсы", day.getJeansId(), itemDao),
                    labelOf("Обувь", day.getShoesId(), itemDao),
                    labelOf("Куртка", day.getJacketId(), itemDao)
            });
            // Домашняя
            appendGroup(sb, "Домашняя одежда", new String[]{
                    labelOf("Футболка", day.getHomeTshirtId(), itemDao),
                    labelOf("Бельё", day.getHomeUnderwearId(), itemDao),
                    labelOf("Брюки", day.getHomePantsId(), itemDao)
            });
            // Часы
            appendGroup(sb, "Часы", new String[]{
                    labelOf("Ремешок", day.getWatchStrapId(), itemDao),
                    labelOf("Циферблат", day.getWatchFaceId(), itemDao)
            });
            // Обои
            appendGroup(sb, "Обои", new String[]{
                    labelOf("iPhone", day.getWallpaperIphoneId(), itemDao),
                    labelOf("MacBook", day.getWallpaperMacbookId(), itemDao),
                    labelOf("iPad", day.getWallpaperIpadId(), itemDao)
            });
            // Косметика
            appendGroup(sb, "Косметика", new String[]{
                    labelOf("Шампунь", day.getCosmeticShampooId(), itemDao),
                    labelOf("Гель", day.getCosmeticGelId(), itemDao),
                    labelOf("Дезодорант", day.getCosmeticDeodorantId(), itemDao),
                    labelOf("Спрей", day.getCosmeticSprayId(), itemDao),
                    labelOf("Антиперспирант", day.getCosmeticAntiperspirantId(), itemDao),
                    labelOf("Крем", day.getCosmeticCreamId(), itemDao),
                    labelOf("Спрей для волос", day.getCosmeticHairSprayId(), itemDao)
            });
            // Нижнее бельё
            appendGroup(sb, "Нижнее бельё", new String[]{
                    labelOf("Трусики", day.getUnderwearPantiesId(), itemDao),
                    labelOf("Носки", day.getUnderwearSocksId(), itemDao),
                    labelOf("Майка", day.getUndershirtId(), itemDao)
            });
            // Рюкзак
            var packNames = backpackDao.getItemNamesForDate(date);
            if (!packNames.isEmpty()) {
                sb.append("Рюкзак: ").append(String.join(", ", packNames)).append('\n');
            }

            String message = sb.toString().trim();
            if (message.isEmpty()) {
                message = "Вы не запланировали";
            }
            new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private String labelOf(String label, Long id, ayzekssoft.nextday.db.ItemDao itemDao) throws Exception {
        String val = nameOrDash(id, itemDao);
        if ("-".equals(val)) return null;
        return label + ": " + val;
    }

    private void appendGroup(StringBuilder sb, String header, String[] lines) {
        boolean any = false;
        StringBuilder tmp = new StringBuilder();
        for (String l : lines) {
            if (l != null) {
                any = true;
                tmp.append("- ").append(l).append('\n');
            }
        }
        if (any) {
            sb.append(header).append(':').append('\n').append(tmp);
        }
    }

    @FXML
    protected void onHomeClick() {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Домашняя одежда");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Application.class.getResource("dialogs/home-dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            HomeDialogController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            dialog.getDialogPane().setContent(root);
            applyStyles(dialog);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
            refreshSummary();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    protected void onCosmeticClick() {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Косметика");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(Application.class.getResource("dialogs/cosmetic-dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            CosmeticDialogController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            dialog.getDialogPane().setContent(root);
            applyStyles(dialog);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
            refreshSummary();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void applyStyles(Dialog<?> dialog) {
        try {
            String css = Application.class.getResource("styles.css").toExternalForm();
            dialog.getDialogPane().getStylesheets().add(css);
        } catch (Exception ignored) {}
    }

    private void refreshSummary() { /* no-op: summary moved to onShowSelectionClick */ }

    private String nameOrDash(Long id, ayzekssoft.nextday.db.ItemDao itemDao) throws Exception {
        if (id == null) return "-";
        String name = itemDao.findNameById(id);
        return name == null ? ("#" + id) : name;
    }
}
