package me.game.pack;

import java.util.Random;
import java.util.Vector;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Frame extends Application{
	public static final int GAME_WIDTH = 1600;
	public static final int GAME_LENGTH = 900;

	public static Timeline tf;

	public static Canvas cv;
	public static GraphicsContext gc;

	public static Stage MainStage;
	public static Scene ConnectScene;
	public static Scene GameScene;

	public static Vector<Spieler> Players = new Vector<Spieler>();
	public static boolean Coop_enabled = false;
	public static boolean P1_inRight = false;
	public static boolean P1_inLeft = false;
	public static boolean P1_inShoot = false;
	public static boolean P2_inRight = false;
	public static boolean P2_inLeft = false;
	public static boolean P2_inShoot = false;
	public static Monster[] Monsters = new Monster[64];
	public static int Monster_Direction = 0;
	public static int Monster_HP = 60;
	public static int clearcount = 0;

	public static int Tick = 0;
	public static int movetick = 0;

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainStage = primaryStage;
		MainStage.setTitle("BlockInvaders");
		// Connect Scene
		BorderPane connect_Bp = new BorderPane();
		VBox connect_MiddlePart = new VBox();
		connect_MiddlePart.setPadding(new Insets(200, 500, 500, 500)); 
		connect_MiddlePart.setSpacing(20);

		connect_Bp.setCenter(connect_MiddlePart);
		//BorderPane.setAlignment(connect_MiddlePart, Pos.CENTER);

		Random r = new Random();
		int id = r.nextInt(20);
		TextField Username = new TextField("Username#" + id);
		connect_MiddlePart.getChildren().add(Username);

		TextField Host = new TextField("Host:Port");
		connect_MiddlePart.getChildren().add(Host);


		Button Confirm = new Button("Connect");
		connect_MiddlePart.getChildren().add(Confirm);
		connect_MiddlePart.setAlignment(Pos.TOP_CENTER);
		Confirm.setAlignment(Pos.BOTTOM_CENTER);
		Confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switchSceneToGame();
			}
		});
		Confirm.setDisable(true);

		Button Coop = new Button("Local Co-op");
		connect_MiddlePart.getChildren().add(Coop);
		Coop.setAlignment(Pos.BOTTOM_CENTER);
		Coop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Coop_enabled = true;
				switchSceneToGame();
			}
		});

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


		tf = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gc.clearRect(0, 0, GAME_WIDTH, GAME_LENGTH);
				gc.setFill(Color.BLACK);
				gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
				Refresh();
				// Players

				for(int i = 0; i<Players.size(); i++){
					if(i == 0)
						gc.setFill(Color.PURPLE);
					else if(i == 1)
						gc.setFill(Color.LIME);
					else if(i == 2)
						gc.setFill(Color.BLUE);
					else if(i == 3)
						gc.setFill(Color.CRIMSON);

					Spieler p = Players.elementAt(i);
					if(!p.isDead())
						gc.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
				}

				int alives = 0;
				for(int i = 0; i<Monsters.length; i++){
					Monster Monti = Monsters[i];
					if(Monti.isAlive()){
						gc.setFill(Color.BROWN);
						gc.fillRect(Monti.getPosX(), Monti.getPosY(), Monti.getWidth(), Monti.getLength());
						if(Monti.getLeben() != Monti.getInitHp()){
							double fragleben = (double)Monti.getLeben()/(double)Monti.getInitHp();
							if(fragleben > 0.75){
								gc.setFill(Color.LIME);
							}else if(fragleben > 0.50){
								gc.setFill(Color.ORANGE);
							}else{
								gc.setFill(Color.RED);
							}
							gc.fillRect(Monti.getPosX()+Monti.getWidth()/10, Monti.getPosY()+Monti.getLength()/3, Monti.getWidth()*fragleben, Monti.getLength()-Monti.getLength()*0.8);
						}
						alives++;
					}
				}
				if(alives == 0){
					clearcount++;
					for(int i = 0; i<Monsters.length; i++){
						Monster Monti = Monsters[i];
						Monti.setInitHp(Monster_HP+20*clearcount);
						Monti.setLeben(Monster_HP+20*clearcount);
					}
				}


				gc.setFont(new Font("Impact", 30));
				gc.setFill(Color.BLUEVIOLET);
				gc.fillText(""+Players.elementAt(0).getScore(), 50, 50);
				gc.setFill(Color.RED);
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i<Players.elementAt(0).getLeben(); i++)
					sb.append("+ ");
				gc.fillText(sb.toString(), 45, 75);

				if(Players.size() > 1){
					gc.setFont(new Font("Impact", 30));
					gc.setFill(Color.LIME);
					gc.fillText(""+Players.elementAt(1).getScore(), 50, 825);
					gc.setFill(Color.RED);
					StringBuffer sb2 = new StringBuffer();
					for(int i = 0; i<Players.elementAt(1).getLeben(); i++)
						sb2.append("+ ");
					gc.fillText(sb2.toString(), 45, 850);
				}


				for(int i = 0; i<Waffen.ActiveWeapons.size(); i++){
					for(int x = 0; x<Waffen.ActiveWeapons.elementAt(i).kugeln.size(); x++){
						Kugel draw = Waffen.ActiveWeapons.elementAt(i).kugeln.elementAt(x);
						gc.fillRect(draw.xPos, draw.yPos, draw.width, draw.height);
					}
				}


			}
		}));

		GameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.A){
					P1_inLeft = true;
				}else if(event.getCode() == KeyCode.D){
					P1_inRight = true;
				}if(event.getCode() == KeyCode.W){
					P1_inShoot = true;
				}

				if(event.getCode() == KeyCode.LEFT){
					P2_inLeft = true;
				}else if(event.getCode() == KeyCode.RIGHT){
					P2_inRight = true;
				}if(event.getCode() == KeyCode.UP){
					P2_inShoot = true;
				}

			}
		});

		GameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.A){
					P1_inLeft = false;
				}else if(event.getCode() == KeyCode.D){
					P1_inRight = false;
				}if(event.getCode() == KeyCode.W){
					P1_inShoot = false;
				}

				if(event.getCode() == KeyCode.LEFT){
					P2_inLeft = false;
				}else if(event.getCode() == KeyCode.RIGHT){
					P2_inRight = false;
				}if(event.getCode() == KeyCode.UP){
					P2_inShoot = false;
				}
			}
		});

	}

	public static void switchSceneToGame(){
		Spieler P1 = new Spieler(GAME_WIDTH/2-100, 10, null);
		P1.giveWeapon(new StandardWaffe(P1));

		int x = 0;
		int ix = 0;
		for(int i = 0; i<Monsters.length; i++){
			ix++;
			if(i%16 == 0){
				x++;
				ix = 0;
			}
			int sub = 0;
			if(x%2 == 0)
				sub = 50;

			Monsters[i] = new Monster(Monster_HP, sub+600+ix*50, x*100, 30, 20, 1);
		}
		Players.add(P1);
		if(Coop_enabled){
			Spieler P2 = new Spieler(GAME_WIDTH/2+100, 10, null);
			P2.giveWeapon(new StandardWaffe(P2));
			Players.add(P2);
		}
		tf.setCycleCount(Timeline.INDEFINITE);
		tf.play();
		MainStage.setScene(GameScene);
	}

	public static void Refresh(){
		Tick++;
		movetick ++ ;
		if(movetick == 500 && Monster_Direction == 0){
			Monster_Direction = 1;
			movetick = 0;
		}else if(movetick == 500 && Monster_Direction == 1){
			Monster_Direction = 0;
			movetick = 0;
		}

		if(Monster_Direction == 1){
			for(int i = 0; i<Monsters.length; i++){
				Monsters[i].moveRight();
			}
		}else if(Monster_Direction == 0){
			for(int i = 0; i<Monsters.length; i++){
				Monsters[i].moveLeft();
			}
		}

		if(P1_inLeft){
			Players.elementAt(0).moveLeft();
		}else if(P1_inRight){
			Players.elementAt(0).moveRight();
		}
		if(P1_inShoot){
			Players.elementAt(0).waffe.shoot(Players.elementAt(0).getX(), Players.elementAt(0).getY());
		}

		if(Coop_enabled){
			if(P2_inLeft){
				Players.elementAt(1).moveLeft();
			}else if(P2_inRight){
				Players.elementAt(1).moveRight();
			}
			if(P2_inShoot){
				Players.elementAt(1).waffe.shoot(Players.elementAt(1).getX(), Players.elementAt(1).getY());
			}
		}

		for(int x =0;x<Players.size(); x++){
			Players.elementAt(x).waffe.refresh();
		}

		for(int i = 0; i<Waffen.ActiveWeapons.size(); i++){
			for(int x = 0; x<Waffen.ActiveWeapons.elementAt(i).kugeln.size(); x++){
				Kugel r = Waffen.ActiveWeapons.elementAt(i).kugeln.elementAt(x);
				if(r.yPos <= 0 || r.yPos >= GAME_LENGTH || r.xPos <= 0 || r.xPos >= GAME_WIDTH)
					Waffen.ActiveWeapons.elementAt(i).kugeln.remove(r);
			}
			Waffen.ActiveWeapons.elementAt(i).refresh();
		}

	}

}
