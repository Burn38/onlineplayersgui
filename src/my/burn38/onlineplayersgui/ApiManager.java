package my.burn38.onlineplayersgui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Logger;

import me.armar.plugins.autorank.Autorank;
import my.burn38.onlineplayersgui.commands.ONPCommandExecutor;
import my.burn38.onlineplayersgui.runnables.MessageEntryCountdown;
import my.burn38.onlineplayersgui.threads.ConvertThread;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class ApiManager {

	Plugin pl = OnlinePlayersGUIMain.getPlugin();

	Boolean useAutorank, useVault, isAutorankEnabled = false, isVaultEnabled = false, logE = false;
	PluginDescriptionFile pdfFile = pl.getDescription();
    Utils utils;
    Logger log = OnlinePlayersGUIMain.getPLLogger();
    GuiManager guiManager;
    EventsManager eventsManager;
    ONPCommandExecutor ONPCMDEX;
    String pluginTag;
    Essentials ess; Autorank ar;
    static Economy eco;
    
    public FileConfiguration getConfig() {
    	return pl.getConfig();
    }
    
    public void loadManagers() {
    	OnlinePlayersGUIMain.utils = new Utils();
    	OnlinePlayersGUIMain.guiManager = new GuiManager();
    	OnlinePlayersGUIMain.eventsManager = new EventsManager();
    	OnlinePlayersGUIMain.convertThread = new ConvertThread();
    	OnlinePlayersGUIMain.messageCountdown = new MessageEntryCountdown();
    	
    	
    	utils = OnlinePlayersGUIMain.getUtils();
    	guiManager = OnlinePlayersGUIMain.getGuiManager();
    	eventsManager = OnlinePlayersGUIMain.getEventsManager();
    	
    	utils.apiManager = OnlinePlayersGUIMain.getApiManager();
    	
    	guiManager.apiManager = OnlinePlayersGUIMain.getApiManager();
    		guiManager.utils = OnlinePlayersGUIMain.getUtils();
    		
    	eventsManager.apiManager = OnlinePlayersGUIMain.getApiManager();
    		eventsManager.guiManager = OnlinePlayersGUIMain.getGuiManager();
    			eventsManager.utils = OnlinePlayersGUIMain.getUtils();
    	
    	eventsManager.apiManager = OnlinePlayersGUIMain.getApiManager();
    	    eventsManager.guiManager = OnlinePlayersGUIMain.getGuiManager();
    	    	eventsManager.utils = OnlinePlayersGUIMain.getUtils();
    	OnlinePlayersGUIMain.convertThread.apiManager = OnlinePlayersGUIMain.getApiManager();
    	    OnlinePlayersGUIMain.convertThread.guiManager = OnlinePlayersGUIMain.getGuiManager();
    			OnlinePlayersGUIMain.convertThread.utils = OnlinePlayersGUIMain.getUtils();
    			
    	OnlinePlayersGUIMain.messageCountdown.utils = OnlinePlayersGUIMain.getUtils();
    }
    public void loadCommands() {
    	
    	ONPCMDEX = new ONPCommandExecutor();
    		ONPCMDEX.utils = OnlinePlayersGUIMain.getUtils();
    			ONPCMDEX.guiManager= OnlinePlayersGUIMain.getGuiManager();
    				ONPCMDEX.apiManager = OnlinePlayersGUIMain.getApiManager();
    	
    			((JavaPlugin)pl).getCommand("onp").setExecutor(ONPCMDEX);
    }
    
    public void loadDependencies() {
        loadEssentials();

		utils.ess = getESS();
			guiManager.ess = getESS();
				eventsManager.ess = getESS();
    }
    public void loadAddons() {
    	loadAutorank();
    		loadVault();
    }
    
    public void loadEssentials() {
    	if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
        	ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        	log.info("[" + pdfFile.getName() + "]"+ " found Essentials, plugin can run correctly. Continuing.");
        } else {
        	log.severe("[" + pdfFile.getName() + "]"+ " needs Essentials to work correctly, stopping the plugin.");
        	Bukkit.getPluginManager().disablePlugin(pl);
        }
    }

    public void loadAutorank() {
    	if (useAutorank) {
    		if ((Bukkit.getServer().getPluginManager().getPlugin("Autorank") instanceof Autorank ? Bukkit.getServer().getPluginManager().getPlugin("Autorank") : null) != null) {
    		    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Autorank");
    		    ar = (Autorank) plugin;
    			this.isAutorankEnabled = true;
    			log.info(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("plugin.tag"))+" Autorank is enabled in the configuration and it's installed, all the features will be displayed!");
					guiManager.autorankAPI = getAR().getAPI();
    		} else {
    			log.info(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("plugin.tag"))+" Autorank is enabled in the configuration but it's not installed, some features will not be displayed!");
    			ar = null;
    			this.isAutorankEnabled = false;
    		}
    	} else {
    		log.info(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("plugin.tag"))+" Autorank is disabled in the configuration, some features will not be displayed!");
    		ar = null;
    		this.isAutorankEnabled = false;
    	}
    }
    public void loadVault() {
    	if (useVault) {
    		if ((Bukkit.getServer().getPluginManager().getPlugin("Vault") instanceof Vault ? Bukkit.getServer().getPluginManager().getPlugin("Vault") : null) != null) {
    		    RegisteredServiceProvider<Economy> economyProvider = pl.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
    		    eco = economyProvider.getProvider();
    		    
    			this.isVaultEnabled = true;
    			log.info(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("plugin.tag"))+" Vault (Economy) is enabled in the configuration and it's installed, all the features will be displayed!");
    				guiManager.vaultAPI = getEconomy();
    		} else {
    			log.info(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("plugin.tag"))+" Vault (Economy) is enabled in the configuration but it's not installed, some features will not be displayed!");
    			eco = null;
    			this.isVaultEnabled = false;
    		}
    	} else {
    		log.info(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("plugin.tag"))+" Vault (Economy) is disabled in the configuration, some features will not be displayed!");
    		eco = null;
    		this.isVaultEnabled = false;
    	}
    }

    public Autorank getAR() {
    	return ar;
    }
    public static Economy getEconomy() {
  	  return eco;
    }
    public static void setEconomy(Economy e) {
  	  eco = e;
    }
    public Essentials getESS() {
    	return ess;
    }
	
    public void loadConfiguration() throws FileNotFoundException {    	
		
    	pl.saveDefaultConfig();

			utils.pluginTag = utils.getPluginTag();
				pluginTag = ChatColor.stripColor(utils.getPluginTag());
			this.useAutorank = getConfig().getBoolean("plugin.addons.Autorank");
			this.useVault = getConfig().getBoolean("plugin.addons.Vault");
			if (getConfig().getString("plugin.tag") != null) {
				utils.pluginTag = getConfig().getString("plugin.tag");
				if (utils.pluginTag.contains("&")) utils.pluginTag = ChatColor.translateAlternateColorCodes('&', utils.pluginTag)+ChatColor.RESET;
			} if (getConfig().getString("guis.main.title") != null) {
				guiManager.guiName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("guis.main.title"));
			}  if (getConfig().getString("guis.more.title") != null) {
				guiManager.moreName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("guis.more.title"));
			}  if (getConfig().getString("guis.jails.title") != null) {
				guiManager.jailsName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("guis.jails.title"));
			}  if (getConfig().getString("guis.homes.title") != null) {
				guiManager.homesName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("guis.homes.title"));
			}
			if (!checkSounds() || !checkMaterials() || !checkTitles()) log.severe("[" + pdfFile.getName() + "]"+" Some Sounds / Materials / Titles weren't correct, plugin stopped. Please, check the config.yml file and modify the given Materials / Sounds / Titles.");
	}
	public void reloadConfiguration() {
		pl.reloadConfig();
			loadAddons();
	}

	
	public boolean checkSounds() {
		boolean result = true;
		ArrayList<String> errors = new ArrayList<String>();
		if (!utils.isSound(getConfig().getString("plugin.welcome_sound"))) errors.add("welcome_sound"+(getConfig().getString("plugin.welcome_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.menus_click_sound"))) errors.add("menus_click_sound"+(getConfig().getString("menus_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.tp_player.tp_sound"))) errors.add("menus.tp_player.tp_sound"+(getConfig().getString("menus.tp_player.tp_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.tp_lastpos.tp_sound"))) errors.add("menus.tp_lastpos.tp_sound"+(getConfig().getString("menus.tp_lastpos.tp_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.repair.on_click_sound"))) errors.add("menus.repair.on_click_sound"+(getConfig().getString("menus.repair.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.openInv.on_click_sound"))) errors.add("menus.openInv.on_click_sound"+(getConfig().getString("menus.openInv.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.openEnderchest.on_click_sound"))) errors.add("menus.openEnderchest.on_click_sound"+(getConfig().getString("menus.openEnderchest.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.mute.mute.on_click_sound"))) errors.add("menus.mute.mute.on_click_sound"+(getConfig().getString("menus.mute.mute.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.mute.unmute.on_click_sound"))) errors.add("menus.mute.unmute.on_click_sound"+(getConfig().getString("menus.mute.unmute.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.jails.cell.on_click_sound"))) errors.add("menus.jails.cell.on_click_sound"+(getConfig().getString("menus.jails.cell.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.jails.make_free.on_click_sound"))) errors.add("menus.jails.make_free.on_click_sound"+(getConfig().getString("menus.jails.make_free.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.homes.home.tp_sound"))) errors.add("menus.homes.home.tp_sound"+(getConfig().getString("menus.homes.home.tp_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.back.on_click_sound"))) errors.add("menus.back.on_click_sound"+(getConfig().getString("menus.back.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.kick.on_click_sound"))) errors.add("menus.kick.on_click_sound"+(getConfig().getString("menus.kick.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.ban.on_click_sound"))) errors.add("menus.ban.on_click_sound"+(getConfig().getString("menus.ban.on_click_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("menus.mail.on_mail_sent_sound"))) errors.add("menus.mail.on_message_sent_sound"+(getConfig().getString("menus.mail.on_message_sent_sound") == null ? " (null)" : ""));
		if (!utils.isSound(getConfig().getString("plugin.commands.short_warps_tp_sound"))) errors.add("plugin.short_warps_tp_sound"+(getConfig().getString("plugin.commands.short_warps_tp_sound") == null ? " (null)" : ""));

		if (errors.size() == 0) {
			log.info("[" + pdfFile.getName() + "]"+ " Sounds checked, they are correct.");
		} else {
			String error = "";
			for (String str : errors) error=error+str+", ";
			log.severe("[" + pdfFile.getName() + "]"+" The sounds: " +error+ "aren't correct, stopping the plugin.");
			OnlinePlayersGUIMain.shouldLoad = false;
			Bukkit.getPluginManager().disablePlugin(pl);
			return false;
		}
		
		return result;
	}	
	public boolean checkMaterials() {
		boolean result = true;
		ArrayList<String> errors = new ArrayList<String>();
		if (!utils.isMaterial(getConfig().getString("menus.tp_lastpos.material"))) errors.add("menus.tp_lastpos.material");
		if (!utils.isMaterial(getConfig().getString("menus.repair.material"))) errors.add("menus.repair.material");
		if (!utils.isMaterial(getConfig().getString("menus.openInv.material"))) errors.add("menus.openInv.material");
		if (!utils.isMaterial(getConfig().getString("menus.openEnderchest.material"))) errors.add("menus.openEnderchest.material");
		if (!utils.isMaterial(getConfig().getString("menus.mute.mute.material"))) errors.add("menus.mute.mute.material");
		if (!utils.isMaterial(getConfig().getString("menus.mute.unmute.material"))) errors.add("menus.mute.unmute.material");
		if (!utils.isMaterial(getConfig().getString("menus.jails.cell.material"))) errors.add("menus.jails.cell.material");
		if (!utils.isMaterial(getConfig().getString("menus.jails.make_free.material"))) errors.add("menus.jails.make_free.material");
		if (!utils.isMaterial(getConfig().getString("menus.homes.home.material"))) errors.add("menus.homes.home.material");
		if (!utils.isMaterial(getConfig().getString("menus.kick.material"))) errors.add("menus.kick.material");
		if (!utils.isMaterial(getConfig().getString("menus.ban.material"))) errors.add("menus.ban.material");
		if (!utils.isMaterial(getConfig().getString("menus.mail.material"))) errors.add("menus.mail.material");
		if (!utils.isMaterial(getConfig().getString("menus.ban.material"))) errors.add("menus.ban.material");

		if (errors.size() == 0) {
			log.info("[" + pdfFile.getName() + "]"+ " Materials checked, they are correct.");
		} else {
			String error = "";
			for (String str : errors) error=error+str+", ";
			log.severe("[" + pdfFile.getName() + "]"+" The materials: " +error+ "aren't correct, stopping the plugin.");
			OnlinePlayersGUIMain.shouldLoad = false;
			Bukkit.getPluginManager().disablePlugin(pl);
			return false;
		}
		
		return result;
	}	
	public boolean checkTitles() {
		boolean result = true;
		ArrayList<String> errors = new ArrayList<String>();
		if (getConfig().getString("guis.main.title").length() > 14) errors.add("guis.main.title");
		if (getConfig().getString("guis.homes.title").length() > 14) errors.add("guis.homes.title");
		if (getConfig().getString("guis.jails.title").length() > 14) errors.add("guis.jails.title");
		if (getConfig().getString("guis.more.title").length() > 14) errors.add("guis.more.title");
		
		if (errors.size() == 0) {
			log.info("[" + pdfFile.getName() + "]"+ " Guis titles checked, they are correct.");
		} else {
			String error = "";
			for (String str : errors) error=error+str+", ";
			log.severe("[" + pdfFile.getName() + "]"+" The guis titles: " +error+ "aren't correct, stopping the plugin.");
			OnlinePlayersGUIMain.shouldLoad = false;
			Bukkit.getPluginManager().disablePlugin(pl);
			return false;
		}
		
		return result;
	}
}