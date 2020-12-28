package io.github.tecses1.UltimatePVP;

import java.util.List;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.ItemList;

public class EventsHandler implements Listener{
	final Main mainPlugin;
	public EventsHandler (Main plugin) {
		mainPlugin = plugin;
	}
	@EventHandler
	public void onCommandPreprocessEvent(PlayerCommandPreprocessEvent  e) {
		if (mainPlugin.config.getBoolean("enable-homes")) {
			
			
			if (e.getMessage().startsWith("/home") || e.getMessage().startsWith("/sethome")) {
				Player caller = e.getPlayer();
				PlayerData playerData = mainPlugin.getPlayerByUUID(caller.getUniqueId().toString());
				
				String worldName = caller.getLocation().getWorld().getName();

				
				if (e.getMessage().startsWith("/home")) {
					if (playerData.setHome != null) {
						boolean canTeleport = true;
						if (mainPlugin.disabledPlayers.contains(caller)) {
							canTeleport = (!playerData.setHome.regionSet.equalsIgnoreCase(playerData.currentRegion)) && playerData.currentRegion.equalsIgnoreCase(playerData.myParty);
						}
						
						if (canTeleport) {
							if (playerData.setHome.isValid()) {
								caller.teleport(playerData.setHome.getLocation());
							}else {
								MessagesHandler.SendMessage(caller, MessagesHandler.homeInvalid, null);
							}

						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.homeDisabledByPlayer, null);
						}
	
						
					}
				}else {
					boolean allowPlace = (!mainPlugin.disabledPlayersByFaction.contains(caller)) || (playerData.currentRegion.equals(playerData.myParty));
					if (allowPlace) {
						if (playerData.setHome != null) {
							playerData.setHome.getLocation().getBlock().setType(Material.AIR);
							playerData.setHome.close();
							
							
						}
						Location loc = caller.getLocation().getBlock().getLocation();
						Material blockType = loc.getBlock().getType();
						boolean allowedByBlock = blockType == Material.AIR || blockType == Material.SNOW || blockType == Material.GRASS;
						boolean disabledByBlock = blockType == Material.BLACK_BANNER || blockType == Material.RED_BANNER || blockType == Material.WHITE_BANNER;
						
						if (allowedByBlock || !disabledByBlock) {
							loc.getBlock().setType(Material.BLACK_BANNER);
							
							
							HomeSet home = new HomeSet();
							
							home.owningPlayer = caller.getDisplayName();
							home.regionSet = playerData.currentRegion;
							
							home.setLocation(loc);
							
							home.initialize();
							
							playerData.setHome = home;
							
							MessagesHandler.SendMessage(caller, MessagesHandler.homeSet, null);
						}else {
							MessagesHandler.SendMessage(caller, MessagesHandler.homeSetDisabledBannerPlace, null);
						}
					}else {
						MessagesHandler.SendMessage(caller, MessagesHandler.homeSetDisabled, null);

					}
				}
				e.setCancelled(true);

			}
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());
		
