package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;


public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private int counter = 0;
	private int turn;
	private String[][] table;
	private boolean gameOver=false;

    public SimpleServer(int port) {
		super(port);
		this.table = new String[3][3];
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try{
			Random random = new Random();
			int rand;
			int tmp;
			String msgString = msg.toString();
			char ch;
			String message = "";
			if(msgString.startsWith("add client")){
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
				this.counter++;
				client.sendToClient("client added successfully");
				System.out.println("client added successfully");
				if(this.counter == 2)
				{
					rand = random.nextInt(2);
					this.turn = rand;
					String notations = "XO";
					ch = notations.charAt(rand);
					message = ""+ch;
					System.out.println("2 clients have been added");
					System.out.println("game started");
					Warning warning = new Warning("ready");
					sendToAllClients(warning);
					SubscribersList.get(0).getClient().sendToClient(message);
					message = ""+notations.charAt(1-rand);
					SubscribersList.get(1).getClient().sendToClient(message);
					SubscribersList.get(this.turn).getClient().sendToClient("your turn");
					tmp = 1-this.turn;
					SubscribersList.get(tmp).getClient().sendToClient("opponent's turn");
				}
			}
			else if(msgString.startsWith("remove client")){
				if(!SubscribersList.isEmpty()){
					for(SubscribedClient subscribedClient: SubscribersList){
						if(subscribedClient.getClient().equals(client)){
							SubscribersList.remove(subscribedClient);
							this.counter--;
							this.gameOver = false;
							break;
						}
					}
				}
			}
			else if(msgString.startsWith("change")){
				int row = Integer.parseInt(String.valueOf(msgString.charAt(7)));
				int col = Integer.parseInt(String.valueOf(msgString.charAt(9)));
				String mark = String.valueOf(msgString.charAt(14));
				if(!gameOver){
					this.table[row][col] = mark;
					SubscribersList.get(this.turn).getClient().sendToClient("opponent's turn");
					this.turn = 1-this.turn;
					SubscribersList.get(this.turn).getClient().sendToClient("your turn");
					SubscribersList.get(this.turn).getClient().sendToClient(msgString);
				}
				message = checkWinner();
				if(message.contains("wins")){
					printBoard();
					System.out.println(message);
					gameOver = true;
					sendToAllClients(message);
					restoreBoard();
				}
				else if(message.contains("tie")){
					printBoard();
					System.out.println(message);
					gameOver = true;
					sendToAllClients(message);
					restoreBoard();
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}


	}
	public void restoreBoard(){
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				this.table[i][j] = null;
			}
		}
	}
	public void printBoard() {
		for (int i = 0; i < 3; i++) {
			System.out.print(" ");
			for (int j = 0; j < 3; j++) {
				System.out.print(this.table[i][j] == null ? " " : this.table[i][j]);
				if (j < 2) System.out.print(" | ");
			}
			System.out.println();
			if (i < 2) {
				System.out.println("---+---+---");
			}
		}
	}
	public String checkWinner(){
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (this.table[i][0] != null &&
                    Objects.equals(this.table[i][0], this.table[i][1]) &&
                    Objects.equals(this.table[i][1], this.table[i][2])) {
				return this.table[i][0] + " wins";
			}
		}

		// Check columns
		for (int i = 0; i < 3; i++) {
			if (this.table[0][i] != null &&
                    Objects.equals(this.table[0][i], this.table[1][i]) &&
                    Objects.equals(this.table[1][i], this.table[2][i])) {
				return this.table[0][i] + " wins";
			}
		}

		// Check main diagonal
		if (this.table[0][0] != null &&
                this.table[0][0].equals(this.table[1][1]) &&
                this.table[1][1].equals(this.table[2][2])) {
			return this.table[0][0] + " wins";
		}

		// Check anti-diagonal
		if (this.table[0][2] != null &&
                this.table[0][2].equals(this.table[1][1]) &&
                this.table[1][1].equals(this.table[2][0])) {
			return this.table[0][2] + " wins";
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if(this.table[i][j] != null){
					count++;
				}
			}
		}
		if(count == 9){
			return "Its a tie!";
		}
		return "No winner";
	}
	public void sendToAllClients(Object message) {
		try {
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
