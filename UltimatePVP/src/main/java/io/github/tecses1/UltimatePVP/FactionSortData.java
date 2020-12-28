package io.github.tecses1.UltimatePVP;

import java.util.Comparator;

import org.bukkit.Bukkit;

public class FactionSortData implements Comparable<FactionSortData> {      
	public RuntimeFactionData faction;
	public String Name = "";
	public double Total = 0;
	public FactionSortData(RuntimeFactionData f, double t) {
		this.faction = f;
		this.Total = t;
		
		this.Name = f.saveData.factionName;

	}
	@Override
	public int compareTo(FactionSortData fsd) {
		Double x = this.Total;
		Double y = fsd.Total;
		
		return y.compareTo(x);
	}

}
