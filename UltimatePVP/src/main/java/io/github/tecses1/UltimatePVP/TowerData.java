package io.github.tecses1.UltimatePVP;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class TowerData implements ConfigurationSerializable {
	public String factionName = "";
	public String worldName = "";
	public double X;
	public double Y;
	public double Z;
	
	public int level = 0;
	
	public TowerData() {
		
	}
	public TowerData(RuntimeFactionData rfd, Location loc) {
		
		
		this.factionName = rfd.saveData.factionName;
		this.worldName = loc.getWorld().getName();
		
		this.X = loc.getX();
		this.Y = loc.getY();
		this.Z = loc.getZ();
	}
	
	public Location getLocation() {
		return new Location(Bukkit.getWorld(worldName), this.X, this.Y, this.Z);
	}
	
	@Override
    public Map<String, Object> serialize() {
		return YmlHandler.getSerializationMap(this);
    }
	
	public static TowerData deserialize(Map<String, Object> map) {
		
		TowerData returnObj = new TowerData();

		return YmlHandler.getObjectFromMap(returnObj, map);
	}
	
}