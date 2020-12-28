package io.github.tecses1.UltimatePVP;

import java.util.Comparator;

import org.bukkit.entity.Player;

public class PlayerComparer implements Comparable<PlayerComparer> {
	
	public double distance = 0;
	public Player player = null;
	public PlayerComparer(Player p, double d){
		this.distance = d;
		this.player = p;
	}
	@Override
	public int compareTo(PlayerComparer p1) {
		Double d = p1.distance;
		
		return d.compareTo(distance);
	}
}
