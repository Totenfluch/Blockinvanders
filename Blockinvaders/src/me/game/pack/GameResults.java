package me.game.pack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import me.game.characters.Player;

public class GameResults {
	private static int gameResultsIdCounter = 0;
	private static Vector<GameResults> resultSet = new Vector<GameResults>(20, 5);
	private static int avgerageScore;
	private static int averageWave;
	
	private int gameResultId;

	private GameSettings gs;
	private Player[] players;
	private int wavesCompleted;
	private long gameDuration;
	private Frame game;

	public GameResults(GameSettings gs, Player players[], int wavesCompleted, long gameDuration){
		this.gs = gs;
		this.game = Frame.getInstance();
		this.players = players;
		this.wavesCompleted = wavesCompleted;
		this.gameDuration = gameDuration;
		resultSet.add(this);
		gameResultId = gameResultsIdCounter;
		gameResultsIdCounter++;
		calcAverage();
		publishLatestScores();
	}
	
	private static void calcAverage(){
		int scores = 0;
		int players = 0;
		int waves = 0;
		int rounds = 0;
		for(GameResults grs : resultSet){
			waves += grs.getWavesCompleted();
			for(Player p : grs.getPlayers()){
				scores += p.getScore();
				players = grs.getPlayers().length;
			}
			rounds++;
		}
		avgerageScore = (scores/players)/rounds;
		averageWave = waves/rounds;
	}

	public int getWavesCompleted(){
		return wavesCompleted;
	}

	public long getGameDuration(){
		return gameDuration;
	}

	public int getId(){
		return gameResultId;
	}
	
	public Player[] getPlayers(){
		return players;
	}

	public String getPlayerStatsToString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Active Players: " + players.length + "\n");
		for(int i = 0; i<players.length; i++){
			Player p = players[i];
			sb.append("("+i+") Name: " + p.getPlayerName() + "\n");
			sb.append("|- Score: " + p.getScore() + "\n");
			sb.append("|- Weapon: " + p.getHisWeapon().getWeaponType() + "\n");
			sb.append("|- Final Health: " + p.getLife() + "\n\n");
		}
		return sb.toString();
	}

	public String getPlayerStatsToString(int Player_Id){
		StringBuffer sb = new StringBuffer();
		if(Player_Id < players.length){
			Player p = players[Player_Id];
			sb.append("("+Player_Id+") Name: " + p.getPlayerName() + "\n");
			sb.append("|- Score: " + p.getScore() + "\n");
			sb.append("|- Weapon: " + p.getHisWeapon().getWeaponType() + "\n");
			sb.append("|- Final Health: " + p.getLife() + "\n\n");
		}else{
			return "Invalid ID";
		}
		return sb.toString();
	}

	public String getGameSettingsToString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Gamespeed: " + gs.getGameSpeed() + "\n");
		sb.append("GameMode: " + getGameModeToString() + "\n");
		sb.append("Auto restart: " + gs.isRestart() + "\n");
		sb.append("Benchmark Settings: \n");
		sb.append("|- Performance: " + gs.getPerformance_benchmark_enabled() + "\n");
		sb.append("|- Bot Performance: " + gs.getBot_Performance_benchmark_enabled() + "\n");
		return sb.toString();
	}

	public String getGameModeToString(){
		switch(gs.getGameMode()){
		case 0: return "Singleplayer";
		case 1: return "Local Coop";
		case 2: return "Online Coop";
		case 3: return "Play with KI";
		case 4: return "Watch KI";
		case 5: return "KI Coop";
		case 6: return "KI Party";
		}
		return "Invalid GameMode";
	}
	
	private void publishLatestScores() {
		if (game.Coop_enabled || game.KI_Coop_enabled) {
			try {
				String request = "http://totenfluch.de/putScores.php?Username1=" + game.Player1Name.replace(" ", "") + "&Username2="
						+ game.Player2Name.replace(" ", "") + "&Score1=" + game.Players[0].getScore() + "&Score2=" + game.Players[1].getScore() + "";
				URL oracle = new URL(request);
				URLConnection yc = oracle.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String line = "";
				while((line = in.readLine()) != null)
					if(line.contains("<body>"))
						System.out.println("Uploaded Stats");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	
	public static Vector<GameResults> getGameResultsResultSet(){
		return resultSet;
	}
	
	public static int getAverageScore(){
		return avgerageScore;
	}
	
	public static int getAverageWave(){
		return averageWave;
	}
	
	public static String averagesToString(){
		return "Avg. Wave: " + averageWave + " | Avg. Score: " + avgerageScore;
	}
}