package me.game.networking;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.game.pack.Frame;
import me.game.pack.Player;

public class GetServerMessages{
	public static String newestreply = null;


	public static void CheckServerMessages(String message){
		//System.out.println(message);
		if(message.startsWith("lobbys")){
			message = message.replaceFirst("lobbys ", "");
			String[] lobbylist = message.split(" ");
			if(message.equals("lobbys"))
				return;
			for(int i = 0; i<lobbylist.length; i++){
				String[] temsp = lobbylist[i].split(",");
				if(temsp.length > 1)
					lobbylist[i] = "Lobby: " + temsp[0] + " ("+temsp[1]+")";
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ObservableList<String> names = FXCollections.observableArrayList(lobbylist);
					Frame.Lobbys.getItems().clear();
					Frame.Lobbys.getItems().addAll(names);
				}
			});
		}else if(message.startsWith("setLobby")){
			String[] split = message.split(" ");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Frame.CurrentLobby.setText("Your Lobby: " + split[1]);
				}
			});
		}else if(message.equals("StartGame")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Frame.Online_Coop = true;
					Frame.Player1Name = "PH";
					Frame.Players = new Player[2];
					Frame.switchSceneToGame();
				}
			});
		}else if(message.startsWith("playerPos")){
			String[] splinter = message.split(" ");
			double xPos = Double.parseDouble(splinter[1]);
			double yPos = Double.parseDouble(splinter[2]);
			Frame.Players[1].setX(xPos);
			Frame.Players[1].setY(yPos);
		}
	}
}