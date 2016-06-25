package me.game.monsters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;
import me.game.pack.Frame;

public class SwarmMonsterMinion extends Monster{
	
	private SwarmMonster host;
	
	public SwarmMonsterMinion(int level, double xPos, double yPos, SwarmMonster host){
		super(null, 11+level*5, xPos, yPos, 30, 20, 1+level, Color.BROWN, 0.25, 20);
		hisWeapon = new MonsterStandardWeapon(this);
		host.swarm.add(this);
		this.host = host;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	@Override
	public void refresh(){
		/*if (game.movetick == 500 && game.Monster_Direction == 0) {
			game.Monster_Direction = 1;
			game.movetick = 0;
		} else if (game.movetick == 500 && game.Monster_Direction == 1) {
			game.Monster_Direction = 0;
			game.movetick = 0;
		}

		if (game.Monster_Direction == 1)
			moveRight();
		else if (game.Monster_Direction == 0) 
			moveLeft();*/
		
		if(yPos <= host.getY()-10-heigth && xPos >= host.getX()-width-10 && xPos < host.getX()+host.getWidth()+10)
			moveRight();
		else if(xPos >= host.getX() && (yPos <= host.getY() || yPos < host.getY()+host.getHeight()+10))
			moveDown();
		else if(yPos > host.getY() && xPos > host.getX()-width-10 && xPos < host.getX()+host.getWidth()+width+10)
			moveLeft();
		else if(yPos > host.getY()-10-heigth)
			moveUp();
	}
	
	@Override
	public void draw(GraphicsContext gc){
		super.draw(gc);
		for(int x = 0; x<Frame.GAME_WIDTH; x+=4){
			for(int y = 0; y<Frame.GAME_LENGTH; y+=4){
				if(y <= host.getY()-10-heigth && x >= host.getX()-width-10 && x < host.getX()+host.getWidth()+10 && y < host.getY())
					gc.setFill(Color.DARKBLUE);
				else if(x >= host.getX() && (y <= host.getY() || y < host.getY()+host.getHeight()+10))
					gc.setFill(Color.BURLYWOOD);
				else if(y > host.getY() && x > host.getX()-width-10 && x < host.getX()+host.getWidth()+width+10)
					gc.setFill(Color.CORNSILK);
				else if(y > host.getY()-10-heigth && x < host.getX()-width-10 && x > host.getX()-width)
					gc.setFill(Color.DARKOLIVEGREEN);
				gc.fillRect(x, y, 4, 4);
				gc.setFill(Color.BLACK);
			}
		}
	}
}
