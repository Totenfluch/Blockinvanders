package me.game.networking;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.game.pack.Controllable;
import me.game.pack.Controller;
import me.game.pack.Frame;
import me.game.pack.Player;

public class GetServerMessages implements Controller{
	public String newestreply = null;
	protected Frame game;
	private Controllable controllable;
	
	public GetServerMessages(Frame game) {
		this.game = game;
		game.Players[1].setController(this);
	}


	public void CheckServerMessages(String message){
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
					game.Lobbys.getItems().clear();
					game.Lobbys.getItems().addAll(names);
				}
			});
		}else if(message.startsWith("setLobby")){
			String[] split = message.split(" ");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					game.CurrentLobby.setText("Your Lobby: " + split[1]);
				}
			});
		}else if(message.equals("StartGame")){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					game.Online_Coop = true;
					game.Player1Name = "PH";
					game.Players = new Player[2];
					game.switchSceneToGame();
				}
			});
		}else if(message.startsWith("playerPos")){
			String[] splinter = message.split(" ");
			double xPos = Double.parseDouble(splinter[1]);
			double yPos = Double.parseDouble(splinter[2]);
			game.Players[1].setX(xPos);
			game.Players[1].setY(yPos);
		}else if(message.startsWith("shoot")){
			String[] splinter = message.split(" ");
			double xPos = Double.parseDouble(splinter[1]);
			double yPos = Double.parseDouble(splinter[2]);
			game.Players[1].getHisWeapon().shoot(xPos, yPos);
		}
	}


	@Override
	public void onSetControl(Controllable controllable) {
		this.controllable = controllable;
	}
}