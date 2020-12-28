package io.github.tecses1.UltimatePVP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class FactionData implements Serializable {
	FileConfiguration myConfig;
	public String factionName = "";
	
	public ArrayList<String> memberUUIDs = new ArrayList<String>();
	
	public boolean capitol = false;
	
	public double wallet = 0;
	public ArrayList<TowerData> towers = new ArrayList<TowerData>();
	public double income = 0;
	
	public double capitolPosX = 0.0;
	public double capitolPosY = 0.0;
	public double capitolPosZ = 0.0;
	
	public String capitolWorld = "";
	int incomeTick = 60;

	public FactionData( String name) {
		
		factionName = name;

	}
	
	public void Save(String path) {
		Bukkit.getLogger().info("[ULTIMATEPVP] Saving faction " + this.factionName);
		
		YmlHandler.saveClass(this, path);
		
	}
	public static FactionData Load(String path) {
		
		Bukkit.getLogger().info("[ULTIMATEPVP] Loading faction " + path);
		
		FactionData newData = new FactionData("");
		
		return YmlHandler.loadClass(newData, path);
		
	}
	
	public void AddMember(PlayerData p) {
		this.memberUUIDs.add(p.myUUID);
		p.myParty = this.factionName;
	}
	public void RemoveMember(PlayerData p) {
		for(String name : this.memberUUIDs) {
			if (name.equals(p.myUUID)) {
				this.memberUUIDs.remove(name);
				break;
			}
		}
	}
}
