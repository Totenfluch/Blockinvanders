package me.game.pack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Vector;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.game.characters.Bot1;
import me.game.characters.Bot2;
import me.game.characters.BotKI;
import me.game.characters.Monster;
import me.game.characters.Player;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.networking.Client;
import me.game.playerWeapons.PlayerWeapon;
import me.game.playerWeapons.RocketLauncher;
import me.game.playerWeapons.StandardWeapon;

public class Frame {
	public static final int GAME_WIDTH = 1600;
	public static final int GAME_LENGTH = 900;

	private Timeline tf;
	private Timeline rTf;
	private Timeline SpawnWaveDelay;

	private Canvas cv;
	private GraphicsContext gc;

	private Canvas gOcanvas;
	private GraphicsContext gOgc;

	private Stage MainStage;
	private Scene ConnectScene;
	private Scene GameScene;
	private Scene gameOverScene;

	public Player[] Players;

	public Client client;
	public static Frame game;

	public boolean autoRestart = false;
	public boolean Coop_enabled = false;
	public boolean Online_Coop = false;
	public boolean Bot_enabled = false;
	public boolean KI_Coop_enabled = false;
	public boolean Play_with_bot_enabled = false;
	public boolean Performance_benchmark_enabled = false;
	public boolean Bot_Performance_benchmark_enabled = false;
	private int Bot_Benchmark_threads = 32;
	
	private int KiPartyPlayers = 0;
	private int botID = 0;
	private BotKI bot;
	private BotKI bot2;
	private BotKI[] bots;
	private TextArea DebugConsole;
	public int GameSpeed = 8;
	private boolean respawnDelayActive = false;

	public int Monster_HP = 60;
	public Vector<Monster> Monsters = new Vector<Monster>(64, 5);
	public int Monster_Direction = 0;
	public String Player1Name;
	public String Player2Name;


	private boolean P1_inRight = false;
	private boolean P1_inLeft = false;
	private boolean P1_inShoot = false;
	private boolean P2_inRight = false;
	private boolean P2_inLeft = false;
	private boolean P2_inShoot = false;

	public int clearcount = 0;
	private int shootChance = 3000;

	private int Tick = 1;
	public int movetick = 0;

	public ListView<String> Lobbys;
	public Text CurrentLobby;

	private long frameTime;
	private long refreshTime;
	private long lastRefresh;
	private int frames = 1;

	private Frame() {}

	public static Frame getInstance() {
		if (game == null) {
			game = new Frame();
		}
		return game;
	}

	public void start(Stage primaryStage) throws Exception {
		MainStage = primaryStage;
		MainStage.setTitle("BlockInvaders");

		connectScene();

		// Game Scene
		HBox cvcontainer = new HBox();
		cv = new Canvas(GAME_WIDTH, GAME_LENGTH);
		gc = cv.getGraphicsContext2D();
		cvcontainer.getChildren().add(cv);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		GameScene = new Scene(cvcontainer, GAME_WIDTH, GAME_LENGTH);

		MainStage.show();

		GameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.A) {
					P1_inLeft = true;
				} 
				if(event.getCode() == KeyCode.D) {
					P1_inRight = true;
				}
				if (event.getCode() == KeyCode.W) {
					P1_inShoot = true;
				}
				if (event.getCode() == KeyCode.CONTROL) {
					if (Players[0].getHisSpecialWeapon() != null)
						Players[0].getHisSpecialWeapon().shoot(Players[0].getX(), Players[0].getY());
				}