		if (pd.myParty.equals(pd.currentRegion) || pd.currentRegion.equals("No Mans Land")) {
			return;
		}
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = p.getItemInHand();
			if (mainPlugin.config.getBoolean("disable-bucket-placing")) {
				if (item.getType().equals(Material.LAVA_BUCKET) || item.getType().equals(Material.WATER_BUCKET)) {
					if (mainPlugin.disabledPlayersByFaction.contains(p)) {
						MessagesHandler.SendMessage(p, MessagesHandler.cannotModifyFactionTerrain, null);
						e.setCancelled(true);
						return;
					}
	
				}
			}
			
		}

	}
	@EventHandler
	public void manipulate(PlayerArmorStandManipulateEvent e)
	{
	        if(!e.getRightClicked().isVisible()){
	            e.setCancelled(true);
	        }
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());
		
		if (!pd.myParty.equalsIgnoreCase("")) {
			RuntimeFactionData rfd = mainPlugin.getFactionByName(pd.myParty);
			rfd.activePlayers.remove(p);
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String playerUUID = p.getUniqueId().toString();
		PlayerData pd = mainPlugin.getPlayerByUUID(playerUUID);
		
		if (pd == null) {
			pd = new PlayerData(playerUUID);
			mainPlugin.addPlayer(pd);
			mainPlugin.saveUsers();
		}else {
			if (!pd.myParty.equalsIgnoreCase("")) {
				RuntimeFactionData rfd = mainPlugin.getFactionByName(pd.myParty);
				if (!rfd.activePlayers.contains(p)) {
					rfd.activePlayers.add(p);
				}

			}
		}
		
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.getEntityType().equals(EntityType.FIREBALL)) {
			event.getLocation().getWorld().createExplosion(event.getLocation(), 0.0F, false);
			event.setCancelled(true);
			
			
		}
		
		if (event.getEntityType() != EntityType.PRIMED_TNT) {
			return;
		}
		
		List<Block> blocks = event.blockList();
		
		List<Block> blocksOther = Functs.getNearbyBlocksSphere(event.getLocation(), 2);
		for (Block b : blocksOther) {
			if (!blocks.contains(b)) {
				blocks.add(b);
			}
		}
		
		String placedName = event.getEntity().getCustomName();
		for (Block b : blocks) {
			if (b.getType() == Material.AIR) {
				continue;
			}
			
			if (b.getType().equals(Material.BEDROCK)) {
				continue;
			}
			PlayerData pd = mainPlugin.getPlayerByUUID(placedName);
			Player p = Bukkit.getPlayer(UUID.fromString(placedName));
			RuntimeFactionData fd = mainPlugin.getFactionByName(pd.currentRegion);
			if (b.getType() == Material.WHITE_BANNER) {
				

				
				if (fd != null) {
					if (fd.capitolLocation.equals(b.getLocation())) {
						fd.Payout(mainPlugin, pd);
						fd.destroyFaction(mainPlugin, p.getDisplayName());
					}
				}
					
			}else if (b.getType() == Material.RED_BANNER) {
				if (fd != null) {
					for (RuntimeTowerData rtd : fd.activeTowers) {
						if (rtd.towerLocation.equals(b.getLocation())) {
							rtd.destroyTower();
							
							fd.activeTowers.remove(rtd);
							
							break;
						}
					}
				}
			
			}else {
				
				String materialName = b.getType().toString();
				int blockHealth = mainPlugin.config.getInt("block-health." + materialName);
				
				if (blockHealth > 0) {
					BlockData bd = mainPlugin.getBlockData(b);
					
					if (bd == null) {
						mainPlugin.blockData.add(new BlockData(b, blockHealth-1));
					}else {
						bd.hp -= 1;
						if (bd.hp <= 0) {
							b.breakNaturally();
							mainPlugin.blockData.remove(bd);
							
						}
					}
				}else {
					b.breakNaturally();
				}
				
			}
		}
		event.getLocation().getWorld().createExplosion(event.getLocation(), 0.0F, false);
		event.setCancelled(true);
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		
		
		if (mainPlugin.disabledPlayersByFaction.contains(p)) {
			
			PlayerData pd = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());
			
			
			if (pd.currentRegion.equalsIgnoreCase(pd.myParty)) {
				if (mainPlugin.disabledPlayers.contains(p)) {
					MessagesHandler.SendMessage(p, MessagesHandler.cannotModifyFactionTerrainEnemies, null);
					event.setCancelled(true);
				}
				return;

			}
			Block b = event.getBlock();
			
			if (b.getType() == Material.TNT) {
				p.getInventory().getItemInHand().setAmount(p.getInventory().getItemInHand().getAmount() - 1);
				
				TNTPrimed tnt = event.getBlock().getWorld().spawn(b.getLocation(), TNTPrimed.class);
				tnt.setCustomName(p.getUniqueId().toString());
			}
			
			MessagesHandler.SendMessage(p, MessagesHandler.cannotModifyFactionTerrain, null);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		PlayerData pd = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());

		if (mainPlugin.disabledPlayersByFaction.contains(p)) {
			
			Block b = event.getBlock();
			RuntimeFactionData fd = mainPlugin.getFactionByName(pd.currentRegion);

			if (b.getType().equals(Material.WHITE_BANNER)) {
					if (fd.capitolLocation.distance(b.getLocation()) < 1) {
						
						fd.Payout(mainPlugin, pd);
						fd.destroyFaction(mainPlugin, p.getDisplayName());

						return;
					}
				
				
			}else if (b.getType().equals(Material.RED_BANNER)) {
				for (RuntimeTowerData rtd : fd.activeTowers) {
					if (rtd.towerLocation.distance(b.getLocation()) < 1) {
						rtd.destroyTower();
						
						fd.activeTowers.remove(rtd);
						
						return;
					}
					
				}
			}else if (b.getType().equals(Material.BLACK_BANNER)) {
				List<Entity> entities = b.getLocation().getWorld().getEntities();
				
				for (Entity e : entities) {
					if (!(e instanceof ArmorStand)) {
						continue;
					}
					if (e.getLocation().distance(b.getLocation()) < 1){
						e.remove();
					}
				}
				
			}
			
			
			
			
			if (pd.currentRegion.equalsIgnoreCase(pd.myParty)) {
				if (mainPlugin.disabledPlayers.contains(p)) {
					MessagesHandler.SendMessage(p, MessagesHandler.cannotModifyFactionTerrainEnemies, null);
					event.setCancelled(true);
				}
				return;
			}
			
			MessagesHandler.SendMessage(p, MessagesHandler.cannotModifyFactionTerrain, null);
			event.setCancelled(true);
		}
	}
	@EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (mainPlugin.disabledPlayers.contains(player)) {
	        if (event.getCause().equals(TeleportCause.COMMAND)) {
	        	event.setCancelled(true);
	        }
        }
    }

    @EventHandler
    public void onEntityDamage(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause().equals(TeleportCause.ENDER_PEARL) && mainPlugin.config.getBoolean("disable-enderpearl-damage")) {
            event.setCancelled(true);
            player.teleport(event.getTo());
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
    	
    	boolean isPlayerAttackPlayer = (e.getEntity() instanceof Player) && (e.getDamager() instanceof Player);
    	if (isPlayerAttackPlayer) {
    
	    	Player playerAttacked= (Player)e.getEntity();
	    	Player playerAttacking = (Player)e.getDamager();
	    	
	    	PlayerData attackedPD = mainPlugin.getPlayerByUUID(playerAttacked.getUniqueId().toString());
	    	PlayerData attackingPD = mainPlugin.getPlayerByUUID(playerAttacking.getUniqueId().toString());
	        if (attackedPD.myParty.equals(attackingPD.myParty)) {
	        	e.setDamage(e.getDamage() / 2);
	        }
	        return;
    	}
    	
    	boolean isArrowHitPlayer = (e.getEntity() instanceof Player) && (e.getDamager() instanceof Arrow);
    	if (isArrowHitPlayer) {
            Arrow a = (Arrow)e.getDamager();
            Player p = (Player)e.getEntity();
            if (a.getCustomName() == null) {
            	return;
            }
            if (a.getCustomName().equals("towershot")) {
    			if (p.isBlocking()) {
    				p.setVelocity(p.getVelocity().add(p.getLocation().toVector().subtract(a.getLocation().toVector()).normalize().multiply(2)));
    			}
    			
            	e.setDamage(e.getDamage() * 2);
            	p.addPotionEffect(PotionEffectType.SLOW.createEffect(40,1));
            	p.setSprinting(false);
            }
            
    	}
    	
    	boolean isArrowHitMonster = (e.getEntity() instanceof Monster) && (e.getDamager() instanceof Arrow);
    	if (isArrowHitMonster) {
            Arrow a = (Arrow)e.getDamager();
            Monster p = (Monster)e.getEntity();
            if (a.getCustomName() == null) {
            	return;
            }
            if (a.getCustomName().equals("towershot")) {
            	e.setDamage(e.getDamage() * 2);
            	p.addPotionEffect(PotionEffectType.SLOW.createEffect(40,1));
            }
            
    	}
    	
    	boolean isFireballExplodePlayer = (e.getEntity() instanceof Player) && (e.getDamager() instanceof Fireball);
    	
    	if (isFireballExplodePlayer) {
    		Fireball b = (Fireball)e.getDamager();
    		Player p = (Player)e.getEntity();
            if (b.getCustomName() == null) {
            	return;
            }
    		if (b.getCustomName().equals("towershot")) {
    			if (p.isBlocking()) {
    				p.setVelocity(p.getVelocity().add(p.getLocation().toVector().subtract(b.getLocation().toVector()).normalize().multiply(2)));
    			}
    			e.setDamage(e.getDamage() * 2);
    			
    			p.addPotionEffect(PotionEffectType.LEVITATION.createEffect(20,1));
    			p.setSprinting(false);
    			p.setFireTicks(60);
    		}
    	}
    	
    	boolean isFireballExplodeMonster = (e.getEntity() instanceof Monster) && (e.getDamager() instanceof Fireball);
    	
    	if (isFireballExplodeMonster) {
    		Fireball b = (Fireball)e.getDamager();
    		Monster p = (Monster)e.getEntity();
            if (b.getCustomName() == null) {
            	return;
            }
    		if (b.getCustomName().equals("towershot")) {
    			e.setDamage(e.getDamage() * 2);
    			
    			p.addPotionEffect(PotionEffectType.LEVITATION.createEffect(20,1));
    			p.setFireTicks(60);
    		}
    	}
    	
    }

}