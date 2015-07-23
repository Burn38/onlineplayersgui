package my.burn38.onlineplayersgui.threads;

import java.io.FileNotFoundException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import my.burn38.onlineplayersgui.ApiManager;
import my.burn38.onlineplayersgui.GuiManager;
import my.burn38.onlineplayersgui.OnlinePlayersGUIMain;
import my.burn38.onlineplayersgui.Utils;

public class ConvertThread extends Thread {

	Plugin pl = OnlinePlayersGUIMain.getPlugin();
	public Utils utils;
	public GuiManager guiManager;
	public ApiManager apiManager;
	
	@Override
	public void run() {
		convert();
	}
	
	@SuppressWarnings("deprecation")
	public void convert() {
		try {
			apiManager.loadConfiguration();
		} catch (FileNotFoundException e) {
			pl.getLogger().severe("[onlineplayersgui] Can't load / copy the configuration !");
			e.printStackTrace();
		}
		String names[] = pl.getConfig().getConfigurationSection("players").getKeys(false).toArray(new String[pl.getConfig().getConfigurationSection("players").getKeys(false).size()]);
		
		for (String name : names) {
			if (name.length() <= 16) {
				UUID uuid;
				if (Bukkit.getPlayer(name) != null) uuid = Bukkit.getPlayer(name).getUniqueId();
				else uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
				utils.moveYamlPath("players."+name, "players."+uuid.toString());
			}
		}
		
		pl.saveConfig();
			pl.reloadConfig();
			
			OnlinePlayersGUIMain.getConvertThread().stop();
	}
	
}
