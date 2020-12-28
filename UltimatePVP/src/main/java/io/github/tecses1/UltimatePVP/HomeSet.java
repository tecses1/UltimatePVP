package io.github.tecses1.UltimatePVP;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ArmorStand;

public class HomeSet implements ConfigurationSerializable {
	
	private ArmorStand nameHologram;
	private Location runtimeLocation;
	
	public String owningPlayer = "";
	
	public String regionSet = "";
	public String world = "";
	public double X;
	public double Y;
	public double Z;
	
	public HomeSet() {
		
	}
	
	public Location getLocation() {
		return this.runtimeLocation;
	}
	
	public boolean isValid() {
		return this.runtimeLocation.getBlock().getType() == Material.BLACK_BANNER;
	}
	public void setLocation(Location x) {
		this.world = x.getWorld().getName();
		this.X = x.getX();
		this.Y = x.getY();
		this.Z = x.getZ();
	}
	public void initialize() {
		
		
		if (this.nameHologram != null) {
			this.nameHologram.remove();
		}
		this.runtimeLocation = new Location(Bukkit.getWorld(this.world), this.X, this.Y, this.Z);
		
		if (this.runtimeLocation.getBlock().getType() == Material.BLACK_BANNER) {
			this.nameHologram = Functs.SpawnHologram(owningPlayer + "'s Home", this.runtimeLocation);
		}
		
		
	}
	
	public void close() {
		this.nameHologram.remove();
	}

	@Override
	public Map<String, Object> serialize() {
		
		return YmlHandler.getSerializationMap(this);
	}
	
	public static HomeSet deserialize(Map<String, Object> map) {
		HomeSet home = new HomeSet();
		
		return YmlHandler.getObjectFromMap(home, map);
		
	}
	
	
}
