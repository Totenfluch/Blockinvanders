package me.game.pack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Vector;

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
import me.game.bullets.Bullet;
import me.game.networking.Client;
import me.game.pack.Drop.Drops;
import me.game.weapons.MonsterWeapon;
import me.game.weapons.PlayerWeapon;
import me.game.weapons.RocketLauncher;
import me.game.weapons.StandardWaffe;

public class Frame {
	public static final int GAME_WIDTH = 1600;
	public static final int GAME_LENGTH = 900;

	public Timeline tf;
	public Timeline rTf;

	public Canvas cv;
	public GraphicsContext gc;

	public Stage MainStage;
	public Scene ConnectScene;
	public Scene GameScene;

	public Player[] Players;

	public Client client;
	public static Frame game;

	public boolean Coop_enabled = false;
	public boolean Online_Coop = false;
	public boolean Bot_enabled = false;
	public boolean KI_Coop_enabled = false;
	public boolean Play_with_bot_enabled = false;
	public int KiPartyPlayers = 0;
	public int botID = 0;
	public BotKI bot;
	public BotKI bot2;
	public BotKI[] bots;
	public TextArea DebugConsole;
	public int GameSpeed = 8;

	public boolean P1_inRight = false;
	public boolean P1_inLeft = false;
	public boolean P1_inShoot = false;
	public boolean P2_inRight = false;
	public boolean P2_inLeft = false;
	public boolean P2_inShoot = false;
	public Vector<Monster> Monsters = new Vector<Monster>(64, 5);
	public int Monster_Direction = 0;
	public int Monster_HP = 60;
	public int clearcount = 0;
	public int shootChance = 3000;

	public String Player1Name;
	public String Player2Name;

	public int Tick = 0;
	public int movetick = 0;

	public ListView<String> Lobbys;
	public Text CurrentLobby;

	public long frameTime;
	public long refreshTime;
	public int frames;

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
		// Connect Scene
		BorderPane connect_Bp = new BorderPane();
		ImageView back = new ImageView(new Image("background.jpg"));
		connect_Bp.getChildren().add(back);
		VBox connect_MiddlePart = new VBox();
		connect_MiddlePart.setPadding(new Insets(200, 500, 500, 500));
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
			Bot1.Bot_debug = true;
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
				if(AlternativeIP.getText().equals("")){
					client = new Client("127.0.0.1", 1521); // for testing
																// purposes. old
																// value:
																// "139.59.134.247"
					connect_Bp.setRight(LobbyVBox);
					DebugConsole.appendText("Connected to Server\n");
				}else if(AlternativeIP.getText().contains(".") && AlternativeIP.getText().length() > 6 && AlternativeIP.getText().length() < 16 && !AlternativeIP.getText().matches("^[a-zA-Z]")){
					client = new Client(AlternativeIP.getText(), 1521);
					connect_Bp.setRight(LobbyVBox);
					DebugConsole.appendText("Connected to Server\n");
				}else
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
		MainStage.setScene(ConnectScene);

		// Connect Scene fin

		// Game Scene
		HBox cvcontainer = new HBox();
		cv = new Canvas(GAME_WIDTH, GAME_LENGTH);
		gc = cv.getGraphicsContext2D();
		cvcontainer.getChildren().add(cv);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		GameScene = new Scene(cvcontainer, GAME_WIDTH, GAME_LENGTH);

		MainStage.show();

