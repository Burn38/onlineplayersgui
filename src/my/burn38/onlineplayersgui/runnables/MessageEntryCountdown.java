package my.burn38.onlineplayersgui.runnables;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import my.burn38.onlineplayersgui.EventsManager;
import my.burn38.onlineplayersgui.OnlinePlayersGUIMain;
import my.burn38.onlineplayersgui.Utils;

public class MessageEntryCountdown implements Runnable {

	Plugin pl = OnlinePlayersGUIMain.getPlugin();
	public Utils utils;
	public static int id = 0;
	public static HashMap<Player, Integer> waitingEntry = new HashMap<Player, Integer>();
	public static HashMap<Player, String> waitingFor = new HashMap<Player, String>();
	
	@Override
	public void run() {
		if (waitingEntry.size() != 0) {
			for (Player player : waitingEntry.keySet()) {
				if (waitingEntry.get(player) <= 0) {
					waitingEntry.remove(player);
						waitingFor.remove(player);
							player.sendMessage(utils.getPluginTag() + ChatColor.RED + utils.getMessage("menus.mail.errors.tookTooMuchTime"));
				} else {
					waitingEntry.put(player, waitingEntry.get(player)-1);
				}
			}
		} else {
			EventsManager.stopCountdown();
		}
	}

	public static int getID() {
		return id;
	}
	  
	  public static HashMap<Player, Integer> getWaiting() {
		  return MessageEntryCountdown.waitingEntry;
	  }
	  
	  public static void setWaiting(HashMap<Player, Integer> l) {
		  MessageEntryCountdown.waitingEntry = l;
	  }

}
