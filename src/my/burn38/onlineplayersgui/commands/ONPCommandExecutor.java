package my.burn38.onlineplayersgui.commands;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import my.burn38.onlineplayersgui.ApiManager;
import my.burn38.onlineplayersgui.GuiManager;
import my.burn38.onlineplayersgui.OnlinePlayersGUIMain;
import my.burn38.onlineplayersgui.Utils;

public class ONPCommandExecutor implements CommandExecutor {

	Plugin pl = OnlinePlayersGUIMain.getPlugin();
	public Utils utils;
	public GuiManager guiManager;
	public ApiManager apiManager;
	
	@Override
	  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		    if (commandLabel.equalsIgnoreCase("onp")) {
		    	if (sender instanceof Player) {
			      if (args.length == 1) {
			        String cmd = args[0];
			        Player p = (Player)sender;
			        if (cmd.equalsIgnoreCase("help")) {
			          help(p);
			          return true;
			        } else if (cmd.equalsIgnoreCase("togglesound")) {
			        	togglesound(p);
			        	return true;
			        } else if (cmd.equalsIgnoreCase("show")) {
			          show(p);
			          return true;
			        } else if (cmd.equalsIgnoreCase("reload")) {
			          reload(p);
			          return true;
			        } else if (cmd.equalsIgnoreCase("convert")) {
				        	if (!OnlinePlayersGUIMain.getConvertThread().isAlive()) {
				        		convert(p);
				        	} else {
				        		p.sendMessage(utils.getPluginTag() + utils.getMessage("plugin.messages.errors.unknown"));
				        	}
			        	return true;
			        } else if (utils.isPlayer(cmd)) {
			        	showPlayer(p, cmd);
			        	return true;
			        }
			      }
			    } else {
			    	OnlinePlayersGUIMain.getPLLogger().info((ChatColor.stripColor(utils.getPluginTag())+ utils.getMessage("plugin.messages.errors.commandBlockOrConsoleCommand")));
			    	return true;
			    }
				return false;
		    }
			return false;
		  }

	  public void show(Player p) {
		  if (utils.hasPermission(p, "plugin.commands.onp_show"))
	      {
	        guiManager.Online(p);
	      }
	}
	  public void help(Player p) {
		  if (utils.hasPermission(p, "plugin.commands.onp_help")) {
			  List<?> custom_help_list = pl.getConfig().getList("plugin.messages.help.custom_display");
			  if (custom_help_list != null && custom_help_list.size() > 0) {
				String[] custom_help = custom_help_list.toArray(new String[custom_help_list.size()]);
				for (String data : custom_help) {
					if (pl.getConfig().get("plugin.messages.help."+data) != null) {
						if (pl.getConfig().get("plugin.messages.help."+data) instanceof String) {
							p.sendMessage(utils.getMessage("plugin.messages.help."+data));
						}
					}
				}
			} else {
				p.sendMessage(utils.getMessage("plugin.messages.help.separator"));
		        p.sendMessage(ChatColor.BLUE + utils.getMessage("plugin.messages.help.title"));
		        p.sendMessage(utils.getMessage("plugin.messages.help.separator"));
		        p.sendMessage(ChatColor.GREEN + utils.getMessage("plugin.messages.help.cmd_help"));
		        p.sendMessage(ChatColor.GREEN + utils.getMessage("plugin.messages.help.cmd_show"));
		        p.sendMessage(ChatColor.GREEN + utils.getMessage("plugin.messages.help.cmd_player"));
		        p.sendMessage(ChatColor.GREEN + utils.getMessage("plugin.messages.help.cmd_togglesound"));
		        p.sendMessage(ChatColor.GREEN + utils.getMessage("plugin.messages.help.cmd_reload"));
		        p.sendMessage(utils.getMessage("plugin.messages.help.separator"));
			}
	      }
	  }
	  public void reload(Player p) {
		  if (p == null) {
			  apiManager.reloadConfiguration();
			  return;
		  }
		  if (utils.hasPermission(p, "plugin.commands.onp_reload")) {
			  apiManager.reloadConfiguration();
			  try {
				apiManager.loadConfiguration();
			} catch (FileNotFoundException e) {
				pl.getLogger().severe("[onlineplayersgui] Can't load / copy the configuration !");
				e.printStackTrace();
			}
			  p.sendMessage(utils.getPluginTag() + utils.getMessage("plugin.messages.infos.reload").replaceAll("%sender%", p.getName()));
		  }
	  }
	  public void convert(Player p) {
		  if (utils.hasPermission(p, "plugin.commands.onp_convert")) {
		  	OnlinePlayersGUIMain.getConvertThread().start();
		  	reload(null);
		  }
	  }
	  public void togglesound(Player p) {
		  if (utils.hasPermission(p, "plugin.commands.onp_togglesound")) {
			String path = "players."+p.getUniqueId().toString()+".sound";
	      	if (utils.getBoolean(path) == null) utils.setBoolean(path, true);
	      	utils.setBoolean(path, !utils.getBoolean(path));
	      	pl.saveConfig();
	      	p.sendMessage(utils.getPluginTag() + utils.getMessage("plugin.messages.infos.toggleSound").replaceAll("%sender%", p.getName()).replaceAll("%bool%", utils.getBoolean(path).toString().toUpperCase()));
		  }
	  }
	  @SuppressWarnings("deprecation")
	  public void showPlayer(Player p, String cmd) {
		if (utils.hasPermission(p, "plugin.commands.onp_player")) {
			  if (utils.isOnline(cmd)) {
	      		Player clicked = Bukkit.getPlayer(cmd) != null ? Bukkit.getPlayer(cmd) : Bukkit.getPlayer(UUID.fromString(cmd));
	      		guiManager.More(clicked, p);
	      	} else if (utils.isOffline(cmd)) {
					OfflinePlayer clicked = Bukkit.getOfflinePlayer(cmd) != null ? Bukkit.getOfflinePlayer(cmd) : Bukkit.getOfflinePlayer(UUID.fromString(cmd));
	      		guiManager.More(clicked, p);
	      	} else {
	      		p.sendMessage(utils.getPluginTag()+ChatColor.RED+" Le joueur \""+cmd+"\" n'existe pas !");
	      	}
		}
	  }
}
