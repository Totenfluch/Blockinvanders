package me.game.bullets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.playerWeapons.PlayerWeapon;
import me.game.playerWeapons.Weapon;

public class ClusterBullet extends TimedBullet{
	private int count = 0;
	boolean triggered = false;
	int clustersSpawned = 0;
	final int maxClusters = 2;
	int clusterTime = 0;
	final int clusterDelay = 35;
	
	public ClusterBullet(Weapon waffe, double xPos, double yPos, double height, double width, double speed, int damage,
			double angle, int lifetimeticks) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle, lifetimeticks);
	}

	@Override
	public void Trigger() {
		triggered = true;
		for(int angle = -180; angle< 180; angle+=10)
			waffe.getKugeln().add(new PlayerBullet((PlayerWeapon) this.waffe, this.xPos, yPos, 7.0, 7.0, waffe.getBulletSpeed()+4, damage, angle ));
	}
	
	public void spawnBulletCluster(){
		for(int angle = -180-count*90; angle< 180+count*90; angle+=10+count*2.5)
			waffe.getKugeln().add(new PlayerBullet((PlayerWeapon) this.waffe, this.xPos, yPos, 7.0, 7.0, waffe.getBulletSpeed()+4, damage, angle ));
	}
	
	@Override
	public void refresh(){
		if(triggered){
			if(clustersSpawned >= maxClusters)
				waffe.getKugeln().remove(this);
			if(clusterTime >= clusterDelay){
				clusterTime = 0;
				spawnBulletCluster();
				clustersSpawned++;
			}else{
				clusterTime++;
			}
		}else{
			super.refresh();
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		if(!triggered){
			gc.setFill(Color.AQUA);
			gc.fillRect(xPos, yPos, width, height);
		}
	}

}
