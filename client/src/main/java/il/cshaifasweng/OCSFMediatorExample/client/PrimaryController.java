package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {
	@FXML
	private Label ipLabel;
	@FXML
	private Label label2;
	@FXML
	private TextField ip;
	@FXML
	private Label portLabel;
	@FXML
	private TextField port;


    @FXML
    void play(ActionEvent event) {
    	/*try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			String ip = this.ip.getText();
			int port = Integer.parseInt(this.port.getText());
			SimpleClient client = SimpleClient.getClient(ip, port);
			client.openConnection();
			client.sendToServer("add client");
			label2.setText("waiting for another player...");
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
