package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {

    @FXML
	public void onWarningEvent(WarningEvent event) {
		if(event.getWarning().getMessage().equals("ready")){

		}
	}
    @FXML
    void sendWarning(ActionEvent event) {
    	/*try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			SimpleClient client = SimpleClient.getClient();
			client.sendToServer("add client");
			while (true)
			{

				break;
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.setTitle("Game Board");
			stage.setScene(new Scene(root,600,480));
			stage.show();
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void initialize(){
		/*try {
			SimpleClient.getClient().sendToServer("add client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