		tf = new Timeline(new KeyFrame(Duration.millis(5), ae -> {
			frames++;
			long time = System.nanoTime();

			gc.clearRect(0, 0, GAME_WIDTH, GAME_LENGTH);
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);

			// Players

			for (int i = 0; i < Players.length; i++) {
				if (i == 0)
					gc.setFill(Color.PURPLE);
				else if (i == 1)
					gc.setFill(Color.LIME);
				else if (i == 2)
					gc.setFill(Color.BLUE);
				else if (i == 3)
					gc.setFill(Color.CRIMSON);

				Player p = Players[i];
				if (p.isAlive())
					gc.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
			}

			for (int i = 0; i < Monsters.size(); i++) {
				Monster Monti = Monsters.elementAt(i);
				if (Monti.isAlive()) {
					gc.setFill(Monti.getColor());
					gc.fillRect(Monti.getX(), Monti.getY(), Monti.getWidth(), Monti.getHeight());
					if (Monti.getLife() != Monti.getInitHp()) {
						double fragleben = (double) Monti.getLife() / (double) Monti.getInitHp();
						Color HpColor = new Color(1.0f * (1 - fragleben), 1.0f * fragleben, 0, 1);
						gc.setFill(HpColor);
						gc.fillRect(Monti.getX() + Monti.getWidth() / 10, Monti.getY() + Monti.getHeight() / 2.5,
								Monti.getWidth() * fragleben * 0.8, Monti.getHeight() - Monti.getHeight() * 0.8);
					}
				}
			}

			gc.setFont(new Font("Impact", 20));
			if (Players[0].isAlive()) {
				gc.setFill(Color.LIGHTGRAY);
				StringBuffer swb = new StringBuffer();
				// gc.fillText(""+Players[0].waffe.getAmmo(), 50, 100);
				swb.append(Players[0].hisWeapon.getWeaponType().toString() + ": ");
				for (int i = 0; i < Players[0].hisWeapon.getAmmo(); i++)
					swb.append("|");
				swb.append("   ");
				for (int x = 0; x < Players[0].getHisSpecialWeapon().getAmmo(); x++)
					swb.append("<|> ");
				gc.fillText(swb.toString(), 50, 100);
			}
			gc.setFont(new Font("Impact", 30));
			gc.setFill(Color.BLUEVIOLET);
			gc.fillText(Player1Name + "> " + Players[0].getScore(), 50, 50);
			gc.setFill(Color.RED);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < Players[0].getLife(); i++)
				sb.append("+ ");
			if (Players[0].isAlive())
				gc.fillText(sb.toString(), 45, 75);
			else
				gc.fillText("DEAD", 45, 80);

			if (Players.length > 1) {
				gc.setFont(new Font("Impact", 20));
				if (Players[1].isAlive()) {
					gc.setFill(Color.LIGHTGRAY);
					// gc.fillText(""+Players[1].waffe.getAmmo(), 50, 875);
					StringBuffer swb2 = new StringBuffer();
					swb2.append(Players[1].hisWeapon.getWeaponType().toString() + ": ");
					for (int i = 0; i < Players[1].hisWeapon.getAmmo(); i++)
						swb2.append("|");
					swb2.append("   ");
					for (int x = 0; x < Players[1].getHisSpecialWeapon().getAmmo(); x++)
						swb2.append("<|> ");
					gc.fillText(swb2.toString(), 50, 875);
				}
				gc.setFont(new Font("Impact", 30));
				gc.setFill(Color.LIME);
				gc.fillText(Player2Name + "> " + Players[1].getScore(), 50, 825);
				gc.setFill(Color.RED);
				StringBuffer sb2 = new StringBuffer();
				for (int i = 0; i < Players[1].getLife(); i++)
					sb2.append("+ ");
				if (Players[1].isAlive())
					gc.fillText(sb2.toString(), 45, 850);
				else
					gc.fillText("DEAD", 45, 855);
			}

			gc.setFill(Color.AQUA);
			for (int i = 0; i < PlayerWeapon.ActiveWeapons.size(); i++) {
				for (int x = 0; x < PlayerWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++) {
					Bullet draw = PlayerWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
					gc.fillRect(draw.getxPos(), draw.getyPos(), draw.getWidth(), draw.getHeight());
				}
			}

			gc.setFill(Color.RED);
			for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++) {
				for (int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++) {
					Bullet draw = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
					gc.fillRect(draw.getxPos(), draw.getyPos(), draw.getWidth(), draw.getHeight());
				}
			}

			for (int i = 0; i < Drop.AllDrops.size(); i++) {
				Drop p = Drop.AllDrops.elementAt(i);
				Drops pd = p.getDroptype();
				if (pd.equals(Drops.NEXTWEAPON))
					gc.setFill(Color.GOLD);
				else if (pd.equals(Drops.ADDAMMO))
					gc.setFill(Color.AQUAMARINE);
				else if (pd.equals(Drops.ADDLEBEN))
					gc.setFill(Color.FIREBRICK);
				else if (pd.equals(Drops.ADDSCORE))
					gc.setFill(Color.BLUE);
				gc.fillOval(p.xPos, p.yPos, Drop.DropSizeX, Drop.DropSizeY);
			}
			gc.setFill(Color.GOLD);

			gc.setFont(new Font("Impact", 20));
			gc.fillText("Wave: " + (clearcount + 1), GAME_WIDTH / 2 - 30, 40);

