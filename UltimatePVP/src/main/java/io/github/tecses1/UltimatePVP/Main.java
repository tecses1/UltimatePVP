package io.github.tecses1.UltimatePVP;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.ScoreboardManager;

import com.google.common.io.Files;

import net.milkbowl.vault.economy.Economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public final class Main extends JavaPlugin {
    
	public FileConfiguration config;
	public Economy econ = null;
	List<String> exemptWorlds;

	
	public ArrayList<RuntimeFactionData> factions = new ArrayList<RuntimeFactionData>();
	public ArrayList<PlayerData> players = new ArrayList<PlayerData>(); 

	public ArrayList<Player> disabledPlayers = new ArrayList<Player>();
	public ArrayList<Player> disabledPlayersByFaction = new ArrayList<Player>();

	public ArrayList<BlockData> blockData = new ArrayList<BlockData>();
	
	EventsHandler eventsHandler;
	
	private boolean first = true;
	
	static {
	    ConfigurationSerialization.registerClass(TowerData.class);
	}

	
	public void deleteFaction(RuntimeFactionData f) {
		 
		File dir = new File(this.getDataFolder() + "/factions/" + f.saveData.factionName + ".yml");
		dir.delete();
		
		factions.remove(this.getFactionByName(f.saveData.factionName));

	}
	
	public void addFaction(RuntimeFactionData f) {
		if (this.getFactionByName(f.saveData.factionName) == null) {
			factions.add(f);
			f.Initialize();

		}
		
	}
	

	public void addPlayer(PlayerData p) {
		players.add(p);
	}
	public void saveFactions() {

		for (RuntimeFactionData f : factions) {
			
			
			String savePath = this.getDataFolder() + "/factions/" + f.saveData.factionName + ".yml";
			f.saveData.Save(savePath);
		}
		
	}
	
	public void saveUsers() {
		for (PlayerData p : players) {
			String savePath = this.getDataFolder() + "/players/" + p.myUUID + ".yml";
			
			p.Save(savePath);
		}
	}

	public BlockData getBlockData(Block b) {
		for (BlockData bd : blockData) {
			if (bd.block.equals(b)) {
				return bd;
			}
		}
		return null;
	}
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public RuntimeFactionData getFactionByName(String name) {
    	for (int i = 0; i < factions.size(); i++) {
    		if (factions.get(i).saveData.factionName.equalsIgnoreCase(name)) {
    			return factions.get(i);
    		}
    	}
    	return null;
    }
    
    public PlayerData getPlayerByUUID(String UUID) {
    	for (int i = 0; i < players.size(); i++) {
    		PlayerData pd = players.get(i);
    		if (pd.myUUID.equals(UUID)) {
    			return pd;
    		}
    	}
    	return null;
    }
    @Override
    public void onEnable() {
    	getLogger().info("[UltimatePVP] Loading UltimatePVP configs...");
    	this.saveDefaultConfig();
    	
    	config = this.getConfig();
    	

        
    	if (!setupEconomy() ) {
            System.out.println(String.format("[%s] - Disabled due to no Economy found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
    	exemptWorlds = config.getStringList("exempt-worlds");
    	
		players = new ArrayList<PlayerData>();
		
		

		
		
	    //factions = new ArrayList<FactionData>();
		

		
		
		File saveDir = new File(this.getDataFolder() + "/players");
	    saveDir.mkdir();
	
		for (File file : saveDir.listFiles()) {
			PlayerData pd = PlayerData.Load(file.getPath());
			players.add(pd);
				
		}
		

		
		File saveDirFactions = new File(this.getDataFolder() + "/factions");
		saveDirFactions.mkdir();
		
		for (File file : saveDirFactions.listFiles()) {			
			FactionData factionSave = FactionData.Load(file.getPath());
			if (factionSave == null) {
				Bukkit.getLogger().info("[UltimatePVP] Failed to load faction.");
				continue;
			}
			factions.add(new RuntimeFactionData(factionSave));

		}

		
	    this.saveFactions();
	    this.saveUsers();
	    

	    
    	getLogger().info("[ULTIMATEPVP] Creating Event Handlers...");
    	eventsHandler = new EventsHandler(this);
    	Bukkit.getPluginManager().registerEvents(eventsHandler, this);
    	
    	this.getCommand("upvp").setExecutor(new CommandHandler(this));
    	this.getCommand("faction").setExecutor(new CommandHandler(this));
    	
    	int updateTime = config.getInt("update-rate");
    	if (updateTime < 20) {
    		updateTime = 20;
    	}
    	
    	getLogger().info("[UltimatePVP] Begining loop,  every " + updateTime + " ticks.");
    	

    	new BukkitRunnable() {
    		
    		@Override
    		public void run() {
    			MainThread();
    		}
    		
    	}.runTaskTimer(this, 0, updateTime);//.runTaskTimer(this, 0, config.getInt("update-time"));
    }
    @Override
    public void onDisable() {

    	this.saveUsers();
    	this.saveFactions();
    }
    
    public void MainThread() {
    	
    	if (first) {
    		
    	
    		
    		List<World> ws = new ArrayList<World>();
    		
    		for (RuntimeFactionData rfd : factions) {
    			if (rfd.saveData.capitol) {
					
    				Location loc = rfd.getCapitolLocation();
    				World w = loc.getWorld();
    				
    				
    				if (!ws.contains(w)) {
    					Bukkit.getLogger().info("[ULTIMATEPVP] Start: Adding world " + w.getName() + " to world check."); 
    					ws.add(w);
    				}
    				
					Bukkit.getLogger().info("[ULTIMATEPVP] Start: Loading chunks on faction " + rfd.saveData.factionName); 
					
					w.loadChunk(loc.getChunk());
					
					if (rfd.saveData.towers.size() > 0) {
						for (TowerData td : rfd.saveData.towers) {
							w.loadChunk(td.getLocation().getChunk());
						}
					}
					
					
    			}
    		}

    		for (World w : ws) {
    			
    			Bukkit.getLogger().info("[ULTIMATEPVP] Start: Clearing holograms from last play on world: " + w.getName()); 

    			List<Entity> entities = w.getEntities();
    			
    			for (Entity e : entities) {
    				if (e instanceof ArmorStand) {
    					ArmorStand as = (ArmorStand)e;
    					if (as.isVisible() == false) {
    						
    						
    						Bukkit.getLogger().info("[UltimatePVP] Removed hologram @ " + as.getLocation());
    						e.remove();
    					}
    				}
    			}
    		}
    		
    		for (RuntimeFactionData rfd : factions) {
    			rfd.Initialize();
    			Bukkit.getLogger().info("[UltimatePVP] Runtime data for " + rfd.saveData.factionName + " created.");
    		}
    		
    		for (PlayerData p : players) {
    			if (p.setHome != null) {
    				p.setHome.initialize();
    			}
    		}
    		first = false;

    		
    	}
    	//Do faction related updates...
    	//Nearby players...
    	for (Player p : Bukkit.getOnlinePlayers()) {
    		
    		boolean cont = false;
    		String pWorld = p.getWorld().getName();
    		for (String w : exemptWorlds) {
    			if (w.equalsIgnoreCase(pWorld)) {
    				cont = true;
    				break;
    			}
    		}
    		if (cont) {
    			disabledPlayers.remove(p);
    			continue;
    		}

    		PlayerData playerData = this.getPlayerByUUID(p.getUniqueId().toString());
    		if (Functs.hasNearbyFaction(playerData, config.getInt("faction-cover-distance"), factions)) {
    			if (!disabledPlayersByFaction.contains(p)) {
    				disabledPlayersByFaction.add(p);  
    				
    			}
    		}else {
    			if (disabledPlayersByFaction.contains(p)) {
    				disabledPlayersByFaction.remove(p);
    				playerData.currentRegion = "No Mans Land";
    				MessagesHandler.SendMessage(p, MessagesHandler.enteringNoMansLand, null);
    			}
    		}
    		
    		if (Functs.hasNearbyPlayer(p, config.getInt("player-cover-distance"), this)) {
    			if (!disabledPlayers.contains(p)) {
    				disabledPlayers.add(p);
    			}
    		}else {
    			if (disabledPlayers.contains(p)) {
    				disabledPlayers.remove(p);
    			}
    			
    		}
    	}
    	
    	//Faction income.
    	for (RuntimeFactionData rfd : factions) {
    		
    		FactionData f = rfd.saveData;
    		
			if (!f.capitol) {
				continue;
			}
			
			for (RuntimeTowerData towerData : rfd.activeTowers) {
				if (towerData.towerData.level <= 0) {
					continue;
				}
				
				Collection<Entity> entities = towerData.towerLocation.getWorld().getNearbyEntities(towerData.towerLocation, 60, 30, 60);

				List<Player> playersNearby = new ArrayList<Player>();
				List<Monster> mobsNearby = new ArrayList<Monster>();
				
				for (Entity e : entities) {
					if (e instanceof Player) {
						playersNearby.add((Player)e);
					}
					if (e instanceof Monster){
						mobsNearby.add((Monster)e);
					}
				}

				boolean playerShot = false;
				for (Player p : playersNearby) {
					PlayerData pd = this.getPlayerByUUID(p.getUniqueId().toString());
					
					if (!pd.myParty.equals(f.factionName)) {

						if (towerData.ShootAt(p)) {
							playerShot = true;
							if (towerData.shooting == false) {
								for (Player pp : rfd.activePlayers) {
									MessagesHandler.SendMessage(pp, MessagesHandler.towerEngaging, null);
								}
								
							}
							towerData.shooting = true;
						}
						




					}
				}
				if (playersNearby.size() == 0) {
					towerData.shooting = false;
					continue;
				}
				
				if (!playerShot) {
					for (Monster m : mobsNearby) {
						
						if (towerData.ShootAt(m)) {
							break;
						}
					}
				
				}
				
			}
			
			
    		f.wallet = Functs.round(f.wallet, 2);

    		f.incomeTick --;
    		if (f.incomeTick <= 0){
    			

    			int totalCalc = 0;
    			f.wallet += f.income;
    			for (Player p : rfd.activePlayers) {
    				PlayerData pd = this.getPlayerByUUID(p.getUniqueId().toString());
    				totalCalc += 1;
					if (pd.currentRegion.equals(f.factionName)) {
						totalCalc += 1 * f.towers.size();
					}
					MessagesHandler.SendMessage(p, MessagesHandler.factionPaid, new String[] {"" + f.income });
    				
    			}

    			Double percentCount = (totalCalc / 100.0);
    			f.income = Functs.round(f.wallet * percentCount, 2);
    			
    			f.incomeTick = config.getInt("income-rate");
    			
    			if (f.incomeTick < 60) {
    				f.incomeTick = 60;
    			}
    			
    		}

    		

    	}
    
    }

    


}