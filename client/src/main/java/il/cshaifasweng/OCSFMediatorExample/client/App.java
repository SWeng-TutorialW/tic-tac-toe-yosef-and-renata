package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;
    private SecondaryController controller;

    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        scene = new Scene(loadFXML("primary"), 600, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
        client.sendToServer("remove client");
        client.closeConnection();
		super.stop();
	}
    
    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        Platform.runLater(() -> {
            try {
                if ("ready".equals(event.getWarning().getMessage())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));

                    // Use controller factory to inject the client
                    loader.setControllerFactory(param -> new SecondaryController(client));

                    Parent root = loader.load(); // This triggers controller creation

                    // Now it's safe to get the controller
                    this.controller = loader.getController();

                    client.setSecondaryController(controller);
                    scene.setRoot(root);
                }
                if(event.getWarning().getMessage().contains("wins")){
                    client.setTurn(false);
                    controller.getStatusLabel().setText(event.getWarning().getMessage());
                }
                if(event.getWarning().getMessage().contains("turn")){
                    controller.getStatusLabel().setText(event.getWarning().getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

	public static void main(String[] args) {
        launch();
    }

}