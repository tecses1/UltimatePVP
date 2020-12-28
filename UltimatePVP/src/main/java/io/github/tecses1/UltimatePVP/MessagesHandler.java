package io.github.tecses1.UltimatePVP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;


public class MessagesHandler{
	public static String CapitolSet = "&cYour capitol has been sucessfuly set.";
	static String title = "&8Ultimate&cPVP";
	static String version = "&61.1";
	

	static String helpMessageAdmin = title + "&aHelp (Admin) :\n"+ 
			"    &bupvp &6version &a- Displays plugin version\n" +
			"    &bupvp &6reload &a- Reloads plugin configuration\n" +
			"    &bupvp &6fixtags &a- fixes hologram nametags above factions.\n" +
			"    &bupvp &6reset &e[player] &a- Resets player's data.\n" +
			"    &bupvp &6pillage &e[faction] &a- Pillages a faction, and voids the wallet.\n" +
			"";
	static String helpMessageFaction = title + "&a Help:\n"+ 
			"    &bfaction &a- Displays information about your faction.\n" +
			"    &bfaction &6nearby &a- Lists nearby factions.\n" +
			"    &bfaction &6top &a- Displays top factions.\n" +
			"    &bfaction &6help &a- Displays help\n" +
			"    &bfaction &6leave &a- Leaves your faction\n" +
			"    &bfaction &6invite &f[player] &a- Invites given player to join your faction.\n" +
			"    &bfaction &6accept &a- Accepts an invitation to a faction. \n" +
			"    &bfaction &6create &f [name] &a- Creates a new faction with the given name.\n" +
			"    &bfaction &6home - &aTeleports to faction home\n" +
			"    &bfaction &6capitol &a- Places your capitol where you are. &4Warning, this cannot be undone.\n" +
			"    &bfaction &fd&6eposit &f[amount] &a- Deposit from player to faction wallet.\n" +
			"    &bfaction &fw&6ithdraw &f[amount] &a- Withdrawal from faction to player wallet.\n" +
			"    &bfaction &6next &a- Displays how long until your next payday.\n" +
			"    &bfaction &6tower &a- Create a tower in your territory for $1,000,000. \n" +
			"    &bfaction &6upgrade &a- Upgrades the tower you're looking at to shoot. \n" +
			"    &bfaction &6defend &a- Teleports to any tower under attack to help defend. \n" +
			"";
	
	static String versionMessage = "&aYou have " + title + "&a version " + version + "&a installed.";
	static String reloadMessage = "&aReloading " + title;
	
	static String factionCreateFailedFactionExists = "&cA faction with the name #0 &calready exists!";
	static String factionCreateFailedPlayerInFaction = "&cYou must leave #0 &cwith /faction leave.";
	static String factionCreateSuccesful = "&aYou sucessfuly established the faction &b #0";

	static String factionMenu = "&aFaction &b#0:\n" + 
			"    &aCaptiol: &6#1\n" +
			"    &aWallet: &2$&6#2\n" +
			"    &aIncome (next): &2$&6#3\n" +
			"    &aTotal Towers: &6#4\n" +
			"    &aMembers:\n#5" +
			"    \n";
	static String factionNotFound = "&cCould not find your faction, or you are not in one.";

	
	static String leaveSucceded = "&cYou have left your faction.";
	static String leaveFailed = "&cYou are not currently in a faction.";
	static String DisbandedFaction = "&f[&bAnnouncement&f] &cThe faction &6 #0 &c has been disbanded.";
	public static String inviteFailedPlayerNotFound = "&cPlayer&6 #0 &c not found.";
	public static String inviteSuccessful = "&aAn invitation was sent to &6 #0";
	public static String inviteToParty = "&aPlayer&6 #0 &a has invited you to join their faction&6 #1";
	public static String acceptSucceeded = "&aYou sucessfully joined the faction&6 #0";
	public static String acceptFailed = "&cYou do not have a pending invite to accept.";
	public static String inviteFailedSelf = "&cYou cannot invite yourself to your faction!";
	public static String playerJoinedFaction = "&aPlayer&6 #0 &ahas joined the faction!";
	public static String inviteFailedPlayerInFaction = "&cThat player is already in a faction!";
	public static String factionHelpMessage = "&cThere was a problem parsing your command. &aTry /faction &6help";
	public static String capitolAlreadySetTeleport = "&aYou already have a capitol. Teleport to it with /faction &6home";
	public static String capitolAlreadySetTeleportFailed = "&cYou can only teleport to your Capitol in your territory.";
	public static String captiolBlocked = "&cCapitol can not be placed underground or under any blocks.";

	public static String factionPaid = "&aPayday! Faction paid &2$&6#0 &ato the wallet.";
	public static String notEnoughMoney = "&cYou do not have that much money." ;
	public static String notANumber = "&cDeposit amount must be a number.";
	
	public static String depositSuccess = "&aYou have sucessfuly deposited &2$&6#0 &ato your faction.";
	public static String withdrawalSuccess= "&aYou have sucessfuly withdrawaled &2$&6#0 &afrom your faction.";
	
