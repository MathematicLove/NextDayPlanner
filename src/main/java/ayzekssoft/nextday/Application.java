package ayzekssoft.nextday;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import ayzekssoft.nextday.db.SchemaInitializer;
import ayzekssoft.nextday.util.AppLogger;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Initialize database schema on startup
            SchemaInitializer.initialize();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("application-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
            scene.getStylesheets().add(Application.class.getResource("styles.css").toExternalForm());
            stage.setTitle("NextDay Planner");
            stage.setScene(scene);
            stage.show();
        } catch (Throwable t) {
            var path = AppLogger.logException("Startup failure", t);
            throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
        }
    }
}
