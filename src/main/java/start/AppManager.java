package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.InitService;

public class AppManager extends Application{


    @Override
    public void start(Stage primaryStage) throws Exception {
        InitService.createTables();
        Parent root = FXMLLoader.load(getClass().getResource("/view/time.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/main.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("SeatTeam");
        primaryStage.show();
    }
}
