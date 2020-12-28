package io.github.tecses1.UltimatePVP;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.util.Vector;

public class RuntimeTowerData {
	
	public RuntimeFactionData parentFaction = null;
	public Location towerLocation;
	public ArmorStand nameHologram;
	
	public TowerData towerData;

	public boolean shooting = false;
	public RuntimeTowerData(TowerData t, RuntimeFactionData rfd) {
		this.parentFaction = rfd;
		this.towerData = t;
		this.towerLocation = t.getLocation();
		this.nameHologram = Functs.SpawnHologram(t.factionName + "_TOWER LVL " + t.level, this.towerLocation);
	}
	

	public void closeTower() {
		if (this.nameHologram != null) {
			this.nameHologram.remove();
		}
	}
	public void resetTag() {
		if (this.nameHologram != null) {
			this.nameHologram.remove();
		}
		try {
			this.nameHologram = Functs.SpawnHologram( this.towerData.factionName + "_TOWER LVL " + this.towerData.level, this.towerLocation);
		}catch(NullPointerException e) {
			
		}
	}
	
	
	public void destroyTower() {
		for (int i = 0; i < 5; i++) {
			Functs.SpawnFirework(towerLocation);
			towerLocation.getWorld().createExplosion(towerLocation.clone().subtract(0,1,0), 0.0F, false);
		}
		
		for (Player p : parentFaction.activePlayers) {
			MessagesHandler.SendMessage(p, MessagesHandler.aTowerFell, null);
		}
		
		Location topvert = towerLocation.clone().add(2,0,2);
		Location bottomvert = towerLocation.clone().subtract(2,0,2);
		
		bottomvert.setY(3);// = new Location(bottomvert.getWorld(), bottomvert.getX(), 0, bottomvert.getY());

		List<Block> capitolBlocks = Functs.getBlocksSquare(topvert, bottomvert);
		
		for (Block thisBlock : capitolBlocks) {
			if (thisBlock.getType() == Material.BEDROCK) {
				thisBlock.setType(Material.STONE);
			}
		}
		
		parentFaction.saveData.towers.remove(towerData);
		
		this.nameHologram.remove();
		

		



	}
	
	public void LevelUp() {
		this.closeTower();
		this.towerData.level ++;
		
		this.nameHologram = Functs.SpawnHologram(towerData.factionName + "_TOWER LVL " + towerData.level, this.towerLocation);
	}
	
	public boolean ShootAt(Player p) {
		Location pLoc = p.getEyeLocation();
		if (Functs.LOS(this.towerLocation, pLoc)) {
			shoot(p.getEyeLocation());
			return true;
		}
		return false;
		
	}
	public boolean ShootAt(Monster m) {
		Location mLoc = m.getEyeLocation();
		if (Functs.LOS(this.towerLocation, mLoc)) {
			shoot(m.getEyeLocation());
			return true;
		}
		return false;
	}
	public void shoot(Location loc) {
	  


		if (this.towerData.level == 1) {
			Arrow arrow = (Arrow)loc.getWorld().spawn(this.towerLocation.clone().add(0,2,0), Arrow.class);
			Vector blockVector1 = arrow.getLocation().toVector();
			Vector blockVector2 = loc.toVector();
			
			Vector directionVector = (blockVector2.subtract(blockVector1)).normalize();
			
			
			
			directionVector = directionVector.multiply(2.5);
			
			arrow.setCustomName("towershot");
			arrow.setVelocity(directionVector);
		}
		
		if (this.towerData.level == 2) {

			
			Fireball arrow = (Fireball)loc.getWorld().spawn(this.towerLocation.clone().add(0,1,0), Fireball.class);
			

			Vector blockVector1 = arrow.getLocation().toVector();
			Vector blockVector2 = loc.toVector();
			

			Vector directionVector = (blockVector2.subtract(blockVector1)).normalize();
			
			arrow.setCustomName("towershot");
			
			arrow.setDirection(directionVector);
			
			directionVector = directionVector.multiply(2.5);
			arrow.setVelocity(directionVector);
			
			
		}
		


		
		
		
		
	}
}
