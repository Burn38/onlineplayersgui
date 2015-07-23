package my.burn38.onlineplayersgui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import net.ess3.api.InvalidWorldException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.WarpNotFoundException;

import my.burn38.onlineplayersgui.runnables.MessageEntryCountdown;

public class EventsManager implements Listener {
	
	Plugin pl = OnlinePlayersGUIMain.getPlugin();
	ApiManager apiManager;
	GuiManager guiManager;
	Essentials ess;
	
	Utils utils;
	Logger log = OnlinePlayersGUIMain.getPLLogger();

	@SuppressWarnings("deprecation")
	 @EventHandler(priority=EventPriority.NORMAL)
	  public void onClick(InventoryClickEvent e) throws Exception {
	    Player player = (Player)e.getWhoClicked();
	    if ((e.getInventory() != null) && 
	      (e.getCurrentItem() != null ? (e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR) : false) && (e.getInventory().getName().equals(guiManager.guiName + " [" + Integer.toString(Bukkit.getOnlinePlayers().size()) + "]")))
	    {
	      e.setCancelled(true);
	      player.updateInventory();
	      Player clicked = Bukkit.getPlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
	      if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.menus_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.menus_click_sound")), 1.0F, 10);
	      if (e.isRightClick()) {
	        if (hasPermission(player, "menus.tp_player") && clicked != null) {
	        	double x = clicked.getLocation().getX();
	            double y = clicked.getLocation().getY();
	            double z = clicked.getLocation().getZ();
	            float yaw = clicked.getLocation().getYaw();
	            float pitch = clicked.getLocation().getPitch();
	            World world = clicked.getLocation().getWorld();
	            Location loc = new Location(world, x, y, z, yaw, pitch);
	            if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.tp_player.tp_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.tp_player.tp_sound")), 1F, 10);
	        	player.teleport(loc);
	        	if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.tp_player.tp_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.tp_player.tp_sound")), 1F, 10);
	        	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.tp_player.successful").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	        }
	      } else if (e.isLeftClick()) {
	    	  if (hasPermission(player, "guis.more"))
	          	guiManager.More(clicked, player);
	      }
	     } else if ((e.getInventory() != null) && 
	    	      (e.getCurrentItem() != null ? (e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR) : false) && (e.getInventory().getName().startsWith(guiManager.moreName))) {
	    	 e.setCancelled(true);
	    	 player.updateInventory();
	    	 if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.menus_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.menus_click_sound")), 1.0F, 10);
	    	 Material itemMaterial = e.getCurrentItem().getType();
	    	 Player clicked = Bukkit.getPlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
	    	 if (itemMaterial == utils.getMaterial(utils.getMessage("menus.openEnderchest.material"))) {
	    		 if (hasPermission(player, "menus.openEnderchest")) {
	    	 		player.performCommand("enderchest "+clicked.getName());
	    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.openEnderchest.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.openEnderChest.on_click_sound")), 1F, 10);
	            	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.openEnderchest.successful").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	    		 }
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.openInv.material"))) {
	    	 	  if (hasPermission(player, "menus.openInv")) {
	    	 		player.performCommand("invsee "+clicked.getName());
	    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.openInv.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.openInv.on_click_sound")), 1F, 10);
	            	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.openInv.successful").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	    	 	  }
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.repair.material"))) {
	    	 	  if (hasPermission(player, "menus.repair")) {
	    	 		int count = 0;
	 				for (int i = 0; i < clicked.getInventory().getContents().length; i++) {
	 					if (utils.isDamageable(clicked.getInventory().getContents()[i])) {
	 						clicked.getInventory().getContents()[i].setDurability((short)0);
	 						count++;
	 					}
	 				}
	 				for (int i = 0; i < clicked.getInventory().getArmorContents().length; i++) {
	 					if (utils.isDamageable(clicked.getInventory().getArmorContents()[i])) {
	 						clicked.getInventory().getArmorContents()[i].setDurability((short)0);
	 						count++;
	 					}
	 				}
	 				if (count == 0) player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.repair.errors.noDamageables").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	 				else player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.repair.successful").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));;
	 				if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.repair.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.repair.on_click_sound")), 1F, 10);
	    	 	  }
	            	
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.kick.material"))) {
	    	 	  if (hasPermission(player, "menus.kick")) {
	    	 		if (!clicked.hasPermission(utils.getPermission("menus.kick")+".exempt")) clicked.kickPlayer(utils.getMessage("menus.kick.successful_target").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.kick.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.kick.on_click_sound")), 1F, 10);
	    	 		if (!clicked.hasPermission(utils.getPermission("menus.kick")+".exempt"))player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.kick.successful_sender").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	    	 		else player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.kick.errors.exempted"));
	    	 	  }
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.ban.material"))) {
	    	 	  if (hasPermission(player, "menus.ban")) {
	    	 		if (clicked != null) {
		    	 		 if (e.getClick().isLeftClick()) {
		    	 			if (clicked.isBanned()) {
		    	 				player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.errors.alreadyBanned").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    	 				return;
		    	 			}
			    	 		if (!clicked.hasPermission(utils.getPermission("menus.ban")+".exempt")) {
			    	 			clicked.setBanned(true);
			    	 			clicked.kickPlayer(utils.getMessage("menus.ban.successful_target").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			    	 			if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.ban.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.ban.on_click_sound")), 1F, 10);
			    	 			player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.successful_sender").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			    	 		} else player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.errors.exempted"));
		    	 		
		    	 		 } else if (e.getClick().isRightClick()){
		    	 			if (clicked.isBanned()) {
		    	 				clicked.setBanned(false);
		    	 				if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.ban.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.ban.on_click_sound")), 1F, 10);
				            	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.successful_unban").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    	 			} else {
		    	 				player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.errors.notBanned").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    	 			}
		    	 		 }
	    	 		} else {
	    	 			OfflinePlayer Offclicked = Bukkit.getOfflinePlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
	    	 		 if (e.getClick().isLeftClick()) {
	    	 			if (Offclicked.isBanned()) {
	    	 				player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.errors.alreadyBanned").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()));
	    	 				return;
	    	 			}
	    	 			if (!Offclicked.isOp()){
	    	 				Offclicked.setBanned(true);
	    	 				if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.ban.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.ban.on_click_sound")), 1F, 10);
	    	 				player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.successful_sender").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()));
	    	 		 	} else player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.errors.exempted"));
	    	 		 } else if (e.getClick().isRightClick()) {
	    	 			if (Offclicked.isBanned()) {
	    	 				Offclicked.setBanned(false);
	    	 				if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.ban.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.ban.on_click_sound")), 1F, 10);
			            	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.successful_unban").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()));
	    	 			} else {
	    	 				player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.ban.errors.notBanned").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()));
	    	 			}
	    	 		 }
	    	 		}
	    	 	  }
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.mute.mute.material")) || itemMaterial == utils.getMaterial(utils.getMessage("menus.mute.unmute.material"))) {
	    	 		
	    	 		if (!ess.getUser(clicked).isMuted()) {
	    	 		  if (hasPermission(player, "menus.mute.mute"))  {
	    	 			if (clicked.hasPermission(utils.getPermission("menus.mute.mute")+".exempt")) {
	    	 			  ess.getUser(clicked).setMuted(true);
	    	 			  if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.mute.mute.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.mute.mute.on_click_sound")), 1F, 10);
	    	 			  player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.mute.mute.successful").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	    	 			} else player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.mute.mute.errors.exempted"));
	    	 		  }
	    	 		} else {
	    	 		  if (hasPermission(player, "menus.mute.unmute"))  {
			    	 	if (clicked.hasPermission(utils.getPermission("menus.mute.unmute")+".exempt")) {
		    	 			ess.getUser(clicked).setMuted(false);
		    	 			if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.mute.unmute.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.mute.unmute.on_click_sound")), 1F, 10);
		                	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.mute.unmute.successful").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			    	 	} else player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.mute.unmute.errors.exempted"));
	    	 		  }
	    	 		}
	    	 		guiManager.More(clicked, player);
	    	 		
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.jails.cell.material"))) {
	    	 		if (clicked != null) {
	    	 			guiManager.Jails(clicked, player);
	    	 		} else {
	    	 			OfflinePlayer Offclicked = Bukkit.getOfflinePlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
	    	 			guiManager.Jails(Offclicked, player);
	    	 		}
	    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.menus_click_sound")).equalsIgnoreCase("NONE")) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.menus_click_sound")), 1F, 10);
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.homes.material"))) {
	    	 		if (clicked != null) {
	    	 			guiManager.Homes(clicked, player);
	    	 		} else {
	    	 			OfflinePlayer Offclicked = Bukkit.getOfflinePlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
	    	 			guiManager.Homes(Offclicked, player);
	    	 		}
	    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.menus_click_sound")).equalsIgnoreCase("NONE")) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.menus_click_sound")), 1F, 10);
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.tp_lastpos.material"))) {
	    	 		if (utils.hasPermission(player, "menus.tp_lastpos")) {
		    	 		OfflinePlayer Offclicked = Bukkit.getOfflinePlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
		    	 		User Offuser = ess.getOfflineUser(Offclicked.getName());
		    	 		if (Offuser == null) return;
		    	 		player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.tp_lastpos.successful").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()));
		    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.tp_lastpos.tp_sound")).equalsIgnoreCase("NONE")) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.tp_lastpos.tp_sound")), 1F, 10);
		    	 		player.teleport(Offuser.getLogoutLocation());
		    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.tp_lastpos.tp_sound")).equalsIgnoreCase("NONE")) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.tp_lastpos.tp_sound")), 1F, 10);
	    	 		}
	    	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.mail.material"))) {
	    	 		if (utils.hasPermission(player, "menus.mail")) {
		    	 		OfflinePlayer Offclicked = Bukkit.getOfflinePlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
		    	 			User Offuser = ess.getOfflineUser(Offclicked.getName());
		    	 				if (Offuser == null) return;
		    	 					player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.mail.send").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()));
		    	 						addWaiting(player, "mail:"+Offclicked.getName());
	    	 		}
	    	 	} else if (itemMaterial == Material.SKULL_ITEM || itemMaterial == Material.SKULL) {
	    	 		guiManager.Online(player);
	    	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.back.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.back.on_click_sound")), 1F, 10);
	    	 	}
	    	 } else if ((e.getInventory() != null) && 
	       	      (e.getCurrentItem() != null ? (e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR) : false) && (e.getInventory().getName().startsWith(guiManager.jailsName))) {
	        	 e.setCancelled(true);
	        	 Player clicked = Bukkit.getPlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
	        	 if (clicked != null) {
	        	 player.updateInventory();
	        	 if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.menus_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.menus_click_sound")), 1.0F, 10);
	        	 Material itemMaterial = e.getCurrentItem().getType();
	        	 User user = ess.getUser(clicked);
	        	 if (itemMaterial == utils.getMaterial(utils.getMessage("menus.jails.cell.material"))) {
	        		 if (utils.hasPermission(player, "menus.jails.cell")) {
	        		 	if (!user.isJailed()) {
	        		 		String jail = utils.getEssLocNameFromString("jail", e.getCurrentItem().getItemMeta().getDisplayName(), null);
	        		 		if (ess.getJails().getJail(jail) != null) {
		        		 		pl.getConfig().set("players."+clicked.getUniqueId().toString()+".posBeforeJail", utils.getLocStr(clicked.getLocation()));
		        		 		 pl.saveConfig();
		        		 		ess.getJails().sendToJail(user, jail);
		        		 		user.setJailed(true);
		        		 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.jails.cell.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.jails.cell.on_click_sound")), 1F, 10);
		        	 			player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.cell.successful_sender").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
		        	 			if (utils.wantsSound(clicked) && !(pl.getConfig().getString("menus.jails.cell.on_click_sound").equalsIgnoreCase("NONE"))) clicked.playSound(clicked.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.jails.cell.on_click_sound")), 1F, 10);
		        	 			clicked.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.cell.successful_target").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
	        		 		} else {
	        		 			player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.cell.errors.doesntExist").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
	        		 		}
	        		 	} else {
	        		 		String jail = utils.getEssLocNameFromString("jail", e.getCurrentItem().getItemMeta().getDisplayName(), null);
	        		 		player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.cell.errors.alreadyJailed").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
	        		 	}
	        		 }
	        	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.jails.make_free.material"))) {
	        	 	  if (utils.hasPermission(player, "menus.jail.make_free")) {
	        	 		if (user.isJailed()) {
	        	 			user.setJailTimeout((long) 0);
	        		 		user.setJailed(false);
	        		 		String jail = utils.getEssLocNameFromString("jail", e.getCurrentItem().getItemMeta().getDisplayName(), null);
	        		 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.jails.make_free.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.jails.make_free.on_click_sound")), 1F, 10);
	        		 		player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.make_free.successful_sender").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
	        		 		if (utils.wantsSound(clicked) && !(pl.getConfig().getString("menus.jails.make_free.on_click_sound").equalsIgnoreCase("NONE"))) clicked.playSound(clicked.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.jails.make_free.on_click_sound")), 1F, 10);
	        		 		clicked.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.make_free.successful_target").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
	        		 		clicked.teleport(utils.getStrLoc(pl.getConfig().getString("players."+clicked.getUniqueId().toString()+".posBeforeJail")));
	        		 		pl.getConfig().set("players."+clicked.getUniqueId().toString()+".posBeforeJail", null);
	        		 		 pl.saveConfig();
	        		 	} else {
	        		 		player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.make_free.errors.notJailed").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
	        		 	}
	        	 	  }
	        	 	} else if (itemMaterial == Material.SKULL_ITEM || itemMaterial == Material.SKULL) {
	        	 		guiManager.More(clicked, player);
	        	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.back.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.back.on_click_sound")), 1F, 10);
	        	 	}
	        	 } else {
	        		 OfflinePlayer Offclicked = Bukkit.getOfflinePlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
		        	 player.updateInventory();
		        	 if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.menus_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.menus_click_sound")), 1.0F, 10);
		        	 Material itemMaterial = e.getCurrentItem().getType();
		        	 User user = ess.getOfflineUser(Offclicked.getName());
		        	 if (itemMaterial == utils.getMaterial(utils.getMessage("menus.jails.cell.material"))) {
		        		 if (utils.hasPermission(player, "menus.jails.cell")) {
		        		 	if (!user.isJailed()) {
		        		 		String jail = utils.getEssLocNameFromString("jail", e.getCurrentItem().getItemMeta().getDisplayName(), null);
		        		 		if (ess.getJails().getJail(jail) != null) {
			        		 		pl.getConfig().set("players."+Offclicked.getUniqueId().toString()+".posBeforeJail", utils.getLocStr(user.getLastLocation()));
			        		 		 pl.saveConfig();
			        		 		ess.getJails().sendToJail(user, jail);
			        		 		user.setJailed(true);
			        		 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.jails.cell.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.jails.cell.on_click_sound")), 1F, 10);
			        	 			player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.cell.successful_sender").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
		        		 		} else {
		        		 			player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.cell.errors.doesntExist").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
		        		 		}
		        		 	} else {
		        		 		String jail = utils.getEssLocNameFromString("jail", e.getCurrentItem().getItemMeta().getDisplayName(), null);
		        		 		player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.cell.errors.alreadyJailed").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
		        		 	}
		        		 }
		        	 	} else if (itemMaterial == utils.getMaterial(utils.getMessage("menus.jails.make_free.material"))) {
		        	 	  if (utils.hasPermission(player, "menus.jails.make_free")) {
		        	 		if (user.isJailed()) {
		        	 			user.setJailTimeout((long) 0);
		        		 		user.setJailed(false);
		        		 		String jail = utils.getEssLocNameFromString("jail", e.getCurrentItem().getItemMeta().getDisplayName(), null);
		        		 		if (utils.wantsSound(player) && (!pl.getConfig().getString("menus.jails.make_free.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.jails.make_free.on_click_sound")), 1F, 10);
		        		 		player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.make_free.successful_sender").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jail%", jail));
		        		 		user.setLogoutLocation(utils.getStrLoc(pl.getConfig().getString("players."+Offclicked.getUniqueId().toString()+".posBeforeJail")));
		        		 		pl.getConfig().set("players."+Offclicked.getUniqueId().toString()+".posBeforeJail", null);
		        		 		 pl.saveConfig();
		        		 	} else {
		        		 		player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.jails.make_free.errors.notJailed").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()));
		        		 	}
		        	 	  }
		        	 	} else if (itemMaterial == Material.SKULL_ITEM || itemMaterial == Material.SKULL) {
		        	 		guiManager.More(Offclicked, player);
		        	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.back.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.back.on_click_sound")), 1F, 10);
		        	 	}
	        	 }
	        	 } else if ((e.getInventory() != null) && 
	              	      (e.getCurrentItem() != null ? (e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR) : false) && (e.getInventory().getName().startsWith(guiManager.homesName))) {
	            	 e.setCancelled(true);
	            	 if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.menus_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.menus_click_sound")), 1.0F, 10);
	            	 player.updateInventory();
	            	 Material itemMaterial = e.getCurrentItem().getType();
	            	 Player clicked = Bukkit.getPlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
	            if (clicked != null) {	 
	            	 if (itemMaterial == utils.getMaterial(utils.getMessage("menus.homes.material"))) {
	            	  if (utils.hasPermission(player, "menus.homes.home")) {
	            		 String home = utils.getEssLocNameFromString("home", e.getCurrentItem().getItemMeta().getDisplayName(), clicked);
	            		 		if (ess.getUser(clicked).getHome(home) != null) {
	            		 			if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.homes.home.tp_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("mesnus.homes.home.tp_sound")), 1F, 10);
	            		        	player.teleport(ess.getUser(clicked).getHome(home));
	            		        	if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.homes.home.tp_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("mesnus.homes.home.tp_sound")), 1F, 10);
	            		        	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.homes.home.tp_sound").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%home%", home));
	            		 		} else {
	            		 			player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.homes.home.errors.doesntExist").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%home%", home));
	            		 		}
	            	  }
	            	} else if (itemMaterial == Material.SKULL_ITEM || itemMaterial == Material.SKULL) {
	            		guiManager.More(clicked, player);
	            		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.back.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.back.on_click_sound")), 1F, 10);
	            	}
	        	 } else {
	        	  if (utils.hasPermission(player, "menus.homes.home")) {
	        		 OfflinePlayer Offclicked = Bukkit.getOfflinePlayer(((SkullMeta)guiManager.hud.getContents()[guiManager.hud.getSize()-1].getItemMeta()).getOwner());
	        		 if (itemMaterial == utils.getMaterial(utils.getMessage("menus.homes.material"))) {
	            		 String home = utils.getEssLocNameFromString("home", e.getCurrentItem().getItemMeta().getDisplayName(), Offclicked);
	            		 User user = ess.getOfflineUser(Offclicked.getName());
	            		 		if (user.getHome(home) != null) {
	            		 			if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.homes.home.tp_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.homes.home.tp_sound")), 1F, 10);
	            		        	player.teleport(user.getHome(home));
	            		        	if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.homes.home.tp_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.homes.home.tp_sound")), 1F, 10);
	            		        	player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.homes.home.successful").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%home%", home));
	            		 		} else {
	            		 			player.sendMessage(utils.getPluginTag() + utils.getMessage("menus.homes.home.errors.doesntExist").replaceAll("%target%", Offclicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%home%", home));
	            		 		}
	            	 	} else if (itemMaterial == Material.SKULL_ITEM || itemMaterial == Material.SKULL) {
	            	 		guiManager.More(Offclicked, player);
	            	 		if (utils.wantsSound(player) && !(pl.getConfig().getString("menus.back.on_click_sound").equalsIgnoreCase("NONE"))) player.playSound(player.getLocation(), Sound.valueOf(pl.getConfig().getString("menus.back.on_click_sound")), 1F, 10);
	            	 	}
	        	  }
	        	 }
	            	 } 
	} 
	
	@EventHandler(priority=EventPriority.NORMAL)
	  public void onJoin(PlayerJoinEvent e) {
		  if (ess != null ) {
			if (ess.getUser(e.getPlayer()) != null) {
				if (ess.getUser(e.getPlayer()).isAfk()) ess.getUser(e.getPlayer()).toggleAfk();
			} else log.severe(utils.getPluginTag()+" Can't get "+e.getPlayer().getName()+"'s ("+e.getPlayer().getUniqueId()+") Essentials User. Can't use Essentials-based features with this User.");
		  } else log.severe(utils.getPluginTag()+" Essentials isn't loaded but plugin is working, is it disabled ? Many features will not work, this can cause many console errors and may probably make the server crash. Re-Enable it the ASAP.");
		
			  if (pl.getConfig().getString("players."+e.getPlayer().getUniqueId()+".first") == null) {
			  Player[] onp = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
			  for (int i = 0; i < onp.length; i++) {
				  if (utils.wantsSound(onp[i]) && !(pl.getConfig().getString("plugin.welcome_sound")).equalsIgnoreCase("NONE")) onp[i].playSound(onp[i].getLocation(), Sound.valueOf(pl.getConfig().getString("plugin.welcome_sound")), 1.0F, 10);
			  }
			  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			  Date date = new Date();
			  pl.getConfig().set("players."+e.getPlayer().getUniqueId().toString()+".first", dateFormat.format(date));
			  pl.getConfig().set("players."+e.getPlayer().getUniqueId().toString()+".last", dateFormat.format(date));
			  	pl.saveConfig();
		  } else {
			  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			  Date date = new Date();
			  pl.getConfig().set("players."+e.getPlayer().getUniqueId().toString()+".last", dateFormat.format(date));
			  	pl.saveConfig();
		  }
	  }
	  @EventHandler(priority=EventPriority.NORMAL)
	  public void onPreChat(AsyncPlayerChatEvent e) {
		  if (!e.getMessage().startsWith("/")) {
			  String message = e.getMessage();
			  HashMap<Player, Integer> waiting = MessageEntryCountdown.getWaiting();
			  if (waiting.containsKey(e.getPlayer()) && waiting.get(e.getPlayer()) >= 1) {
				  System.out.println(e.getPlayer().getName() + " is waiting");
				  String toDo = MessageEntryCountdown.waitingFor.get(e.getPlayer());
				  waiting.remove(e.getPlayer());
				  	MessageEntryCountdown.setWaiting(waiting);
				  		MessageEntryCountdown.waitingFor.remove(e.getPlayer());
				  if (toDo.startsWith("mail:")) {
					  String name = toDo.substring(5,toDo.length());
					  User receiver = ess.getOfflineUser(name); 
					  receiver.addMail(message);
					  e.getPlayer().sendMessage(utils.getPluginTag() + utils.getMessage("menus.mail.sent").replaceAll("%target%", name).replaceAll("%sender%", e.getPlayer().getName()));
					  if (utils.wantsSound(e.getPlayer()) && !(pl.getConfig().getString("menus.mail.on_mail_sent_sound").equalsIgnoreCase("NONE"))) e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(pl.getConfig().getString("menus.mail.on_mail_sent_sound")), 1F, 10);
				  }
				  e.setCancelled(true);
			  }
		  }
	  }
	  
	  @EventHandler(priority=EventPriority.HIGHEST)
	  public void onPreCommand(PlayerCommandPreprocessEvent e) {
		  String cmd = e.getMessage().substring(1, e.getMessage().length()).toLowerCase();
		  ArrayList<String> warps = new ArrayList<String>();
		  for (String str : ess.getWarps().getList()) warps.add(str.toLowerCase());
		  if (OnlinePlayersGUIMain.getAllowedWarps().contains(cmd) && ess.getCommand(cmd) == null && warps.contains(cmd)) {
			  try {
				if (utils.wantsSound(e.getPlayer()) && !(pl.getConfig().getString("plugin.commands.short_warps_tp_sound")).equalsIgnoreCase("NONE")) e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(pl.getConfig().getString("plugin.short_warps_tp_sound")), 1.0F, 10);
				e.getPlayer().teleport(ess.getWarps().getWarp(cmd));
				if (utils.wantsSound(e.getPlayer()) && !(pl.getConfig().getString("plugin.commands.short_warp_tp_sound")).equalsIgnoreCase("NONE")) e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(pl.getConfig().getString("plugin.short_warps_tp_sound")), 1.0F, 10);
			} catch (WarpNotFoundException e1) {
				e.getPlayer().sendMessage(utils.getPluginTag() + utils.getMessage("plugin.messages.errors.short_warps.warpNotExists").replaceAll("%warp%", cmd));
			} catch (InvalidWorldException e1) {
				e.getPlayer().sendMessage(utils.getPluginTag() + utils.getMessage("plugin.messages.errors.short_warps.warpWorldNotExists").replaceAll("%warp%", cmd));
			}
			  
		      HelpTopic htopic = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);
		      if (htopic == null)
		      {
		        e.setCancelled(true);
		      }
		  }
		  
	  }
	
	  public void addWaiting(Player p, String str) {
		  MessageEntryCountdown.waitingEntry.put(p,60);
		  	MessageEntryCountdown.waitingFor.put(p,str);
		  	if (MessageEntryCountdown.getID() == 0) {
		  		MessageEntryCountdown.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, OnlinePlayersGUIMain.getMessageCountdown(), 0L, 20L);
		  	}
	  }
	  
	  public static void stopCountdown() {
		  Bukkit.getScheduler().cancelTask(MessageEntryCountdown.id);
		  	MessageEntryCountdown.id = 0;
	  }
	  
	  public boolean hasPermission(Player player, String string) {
		  return utils.hasPermission(player, string);
	  }
}
