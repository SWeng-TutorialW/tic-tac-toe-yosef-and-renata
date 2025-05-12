package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private int counter = 0;
	private static String notations;

	public SimpleServer(int port) {
		super(port);
		notations = "XO";
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		int rand;
		String msgString = msg.toString();
		char ch;
		if(msgString.startsWith("add client")){
			SubscribedClient connection = new SubscribedClient(client);
			SubscribersList.add(connection);
			this.counter++;
			try {
				client.sendToClient("client added successfully");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if(this.counter == 2)
			{
				try {
					rand = (int)(Math.random()*SubscribersList.size());
					ch = notations.charAt(rand);
					System.out.println("2 clients have been added");
					Warning warning = new Warning("ready");
					sendToAllClients(warning);
					SubscribersList.get(0).getClient().sendToClient(ch);
					SubscribersList.get(1).getClient().sendToClient(notations.charAt(1-rand));
					SubscribersList.get(rand).getClient().sendToClient("you turn");
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		else if(msgString.startsWith("remove client")){
			if(!SubscribersList.isEmpty()){
				for(SubscribedClient subscribedClient: SubscribersList){
					if(subscribedClient.getClient().equals(client)){
						SubscribersList.remove(subscribedClient);
						this.counter--;
						break;
					}
				}
			}
		}

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
