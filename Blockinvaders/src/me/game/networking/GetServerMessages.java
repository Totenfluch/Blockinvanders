package me.game.networking;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.game.characters.Controllable;
import me.game.characters.Controller;
import me.game.characters.Player;
import me.game.pack.Frame;

public class GetServerMessages implements Controller{
	public String newestreply = null;
	protected Frame game;
	private Player controllable;
	
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
			GetServerMessages gsm = this;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					game.Online_Coop = true;
					game.Player1Name = "PH";
					game.Players = new Player[2];
					game.switchSceneToGame();
					game.Players[1].setController(gsm);
				}
			});
		}else if(message.startsWith("playerPos")){
			// playerpos x y
			String[] splinter = message.split(" ");
			double xPos = Double.parseDouble(splinter[1]);
			double yPos = Double.parseDouble(splinter[2]);
			controllable.setX(xPos);
			controllable.setY(yPos);
		}else if(message.startsWith("shoot")){
			// shoot x y
			String[] splinter = message.split(" ");
			double xPos = Double.parseDouble(splinter[1]);
			double yPos = Double.parseDouble(splinter[2]);
			controllable.getHisWeapon().shoot(xPos, yPos);
		}else if(message.startsWith("hitMonster")){
			// hitMonser Hashcode dmg amount
			String[] splinter = message.split(" ");
			game.Monsters.stream().filter(m -> (m.hashCode() == Integer.parseInt(splinter[1]))).forEach(monti -> monti.setLife(Integer.parseInt(splinter[2]), true));
		}else if(message.startsWith("hitPlayer")){
			// hitPlayer dmg amount
			String[] splinter = message.split(" ");
			controllable.setLife(Integer.parseInt(splinter[1]), true);
		}else if(message.startsWith("playerDeath")){
			controllable.setLife(0, true);
		}else if(message.startsWith("syncScore")){
			String[] splinter = message.split(" ");
			controllable.setScore(Integer.valueOf(splinter[1]));
		}
	}


	@Override
	public void onSetControl(Controllable controllable) {
		this.controllable = (Player)controllable;
		
	}
}