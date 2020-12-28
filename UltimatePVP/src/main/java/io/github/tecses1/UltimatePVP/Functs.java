package io.github.tecses1.UltimatePVP;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Functs {
	
	public static void SetRegion(Location loc1, Location loc2, Material material, World world) {
		int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		 
		for(int x = minX; x <= maxX; x++){
		  for(int y = minY; y <= maxY; y++){
		    for(int z = minZ; z <= maxZ; z++){
		    	
		      Block block = world.getBlockAt(x, y, z);
		      
		      block.setType(material);
		    }
		  }
		}
	}
	public static String formatDouble (double d) {
		
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		return formatter.format(d);
		
	}
	public static boolean ScanRegion(Location loc1, Location loc2, World world) {
		int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		 
		for(int x = minX; x <= maxX; x++){
		  for(int y = minY; y <= maxY; y++){
		    for(int z = minZ; z <= maxZ; z++){
		    	
		      Block block = world.getBlockAt(x, y, z);

		      
		      if (!block.isEmpty() || block.getType() != Material.AIR) {
		    	  return false;
		      }
		     
		    }
		  }
		}
		
		return true;
	}
	
	public static boolean ScanRegionForBanners(Location loc1, Location loc2, World world) {
		int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		 
		for(int x = minX; x <= maxX; x++){
		  for(int y = minY; y <= maxY; y++){
		    for(int z = minZ; z <= maxZ; z++){
		    	
		      Block block = world.getBlockAt(x, y, z);

		      
		      if (block.getType() == Material.RED_BANNER || block.getType() == Material.WHITE_BANNER) {
		    	  return false;
		      }
		     
		    }
		  }
		}
		
		return true;
	}
	
	public static List<Player> getNearbyPlayers(Main mainPlugin, Location loc, int distance, RuntimeFactionData faction)
	{

		World world = loc.getWorld();
	    int distanceSquared = distance*distance;
	    List<PlayerComparer> list = new ArrayList<PlayerComparer>();
        

		
	    for(Player p: Bukkit.getOnlinePlayers()) {
			if (faction != null) {
				PlayerData pd = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());
				if (faction.saveData.factionName.equals(pd.myParty)) {
					continue;
				}
			}
	    	if (p.getWorld() == world) {
	    		if (p.getLocation() != loc) {
	    			if(p.getLocation().distanceSquared(loc) < distanceSquared ) {
	    				
	    				list.add(new PlayerComparer (p, p.getLocation().distanceSquared(loc)));
	    			}
	    		}
	    		
	    	}
	    }
	    
	    Collections.sort(list);
	    
	    ArrayList<Player> pList = new ArrayList<Player>();
	    
	    for (PlayerComparer p : list) {
	    	pList.add(p.player);
	    }
	    
	    return pList;
	}
	public static String directionStringBetween(Location from , Location to) {
		Vector v1 = from.toVector();
		Vector v2 = to.toVector();
		
		
		double angle = (Math.atan2(v1.getX() - v2.getX(), v1.getZ() - v2.getZ()));
		angle = (-(angle / Math.PI) * 360.0d) / 2.0d + 180.0d;

		Double direction = Functs.round(angle, 2);
		String toSetDirection = "";
		
		if (direction >= 0 && direction < 30) {
			toSetDirection += "N";
		}else if (direction >= 30 && direction < 60) {
			toSetDirection += "NE";
		}else if (direction >= 60 && direction < 90) {
			toSetDirection += "E";
		}else if (direction >= 90 && direction < 120) {
			toSetDirection += "E";
		}else if (direction >= 120 && direction < 150) {
			toSetDirection += "SE";
		}else if (direction >= 150 && direction < 180) {
			toSetDirection += "S";
		}else if (direction >= 180 && direction < 210) {
			toSetDirection += "S";
		}else if (direction >= 210 && direction < 240) {
			toSetDirection += "SW";
		}else if (direction >= 240 && direction < 270) {
			toSetDirection += "W";
		}else if (direction >= 270 && direction < 300) {
			toSetDirection += "W";
		}else if (direction >= 300 && direction < 330) {
			toSetDirection += "NW";
		}else if (direction >= 330 && direction < 360) {
			toSetDirection += "N";
		}
		toSetDirection += " (" + direction + ")";
		
		return toSetDirection;
	}
	public static boolean hasNearbyPlayer(Player player, int distance, Main mainPlugin){
		
	    int distanceSquared = distance*distance;
	    
        
	    PlayerData playerDat = mainPlugin.getPlayerByUUID(player.getUniqueId().toString());
		
	    for(Player p : Bukkit.getOnlinePlayers()) {
	    	
	    	if (p == player) {
	    		continue;
	    	}
	    	
	    	if (p.getWorld() == player.getWorld()) {
	    		if(p.getLocation().distanceSquared(player.getLocation()) < distanceSquared ) {
	    			PlayerData enemyDat = mainPlugin.getPlayerByUUID(p.getUniqueId().toString());
	    			if (!enemyDat.myParty.equals(playerDat.myParty)) {
	    				return true;
	    			}
	    		}
	    	}
	    }
	    return false;
	}
	public static double round(double x, int places) {
		double roundPlaces = 10.0 * places;
		
		return Math.round(x * roundPlaces) / roundPlaces;
	}
	
	public static boolean LOS(Location loc1, Location loc2) {
		
		World w = loc1.getWorld();

		Vector v1 = loc1.toVector();
		Vector v2 = loc2.toVector();
		
		Vector v3 = v2.subtract(v1).normalize();
		
		double dist = loc1.distance(loc2);
		
		RayTraceResult r = w.rayTraceBlocks(loc1, v3, dist);
		
		if (r == null) {
			return true;
		}else {
			return false;
		}
		
	}
	public static List<FDReturn> hasNearbyFactionNoUpdate(PlayerData player, int distance, List<RuntimeFactionData> factions){
		 
	    int distanceSquared = distance*distance;
        
	    Player bukkitPlayer = Bukkit.getPlayer(UUID.fromString(player.myUUID));
	    List<FDReturn> fd = new ArrayList<FDReturn>();
	    
	    for(RuntimeFactionData rfd : factions) {
	    	
	    	FactionData f = rfd.saveData;
	    	
	    	if (f.factionName.equals(player.myParty)) {
	    		continue;
	    	}
	    	if (!bukkitPlayer.getWorld().equals(Bukkit.getWorld(f.capitolWorld))) {
	    		continue;
	    	}
	    	
	    	if (!f.capitol) {
	    		continue;
	    	}
	    	//Location factionLocation = new Location(Bukkit.getWorld(f.capitolWorld), f.capitolPosX, f.capitolPosY, f.capitolPosZ);
	    	Double dist = bukkitPlayer.getLocation().distanceSquared(rfd.capitolLocation);
    		if(dist < distanceSquared ) {

    			
    			FDReturn returnFD = new FDReturn(f.factionName, Math.sqrt(dist), Functs.directionStringBetween(rfd.capitolLocation, bukkitPlayer.getLocation()));
    	    	fd.add(returnFD);
    		}
	    	
	    }
	    Collections.sort(fd);
	    return fd;
	}
	public static boolean hasNearbyFaction(PlayerData player, int distance, List<RuntimeFactionData> factions){
		 
	    int distanceSquared = distance*distance;
    	Player bukkitPlayer = Bukkit.getPlayer(UUID.fromString(player.myUUID));

    	
    	
	    for(RuntimeFactionData rfd : factions) {
	    	
	    	FactionData f = rfd.saveData;
	    	if (!bukkitPlayer.getWorld().equals(rfd.capitolLocation.getWorld())) {
	    		continue;
	    	}
	    	if (!f.capitol) {
	    		continue;
	    	}
	    	
			boolean nearFaction = false;
			
			for (RuntimeTowerData t : rfd.activeTowers) {
				Location l = t.towerLocation;
				
				
				Location p = bukkitPlayer.getLocation();
				
				p = new Location(p.getWorld(), p.getX(), l.getY(), p.getZ());
		    	
		    	if (p.distanceSquared(l) <= distanceSquared) {
		    		nearFaction = true;
		    		break;
		    	}
		    	
		    	
			}
			if (!nearFaction) {
				Location l = rfd.capitolLocation;
				
				Location p = bukkitPlayer.getLocation();
				p = new Location(p.getWorld(), p.getX(), l.getY(), p.getZ());
		    	
		    	if (p.distanceSquared(l) <= distanceSquared) {
		    		nearFaction = true;
		    	}
		    	
			}
			
    		if(nearFaction) {
    			
    			if (f.memberUUIDs.contains(player.myUUID)) {
    	    		if (player.currentRegion != f.factionName) {
    	    			MessagesHandler.SendMessage(bukkitPlayer, MessagesHandler.inOwnFactionTerritory, new String[] {f.factionName});
    	    			player.currentRegion = f.factionName;
    	    		}
        			
    	    	}else {
    	    		if (player.currentRegion != f.factionName) {
        	    		MessagesHandler.SendMessage(bukkitPlayer, MessagesHandler.inFactionTerritory, new String[] {f.factionName});
    	    			player.currentRegion = f.factionName;
    	    			
    	    			String directionString = Functs.directionStringBetween(bukkitPlayer.getLocation(), rfd.capitolLocation);


        	    		for (String u : f.memberUUIDs) {
        	    			Player pp = Bukkit.getPlayer(UUID.fromString(u));
        	    			MessagesHandler.SendMessage(pp, MessagesHandler.playerEnteredTerritory, new String[] {bukkitPlayer.getDisplayName(), directionString});
        	    		}
    	    		}
    	    		

    	    		
    	    	}
    	    	return true;
    		}
	    	
	    }
	    return false;
	    
	}
	public static ArrayList<Block> getNearbyBlocksSphere(final Location center, final int radius) {
	    ArrayList<Block> sphere = new ArrayList<Block>();
	    for (int Y = -radius; Y < radius; Y++)
	      for (int X = -radius; X < radius; X++)
	         for (int Z = -radius; Z < radius; Z++)
	            if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
	               final Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
	               sphere.add(block);
	            }
	return sphere;
	}
	public static Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}
	
	public static ArmorStand SpawnHologram(String text, Location loc) {

		ArmorStand as = (ArmorStand)  loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND); //Spawn the ArmorStand
		
		as.setGravity(false); //Make sure it doesn't fall
		as.setCanPickupItems(false); //I'm not sure what happens if you leave this as it is, but you might as well disable it
		as.setCustomName(text); //Set this to the text you want
		as.setCustomNameVisible(true); //This makes the text appear no matter if your looking at the entity or not
		as.setVisible(false); //Makes the ArmorStand invisible\
		as.setCollidable(false);
		
		return as;
		
		
	}
	public static void SpawnFirework(Location loc) {
		//Spawn the Firework, get the FireworkMeta.
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
       
        //Our random generator
        Random r = new Random();   

        //Get the type
        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;       
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.STAR;
       
        //Get our random colours   
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);
       
        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
       
        //Then apply the effect to the meta
        fwm.addEffect(effect);
       
        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
       
        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);           
	}
	public static List<Block> getBlocksSquare(Location loc1, Location loc2) {
		int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		 
		
		List<Block> blocks = new ArrayList<Block>();
		World world = loc1.getWorld();
		
		if (!world.equals(loc2.getWorld())) {
			return null;
		}
		
		for(int x = minX; x <= maxX; x++){
		  for(int y = minY; y <= maxY; y++){
		    for(int z = minZ; z <= maxZ; z++){
		    	
		      Block block = world.getBlockAt(x, y, z);
		      blocks.add(block);
		      
		    }
		  }
		}
		return blocks;
	}
    
    public static ArrayList<Block> getNearbyBlocksCircle(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
               blocks.add(location.getWorld().getBlockAt(x, (int)location.getY(), z));
            
            }
        }
        return blocks;
    }
    

}

