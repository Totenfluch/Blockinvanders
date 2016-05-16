package me.game.pack;

import java.util.Vector;

import me.game.characters.Player;

public class GameResults {
	private static int gameResultsIdCounter = 0;
	private static Vector<GameResults> resultSet = new Vector<GameResults>(20, 5);
	private int gameResultId;

	private GameSettings gs;
	private Player[] players;
	private int wavesCompleted;
	private int gameDuration;

	public GameResults(GameSettings gs, Player players[], int wavesCompleted, int gameDuration){
		this.gs = gs;
		this.players = players;
		this.wavesCompleted = wavesCompleted;
		this.gameDuration = gameDuration;
		resultSet.add(this);
		gameResultId = gameResultsIdCounter;
		gameResultsIdCounter++;
	}

	public int getWavesCompleted(){
		return wavesCompleted;
	}

	public int getGameDuration(){
		return gameDuration;
	}

	public int getId(){
		return gameResultId;
	}

	public String getPlayerStatsToString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Active Players: " + players.length + "\n");
		for(int i = 0; i<players.length; i++){
			Player p = players[i];
			sb.append("("+i+") Name: " + p.getPlayerName() + "\n");
			sb.append("|- Score:" + p.getScore() + "\n");
			sb.append("|- Final Weapon: " + p.getHisWeapon().getWeaponType() + "\n");
			sb.append("|- Final Health: " + p.getLife() + "\n\n");
		}
		return sb.toString();
	}
	
	public String getPlayerStatsToString(int Player_Id){
		StringBuffer sb = new StringBuffer();
		if(Player_Id < players.length){
			Player p = players[Player_Id];
			sb.append("("+Player_Id+") Name: " + p.getPlayerName() + "\n");
			sb.append("|- Score:" + p.getScore() + "\n");
			sb.append("|- Final Weapon: " + p.getHisWeapon().getWeaponType() + "\n");
			sb.append("|- Final Health: " + p.getLife() + "\n\n");
		}else{
			return "Invalid ID";
		}
		return sb.toString();
	}

	public String getGameSettingsToString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Gamespeed: " + gs.getGameSpeed() + "\n");
		sb.append("GameMode: " + gs.getGameMode() + "\n");
		sb.append("Auto restart: " + gs.isRestart() + "\n");
		sb.append("Benchmark Settings: \n");
		sb.append("|- Performance: " + gs.getPerformance_benchmark_enabled() + "\n");
		sb.append("|- Bot Performance: " + gs.getBot_Performance_benchmark_enabled() + "\n");
		return sb.toString();
	}
	
	public String getWavesCompletedToString(){
		return "Waves completed: " + wavesCompleted + "\n";
	}
	
	public String getIdToString(){
		return "Game Id: " + gameResultId + "\n";
	}
	
	public String getGameDurationInMsToString(){
		return "Game duration: " + gameDuration/10000 + "\n";
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getIdToString());
		sb.append(getWavesCompletedToString());
		sb.append(getGameSettingsToString());
		sb.append(getPlayerStatsToString());
		return sb.toString();
	}
	
	public static String AllGamesToString(){
		StringBuffer sb = new StringBuffer();
		for(GameResults gs : resultSet)
			sb.append(gs.toString());
		return sb.toString();
	}
}