			frameTime += System.nanoTime() - time;
		}));

		GameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.A) {
					P1_inLeft = true;
				} else if (event.getCode() == KeyCode.D) {
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
				} else if (event.getCode() == KeyCode.RIGHT) {
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
				} else if (event.getCode() == KeyCode.D) {
					P1_inRight = false;
				}
				if (event.getCode() == KeyCode.W) {
					P1_inShoot = false;
				}

				if (Coop_enabled) {
					if (event.getCode() == KeyCode.LEFT) {
						P2_inLeft = false;
					} else if (event.getCode() == KeyCode.RIGHT) {
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
	}

	public void switchSceneToGame() {
		Player P1 = new Player(GAME_WIDTH / 2 - 100, 10, null);
		P1.giveWeapon(new StandardWaffe(P1));
		P1.giveSpecialWeapon(new RocketLauncher(Players[0], 0));
		Players[0] = P1;

		if (Bot_enabled)
			if (botID == 0)
				bot = new Bot1(Players[0]);
			else if (botID == 1)
				bot = new Bot2(Players[0]);
		if (Play_with_bot_enabled) {
			Player P2 = new Player(GAME_WIDTH / 2 + 100, 10, null);
			P2.giveWeapon(new StandardWaffe(P2));
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
			P2.giveWeapon(new StandardWaffe(P2));
			Players[1] = P2;
			Players[1].giveSpecialWeapon(new RocketLauncher(Players[1], 0));

			bot2 = new Bot2(Players[1]);
		}

		if (Coop_enabled || Online_Coop) {
			Player P2 = new Player(GAME_WIDTH / 2 + 100, 10, null);
			P2.giveWeapon(new StandardWaffe(P2));
			Players[1] = P2;
			Players[1].giveSpecialWeapon(new RocketLauncher(Players[1], 0));
		}

		if (KiPartyPlayers > 0) {
			bots[0] = new Bot1(Players[0]);
			for (int i = 1; i < KiPartyPlayers; i++) {
				Players[i] = new Player(GAME_WIDTH / 2 + 100 + 100 * i, 10, null);
				Players[i].giveWeapon(new StandardWaffe(Players[i]));
				Players[i].giveSpecialWeapon(new RocketLauncher(Players[i], 0));
				if (i % 2 == 0)
					bots[i] = new Bot1(Players[i]);
				else
					bots[i] = new Bot2(Players[i]);
			}
		}

		MonsterWaves.SpawnWave(0);
		CreateTimers();
		rTf.setCycleCount(Timeline.INDEFINITE);
		tf.setCycleCount(Timeline.INDEFINITE);
		tf.play();
		rTf.play();

		MainStage.setScene(GameScene);
	}

	public void CreateTimers() {
		rTf = new Timeline(new KeyFrame(Duration.millis(GameSpeed), ae -> {
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

			refreshTime += System.nanoTime() - time;
		}));
	}

	public void Refresh() {

		Tick++;
		movetick++;
		if (movetick == 500 && Monster_Direction == 0) {
			Monster_Direction = 1;
			movetick = 0;
		} else if (movetick == 500 && Monster_Direction == 1) {
			Monster_Direction = 0;
			movetick = 0;
		}

		if (Monster_Direction == 1) {
			for (int i = 0; i < Monsters.size(); i++)
				Monsters.elementAt(i).moveRight();
		} else if (Monster_Direction == 0) {
			for (int i = 0; i < Monsters.size(); i++)
				Monsters.elementAt(i).moveLeft();
		}

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

		for (int i = 0; i < PlayerWeapon.ActiveWeapons.size(); i++)
			PlayerWeapon.ActiveWeapons.elementAt(i).refresh();

		for (int i = 0; i < Drop.AllDrops.size(); i++)
			Drop.AllDrops.elementAt(i).refresh();

		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++)
			MonsterWeapon.ActiveWeapons.elementAt(i).refresh();

		if (Monsters.size() == 0) {
			clearcount++;
			shootChance = (int) (10.0 + 2990 * Math.pow(Math.E, -0.07 * (clearcount + 1)));
			MonsterWaves.SpawnWave(clearcount);
		}

		int dead = 0;
		for (int i = 0; i < Players.length; i++) {
			if (!Players[i].isAlive())
				dead++;
			if (dead == Players.length)
				EndGame();
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
		gc.clearRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		gc.setFont(new Font("Futura", 140));
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		gc.setFill(Color.RED);
		gc.fillText("Game Over", 450, GAME_LENGTH / 2 + 150);
		gc.setFont(new Font("Futura", 30));
		gc.fillText("You survived " + clearcount + " Rounds", 650, GAME_LENGTH / 2 + 200);

		System.out.println("Frametime (ns): " + frameTime / frames);
		System.out.println("Refreshtime (ns): " + refreshTime / Tick);
		PublishScores();
	}

	@SuppressWarnings("unused")
	public void PublishScores() {
		if (Coop_enabled) {
			try {
				String request = "http://totenfluch.de/putScores.php?Username1=" + Player1Name + "&Username2="
						+ Player2Name + "&Score1=" + Players[0].getScore() + "&Score2=" + Players[1].getScore() + "";
				URL oracle = new URL(request);
				URLConnection yc = oracle.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
