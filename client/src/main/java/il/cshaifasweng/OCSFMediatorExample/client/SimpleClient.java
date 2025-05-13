package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;
	private boolean isMyturn;
	private String notation;
	private SecondaryController controller;

	private SimpleClient(String host, int port) {
		super(host, port);
	}
	public void setSecondaryController(SecondaryController controller) {
		this.controller = controller;
	}
	public void setTurn(boolean turn) {
		this.isMyturn = turn;
	}
	public boolean isMyturn() {
		return isMyturn;
	}
	public String getNotation() {
		return notation;
	}
	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}
		else if (msg.getClass().equals(String.class)) {
			String message = (String) msg;

			if(message.equals("X")){
				this.notation = "X";
			}
			else if(message.equals("O")){
				this.notation = "O";
			}
			if(message.equals("your turn")){
				this.isMyturn = true;
				Platform.runLater(()-> {
					controller.getStatusLabel().setText("your turn");
				});

			}
			if(message.equals("opponent's turn")){
				Platform.runLater(()-> {
					controller.getStatusLabel().setText("opponent's turn");
				});
			}
			else if(((String) msg).startsWith("change")){
				int row = Integer.parseInt(String.valueOf(message.charAt(7)));
				int col = Integer.parseInt(String.valueOf(message.charAt(9)));
				String mark = String.valueOf(message.charAt(14));
				Platform.runLater(() -> {
					controller.getButton(row,col).setText(mark);
				});
			}
			else if(((String) msg).contains("wins")){
				EventBus.getDefault().post(new WarningEvent(new Warning((String) msg)));
			}
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}
	public static SimpleClient getClient(String host, int port) {
		if (client == null) {
			client = new SimpleClient(host, port);
		}
		return client;
	}

}
