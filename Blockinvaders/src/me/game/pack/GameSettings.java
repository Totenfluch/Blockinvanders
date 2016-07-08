package me.game.pack;

public class GameSettings {

	private int gameMode;
	private int playerNum;
	private int gameSpeed;
	private int botID;

	private String Player1Name;
	private String Player2Name;
	
	private boolean restart;
	private boolean Performance_benchmark_enabled;
	private boolean Bot_Performance_benchmark_enabled;
	private int botThreads;
	
	/*
	 * GameType:
	 * 	
	 * 0: Singleplayer
	 * 1: local Coop
	 * 2: online Coop
	 * 3: play with KI
	 * 4: watch KI
	 * 5: KI Coop
	 * 6: KI Party
	 */
	
	//Getters and Setters
	public int getGameMode() {
		return gameMode;
	}
	public void setGameMode(int gameType) {
		this.gameMode = gameType;
	}
	
	
	public int getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	
	
	public int getGameSpeed() {
		return gameSpeed;
	}
	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}
	
	
	public int getBotID() {
		return botID;
	}
	public void setBotID(int botID) {
		this.botID = botID;
	}
	
	
	public String getPlayer1Name() {
		return Player1Name;
	}
	public void setPlayer1Name(String player1Name) {
		Player1Name = player1Name;
	}
	
	public String getPlayer2Name() {
		return Player2Name;
	}
	public void setPlayer2Name(String player2Name) {
		Player2Name = player2Name;
	}
	
	
	public boolean isRestart() {
		return restart;
	}
	public void setRestart(boolean restart) {
		this.restart = restart;
	}
	
	public boolean getPerformance_benchmark_enabled() {
		return Performance_benchmark_enabled;
	}
	public void setPerformance_benchmark_enabled(boolean performance_benchmark_enabled) {
		Performance_benchmark_enabled = performance_benchmark_enabled;
	}
	public boolean getBot_Performance_benchmark_enabled() {
		return Bot_Performance_benchmark_enabled;
	}
	public void setBot_Performance_benchmark_enabled(boolean bot_Performance_benchmark_enabled) {
		Bot_Performance_benchmark_enabled = bot_Performance_benchmark_enabled;
	}
	public int getBotThreads() {
		return botThreads;
	}
	public void setBotThreads(int botThreads) {
		this.botThreads = botThreads;
	}
	public GameSettings(int gameType, int playerNum, int gameSpeed, int botID, String player1Name, String player2Name, boolean restart, boolean Performance_benchmark_enabled, boolean Bot_Performance_benchmark_enabled, int botThreads) {
		this.gameMode = gameType;
		this.playerNum = playerNum;
		this.gameSpeed = gameSpeed;
		this.botID = botID;
		Player1Name = player1Name;
		Player2Name = player2Name;
		this.restart = restart;	
		this.Performance_benchmark_enabled = Performance_benchmark_enabled;
		this.Bot_Performance_benchmark_enabled = Bot_Performance_benchmark_enabled;
		this.botThreads = botThreads;
	}

	
	
	
	
	

}
