package me.game.networking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.game.pack.Frame;

public class GetServerMessages{
	public static String newestreply = null;


	public static void CheckServerMessages(String message){
		if(message.startsWith("lobbys")){
			message = message.replaceFirst("lobbys ", "");
			String[] lobbylist = message.split(" ");
			for(int i = 0; i<lobbylist.length; i++){
				String[] temsp = lobbylist[i].split(",");
				lobbylist[i] = "Lobby: " + temsp[0] + " ("+temsp[1]+")";
			}
			ObservableList<String> names = FXCollections.observableArrayList(lobbylist);
			Frame.Lobbys.getItems().addAll(names);
		}
	}
}