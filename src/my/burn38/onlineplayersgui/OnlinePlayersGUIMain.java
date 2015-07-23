package my.burn38.onlineplayersgui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import my.burn38.onlineplayersgui.runnables.MessageEntryCountdown;
import my.burn38.onlineplayersgui.threads.ConvertThread;

public class OnlinePlayersGUIMain extends JavaPlugin
{
  private static Plugin plugin;
  static Utils utils;
  static GuiManager guiManager;
  static ApiManager apiManager;
  static EventsManager eventsManager;
  static ConvertThread convertThread;
  static MessageEntryCountdown messageCountdown;
  static Logger log;
  static boolean shouldLoad = true;
  
  public void onEnable() {
   plugin = (Plugin)this;
    
   log = Logger.getLogger("Minecraft");
   
   	apiManager = new ApiManager();
   	
   	
    apiManager.loadManagers();  	     		
    		try {
				apiManager.loadConfiguration();
			} catch (FileNotFoundException e) {
				getLogger().severe("[onlineplayersgui] Can't"+(new File(getDataFolder(), "config.yml").exists() ? "" : " write and")+" load the configuration !");
				e.printStackTrace();
			}
    
	  if (shouldLoad) {
	    	apiManager.loadDependencies();
	    		apiManager.loadAddons();
	    		
	    apiManager.loadCommands();
	    
	    Bukkit.getServer().getPluginManager().registerEvents(eventsManager, this);
	       
	    
	    OnlinePlayersGUIMain.log.info("[" + utils.pdfFile.getName() + "]"+ " Plugin started on version " + utils.pdfFile.getVersion() + " !");
	  }
  }
  public void onDisable() {
    PluginDescriptionFile pdfFile = getDescription();
    if (MessageEntryCountdown.id != 0) EventsManager.stopCountdown();
	 plugin = null;
    OnlinePlayersGUIMain.log.info("[" + pdfFile.getName() + "] Plugin stopped !");
  }

  public static Plugin getPlugin() {
	  return plugin;
  }

  public static Utils getUtils() {
	  if (utils == null) utils = new Utils();
	  return utils;
  }
  public static GuiManager getGuiManager() {
	  if (guiManager == null) guiManager = new GuiManager();
	  return guiManager;
  }
  public static ApiManager getApiManager() {
	  if (apiManager == null) apiManager = new ApiManager();
	  return apiManager;
  }
  public static EventsManager getEventsManager() {
	  if (eventsManager == null) eventsManager = new EventsManager();
	  return eventsManager;
  }
  public static ConvertThread getConvertThread() {
	  if (convertThread == null) convertThread = new ConvertThread();
	  return convertThread;
  }
  public static MessageEntryCountdown getMessageCountdown() {
	  if (messageCountdown == null) messageCountdown = new MessageEntryCountdown();
	  return messageCountdown;
  }
  public static List<String> getAllowedWarps() {
	  List<?> list = plugin.getConfig().getList("plugin.commands.short_warps");
	  List<String> Slist = new ArrayList<String>();
	  
	  for (Object obj : list) {
		  Slist.add(obj.toString().toLowerCase());
	  }
	  
	return Slist;
  }
  
  public static Logger getPLLogger() {
	  return Logger.getLogger("Minecraft");
  }
}