				if (event.getCode() == KeyCode.LEFT) {
					P2_inLeft = true;
				}
				if(event.getCode() == KeyCode.RIGHT) {
					P2_inRight = true;
				}
				if (event.getCode() == KeyCode.UP) {
					P2_inShoot = true;
				}
				if (event.getCode() == KeyCode.NUMPAD0 && Coop_enabled) {
					if (Players[1].getHisSpecialWeapon() != null)
						Players[1].getHisSpecialWeapon().shoot(Players[1].getX(), Players[1].getY());
				}

			}
		});

		GameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.A) {
					P1_inLeft = false;
				}
				if (event.getCode() == KeyCode.D) {
					P1_inRight = false;
				}
				if (event.getCode() == KeyCode.W) {
					P1_inShoot = false;
				}

				if (Coop_enabled) {
					if (event.getCode() == KeyCode.LEFT) {
						P2_inLeft = false;
					}
					if (event.getCode() == KeyCode.RIGHT) {
						P2_inRight = false;
					}
					if (event.getCode() == KeyCode.UP) {
						P2_inShoot = false;
					}
				}
			}
		});

		MainStage.setOnCloseRequest(e -> {
			System.exit(0);
		});

		if (loadSettings())
			switchSceneToGame();
	}

	public void switchSceneToGame() {
		Player P1 = new Player(GAME_WIDTH / 2 - 100, 10, null);
		P1.giveWeapon(new StandardWeapon(P1));
		P1.giveSpecialWeapon(new RocketLauncher(Players[0], 0));
		Players[0] = P1;

		if (Bot_enabled)
			if (botID == 0)
				bot = new Bot1(Players[0]);
			else if (botID == 1)
				bot = new Bot2(Players[0]);
		if (Play_with_bot_enabled) {
			Player P2 = new Player(GAME_WIDTH / 2 + 100, 10, null);
			P2.giveWeapon(new StandardWeapon(P2));
			Players[1] = P2;
			Players[1].giveSpecialWeapon(new RocketLauncher(Players[1], 0));

			if (botID == 0)
				bot = new Bot1(Players[1]);
			else if (botID == 1)
				bot = new Bot2(Players[1]);
		}

		if (KI_Coop_enabled) {
			bot = new Bot1(Players[0]);

			Player P2 = new Player(GAME_WIDTH / 2 + 100, 10, null);
			P2.giveWeapon(new StandardWeapon(P2));
			Players[1] = P2;
			Players[1].giveSpecialWeapon(new RocketLauncher(Players[1], 0));

			bot2 = new Bot2(Players[1]);
		}

		if (Coop_enabled || Online_Coop) {
			Player P2 = new Player(GAME_WIDTH / 2 + 100, 10, null);
			P2.giveWeapon(new StandardWeapon(P2));
			Players[1] = P2;
			Players[1].giveSpecialWeapon(new RocketLauncher(Players[1], 0));
		}

		if (KiPartyPlayers > 0) {
			bots[0] = new Bot1(Players[0]);
			for (int i = 1; i < KiPartyPlayers; i++) {
				Players[i] = new Player(GAME_WIDTH / 2 + 100 + 100 * i, 10, null);
				Players[i].giveWeapon(new StandardWeapon(Players[i]));
				Players[i].giveSpecialWeapon(new RocketLauncher(Players[i], 0));
				if (i % 2 == 0)
					bots[i] = new Bot1(Players[i]);
				else
					bots[i] = new Bot2(Players[i]);
			}
		}

		SetupDraw();
		MonsterWaves.SpawnWave(0);
		CreateTimers();
		rTf.setCycleCount(Animation.INDEFINITE);
		tf.setCycleCount(Animation.INDEFINITE);
		tf.play();
		rTf.play();

		MainStage.setScene(GameScene);
	}

	public void connectScene() {
		// Connect Scene
		BorderPane connect_Bp = new BorderPane();
		ImageView back = new ImageView(new Image("background.jpg"));
		connect_Bp.getChildren().add(back);
		VBox connect_MiddlePart = new VBox();
		connect_MiddlePart.setPadding(new Insets(100, 500, 100, 500));
		connect_MiddlePart.setSpacing(20);

		connect_Bp.setCenter(connect_MiddlePart);
		// BorderPane.setAlignment(connect_MiddlePart, Pos.CENTER);

		Random r = new Random();
		int id = r.nextInt(20);

		HBox Usernames = new HBox();
		connect_MiddlePart.getChildren().add(Usernames);
		Usernames.setSpacing(5);

		final TextField Username1 = new TextField("Username-1#" + id);

		Username1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 16)
					Username1.setText(oldValue);
			}
		});
		Usernames.getChildren().add(Username1);

		final TextField Username2 = new TextField("Username-2#" + id);
		Username2.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 16)
					Username2.setText(oldValue);
			}
		});
		Usernames.getChildren().add(Username2);
		Usernames.setAlignment(Pos.BOTTOM_CENTER);

		Button Coop = new Button("Local Co-op");
		connect_MiddlePart.getChildren().add(Coop);
		Coop.setAlignment(Pos.BOTTOM_CENTER);
		Coop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Player1Name = Username1.getText();
				Player2Name = Username2.getText();
				Players = new Player[2];
				Coop_enabled = true;
				switchSceneToGame();
			}
		});

		Button SinglePlayer = new Button("Single Player");
		connect_MiddlePart.getChildren().add(SinglePlayer);
		SinglePlayer.setAlignment(Pos.BOTTOM_CENTER);
		SinglePlayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Player1Name = Username1.getText();
				Player2Name = Username2.getText();
				Players = new Player[1];
				Coop_enabled = false;
				switchSceneToGame();
			}
		});

		VBox VBotBox = new VBox();
		VBotBox.setAlignment(Pos.BASELINE_CENTER);
		VBotBox.setSpacing(10);
		HBox BotBox = new HBox();

		ChoiceBox<String> botType = new ChoiceBox<String>();
		botType.getItems().addAll("Bot 1 (Totenfluch)", "Bot 2 (toxi675)");
		botType.setValue("Bot 1 (Totenfluch)");
		VBotBox.getChildren().add(botType);

		botType.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			if (newValue.equals("Bot 2 (toxi675)"))
				botID = 1;
			else
				botID = 0;
		});

		Button WatchKi = new Button("Watch KI Play");
		BotBox.getChildren().add(WatchKi);
		BotBox.setSpacing(10);
		BotBox.setAlignment(Pos.BASELINE_CENTER);
		connect_MiddlePart.getChildren().add(VBotBox);

		WatchKi.setOnAction(ae -> {
			Bot1.setBot_debug(true);
			Bot_enabled = true;
			if (botID == 0)
				Player1Name = "Bot 1 - Totenfluch";
			else
				Player1Name = "Bot 2 - toxi675";
			Players = new Player[1];
			switchSceneToGame();
		});
		VBotBox.getChildren().add(BotBox);
		VBotBox.setPadding(new Insets(20));

		Button PlayWithKi = new Button("Play with KI (You have no friends)");
		BotBox.getChildren().add(PlayWithKi);
		PlayWithKi.setOnAction(ae -> {
			Play_with_bot_enabled = true;
			if (botID == 0)
				Player2Name = "Bot 1 - Totenfluch";
			else
				Player2Name = "Bot 2 - toxi675";
			Player1Name = Username1.getText();
			Players = new Player[2];
			switchSceneToGame();
		});

		Button KiCoop = new Button("KI Coop");
		connect_MiddlePart.getChildren().add(KiCoop);
		KiCoop.setOnAction(ae -> {
			KI_Coop_enabled = true;
			Player1Name = "Bot 1 - Totenfluch";
			Player2Name = "Bot 2 - toxi675";
			Players = new Player[2];
			switchSceneToGame();
		});

		Button KiCoopParty = new Button("KI Party");
		connect_MiddlePart.getChildren().add(KiCoopParty);
		KiCoopParty.setOnAction(ae -> {
			Player1Name = "Bot 1 - Totenfluch";
			Player2Name = "Bot 2 - toxi675";
			KiPartyPlayers = 4;
			Players = new Player[4];
			bots = new BotKI[4];
			switchSceneToGame();
		});

		Text SpeedBoxDesc = new Text("Gamespeed");
		SpeedBoxDesc.setFont(new Font("Futura", 15));
		SpeedBoxDesc.setFill(Color.WHITE);
		connect_MiddlePart.getChildren().add(SpeedBoxDesc);
		ChoiceBox<String> SpeedBox = new ChoiceBox<String>();
		SpeedBox.getItems().addAll("Slow", "Normal", "Fast", "Very Fast", "Insane", "Ok...");
		SpeedBox.setValue("Normal");
		connect_MiddlePart.getChildren().add(SpeedBox);

		SpeedBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			if (newValue.equals("Slow"))
				GameSpeed = 10;
			else if (newValue.equals("Normal"))
				GameSpeed = 8;
			else if (newValue.equals("Fast"))
				GameSpeed = 6;
			else if (newValue.equals("Very Fast"))
				GameSpeed = 4;
			else if (newValue.equals("Insane"))
				GameSpeed = 2;
			else if (newValue.equals("Ok..."))
				GameSpeed = 1;
		});

		Text BenchmarkDesc = new Text("Benchmark");
		BenchmarkDesc.setFont(new Font("Futura", 15));
		BenchmarkDesc.setFill(Color.WHITE);
		connect_MiddlePart.getChildren().add(BenchmarkDesc);
		ChoiceBox<String> BenchmarkBox = new ChoiceBox<String>();
		BenchmarkBox.getItems().addAll("Disabled", "Enabled", "Bot Benchmark", "Speedy Gonzales");
		BenchmarkBox.setValue("Disabled");
		

		TextField FrameCounter = new TextField("32");
		HBox BenchmarkCounterBox = new HBox();
		BenchmarkCounterBox.setSpacing(10);
		BenchmarkCounterBox.setAlignment(Pos.BASELINE_CENTER);
		BenchmarkCounterBox.getChildren().add(BenchmarkBox);
		
		connect_MiddlePart.getChildren().add(BenchmarkCounterBox);
		
		BenchmarkBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			if (newValue.equals("Disabled")){
				Performance_benchmark_enabled = false;
				BenchmarkCounterBox.getChildren().remove(FrameCounter);
			}else if (newValue.equals("Enabled")){
				Performance_benchmark_enabled = true;
				Bot_Performance_benchmark_enabled = false;
				BenchmarkCounterBox.getChildren().remove(FrameCounter);
			}else if (newValue.equals("Bot Benchmark")){
				Bot_Performance_benchmark_enabled = true;
				Performance_benchmark_enabled = true;
				BenchmarkCounterBox.getChildren().remove(FrameCounter);
				BenchmarkCounterBox.getChildren().add(FrameCounter);
			} else if (newValue.equals("Speedy Gonzales")){
				Bot_Performance_benchmark_enabled = true;
				BenchmarkCounterBox.getChildren().remove(FrameCounter);
				BenchmarkCounterBox.getChildren().add(FrameCounter);
			}
		});
		
		FrameCounter.textProperty().addListener((obss, ov, nv)->{
			if (!nv.matches("\\d*")) {
				FrameCounter.setText(nv.replaceAll("[^\\d]", ""));
				Bot_Benchmark_threads = Integer.valueOf(nv.replaceAll("[^\\d]", ""));
            }
		});

		Text restartDesc = new Text("Restart");
		restartDesc.setFont(new Font("Futura", 15));
		restartDesc.setFill(Color.WHITE);
		connect_MiddlePart.getChildren().add(restartDesc);
		ChoiceBox<String> restartToggle = new ChoiceBox<String>();
		restartToggle.getItems().addAll("Disabled", "Enabled");
		restartToggle.setValue("Disabled");
		connect_MiddlePart.getChildren().add(restartToggle);

		restartToggle.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			if (newValue.equals("Disabled"))
				autoRestart = false;
			else if (newValue.equals("Enabled"))
				autoRestart = true;
		});

		VBox LobbyVBox = new VBox();
		LobbyVBox.setPrefHeight(500);
		LobbyVBox.setPrefWidth(400);
		// connect_Bp.setRight(LobbyVBox);

		Lobbys = new ListView<String>();
		LobbyVBox.getChildren().add(Lobbys);

		HBox LobbyControls = new HBox();
		LobbyVBox.getChildren().add(LobbyControls);

		TextField LobbyName = new TextField();
		LobbyName.setTooltip(new Tooltip("Lobby Name"));
		LobbyName.setPromptText("Lobby Name");
		LobbyControls.getChildren().add(LobbyName);
		LobbyName.setPrefWidth(300);

		Button CreateLobby = new Button("Create");
		CreateLobby.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (LobbyName.getText().length() > 4 && LobbyName.getText().length() < 16) {
					client.processMessage("createLobby " + LobbyName.getText().replace(" ", ""));
					DebugConsole
					.appendText("Attempting to create Lobby " + LobbyName.getText().replace(" ", "") + "\n");
				} else {
					DebugConsole.appendText("Invalid Lobby Name length\n");
				}
			}
		});
		CreateLobby.setPrefWidth(LobbyVBox.getPrefWidth() - 160);

		LobbyControls.getChildren().add(CreateLobby);
		LobbyControls.setSpacing(5);

		Button JoinLobby = new Button("Join");
		JoinLobby.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Lobbys.getSelectionModel().getSelectedItem() != null) {
					String[] selectedLobbypart = Lobbys.getSelectionModel().getSelectedItem().split(" ");
					client.processMessage("joinLobby " + selectedLobbypart[1]);
					DebugConsole.appendText("Attempting to join " + selectedLobbypart[1] + "\n");
				}
			}
		});
		JoinLobby.setPrefWidth(LobbyVBox.getPrefWidth() - 100);

		LobbyVBox.getChildren().add(JoinLobby);

		LobbyVBox.setPadding(new Insets(50, 50, 50, 50));
		LobbyVBox.setSpacing(10);
		LobbyVBox.setBackground(
				new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, LobbyVBox.getInsets())));

		TextField OnlineUsername = new TextField();
		OnlineUsername.setPromptText("Online Username");
		LobbyVBox.getChildren().add(OnlineUsername);
		OnlineUsername.setPrefWidth(350);

		CurrentLobby = new Text("Your Lobby: none");
		LobbyVBox.getChildren().add(CurrentLobby);
		CurrentLobby.setFont(new Font(20));
		CurrentLobby.setFill(Color.WHITE);

		HBox Online_coop_hbox = new HBox();

		Button Confirm = new Button("Online Coop");
		connect_MiddlePart.setAlignment(Pos.TOP_CENTER);
		connect_MiddlePart.getChildren().add(Online_coop_hbox);
		Confirm.setAlignment(Pos.BOTTOM_CENTER);

		TextField AlternativeIP = new TextField();
		AlternativeIP.setPromptText("Alternative IP (Optional)");
		Online_coop_hbox.getChildren().add(AlternativeIP);
		Online_coop_hbox.getChildren().add(Confirm);
		Online_coop_hbox.setAlignment(Pos.TOP_CENTER);
		Online_coop_hbox.setSpacing(10);

		Confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (AlternativeIP.getText().equals("")) {
					client = new Client("127.0.0.1", 1521);
					connect_Bp.setRight(LobbyVBox);
					DebugConsole.appendText("Connected to Server\n");
				} else if (AlternativeIP.getText().contains(".") && AlternativeIP.getText().length() > 6
						&& AlternativeIP.getText().length() < 16 && !AlternativeIP.getText().matches("^[a-zA-Z]")) {
					client = new Client(AlternativeIP.getText(), 1521);
					connect_Bp.setRight(LobbyVBox);
					DebugConsole.appendText("Connected to Server\n");
				} else
					System.out.println("Wrong IP m8");
			}
		});

		DebugConsole = new TextArea();
		// DebugConsole.setDisable(true); -- disable user input
		DebugConsole.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				DebugConsole.setScrollTop(Double.MAX_VALUE);
			}
		});
		LobbyVBox.getChildren().add(DebugConsole);

		ConnectScene = new Scene(connect_Bp, GAME_WIDTH, GAME_LENGTH);
		ConnectScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		MainStage.setScene(ConnectScene);

		// Connect Scene fin
	}

	public void SetupDraw() {
		if(!Performance_benchmark_enabled){
			tf = new Timeline(new KeyFrame(Duration.millis(5), ae -> {
				frames++;
				long time = System.nanoTime();

				// Reset Canvas
				gc.clearRect(0, 0, GAME_WIDTH, GAME_LENGTH);
				gc.setFill(Color.BLACK);
				gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);

				// Draw Players
				for(Player player : Players)
					player.draw(gc);

				// Draw Monsters
				Monsters.forEach(m -> m.draw(gc));

				// Draw Player Bullets
				PlayerWeapon.ActiveWeapons.forEach(Pw -> Pw.getKugeln().forEach(pb -> pb.draw(gc)));

				// Draw Monster Bullets
				MonsterWeapon.ActiveWeapons.forEach(Pw -> Pw.getKugeln().forEach(pb -> pb.draw(gc)));

				// Draw Drops
				Drop.AllDrops.forEach(b -> b.draw(gc));

				// Wave Counter
				gc.setFill(Color.GOLD);
				gc.setFont(new Font("Impact", 20));
				gc.fillText("Wave: " + (clearcount + 1), GAME_WIDTH / 2 - 30, 40);


				frameTime += System.nanoTime() - time;
			}));
		}else if(Performance_benchmark_enabled && !Bot_Performance_benchmark_enabled){
			tf = new Timeline(new KeyFrame(Duration.millis(100), ae -> {
				gc.clearRect(0, 0, GAME_WIDTH, GAME_LENGTH);
				gc.setFill(Color.BLACK);
				gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
				gc.setFont(new Font("Futura", 30));
				gc.setFill(Color.CRIMSON);
				gc.fillText("Average Refreshtime: " + refreshTime/Tick/1000 + "µs", 50, 50);
				gc.fillText("Current Refreshtime: " + lastRefresh/1000 + "µs", 50, 100);
				gc.fillText("Wave: " + (clearcount+1), 50, 150);
				gc.fillText("Monsters Alive: " + Monsters.size(), 50, 200);
				gc.fillText("Active Weapons: " + (MonsterWeapon.ActiveWeapons.size()+PlayerWeapon.ActiveWeapons.size()), 50, 250);
				gc.fillText("|-> Monster Weapons: " + MonsterWeapon.ActiveWeapons.size(), 125, 300);
				gc.fillText("|-> Player Weapons: " + PlayerWeapon.ActiveWeapons.size(), 125, 350);
				int Bullets=0;
				int PlayerBullets=0;
				int MonsterBullets=0;
				for(PlayerWeapon w : PlayerWeapon.ActiveWeapons){
					Bullets+=w.getKugeln().size();
					PlayerBullets+=w.getKugeln().size();
				}
				for(MonsterWeapon w : MonsterWeapon.ActiveWeapons){
					Bullets+=w.getKugeln().size();
					MonsterBullets+=w.getKugeln().size();
				}
				gc.fillText("Active Bullets: " + Bullets, 50, 400);
				gc.fillText("|-> Active Player Bullets: " + PlayerBullets, 125, 450);
				gc.fillText("|-> Active Monster Bullets: " + MonsterBullets, 125, 500);
				int players=0;
				int blockwidth = 200;

				for(Player player : Players){
					gc.setFill(Color.PURPLE);
					gc.setFont(new Font("Futura", 25));
					gc.fillText("Player - " + players, 600, 50+players*blockwidth);
					gc.fillText("|-> Health: " + player.getLife(), 625, 75+players*blockwidth);
					gc.fillText("|-> Score: " + player.getScore(), 625, 100+players*blockwidth);
					gc.fillText("|-> Ammo: " + player.getHisWeapon().getAmmo(), 625, 125+players*blockwidth);
					gc.fillText("|-> SpecialAmmo: " + player.getHisSpecialWeapon().getAmmo(), 625, 150+players*blockwidth);
					gc.fillText("|-> Weapon: " + player.getHisWeapon().getWeaponType().toString(), 625, 175+players*blockwidth);
					players++;
				}
				int monsters=0;
				for(Monster monti: Monsters){
					if(monsters < 80){
						gc.setFill(Color.CORAL);
						gc.setFont(new Font("Futura", 10));
						gc.fillText("Monster - " + monsters + " - Hp:" + monti.getLife() + " Droprate:" + monti.getDropRate() + " Worth: " + monti.getWorth(), 925, 50+monsters*10);
						monsters++;
					}else{
						gc.setFill(Color.GREEN);
						gc.setFont(new Font("Impact", 25));
						gc.fillText(".   .   .", 925, 850);
					}
				}
				int weapons = 0;
				for(MonsterWeapon wp : MonsterWeapon.ActiveWeapons){
					if(weapons < 80){
						gc.setFill(Color.CORAL);
						gc.setFont(new Font("Futura", 10));
						
						gc.fillText(wp.toString() + wp.getOwner().toString(), 1125, 50+weapons*10);
						weapons++;
					}else{
						gc.setFill(Color.GREEN);
						gc.setFont(new Font("Impact", 25));
						gc.fillText(".   .   .", 925, 850);
					}
				}
			}));
		}else{
			tf = new Timeline(new KeyFrame(Duration.millis(100), ae -> {
				gc.setFill(Color.RED);
				gc.setFont(new Font("Futura", 15));
				gc.fillText(GameResults.averagesToString(), 5, 12);
				gc.setFill(Color.AZURE);
				int x = 25;
				int y = 25;
				for(GameResults gs : GameResults.getGameResultsResultSet()){
					gc.fillText(gs.toString(), x, y);
					y+= 245 + Players.length*65;
					if(y>= 650){
						x+= 155;
						y = 25;	
					}
				}
			}));
		}
	}

	public void CreateTimers() {
		KeyFrame mainRefresh = new KeyFrame(Duration.millis(GameSpeed), ae -> {
			long time = System.nanoTime();

			Refresh();
			if (Online_Coop)
				SyncOnline();
			if (Bot_enabled || Play_with_bot_enabled)
				bot.refresh();
			if (KI_Coop_enabled) {
				bot.refresh();
				bot2.refresh();
			}

			long timeEnd = System.nanoTime();
			lastRefresh = timeEnd - time;
			refreshTime += timeEnd - time;
		});
		rTf = new Timeline(mainRefresh);
		if(Bot_Performance_benchmark_enabled)
			for(int i = 0; i<Bot_Benchmark_threads; i++)
				rTf.getKeyFrames().add(mainRefresh);
	}

	public void Refresh() {

		Tick++;
		movetick++;

		for(int i = 0 ; i < Monsters.size() ; i++)
			Monsters.elementAt(i).doMovement();

		for(int i = 0; i < Monsters.size(); i++)
			Monsters.elementAt(i).refresh();

		for (int i = 0; i < Monsters.size(); i++)
			if (Monsters.elementAt(i).isAlive())
				if (new Random().nextInt((int) (shootChance * (1 / Monsters.elementAt(i).getShootRate()))) == 0)
					Monsters.elementAt(i).getHisWeapon().shoot(Monsters.elementAt(i).getX(),
							Monsters.elementAt(i).getY());

		if (P1_inLeft) {
			if (Players[0].isAlive())
				Players[0].moveLeft();

		} else if (P1_inRight) {
			if (Players[0].isAlive())
				Players[0].moveRight();
		}
		if (P1_inShoot) {
			if (Players[0].isAlive()) {
				Players[0].hisWeapon.shoot(Players[0].getX(), Players[0].getY());
				if (Online_Coop)
					client.processMessage("shoot " + Players[0].getX() + " " + Players[0].getY());
			}
		}

		if (Coop_enabled) {
			if (P2_inLeft) {
				if (Players[1].isAlive())
					Players[1].moveLeft();
			} else if (P2_inRight) {
				if (Players[1].isAlive())
					Players[1].moveRight();
			}
			if (P2_inShoot) {
				if (Players[1].isAlive())
					Players[1].hisWeapon.shoot(Players[1].getX(), Players[1].getY());
			}
		}

		// Can't use lambda. Need to modify weapon in refresh
		//PlayerWeapon.ActiveWeapons.forEach(PlayerWeapon::refresh);
		//MonsterWeapon.ActiveWeapons.forEach(MonsterWeapon::refresh);

		for(int i = 0; i<PlayerWeapon.ActiveWeapons.size(); i++)
			PlayerWeapon.ActiveWeapons.elementAt(i).refresh();

		for(int i = 0; i<MonsterWeapon.ActiveWeapons.size(); i++)
			MonsterWeapon.ActiveWeapons.elementAt(i).refresh();



		for (int i = 0; i < Drop.AllDrops.size(); i++)
			Drop.AllDrops.elementAt(i).refresh();

		if (Monsters.size() == 0 && !respawnDelayActive) {
			clearcount++;
			shootChance = (int) (10.0 + 2990 * Math.pow(Math.E, -0.07 * (clearcount + 1)));
			if(!Bot_Performance_benchmark_enabled){
				SpawnWaveDelay = new Timeline(new KeyFrame(Duration.millis(GameSpeed*375), ae->{
					MonsterWaves.SpawnWave(clearcount);
					respawnDelayActive = false;
				}));
				SpawnWaveDelay.play();
				respawnDelayActive = true;
			}else{
				MonsterWaves.SpawnWave(clearcount);
			}
		}

		int dead = 0;
		for (int i = 0; i < Players.length; i++) {
			Players[i].refresh();
			if (!Players[i].isAlive())
				dead++;
			if (dead == Players.length){
				EndGame();
				if(Bot_Performance_benchmark_enabled)
					System.out.println("Wave reached: " + clearcount);
			}

		}

		if (KiPartyPlayers > 0)
			for (int i = 0; i < bots.length; i++)
				bots[i].refresh();
	}

	public void SyncOnline() {
		client.processMessage("playerPos " + Players[0].getX() + " " + Players[0].getY());
	}

	public void EndGame() {
		rTf.stop();
		tf.stop();
		if(SpawnWaveDelay != null)
			SpawnWaveDelay.stop();


		System.out.println("Frametime (ns): " + frameTime / frames);
		System.out.println("Refreshtime (ns): " + refreshTime / Tick);
		PublishScores();
		new GameResults(new GameSettings(getGameMode(), Players.length, GameSpeed, botID, Player1Name, Player2Name, autoRestart, Performance_benchmark_enabled, Bot_Performance_benchmark_enabled), Players, clearcount, refreshTime);

		if(autoRestart){
			saveSettings();
			Game.restart();
			return;
		}

		VBox gameOver = new VBox();
		gameOver.setStyle("-fx-background: #000000");
		gOcanvas = new Canvas(800, 250);
		gOgc = gOcanvas.getGraphicsContext2D();

		gameOverScene = new Scene(gameOver, GAME_WIDTH, GAME_LENGTH);

		gameOver.setSpacing(20);

		gameOver.getChildren().add(gOcanvas);


		gOgc.setFont(new Font("Futura", 140));
		gOgc.setFill(Color.BLACK);
		gOgc.fillRect(0, 0, 800, 250);
		gOgc.setFill(Color.RED);
		gOgc.fillText("Game Over", 50, 150);
		gOgc.setFont(new Font("Futura", 30));
		gOgc.fillText("You survived " + clearcount + " Rounds", 250, 200);

		Button restart = new Button("Try Again");
		restart.setOnAction(ae -> {
			saveSettings();
			Game.restart();
		});


		Button settings = new Button("Settings");
		settings.setOnAction(ae -> {
			Game.setSettings(null);
			Game.restart();
		});

		Button exit = new Button("Exit");
		exit.setOnAction(ae -> System.exit(0));

		gameOver.getChildren().addAll(restart, settings, exit);	
		gameOver.setAlignment(Pos.CENTER);
		gameOverScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

		MainStage.setScene(gameOverScene);

	}

	public GraphicsContext getGraphicsContext(){
		return gc;
	}

	public void PublishScores() {
		if (Coop_enabled) {
			try {
				String request = "http://totenfluch.de/putScores.php?Username1=" + Player1Name + "&Username2="
						+ Player2Name + "&Score1=" + Players[0].getScore() + "&Score2=" + Players[1].getScore() + "";
				URL oracle = new URL(request);
				URLConnection yc = oracle.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String line;
				while((line = in.readLine()) != null)
					System.out.println(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean loadSettings() {
		if (Game.getSettings() == null)
			return false;

		GameSettings gs = Game.getSettings();

		switch (gs.getGameMode()) {
		case 0:
			Coop_enabled = false;
			break;

		case 1:
			Coop_enabled = true;
			break;

		case 2:
			// not supported
			return false;

		case 3:
			Play_with_bot_enabled = true;
			break;

		case 4:
			Bot_enabled = true;
			Bot1.setBot_debug(true);
			break;

		case 5:
			KI_Coop_enabled = true;
			break;

		case 6:
			KiPartyPlayers = gs.getPlayerNum();
			bots = new BotKI[gs.getPlayerNum()];
			break;
		}

		Players = new Player[gs.getPlayerNum()];
		GameSpeed = gs.getGameSpeed();
		botID = gs.getBotID();

		Player1Name = gs.getPlayer1Name();
		Player2Name = gs.getPlayer2Name();

		autoRestart = gs.isRestart();

		Performance_benchmark_enabled = gs.getPerformance_benchmark_enabled();
		Bot_Performance_benchmark_enabled = gs.getBot_Performance_benchmark_enabled();

		return true;
	}

	public void saveSettings() {
		int gameMode = getGameMode();

		GameSettings gs = new GameSettings(gameMode, Players.length, GameSpeed, botID, Player1Name, Player2Name, autoRestart, Performance_benchmark_enabled, Bot_Performance_benchmark_enabled);
		Game.setSettings(gs);
	}

	public int getGameMode(){
		int gameMode = 0;

		if (Coop_enabled)
			gameMode = 1;
		else if (Online_Coop)
			gameMode = 2;
		else if (Play_with_bot_enabled)
			gameMode = 3;
		else if (Bot_enabled)
			gameMode = 4;
		else if (KI_Coop_enabled)
			gameMode = 5;
		else if (KiPartyPlayers > 0)
			gameMode = 6;

		return gameMode;
	}

}
