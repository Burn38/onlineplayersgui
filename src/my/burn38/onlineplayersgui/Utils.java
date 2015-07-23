package my.burn38.onlineplayersgui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

public class Utils {
	
	Plugin pl = OnlinePlayersGUIMain.getPlugin();
	ApiManager apiManager;
	PluginDescriptionFile pdfFile = pl.getDescription();
	Essentials ess;  
	Logger log; String pluginTag;
	
	  boolean isSound(String str) {
		  log = OnlinePlayersGUIMain.getPLLogger();
		  boolean result = false;
		  Sound[] sounds = Sound.values();
		  for (int i = 0; i < sounds.length; i++) {
			  if (sounds[i].name().equals(str)) result = true;
		  }
		  if (str == null) return false;
		  if (!result && str.equalsIgnoreCase("none")) result = true;
		  if (!result) log.severe("[" + pdfFile.getName() + "]"+" SOUND: " + str + " doesn't exist");
		return result;
	  }
	  boolean isMaterial(String str) {
		  log = OnlinePlayersGUIMain.getPLLogger();
		  boolean result = false;
		  if (str != null) str=str.toUpperCase();
		  Material[] materials = Material.values();
		  for (int i = 0; i < materials.length; i++) {
			  if (materials[i].name().equals(str)) result = true;
		  }
		  if (str == null) return false;
		  if (!result && str.equalsIgnoreCase("none")) result = true;
		  if (!result) log.severe("[" + pdfFile.getName() + "]"+" MATERIAL: " + str + " doesn't exist");
		return result;
	  }
	  Material getMaterial(String str) {
		  return isMaterial(str) ? Material.valueOf(str) : null;
	  }
	  boolean isDamageable(ItemStack i) {
	   if (i != null) {
		 if (i.getType() != null) {
			  if (i.getType().getMaxDurability() > 0) {
				  return i.getType().getMaxDurability() > 0;  
			  }	else {
				  return false;
			  }  
		  } else {
			  return false;
		  }
	  } else {
		  return false;
	  }
	}
	  
