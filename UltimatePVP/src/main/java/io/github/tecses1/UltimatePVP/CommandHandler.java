package io.github.tecses1.UltimatePVP;




import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class CommandHandler implements CommandExecutor {
	final Main mainPlugin;
	public CommandHandler (Main plugin) {
		mainPlugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String cmdName = cmd.getName();
		
		Player caller = null;
		PlayerData playerData = null;
		
		RuntimeFactionData factionData = null;
		boolean op = true;
    	if (!(sender instanceof ConsoleCommandSender)) {
    		caller = (Player)sender;
    		String callerUUID = caller.getUniqueId().toString();
    		
    		if (!caller.hasPermission("ultimatePVP.main.admin")) {
    			op = false;
    		}
    		
    		playerData = mainPlugin.getPlayerByUUID(callerUUID);
    		
    		if (playerData == null) {
    			return false;
    		}
    		
    		if (!playerData.myParty.equalsIgnoreCase("")) {
    			factionData = mainPlugin.getFactionByName(playerData.myParty);    		
    		}
    	}
		
		
		if (cmdName.equalsIgnoreCase("upvp") && op) {

			switch (args.length) {
				case 0:
					break;
				case 1:
					if (args[0].equalsIgnoreCase("help")) {
						MessagesHandler.SendMessage(caller, MessagesHandler.helpMessageAdmin, null);
						
					}else if (args[0].equalsIgnoreCase("version")) {
						MessagesHandler.SendMessage(caller, MessagesHandler.versionMessage, null);
						
					}else if (args[0].equalsIgnoreCase("reload")) {
						MessagesHandler.SendMessage(caller, MessagesHandler.reloadMessage, null);
						mainPlugin.reloadConfig();
						
					}else if (args[0].equalsIgnoreCase("testcapitolbuild")) {
						if (caller != null) {
							RuntimeFactionData.buildCaptiol(caller.getLocation());
						}
					}else if (args[0].equalsIgnoreCase("testtowerbuild")) {
						if (caller != null) {
							RuntimeFactionData.buildTower(caller.getLocation());
						}
					}else if (args[0].equalsIgnoreCase("fixtags")) {
						List<World> ws = Bukkit.getWorlds();
			    		
			    		for (World w : ws) {
			    			if (mainPlugin.exemptWorlds.contains(w.getName())) {
			    				continue;
			    			}
			    			List<Entity> entities = w.getEntities();
			    			
			    			for (Entity e : entities) {
			    				if (e instanceof ArmorStand) {
			    					ArmorStand as = (ArmorStand)e;
			    					if (as.isVisible() == false) {
			    						Bukkit.getLogger().info("[UltimatePVP] Removed broken hologram @ " + as.getLocation());
			    						e.remove();
			    					}
			    				}
			    			}
			    		}
			    		
			    		for (RuntimeFactionData rfd : mainPlugin.factions) {
			    			rfd.resetTag();
			    		}
			    		
			    		for (PlayerData pd : mainPlugin.players) {
			    			if (pd.setHome != null) {
			    				pd.setHome.initialize();
			    			}
			    		}
					}
					
					break;
				case 2:
					if (args[0].equalsIgnoreCase("reset")) {
						Player p = Bukkit.getPlayer(args[1]);
						if (p != null) {
							PlayerData pd = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());
							RuntimeFactionData rfd = mainPlugin.getFactionByName(pd.myParty);
							
							if (rfd != null) {
								for (String u : rfd.saveData.memberUUIDs) {
									if (u.equals(pd.myUUID)) {
										rfd.saveData.memberUUIDs.remove(u);
										break;
									}
								}
								
								if (rfd.saveData.memberUUIDs.size() == 0) {
									rfd.destroyFaction(mainPlugin, "Server");
								}
							}
							pd.myParty = "";
						}
						
						
					}else if (args[0].equalsIgnoreCase("pillage")) {
						RuntimeFactionData rfd = mainPlugin.getFactionByName(args[1]);
						if (rfd != null) {
							rfd.destroyFaction(mainPlugin, "Server");
						}
					}
					break;
				case 3:

					break;
			} 
			if (playerData != null) {
				mainPlugin.saveUsers();
			}
			if (factionData != null) {
				mainPlugin.saveFactions();

			}
			
			
			return true;
		}else if (cmdName.equalsIgnoreCase("faction") || cmdName.equalsIgnoreCase("f")) {
			switch (args.length) {
				case 0:
					if (factionData != null) {
						String playersOnline = "";
						String playersOffline = "";
						for (String playerUUID : factionData.saveData.memberUUIDs) {
							OfflinePlayer p = Bukkit.getPlayer(UUID.fromString(playerUUID));
							if (p == null) {
								p = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
								playersOffline += "       &cOffline &f- " + p.getName() + "\n";
							}else {
								
								playersOnline +=  "        &2Online &f- " + ((Player)p).getDisplayName() + " [" + mainPlugin.getPlayerByUUID(playerUUID).currentRegion + "] " + "\n";
	
							}
						}
						
						String players = playersOnline + playersOffline;
						String capitolExists = "&4None - &cYou must place a capitol for benefits (/faction capitol!)";
						String factionWallet = "" + Functs.formatDouble(factionData.saveData.wallet);
						String factionIncome = "&4Disabled.";
						String factionBlocksClaimed = "&4Disabled.";
						if (factionData.saveData.capitol) {
							capitolExists = "&2Yes - &bBenefits listed below.";
							factionIncome = "" + Functs.formatDouble(factionData.saveData.income);
							factionBlocksClaimed = "" + factionData.saveData.towers.size();
						}
						


						MessagesHandler.SendMessage(caller, MessagesHandler.factionMenu, 
								new String[] {factionData.saveData.factionName,
										capitolExists, 
										factionWallet,
										factionIncome,
										factionBlocksClaimed,
										players});
					}else {
						
						MessagesHandler.SendMessage(caller, MessagesHandler.factionNotFound, null);
	
					}
					break;
				case 1:
					if (args[0].equalsIgnoreCase("leave")) {
						if (factionData != null) {
							factionData.saveData.RemoveMember(playerData);
							playerData.myParty = "";
							
							MessagesHandler.SendMessage(caller, MessagesHandler.leaveSucceded, null);
							
							if (factionData.saveData.memberUUIDs.size() == 0){
								factionData.destroyFaction(mainPlugin, "Server");
							}
							
							mainPlugin.saveFactions();
							
						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.leaveFailed, null);
							
						}
						break;
					}else if (args[0].equalsIgnoreCase("defend")) {
						
						if (factionData == null) {
							MessagesHandler.SendMessage(caller, MessagesHandler.factionNotFound, null);
							break;
						}
						
						if (!factionData.saveData.capitol) {
							MessagesHandler.SendMessage(caller, MessagesHandler.mustHaveCapitol, null);
							break;
						}
						
						if (factionData.saveData.towers.size() == 0) {
							MessagesHandler.SendMessage(caller, MessagesHandler.factionHasNoTowers, null);
							break;
						}
						
						boolean noTowerShooting = true;
						for (RuntimeTowerData tower : factionData.activeTowers) {
							if (tower.shooting) {
								if (playerData.currentRegion.equals(playerData.myParty)) {
									caller.teleport(tower.towerLocation);
								}else {
									if (mainPlugin.disabledPlayers.contains(caller)) {
										MessagesHandler.SendMessage(caller, MessagesHandler.towerTeleportFailedDisabled, null);

									}else {
										caller.teleport(tower.towerLocation);
									}

								}
								noTowerShooting = false;
								break;
							}
						}
						
						if (noTowerShooting) {
							MessagesHandler.SendMessage(caller, MessagesHandler.towerTeleportFailedNoTower, null);

						}
						break;
					}else if (args[0].equalsIgnoreCase("accept")) {
						if (playerData.pendingToJoin != null) {
							MessagesHandler.SendMessage(caller, MessagesHandler.acceptSucceeded, 
									new String[] {playerData.pendingToJoin});
							
							RuntimeFactionData fd = mainPlugin.getFactionByName(playerData.pendingToJoin);
							for (Player p : fd.activePlayers) {
								
								MessagesHandler.SendMessage(p, 
										MessagesHandler.playerJoinedFaction,
										new String[] {Bukkit.getPlayer(UUID.fromString(playerData.myUUID)).getDisplayName() });
							
							}
							fd.activePlayers.add(caller);
							fd.saveData.AddMember(playerData);
							
							playerData.pendingToJoin = null;
						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.acceptFailed, null);
						}
						break;
					}else if (args[0].equalsIgnoreCase("help")) {
						MessagesHandler.SendMessage(caller, MessagesHandler.helpMessageFaction, null);
						break;
					}else if (args[0].equalsIgnoreCase("tower")) {
						if (factionData.saveData.capitol) {
							
							
							if (factionData.saveData.wallet < 1000000) {
								MessagesHandler.SendMessage(caller, MessagesHandler.towerBlockedNotEnoughMoney, null);

								break;
							}
							
							World w = caller.getWorld();
							
							if (!playerData.currentRegion.equalsIgnoreCase(playerData.myParty)) {
								MessagesHandler.SendMessage(caller, MessagesHandler.towerBlockedNotInTerritory, null);
								break;
							}
							
							int distSquared = 120 * 120;
							if (caller.getLocation().distanceSquared(w.getSpawnLocation()) < distSquared){
								MessagesHandler.SendMessage(caller, MessagesHandler.towerBlockedBySpawn, null);
								break;
							}
							
							if (Functs.hasNearbyFactionNoUpdate(playerData, 240, mainPlugin.factions).size() > 0) {
								MessagesHandler.SendMessage(caller, MessagesHandler.towerBlockedByFaction, null);

								break;
							}
							

							
							Location blockAtPlayer = caller.getLocation().clone();
							
							
							
							Location VertTop = blockAtPlayer.clone().add(0,0,0);
							Location VertBottom = blockAtPlayer.clone().subtract(0,0,0);
							
							Location ScanTop = VertTop.clone().add(0,64,0);


							if (!Functs.ScanRegion(ScanTop, VertBottom, w) || blockAtPlayer.getY() < 52 || blockAtPlayer.getY() > 196) {
								MessagesHandler.SendMessage(caller, MessagesHandler.towerBlocked, null);
								break;
							}
							Location bedrockBottom = VertBottom.clone();
							bedrockBottom.setY(2);
							
							if (!Functs.ScanRegionForBanners(ScanTop, bedrockBottom, w)){
								MessagesHandler.SendMessage(caller, MessagesHandler.towerBlockedByBanner, null);
								break;
							}

							factionData.buildTower(blockAtPlayer);
							//Set blocks.
							Material flag = Material.RED_BANNER;
							
							Location flagLocation = blockAtPlayer.add(0,1,0);
						    w.getBlockAt(flagLocation).setType(flag);
						    
							TowerData tower = new TowerData(factionData, w.getBlockAt(flagLocation).getLocation());
							
							RuntimeTowerData newTower = new RuntimeTowerData(tower, factionData);
							
							factionData.activeTowers.add(newTower);
							factionData.saveData.towers.add(tower);
							
							factionData.saveData.wallet -= 1000000;

						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.towerBlockedNoCapitol, null);

						}
						break;
					}else if (args[0].equalsIgnoreCase("upgrade")) {
						Block b = caller.getTargetBlock(null, 15);
						if (b.getType() == Material.RED_BANNER) {
							for (RuntimeTowerData tower : factionData.activeTowers) {
								if (tower.towerLocation.equals(b.getLocation())) {
									if (tower.towerData.level < 2) {
										double cost = 0;
										
										if (tower.towerData.level == 0) {
											cost = 1000000;
										}else {
											cost = 5000000;
										}
										
										if (factionData.saveData.wallet >= cost) {
											factionData.saveData.wallet -= cost;
											tower.LevelUp();
										}else {
											MessagesHandler.SendMessage(caller, 
													MessagesHandler.upgradeDeniedMoney, null);
			
										}
										
										
			
									}else {
										MessagesHandler.SendMessage(caller, 
												MessagesHandler.upgradeDeniedMaxLevel, null);
									}
								}
							}
						}else {
							MessagesHandler.SendMessage(caller, 
									MessagesHandler.upgradeDeniedNotATower, null);
						}
						break;
					}else if (args[0].equalsIgnoreCase("capitol")) {
						if (factionData.saveData.capitol) {
							if (playerData.currentRegion.equals(factionData.saveData.factionName)) {
								MessagesHandler.SendMessage(caller, MessagesHandler.capitolAlreadySetTeleport, null);
							}else {
								MessagesHandler.SendMessage(caller, MessagesHandler.capitolAlreadySetTeleportFailed, null);
							}
						}else {
							World w = caller.getWorld();
							
							if (mainPlugin.exemptWorlds.contains(w.getName())) {
								MessagesHandler.SendMessage(caller, MessagesHandler.captiolBlockedByWorld, null);
								break;
							}
							
							int factionCover = mainPlugin.config.getInt("faction-cover-distance");
							
							int distanceSquared = (factionCover * factionCover) * 2;
							double pDistance = caller.getLocation().distanceSquared(w.getSpawnLocation());
							
							if (pDistance < distanceSquared){
								MessagesHandler.SendMessage(caller, MessagesHandler.capitolBlockedBySpawn, null);
								break;
							}
							
							if (Functs.hasNearbyFactionNoUpdate(playerData, factionCover * 2, mainPlugin.factions).size() > 0) {
								MessagesHandler.SendMessage(caller, MessagesHandler.captiolBlockedByFaction, null);

								break;
							}
							//Move player off the ground.
							
							//Get the block under the player.
							Location blockAtPlayer = caller.getLocation().clone();
							
							Location VertTop = blockAtPlayer.clone().add(2,0,2);
							Location VertBottom = blockAtPlayer.clone().subtract(2,0,2);
							
							Location ScanTop = VertTop.clone().add(0,64,0);


							if (!Functs.ScanRegion(ScanTop, VertBottom, w) || blockAtPlayer.getY() < 52 || blockAtPlayer.getY() > 196) {
								MessagesHandler.SendMessage(caller, MessagesHandler.captiolBlocked, null);
								break;
							}

							factionData.buildCaptiol(blockAtPlayer);
							
							Material flag = (Material.WHITE_BANNER);
							//Set blocks.
							
							Location flagLocation = blockAtPlayer.add(0,1,0);
						    w.getBlockAt(flagLocation).setType(flag);
							
							

						    
							caller.teleport(caller.getLocation().add(0,4,0));

							factionData.saveData.capitol = true;
							
							factionData.saveData.capitolPosX = w.getBlockAt(flagLocation).getX();
							factionData.saveData.capitolPosY = w.getBlockAt(flagLocation).getY();
							factionData.saveData.capitolPosZ = w.getBlockAt(flagLocation).getZ();
						    
							factionData.saveData.capitolWorld = flagLocation.getWorld().getName();
							// = Functs.getNearbyBlocksCircle(flagLocation, 80).size();
							
							
							factionData.Initialize();
							
							
							MessagesHandler.SendMessage(caller, MessagesHandler.CapitolSet, null);

						}
						
						break;
					}else if(args[0].equalsIgnoreCase("nearby")) {
						
						List<FDReturn> nearbyFactions = Functs.hasNearbyFactionNoUpdate(playerData, 100000, mainPlugin.factions);
						if (nearbyFactions.size() > 0) {
							String factionList = "";
							
							for (FDReturn f : nearbyFactions) {
								if (f.distance > 2500) {
									if (mainPlugin.getFactionByName(f.name).activePlayers.size() == 0){
										factionList += "    &a" + f.name + " &f- &6Distance and Directon hidden. &f\n";
										continue;
									}
								}
								factionList += "    &a" + f.name + " &f- &6" + f.distance + "&f blocks &6" + f.direction + "\n";
							}
							MessagesHandler.SendMessage(caller, MessagesHandler.yesNearbyFactions, new String[] {factionList});

							
						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.noNearbyFactions, null);

						}
						break;
					}else if(args[0].equalsIgnoreCase("home")) {
						
						if (!playerData.myParty.equals(playerData.currentRegion)) {
							if (mainPlugin.disabledPlayers.contains(caller)) {
								MessagesHandler.SendMessage(caller, MessagesHandler.cannotFactionHomePlayersNearby, null);
								break;
							}
						}
						if (factionData.saveData.capitol) {
							caller.teleport(factionData.capitolLocation);
						}
						break;
					}else if (args[0].equalsIgnoreCase("top")) {
						
						
						List<FactionSortData> factionSortDatas = new ArrayList<FactionSortData>();
						
						
						for (RuntimeFactionData rfd : mainPlugin.factions) {
							double total = 0;
							
							total += rfd.saveData.wallet;
							
							for (String u : rfd.saveData.memberUUIDs) {
								OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(u));
								
								if (p != null) {
									
									total += mainPlugin.econ.getBalance(p);
								}
							}
							FactionSortData fsd = new FactionSortData(rfd, total);
							factionSortDatas.add(fsd);
						}
						Collections.sort(factionSortDatas);
						String factionList = "";
						
						
						 
						int max = factionSortDatas.size();
						if (max > 9) {
							max = 10;
						}
						for (int i = 0; i < max; i++) {
							FactionSortData thisData = factionSortDatas.get(i);
							factionList += "&a" + (i+1) + "." + "    &b" + thisData.Name + " - In Wallet: &6"
							+ Functs.formatDouble(thisData.faction.saveData.wallet)
							+ "&a Net worth: &6" + Functs.formatDouble(thisData.Total) + "\n";
						}
						
						MessagesHandler.SendMessage(caller, MessagesHandler.factionTop, new String[] {factionList});
						break;
					}else if (args[0].equalsIgnoreCase("next")) {
						if (factionData != null) {
							if (factionData.saveData.capitol) {
								double tickTime = mainPlugin.config.getInt("update-rate") / 20.0; //Subtract 1 every this amount of seconds.
								double secondsLeft = Functs.round(tickTime * factionData.saveData.incomeTick,2);
								
								int minutesLeft = 0;
								if (secondsLeft > 60){
									minutesLeft = (int)(secondsLeft/60);
									secondsLeft = secondsLeft % 60;
								}
								
								MessagesHandler.SendMessage(caller, MessagesHandler.timeLeftTillNextIncome, new String[] {minutesLeft + "", secondsLeft + ""});
							}else {
								MessagesHandler.SendMessage(caller, MessagesHandler.mustHaveCapitol, null);
							}
						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.mustHaveFaction, null);
						}
						break;

					}
						
					
				case 2: 
					if (args[0].equalsIgnoreCase("create")) {
						if (args[1] == null) {
							MessagesHandler.SendMessage(caller, MessagesHandler.factionCreateFailedNameInvalid, null);
							break;
						}
						String createName = args[1];
						if (!playerData.myParty.equalsIgnoreCase("")) {
							MessagesHandler.SendMessage(caller, MessagesHandler.factionCreateFailedPlayerInFaction, new String[] {"&6" + createName });
							break;
						}
						for (int i = 0; i < mainPlugin.factions.size(); i++) {
							if (args[1].equalsIgnoreCase(mainPlugin.factions.get(i).saveData.factionName)) {
								MessagesHandler.SendMessage(caller, MessagesHandler.factionCreateFailedFactionExists, new String[] {"&6" + createName});
							}
						}
						
						FactionData fd = new FactionData(createName);
						fd.AddMember(playerData);
						
						factionData = new RuntimeFactionData(fd);
						
						
						playerData.myParty = createName;
						
						mainPlugin.addFaction(factionData);
						
						MessagesHandler.SendMessage(caller, MessagesHandler.factionCreateSuccesful, new String[] {"&6" + createName });
						break;
					}else if (args[0].equalsIgnoreCase("invite")) {
						String invitePlayer = args[1];
						
						@SuppressWarnings("deprecation")
						Player p = Bukkit.getPlayer(invitePlayer);
						if (p == null) {
							MessagesHandler.SendMessage(caller, MessagesHandler.inviteFailedPlayerNotFound, new String[] {"&6" + invitePlayer });
							break;
						}
						if (p != Bukkit.getPlayer(UUID.fromString(playerData.myUUID))) {
							if (!mainPlugin.getPlayerByUUID(p.getUniqueId().toString()).myParty.equalsIgnoreCase("")) {
								MessagesHandler.SendMessage(caller, MessagesHandler.inviteFailedPlayerInFaction, null);
								break;
							}else {
								MessagesHandler.SendMessage(p, MessagesHandler.inviteToParty, 
										new String[] {Bukkit.getPlayer(UUID.fromString(playerData.myUUID)).getDisplayName(), factionData.saveData.factionName });
								PlayerData ip = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());
								if (ip == null) {
									ip = new PlayerData(p.getUniqueId().toString());
									mainPlugin.saveUsers();
								}
								
								ip.pendingToJoin = factionData.saveData.factionName;
								
								
								MessagesHandler.SendMessage(caller, MessagesHandler.inviteSuccessful, new String[] {"&6" + invitePlayer });
							}
						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.inviteFailedSelf, null);
						}
						break;
					}else if (args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("d")) {
						try {
							if (factionData == null) {
								break;
							}
							double d = Double.parseDouble(args[1]);
							
							if (mainPlugin.econ.getBalance(caller) >= d) {
								mainPlugin.econ.withdrawPlayer(caller, d);
								factionData.saveData.wallet += d;
								MessagesHandler.SendMessage(caller, MessagesHandler.depositSuccess, new String[] {"" + d});
								for (Player p : factionData.activePlayers) {
									if (p != caller) {
										MessagesHandler.SendMessage(p, MessagesHandler.depositSuccessOthers,
												new String[] {caller.getDisplayName(), "" + d});
									}
								}
							}else {
								MessagesHandler.SendMessage(caller, MessagesHandler.notEnoughMoney, null);

							}
						}catch(NumberFormatException e){
							MessagesHandler.SendMessage(caller, MessagesHandler.notANumber, null);

						
						}
						break;
					}
					else if (args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("w")) {
						try {
							double d = Double.parseDouble(args[1]);
							
							if (factionData.saveData.wallet >= d) {
								factionData.saveData.wallet -= d;
								mainPlugin.econ.depositPlayer(caller, d);
								MessagesHandler.SendMessage(caller, MessagesHandler.withdrawalSuccess, new String[] {"" + d});
								for (Player p : factionData.activePlayers) {
									if (p != caller) {
										MessagesHandler.SendMessage(p, MessagesHandler.withdrawalSuccessOthers,
												new String[] {caller.getDisplayName(), "" + d});
									}
								}
							}else {
								MessagesHandler.SendMessage(caller, MessagesHandler.notEnoughMoney, null);

							}
						}catch(NumberFormatException e){
							MessagesHandler.SendMessage(caller, MessagesHandler.notANumber, null);

						
						}
						break;
					}
				default:
					MessagesHandler.SendMessage(caller, MessagesHandler.factionHelpMessage, null);
					break;
					
			}
			if (playerData != null) {
				mainPlugin.saveUsers();
			}
			if (factionData != null) {
				mainPlugin.saveFactions();

			}
			
			return true;
		}
		

		return false;
	}
	
	
	
}