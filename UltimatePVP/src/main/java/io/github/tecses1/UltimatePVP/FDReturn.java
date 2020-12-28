package io.github.tecses1.UltimatePVP;

import org.bukkit.Bukkit;

public class FDReturn implements Comparable<FDReturn> {
	public String name = "Null";
	public double distance = 0;
	public String direction = "North";
	public FDReturn(String name, double dist, String dir) {
		this.name = name;
		this.distance = Functs.round(dist, 2);
		this.direction = dir;

	}
	
	public int compareTo(FDReturn fd) {
		Double d = fd.distance;

		return d.compareTo(distance);
		
		
	}

}