	public static String depositSuccessOthers = "&a#0 has deposited &2$&6#1 &ato your faction.";
	public static String withdrawalSuccessOthers = "&a#0 has withdrawaled &2$&6#1 &afrom your faction.";
	
	
	public static String inFactionTerritory = "&aYou are now entering the territory of &b#0";
	public static String cannotModifyFactionTerrain = "&cYou can not modify another faction's terrain!";
	public static String inOwnFactionTerritory = "&aWelcome home to your faction, faction member.";
	public static String enteringNoMansLand = "&cEntering no mans land. (no faction control.)";
	public static String cannotModifyFactionTerrainEnemies = "&cCan not modify your terrain when enemies are nearby!";
	public static String captiolBlockedByWorld = "&cYou can not set your capitol on an exempt world!";
	public static String captiolBlockedByFaction = "&cYou can not place your capitol so close to another faction.";
	public static String yesNearbyFactions = "&aNearby factions (Name, Distance.):\n#0";
	public static String factionTop = "&aTop 10 factions:\n#0";

	public static String noNearbyFactions = "&aGood news, you're not near any factions!";
	public static String factionHasFallen = "&cThe faction &6#0 &chas fallen to &6#1";
	public static String yourFactionFell = "&4Your faction's capitol was destroyed, and has fallen.";
	public static String killedFactionWinnings = "&aYour faction killed the faction &6#0, and recieved their wallet of &2$&6#1";
	public static String killedFactionWinningsP =  "&aYou killed the faction &6#0, and recieved their wallet of &2$&6#1";
	public static String playerEnteredTerritory = "&aPlayer &6#0 &a has entered your territroy from the &6#1";
	public static String capitolBlockedBySpawn = "&cYou can't place your capitol close to world spawn!";
	public static String cannotFactionHomePlayersNearby = "&cYou can not teleport to your home when enemeis are nearby.";
	public static String towerBlockedBySpawn = "&cYou can't place a tower close to world spawn!";
	public static String towerBlockedNotInTerritory = "&cTower must be placed inside your territory.";
	public static String towerBlockedByFaction = "&cTower can not be placed near other factions.";
	public static String aTowerFell = "&cA tower has fallen in your faction!";
	public static String towerBlocked = "&cYou can not place a tower underground or under any blocks.";
	public static String upgradeDeniedMoney = "&cYou do not have enough money to upgrade that tower.";
	public static String upgradeDeniedMaxLevel = "&cTower is already max level.";
	public static String upgradeDeniedNotATower = "&cYou must be looking at a tower to upgrade!";
	public static String towerBlockedNoCapitol = "&cYou must build a capitol before building towers.";
	public static String towerBlockedNotEnoughMoney = "&cYou need 1,000,000 in your wallet to build a tower.";
	public static String towerEngaging = "&cA tower in your territory is &4engaging &cthe enemy! Type /faction defend!";
	public static String towerTeleportFailedNoTower = "&cThere are no towers under attack in your faction!";
	public static String towerTeleportFailedDisabled = "&cYou can not teleport to defend while disabled!";
	public static String homeInvalid = "&cYour last set home has no banner! It was destroyed.";
	public static String homeDisabledByPlayer = "&cYou may not teleport home when near other players!";
	public static String homeSet = "&aYour home was successfully set to your location!";
	public static String homeSetDisabled = "&cYou can not set your home in enemy factions or near other players.";
	public static String homeSetDisabledBannerPlace = "&cYour home was not set because a banner cannot be placed there!";
	public static String towerBlockedByBanner = "&cYou cannot build a tower that will break an existing banner.";
	public static String factionCreateFailedNameInvalid = "&cThe name specified for your faction was invalid!";
	public static String timeLeftTillNextIncome = "&aYour next income is in &6#0 &aminutes and &6#1 &aseconds.";
	public static String mustHaveCapitol = "&cYou must have a capitol to see your next income!";
	public static String mustHaveFaction = "&cYou must be in a faction to have income!";
	public static String factionHasNoTowers = "&cYour faction does not have any towers built!";
	
	static void SendMessage(Player p, String m, String[] args) {
		String message = "";
		message += m;
		
		String returnString = "";
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				String toReplace = "" + args[i];
				String newMessage = message.replaceAll("#" + i, toReplace);
				if (newMessage != null) {
					message = newMessage;
				}

			}
		}
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			
			if (c == '&') {

				if (i + 1 < message.length()) {
					
					char nextC = message.charAt(i + 1);
					i ++;
					
					if (p == null) {
						continue;
					}
					switch (nextC) {
						case '0':
							returnString += ChatColor.BLACK;
							break;
						case '1':
							returnString += ChatColor.DARK_BLUE;
							break;
						case '2':
							returnString += ChatColor.DARK_GREEN;
							break;
						case '3':
							returnString += ChatColor.DARK_AQUA;
							break;
						case '4':
							returnString += ChatColor.DARK_RED;
							break;
						case '5':
							returnString += ChatColor.DARK_PURPLE;
							break;
						case '6':
							returnString += ChatColor.GOLD;
							break;
						case '7':
							returnString += ChatColor.GRAY;
							break;
						case '8':
							returnString += ChatColor.DARK_GRAY;
							break;
						case '9':
							returnString += ChatColor.BLUE;
							break;
						case 'a':
							returnString += ChatColor.GREEN;
							break;
						case 'b':
							returnString += ChatColor.AQUA;
							break;
						case 'c':
							returnString += ChatColor.RED;
							break;
						case 'd':
							returnString += ChatColor.LIGHT_PURPLE;
							break;
						case 'e':
							returnString += ChatColor.YELLOW;
							break;
						case 'f':
							returnString += ChatColor.WHITE;
							break;
						default:
							i --;
							returnString += '&';
							break;
						
					}
				}
				
				
			}else {
				returnString += c;
			}
		}

		
		if (p == null) {
			Bukkit.getLogger().info(returnString);
		}else {
			p.sendMessage(returnString);
		}
	}

}