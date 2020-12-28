package io.github.tecses1.UltimatePVP;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class RuntimeFactionData{
	
	
	public FactionData saveData;
	public Location capitolLocation;
	
	public ArmorStand nameHologram;
	
	public ArrayList<RuntimeTowerData> activeTowers = new ArrayList<RuntimeTowerData>();
	
	
	public ArrayList<Player> activePlayers = new ArrayList<Player>();;
	
	
	public RuntimeFactionData(FactionData f) {
		this.saveData = f;
		
	}
	
	public void Initialize() {

		capitolLocation = new Location(Bukkit.getWorld(saveData.capitolWorld), saveData.capitolPosX, saveData.capitolPosY, saveData.capitolPosZ);
		
	
		for (TowerData td : this.saveData.towers) {
			RuntimeTowerData t = new RuntimeTowerData(td, this);
			this.activeTowers.add(t);
		}
		try {
			this.nameHologram = Functs.SpawnHologram(saveData.factionName, capitolLocation);
		}catch(NullPointerException e){
			
		}
		for (String u : saveData.memberUUIDs) {
			Player p = Bukkit.getPlayer(UUID.fromString(u));
			
			if (p != null) {
				activePlayers.add(p);
			}
		}
	}
	
	public void resetTag() {
		if (this.nameHologram != null) {
			this.nameHologram.remove();
		}
		try {
			this.nameHologram = Functs.SpawnHologram(saveData.factionName, capitolLocation); 
		}catch(NullPointerException e) {
			
		}
		for (RuntimeTowerData rtd : this.activeTowers) {
			rtd.resetTag();
		}
	}
	public void closeFaction() {
		

		
		this.activePlayers.clear();
		this.activeTowers.clear();
		
		
	}
	public static void buildTower(Location loc) {
		World w = loc.getWorld();
		
		Material block = (Material.BEDROCK);
		//Set blocks.
		
		Location VertTop = loc.clone().add(0,0,0);
		Location VertBottom = loc.clone().subtract(0,0,0);
		VertBottom.setY(0);
		
		
		Functs.SetRegion(VertTop, VertBottom, block, w);

		Location startBlock = VertTop.clone().add(1,0,1);

		double DirectionX = -1;
		double CurrentY = 0;
		double DirectionZ = 0;
		
		int time = 0;
		for (CurrentY = VertTop.getY(); CurrentY > 0; CurrentY -= 1) {
			
			Block b = startBlock.getBlock();
			b.setType(block);
			

			
			startBlock.add(DirectionX, -1, DirectionZ);


			time ++;
			if (time == 2) {
				if (DirectionX == 1 && DirectionZ == 0) {
					DirectionZ = 1;
					DirectionX = 0;
				}else
				if (DirectionX == 0 && DirectionZ == 1) {
					DirectionZ = 0;
					DirectionX = -1;
				}else
				if (DirectionX == -1 && DirectionZ == 0) {
					DirectionZ = -1;
					DirectionX = 0;
				}else
				if (DirectionX == 0 && DirectionZ == -1) {
					DirectionZ = 0;
					DirectionX = 1;
				}
				time = 0;
				
			}
		}
		

		
		
	}
	public static void buildCaptiol(Location loc) {
		
		World w = loc.getWorld();
		
		Material block = (Material.BEDROCK);
		//Set blocks.
		
		Location VertTop = loc.clone().add(1,0,1);
		Location VertBottom = loc.clone().subtract(1,0,1);
		VertBottom.setY(0);
		
		
		Functs.SetRegion(VertTop, VertBottom, block, w);

		Location startBlock = VertTop.clone().add(1,0,1);
		Location startBlockOpp = VertTop.clone().subtract(3,0,3);

		double DirectionX = -1;
		double CurrentY = 0;
		double DirectionZ = 0;
		
		int time = 0;
		for (CurrentY = VertTop.getY(); CurrentY > 0; CurrentY -= 1) {
			
			Block b = startBlock.getBlock();
			b.setType(block);
			
			Block bOpp = startBlockOpp.getBlock();
			bOpp.setType(block);
			
			startBlock.add(DirectionX, -1, DirectionZ);
			startBlockOpp.subtract(DirectionX, 1, DirectionZ);


			time ++;
			if (time == 4) {
				if (DirectionX == 1 && DirectionZ == 0) {
					DirectionZ = 1;
					DirectionX = 0;
				}else
				if (DirectionX == 0 && DirectionZ == 1) {
					DirectionZ = 0;
					DirectionX = -1;
				}else
				if (DirectionX == -1 && DirectionZ == 0) {
					DirectionZ = -1;
					DirectionX = 0;
				}else
				if (DirectionX == 0 && DirectionZ == -1) {
					DirectionZ = 0;
					DirectionX = 1;
				}
				time = 0;
				
			}
		}
		
	
	}
	
	public Location getCapitolLocation() {
		return new Location(Bukkit.getWorld(this.saveData.capitolWorld),this.saveData.capitolPosX, this.saveData.capitolPosY, this.saveData.capitolPosZ);
	}
	public void destroyFaction(Main mainPlugin, String killer) {
		
		if (nameHologram != null) {
			nameHologram.remove();
		}
		
		for (RuntimeTowerData t : this.activeTowers) {
			t.destroyTower();
			

		}
		this.activeTowers.clear();
		
		Location topvert = capitolLocation.clone().add(2,0,2);
		Location bottomvert = capitolLocation.clone().subtract(2,0,2);
		
		bottomvert.setY(3);// = new Location(bottomvert.getWorld(), bottomvert.getX(), 0, bottomvert.getY());
		
		if (this.saveData.capitol) {
			List<Block> capitolBlocks = Functs.getBlocksSquare(topvert, bottomvert);
			
			for (Block thisBlock : capitolBlocks) {
				if (thisBlock.getType() == Material.BEDROCK) {
					thisBlock.setType(Material.STONE);
				}
			}
			
			for (int i = 0; i < 5; i++) {
				Functs.SpawnFirework(capitolLocation);
				capitolLocation.getWorld().createExplosion(capitolLocation.clone().subtract(0,1,0), 0.0F, false);
			}
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			MessagesHandler.SendMessage(p, MessagesHandler.factionHasFallen, new String[] {this.saveData.factionName, killer});
		}
		//for (String id : this.saveData.memberUUIDs) {
		//	mainPlugin.getPlayerByUUID(id).myParty = "";
		//}
		for (Player p : this.activePlayers) {
			MessagesHandler.SendMessage(p, MessagesHandler.yourFactionFell, null);
		}
		
		this.saveData.capitol = false;
		this.saveData.towers.clear();
		this.saveData.income = 0;
		//mainPlugin.deleteFaction(this);
		
		mainPlugin.saveUsers();
		mainPlugin.saveFactions();
		
	}
	
	public void Payout(Main mainPlugin, PlayerData player) {
		RuntimeFactionData killerFaction = mainPlugin.getFactionByName(player.myParty);
		Player p = Bukkit.getPlayer(UUID.fromString(player.myUUID));
		String killer = p.getDisplayName();
		
		double total = 0;
		
		for (String id : this.saveData.memberUUIDs) {
			OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(id));
			
			double opTake = mainPlugin.econ.getBalance(op) * 0.25;
			
			mainPlugin.econ.withdrawPlayer(op, opTake);
			total += opTake;
			
		}
		
		total += this.saveData.wallet;
		
		if (killerFaction != null) {
			killerFaction.saveData.wallet += total;
			for (Player kp : killerFaction.activePlayers) {
				MessagesHandler.SendMessage(kp,MessagesHandler.killedFactionWinnings, 
						new String[]{this.saveData.factionName, "" + total});
			}
			return;
		}
		
		mainPlugin.econ.depositPlayer(p, total);
		MessagesHandler.SendMessage(p,
				MessagesHandler.killedFactionWinningsP, new String[]{this.saveData.factionName, "" + total});
	    
		
	}

	public double GetPlayerWorth(Main mainPlugin) {
		double total = 0;
		
		for (String id : saveData.memberUUIDs) {
			try {
			OfflinePlayer p = Bukkit.getPlayer(UUID.fromString(id));
			total += mainPlugin.econ.getBalance(p);
			}catch(NullPointerException e) {
				
			}
			
		}
		
		return total;
		
	}

}