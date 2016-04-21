package me.game.pack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Vector;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import me.game.weapons.StandardWaffe;

public class Frame extends Application {
	public static final int GAME_WIDTH = 1600;
	public static final int GAME_LENGTH = 900;

	public static Timeline tf;
	public static Timeline rTf;

	public static Canvas cv;
	public static GraphicsContext gc;

	public static Stage MainStage;
	public static Scene ConnectScene;
	public static Scene GameScene;

	public static Player[] Players;
	
	public static boolean Coop_enabled = false;
	public static boolean Online_Coop = false;
	public static boolean Bot_enabled = false;
	public static BotKI bot;
	public static TextArea DebugConsole;
	
	public static boolean P1_inRight = false;
	public static boolean P1_inLeft = false;
	public static boolean P1_inShoot = false;
	public static boolean P2_inRight = false;
	public static boolean P2_inLeft = false;
	public static boolean P2_inShoot = false;
	public static Vector<Monster> Monsters = new Vector<Monster>(64, 5);
	public static int Monster_Direction = 0;
	public static int Monster_HP = 60;
	public static int clearcount = 0;

	public static String Player1Name;
	public static String Player2Name;

	public static int Tick = 0;
	public static int movetick = 0;

	public static ListView<String> Lobbys;
	public static Text CurrentLobby;

	public static long frameTime;
	public static long refreshTime;
	public static int frames;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
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
		
		Button WatchKi = new Button("Watch KI Play");
		connect_MiddlePart.getChildren().add(WatchKi);
		WatchKi.setOnAction( ae -> {
			Bot_enabled = true;
			Player1Name = "Bot";
			Players = new Player[1];
			switchSceneToGame();
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
					Client.processMessage("createLobby " + LobbyName.getText().replace(" ", ""));
					DebugConsole.appendText("Attempting to create Lobby " + LobbyName.getText().replace(" ", "") + "\n");
				}else{
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
					Client.processMessage("joinLobby " + selectedLobbypart[1]);
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

		Button Confirm = new Button("Online Coop");
		connect_MiddlePart.getChildren().add(Confirm);
		connect_MiddlePart.setAlignment(Pos.TOP_CENTER);
		Confirm.setAlignment(Pos.BOTTOM_CENTER);
		Confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				connect_Bp.setRight(LobbyVBox);
				Client.ConnectToServer("127.0.0.1", 1521);  //for testing purposes. old value: "totenfluch.de"
				DebugConsole.appendText("Connected to Server\n");
			}
		});
		DebugConsole = new TextArea();
		//DebugConsole.setDisable(true); -- disable user input
		DebugConsole.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
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

			for (int i = 0; i < PlayerWeapon.ActiveWeapons.size(); i++) {
				for (int x = 0; x < PlayerWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++) {
					Bullet draw = PlayerWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
					gc.fillRect(draw.getxPos(), draw.getyPos(), draw.getWidth(), draw.getHeight());
				}
			}

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

		rTf = new Timeline(new KeyFrame(Duration.millis(8), ae -> {

			long time = System.nanoTime();

			Refresh();
			if (Online_Coop)
				SyncOnline();
			if(Bot_enabled)
				bot.Refresh();

			refreshTime += System.nanoTime() - time;
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

				if (event.getCode() == KeyCode.LEFT) {
					P2_inLeft = true;
				} else if (event.getCode() == KeyCode.RIGHT) {
					P2_inRight = true;
				}
				if (event.getCode() == KeyCode.UP) {
					P2_inShoot = true;
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

				if (event.getCode() == KeyCode.LEFT) {
					P2_inLeft = false;
				} else if (event.getCode() == KeyCode.RIGHT) {
					P2_inRight = false;
				}
				if (event.getCode() == KeyCode.UP) {
					P2_inShoot = false;
				}
			}
		});

		MainStage.setOnCloseRequest(e -> {
			System.exit(0);

		});
	}

	public static void switchSceneToGame() {
		Player P1 = new Player(GAME_WIDTH / 2 - 100, 10, null);
		P1.giveWeapon(new StandardWaffe(P1));

		MonsterWaves.SpawnWave(0);

		Players[0] = P1;
		if(Bot_enabled)
			bot = new BotKI(Players[0]);
		if (Coop_enabled || Online_Coop) {
			Player P2 = new Player(GAME_WIDTH / 2 + 100, 10, null);
			P2.giveWeapon(new StandardWaffe(P2));
			Players[1] = P2;
		}
		rTf.setCycleCount(Timeline.INDEFINITE);
		tf.setCycleCount(Timeline.INDEFINITE);
		tf.play();
		rTf.play();

		MainStage.setScene(GameScene);
	}

	public static void Refresh() {

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
				if (new Random().nextInt(3200 - clearcount * 75) == 1)
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
			if (Players[0].isAlive()){
				Players[0].hisWeapon.shoot(Players[0].getX(), Players[0].getY());
				if(Online_Coop)
					Client.processMessage("shoot " + Players[0].getX() + " " + Players[0].getY());
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
			MonsterWaves.SpawnWave(clearcount);
		}

		int dead = 0;
		for (int i = 0; i < Players.length; i++) {
			if (!Players[i].isAlive())
				dead++;
			if (dead == Players.length)
				EndGame();
		}

	}

	public void SyncOnline() {
		Client.processMessage("playerPos " + Players[0].getX() + " " + Players[0].getY());
	}

	public static void EndGame() {
		rTf.stop();
		tf.stop();
		gc.clearRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		gc.setFont(new Font("Futura", 140));
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		gc.setFill(Color.RED);
		gc.fillText("Game Over", 500, GAME_LENGTH / 2 + 200);

		System.out.println("Frametime: " + frameTime / frames);
		System.out.println("Refreshtime: " + refreshTime / Tick);
		PublishScores();
	}

	@SuppressWarnings("unused")
	public static void PublishScores() {
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
