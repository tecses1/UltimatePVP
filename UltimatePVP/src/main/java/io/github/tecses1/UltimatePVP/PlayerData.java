package io.github.tecses1.UltimatePVP;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerData implements Serializable {

	public String myUUID = "";
	public String myParty = "";
	
	public String pendingToJoin;
		
	public String currentRegion = "No Mans Land";
	
	public HomeSet setHome;
	
	public PlayerData(String UUID) {

		this.myUUID = UUID;
		
	}
	
	public void Save(String path) {
		//Bukkit.getLogger().info("[ULTIMATEPVP] Saving player " + this.myUUID);
		
		YmlHandler.saveClass(this, path);
		
	}
	
	public static PlayerData Load(String path) {
		PlayerData returnObj = new PlayerData("");
		Bukkit.getLogger().info("[ULTIMATEPVP] Loading player " + path);
		return YmlHandler.loadClass(returnObj, path);
		
	}
}