	  int getMultiple(int s, int m) {
		if (s <= m) s = m;
		else {
			do {
				s++;
			} while ((s % m) != 0);
		}
		return s;
	}
	  public String getPermission(String path) {
		  String permission = "";
		  if (pl.getConfig().getString(path+".permission") != null) permission = pl.getConfig().getString(path+".permission");
		  return permission; 
	  }
	  public String getPermissionErrorMessage(String path) {
		  String message;
		  if (pl.getConfig().getString(path+".permission_error") != null && !(pl.getConfig().getString(path+".permission_error").equalsIgnoreCase("NONE"))) message = pl.getConfig().getString(path+".permission_error");
		  else message = pl.getConfig().getString("plugin.messages.errors.permission_command");
		  if (message.contains("&")) message = ChatColor.translateAlternateColorCodes('&', message);
		  return getPluginTag() + message.replaceAll("%pluginName%", pdfFile.getName()).replaceAll("%pluginVersion%", pdfFile.getVersion()).replaceAll("%author%", "Burn38").replaceAll("%tag%", getPluginTag()).replaceAll("%prefix%", getPluginTag());
	  }
	  public String getMessage(String path) {
		  String msg = "&cMALCONFIGURED PLUGIN: " + (path != null ? path:""); 
		  if (pl.getConfig().getString(path) != null) msg = pl.getConfig().getString(path);
		  if(msg.contains("&")) msg = ChatColor.translateAlternateColorCodes('&', msg);
		  msg = msg.replaceAll("%pluginName%", pdfFile.getName()).replaceAll("%pluginVersion%", pdfFile.getVersion()).replaceAll("%author%", "Burn38").replaceAll("%tag%", getPluginTag()).replaceAll("%prefix%", getPluginTag());
		  return msg;
	  }
	  public Boolean getBoolean(String path) {
		  return pl.getConfig().getBoolean(path);
	  }
	  public void setBoolean(String path, boolean bool) {
		  pl.getConfig().set(path, bool);
	  }
	  String getEssLocNameFromString(String type, String sentence, Player p) throws Exception {
		  switch (type) {
		  
		  case "home": {
			  String[] homes = ess.getUser(p).getHomes().toArray(new String[ess.getUser(p).getHomes().size()]);
			  for (int i = 0; i < homes.length; i++) {
				  if (sentence.contains(homes[i])) return homes[i];
			  }
		  }
		  
		  case "jail": {
			  String[] jails = ess.getJails().getList().toArray(new String[ess.getJails().getCount()]);
			  for (int i = 0; i < jails.length; i++) {
				  if (sentence.contains(jails[i])) return jails[i];
			  }
		  }
		  	default: return null;
		  }
	  }
	  String getEssLocNameFromString(String type, String sentence, OfflinePlayer p) throws Exception {
		  switch (type) {
		  
		  case "home": {
			  String[] homes = ess.getOfflineUser(p.getName()).getHomes().toArray(new String[ess.getOfflineUser(p.getName()).getHomes().size()]);
			  for (int i = 0; i < homes.length; i++) {
				  if (sentence.contains(homes[i])) return homes[i];
			  }
		  }
		  
		  case "jail": {
			  String[] jails = ess.getJails().getList().toArray(new String[ess.getJails().getCount()]);
			  for (int i = 0; i < jails.length; i++) {
				  if (sentence.contains(jails[i])) return jails[i];
			  }
		  }
		  	default: return null;
		  }
	  }
	  Location getEssLocFromName(String type, String name, Player p) throws Exception {
		  switch (type) {
		  
		  	case "home": {
		  		if (ess.getUser(p).getHomes().contains(name)) return ess.getUser(p).getHome(name);
		  	}
		  
		  	case "jail": {
		  		if (ess.getJails().getList().contains(name)) return ess.getJails().getJail(name);
		  	}
		  
		  	default: return null;
		  }
	  }
	  String getLocStr(Location l) {
		  String str="world,0,0,0,0,0";
		  str = l.getWorld().getName()+":";
		  str += l.getX()+":"; str += l.getY()+":"; str += l.getZ()+":"; str += l.getPitch()+":"; str += l.getYaw();
		  return str;
	  }
	  Location getStrLoc(String s) {
		  String[] a = s.split(":");
		  return new Location(Bukkit.getWorld(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]), Float.parseFloat(a[4]), Float.parseFloat(a[5]));
	  }
	  String getStringTime(int time) {
			String str;
			 int days = (int)TimeUnit.MINUTES.toDays(time);        
			 long hours = TimeUnit.MINUTES.toHours(time) - (days *24);
			 long minutes = TimeUnit.MINUTES.toMinutes(time) - (TimeUnit.MINUTES.toHours(time)* 60);
			 str = days + "d " + hours +"h "+minutes+"mins";
			return str;
		}  
	  String formatDouble(Double d) {
		  DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
		  DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		  symbols.setGroupingSeparator(' ');
		  formatter.setDecimalFormatSymbols(symbols);
		  String str = formatter.format(d).replaceAll(",",".");
		return str;
	  }
	  public void moveYamlPath(String from, String to) {
		  if (from==to) return;
		FileConfiguration c = pl.getConfig();
		for (String s : pl.getConfig().getConfigurationSection(from).getKeys(false)) {
			c.set(to+"."+s, c.get(from+"."+s));
			c.set(from+"."+s, null);
		}
		c.set(from, null);
		pl.saveConfig();
		System.out.println("\""+from+"\" moved to \""+to);
	}
	@SuppressWarnings("deprecation")
	  public boolean isPlayer(String str) {
		pluginTag = getPluginTag();
		if ((Bukkit.getPlayer(str) != null) || (Bukkit.getOfflinePlayer(str) != null)) {
			return true;
		}
		else {
			return false;
		}
	}
	  public boolean isOnline(String str) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player p : players) {
			if (p.getName().toLowerCase().equalsIgnoreCase(str.toLowerCase())|| p.getUniqueId().toString().toLowerCase().equalsIgnoreCase(str.toLowerCase())) {
				return true;
			}
	    }
		return false;
	 }
	  public boolean isOffline(String str) {
		  User user = ess.getOfflineUser(str);
			return user == null ? false : true;
	  }
	  public String getPlayerMoney(Player p) {
		  String str = "";
		  Economy eco = ApiManager.getEconomy();
			  String money = formatDouble(eco.getBalance(p));
			  str = money+" "+ChatColor.GOLD+(eco.getBalance(p) <= 1 ? eco.currencyNameSingular() : eco.currencyNamePlural());
		  return str;
	  }
	  public String getPlayerMoney(OfflinePlayer p) {
		  String str = "";
		  Economy eco = ApiManager.getEconomy();
			  String money = formatDouble(eco.getBalance(p));
			  str = money+" "+ChatColor.GOLD+(eco.getBalance(p) <= 1 ? eco.currencyNameSingular() : eco.currencyNamePlural());
		  return str;
	  }
	  public String getPluginTag() {
		  return ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("plugin.tag"));
	  }
	  public boolean wantsSound(Player player) {
		Boolean bool = getBoolean("players."+player.getUniqueId().toString()+".sound");
		if (bool == null) {setBoolean("players."+player.getUniqueId().toString()+".sound", true); bool = true;}
		return bool;
	  }
	  public boolean hasPermission(Player p, String path) {
			if (p.hasPermission(getPermission(path)) || getPermission(path).equalsIgnoreCase("NONE") || p.hasPermission(pl.getConfig().getString("plugin.commands.super_perm")) || p.isOp()) return true;
			else {
				p.sendMessage(getPermissionErrorMessage(path).replaceAll("%sender%", p.getName()));
				return false;
			}
	  }
}
