package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//    }

    @Override
    public void start(Stage stage) throws Exception {
        GamePanel gamePanel = new GamePanel();
        final Scene scene = new Scene(gamePanel,800, 600);
        gamePanel.initial();
        scene.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("img/游戏窗口.png"))));
        stage.setScene(scene);
        stage.setTitle("JavaFX游戏开发--QQ堂--by HUPENG");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                gamePanel.stageCloseRequest();
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(Main.class, args);
    }
}
