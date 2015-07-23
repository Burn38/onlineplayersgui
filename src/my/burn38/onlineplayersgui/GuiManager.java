package my.burn38.onlineplayersgui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

public class GuiManager {
	
	Plugin pl = OnlinePlayersGUIMain.getPlugin();
	ApiManager apiManager;
	Utils utils;
	String guiName = "", moreName = "", jailsName = "", homesName = "";
	Inventory ghost, hud, jails, homes;
	Essentials ess; Economy vaultAPI; 
	me.armar.plugins.autorank.api.API autorankAPI;
	
	  public void More(Player clicked, Player player) {
		if(utils.hasPermission(player, "guis.more")) {
		  int hudSize = 9;
		  this.hud = Bukkit.createInventory(player, hudSize, this.moreName + " [" + clicked.getName() + "]");
		  
			  ItemStack enderChest = new ItemStack(utils.getMaterial(utils.getMessage("menus.openEnderchest.material")));
			  ItemMeta MenderChest = enderChest.getItemMeta();
			  MenderChest.setDisplayName(utils.getMessage("menus.openEnderchest.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  enderChest.setItemMeta(MenderChest);
			  
			  ItemStack invChest = new ItemStack(utils.getMaterial(utils.getMessage("menus.openInv.material")));
			  ItemMeta MinvChestItem = invChest.getItemMeta();
			  MinvChestItem.setDisplayName(utils.getMessage("menus.openInv.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  invChest.setItemMeta(MinvChestItem);
			  
			  ItemStack repairItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.repair.material")));
			  ItemMeta MrepairItem = repairItem.getItemMeta();
			  MrepairItem.setDisplayName(utils.getMessage("menus.repair.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  repairItem.setItemMeta(MrepairItem);
			  
			  ItemStack muteItem = new ItemStack(ess.getUser(clicked).isMuted() ? utils.getMaterial(utils.getMessage("menus.mute.unmute.material")) : utils.getMaterial(utils.getMessage("menus.mute.mute.material")));
			  ItemMeta MmuteItem = muteItem.getItemMeta();
			  MmuteItem.setDisplayName(ess.getUser(clicked).isMuted() ? utils.getMessage("menus.mute.unmute.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()) : utils.getMessage("menus.mute.mute.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  muteItem.setItemMeta(MmuteItem);
			  
			  ItemStack jailItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.jails.material")));
			  ItemMeta MjailItem = jailItem.getItemMeta();
			  MjailItem.setDisplayName(utils.getMessage("menus.jails.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jailsCount%", Integer.toString(ess.getJails().getCount())));
			  jailItem.setItemMeta(MjailItem);
			  
			  ItemStack homeItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.homes.material")));
			  ItemMeta MhomeItem = homeItem.getItemMeta();
			  MhomeItem.setDisplayName(utils.getMessage("menus.homes.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  homeItem.setItemMeta(MhomeItem);
			  
			  ItemStack kickItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.kick.material")));
			  ItemMeta MkickItem = kickItem.getItemMeta();
			  MkickItem.setDisplayName(utils.getMessage("menus.kick.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  kickItem.setItemMeta(MkickItem);
			  
			  ItemStack banItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.ban.material")));
			  ItemMeta MbanItem = banItem.getItemMeta();
			  MbanItem.setDisplayName(utils.getMessage("menus.ban.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  banItem.setItemMeta(MbanItem);
			  
			  ArrayList<ItemStack> items_list = new ArrayList<ItemStack>();
			  if (player.hasPermission(utils.getPermission("menus.openEnderchest"))) items_list.add(enderChest);
			  if (player.hasPermission(utils.getPermission("menus.openInv"))) items_list.add(invChest);
			  if (player.hasPermission(utils.getPermission("menus.repair"))) items_list.add(repairItem);
			  if (player.hasPermission(utils.getPermission("menus.mute."+(ess.getUser(clicked).isMuted() ? "unmute" : "mute")))) items_list.add(muteItem);
			  if (player.hasPermission(utils.getPermission("guis.jails"))) items_list.add(jailItem);
			  if (player.hasPermission(utils.getPermission("guis.homes"))) items_list.add(homeItem);
			  if (player.hasPermission(utils.getPermission("menus.kick"))) items_list.add(kickItem);
			  if (player.hasPermission(utils.getPermission("menus.ban"))) items_list.add(banItem);
			  ItemStack[] items = items_list.toArray(new ItemStack[items_list.size()]);
			  hud.setContents(items);
		  
		    ItemStack head = new ItemStack(Material.SKULL_ITEM);
		    SkullMeta m = (SkullMeta) head.getItemMeta();
		    head.setDurability((short) 3);
		    m.setDisplayName(utils.getMessage("menus.back.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    List<String> lore = new ArrayList<String>();
		    lore.add("Clique sur la tête pour");
		    lore.add("retourner à la liste des joueurs");
		    m.setLore(lore);
		    m.setOwner(clicked.getName());
		    head.setItemMeta(m);
		    

		    
		    hud.setItem(hud.getSize()-1, head);
		  
		  player.openInventory(this.hud);
		}
		  
	  }
	  public void More(OfflinePlayer clicked, Player player) {
       if(utils.hasPermission(player, "guis.more")) {
		  int hudSize = 9;
		  this.hud = Bukkit.createInventory(player, hudSize, this.moreName + " [" + clicked.getName() + "]");
			  ItemStack jailItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.jails.material")));
			  ItemMeta MjailItem = jailItem.getItemMeta();
			  MjailItem.setDisplayName(utils.getMessage("menus.jails.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()).replaceAll("%jailsCount%", Integer.toString(ess.getJails().getCount())));
			  jailItem.setItemMeta(MjailItem);
			  
			  ItemStack homeItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.homes.material")));
			  ItemMeta MhomeItem = homeItem.getItemMeta();
			  MhomeItem.setDisplayName(utils.getMessage("menus.homes.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  homeItem.setItemMeta(MhomeItem);

			  ItemStack banItem = new ItemStack(utils.getMaterial(utils.getMessage("menus.ban.material")));
			  ItemMeta MbanItem = banItem.getItemMeta();
			  MbanItem.setDisplayName(utils.getMessage("menus.ban.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  banItem.setItemMeta(MbanItem);
			  
			  ItemStack tpLastPos = new ItemStack(utils.getMaterial(utils.getMessage("items.tp.lastpos.material")));
			  ItemMeta MtpLastPos = tpLastPos.getItemMeta();
			  MtpLastPos.setDisplayName(utils.getMessage("items.tp.lastpos.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  tpLastPos.setItemMeta(MtpLastPos);
			  
			  ItemStack mail = new ItemStack(utils.getMaterial(utils.getMessage("menus.mail.material")));
			  ItemMeta Mmail = mail.getItemMeta();
			  Mmail.setDisplayName(utils.getMessage("menus.mail.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
			  mail.setItemMeta(Mmail);
			  
			  ArrayList<ItemStack> items_list = new ArrayList<ItemStack>();
			  if (player.hasPermission(utils.getPermission("menus.tp_lastpos"))) items_list.add(tpLastPos);
			  if (player.hasPermission(utils.getPermission("guis.jails"))) items_list.add(jailItem);
			  if (player.hasPermission(utils.getPermission("guis.homes"))) items_list.add(homeItem);
			  if (player.hasPermission(utils.getPermission("menus.ban"))) items_list.add(banItem);
			  if (player.hasPermission(utils.getPermission("menus.mail"))) items_list.add(mail);
			  ItemStack[] items = items_list.toArray(new ItemStack[items_list.size()]);
			  hud.setContents(items);
		  
		    ItemStack head = new ItemStack(Material.SKULL_ITEM);
		    SkullMeta m = (SkullMeta) head.getItemMeta();
		    head.setDurability((short) 3);
		    m.setDisplayName(utils.getMessage("menus.back.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    List<String> lore = new ArrayList<String>();
		    //TODO: MAKE THIS CUSTOMIZABLE
		    lore.add("Clique sur la tête pour");
		    lore.add("retourner à la liste des joueurs");
		    m.setLore(lore);
		    m.setOwner(clicked.getName());
		    head.setItemMeta(m);
		    
		    hud.setItem(hud.getSize()-1, head);
		    
		  	ItemStack paper = new ItemStack(utils.getMaterial(utils.getMessage("menus.infoItem.material")));
		    ItemMeta Mpaper = paper.getItemMeta();
		    User user = ess.getOfflineUser(clicked.getName());
		    if (user == null) return;
		    List<String> loreP = new ArrayList<String>();
		    loreP.add(ChatColor.DARK_GRAY + "* UUID: " + ChatColor.DARK_BLUE + clicked.getUniqueId());
		      if (user.isJailed() == true) loreP.add(ChatColor.DARK_GRAY + "* Jail: " + (user.isJailed() == true ? ChatColor.GREEN : ChatColor.RED) + Boolean.toString(user.isJailed()).toUpperCase() + ChatColor.AQUA + (user.isJailed() ? " ["+user.getJail().toUpperCase()+"]" : ""));
		      if (user.isMuted() == true) loreP.add(ChatColor.DARK_GRAY + "* Mute: " + (user.isMuted() == true ? ChatColor.GREEN : ChatColor.RED) + Boolean.toString(user.isMuted()).toUpperCase());
		      if (apiManager.isVaultEnabled) loreP.add(ChatColor.DARK_GRAY + "* Argent: " + ChatColor.GOLD + utils.getPlayerMoney(clicked));
		      loreP.add(ChatColor.DARK_GRAY + "* Position: ");
		      loreP.add(ChatColor.GRAY + "   - World: " + ChatColor.AQUA + user.getLogoutLocation().getWorld().getName().toUpperCase());
		      loreP.add(ChatColor.GRAY + "   - X: " + ChatColor.AQUA + user.getLogoutLocation().getBlockX());
		      loreP.add(ChatColor.GRAY + "   - Y: " + ChatColor.AQUA + user.getLogoutLocation().getBlockY());
		      loreP.add(ChatColor.GRAY + "   - Z: " + ChatColor.AQUA + user.getLogoutLocation().getBlockZ());
		      loreP.add(ChatColor.DARK_GRAY + "* Connexions: ");
		      loreP.add(ChatColor.GRAY + "   - Première: " + ChatColor.AQUA + pl.getConfig().getString("players."+clicked.getUniqueId()+".first"));
		      loreP.add(ChatColor.GRAY + "   - Dernière: " + ChatColor.AQUA + pl.getConfig().getString("players."+clicked.getUniqueId()+".last"));
		      if (apiManager.isAutorankEnabled) loreP.add(ChatColor.GRAY + "   - Temps: " + ChatColor.AQUA + utils.getStringTime((autorankAPI.getGlobalPlayTime(clicked.getUniqueId()) == -1 ? autorankAPI.getLocalPlayTime(clicked.getUniqueId()) : autorankAPI.getGlobalPlayTime(clicked.getUniqueId()))));
		    Mpaper.setLore(loreP);
		    Mpaper.setDisplayName(ChatColor.GREEN+utils.getMessage("menus.infoItem.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    paper.setItemMeta(Mpaper);
		    
		    hud.setItem(hud.getSize()-2, paper);
		    
		  player.openInventory(this.hud);
       }
	  }	
	  
	  public void Jails(Player clicked, Player player) {
		if (utils.hasPermission(player, "guis.jails")) {
		  Collection<String> ljails = null;
		  try {
			ljails = ess.getJails().getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  String[] jails = ljails.toArray(new String[ljails.size()]);
		  int hudSize = jails.length;

		  
		  hudSize = utils.getMultiple(hudSize+2, 9);
		  this.hud = Bukkit.createInventory(player, hudSize, this.jailsName + " [" + clicked.getName() + "]");
		  
		  List<ItemStack> items = new ArrayList<ItemStack>();
		  
		  for (int i = 0; i < jails.length; i++) {
			  ItemStack x = new ItemStack(utils.getMaterial(utils.getMessage("menus.jails.cell.material")));
			  ItemMeta xM = x.getItemMeta();
			  xM.setDisplayName(utils.getMessage("menus.jails.cell.title").replaceAll("%sender%", player.getName()).replaceAll("%target%", clicked.getName()).replaceAll("%jail%", jails[i]));
			  List<String> l = new ArrayList<String>();
			//TODO: MAKE THIS CUSTOMIZABLE
			  l.add("Envoyer "+clicked.getName()+" dans la prison "+jails[i]+".");
			  xM.setLore(l);
			  x.setItemMeta(xM);
			  items.add(x);
		  }
		  ItemStack f = new ItemStack(utils.getMaterial(utils.getMessage("menus.jails.make_free.material")));
		  ItemMeta fM = f.getItemMeta();
		  fM.setDisplayName(utils.getMessage("menus.jails.make_free.title").replaceAll("%sender%", player.getName()).replaceAll("%target%", clicked.getName()));
		  List<String> l = new ArrayList<String>();
		//TODO: MAKE THIS CUSTOMIZABLE
		  l.add("Rendre sa liberté à "+clicked.getName()+" et le sortir de prison.");
		  fM.setLore(l);
		  f.setItemMeta(fM);
		  hud.setContents(items.toArray(new ItemStack[items.size()]));
		  hud.setItem(hud.getSize()-2, f);
		  
		    ItemStack head = new ItemStack(Material.SKULL_ITEM);
		    SkullMeta m = (SkullMeta) head.getItemMeta();
		    head.setDurability((short) 3);
		    m.setDisplayName(utils.getMessage("menus.back.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    List<String> lore = new ArrayList<String>();
		  //TODO: MAKE THIS CUSTOMIZABLE
		    lore.add("Clique sur la tête pour");
		    lore.add("retourner à la liste des joueurs");
		    m.setLore(lore);
		    m.setOwner(clicked.getName());
		    head.setItemMeta(m);
		    
		    hud.setItem(hud.getSize()-1, head);
		  
		  player.openInventory(this.hud);
		}
		  
	  }
	  public void Jails(OfflinePlayer clicked, Player player) {
		if (utils.hasPermission(player, "guis.jails")) {
		  Collection<String> ljails = null;
		  try {
			ljails = ess.getJails().getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  String[] jails = ljails.toArray(new String[ljails.size()]);
		  int hudSize = jails.length;

		  
		  hudSize = utils.getMultiple(hudSize+2, 9);
		  this.hud = Bukkit.createInventory(player, hudSize, this.jailsName + " [" + clicked.getName() + "]");
		  
		  List<ItemStack> items = new ArrayList<ItemStack>();
		  
		  for (int i = 0; i < jails.length; i++) {
			  ItemStack x = new ItemStack(utils.getMaterial(utils.getMessage("menus.jails.cell.material")));
			  ItemMeta xM = x.getItemMeta();
			  xM.setDisplayName(utils.getMessage("menus.jails.cell.title").replaceAll("%sender%", player.getName()).replaceAll("%target%", clicked.getName()).replaceAll("%jail%", jails[i]));
			  List<String> l = new ArrayList<String>();
			//TODO: MAKE THIS CUSTOMIZABLE
			  l.add("Envoyer "+clicked.getName()+" dans la prison "+jails[i]+".");
			  xM.setLore(l);
			  x.setItemMeta(xM);
			  items.add(x);
		  }
		  ItemStack f = new ItemStack(utils.getMaterial(utils.getMessage("menus.jails.make_free.material")));
		  ItemMeta fM = f.getItemMeta();
		  fM.setDisplayName(utils.getMessage("menus.jails.make_free.title").replaceAll("%sender%", player.getName()).replaceAll("%target%", clicked.getName()));
		  List<String> l = new ArrayList<String>();
		//TODO: MAKE THIS CUSTOMIZABLE
		  l.add("Rendre sa liberté à "+clicked.getName()+" et le sortir de prison.");
		  fM.setLore(l);
		  f.setItemMeta(fM);
		  hud.setContents(items.toArray(new ItemStack[items.size()]));
		  hud.setItem(hud.getSize()-2, f);
		  
		    ItemStack head = new ItemStack(Material.SKULL_ITEM);
		    SkullMeta m = (SkullMeta) head.getItemMeta();
		    head.setDurability((short) 3);
		    m.setDisplayName(utils.getMessage("menus.back.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    List<String> lore = new ArrayList<String>();
		  //TODO: MAKE THIS CUSTOMIZABLE
		    lore.add("Clique sur la tête pour");
		    lore.add("retourner à la liste des joueurs");
		    m.setLore(lore);
		    m.setOwner(clicked.getName());
		    head.setItemMeta(m);
		    
		    hud.setItem(hud.getSize()-1, head);
		  
		  player.closeInventory();
		  player.openInventory(this.hud);
		}
	  }

	  public void Homes(Player clicked, Player player) {
		if (utils.hasPermission(player, "guis.homes")) {
		  Collection<String> lhomes = null;
		  try {
			  lhomes = ess.getUser(clicked).getHomes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  String[] homes = lhomes.toArray(new String[lhomes.size()]);
		  int hudSize = homes.length;

		  
		  hudSize = utils.getMultiple(hudSize+1, 9);
		  this.hud = Bukkit.createInventory(player, hudSize, this.homesName + " [" + clicked.getName() + "]");
		  
		  List<ItemStack> items = new ArrayList<ItemStack>();
		  
		  for (int i = 0; i < homes.length; i++) {
			  ItemStack x = new ItemStack(utils.getMaterial(utils.getMessage("menus.homes.home.material")));
			  ItemMeta xM = x.getItemMeta();
			  xM.setDisplayName(utils.getMessage("menus.homes.home.title").replaceAll("%sender%", player.getName()).replaceAll("%target%", clicked.getName()).replaceAll("%home%", homes[i]));
			  List<String> l = new ArrayList<String>();
			//TODO: MAKE THIS CUSTOMIZABLE
			  l.add("Se téléporter à la maison "+"["+homes[i]+"]"+" de "+clicked.getName()+".");
			  xM.setLore(l);
			  x.setItemMeta(xM);
			  items.add(x);
		  }

		    ItemStack head = new ItemStack(Material.SKULL_ITEM);
		    SkullMeta m = (SkullMeta) head.getItemMeta();
		    head.setDurability((short) 3);
		    m.setDisplayName(utils.getMessage("menus.back.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    List<String> lore = new ArrayList<String>();
		  //TODO: MAKE THIS CUSTOMIZABLE
		    lore.add("Clique sur la tête pour");
		    lore.add("retourner à la liste des joueurs");
		    m.setLore(lore);
		    m.setOwner(clicked.getName());
		    head.setItemMeta(m);
		    
		    hud.setContents(items.toArray(new ItemStack[items.size()]));
		    hud.setItem(hud.getSize()-1, head);
		  
		  player.openInventory(this.hud);
		}
		  
	  }
 	  public void Homes(OfflinePlayer clicked, Player player) {
 		if (utils.hasPermission(player, "guis.homes")) {
		  Collection<String> lhomes = null;
		  User user = ess.getOfflineUser(clicked.getName());
		  if (user == null) {
			//TODO: MAKE THIS CUSTOMIZABLE
			  player.sendMessage(apiManager.pluginTag + ChatColor.RED + " Essentials ne trouve pas le joueur \""+clicked.getName()+"\". S'est-il déjà connecté ?");
			  return;
		  }
		  try {
			  lhomes = user.getHomes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  String[] homes = lhomes.toArray(new String[lhomes.size()]);
		  int hudSize = homes.length;

		  
		  hudSize = utils.getMultiple(hudSize+1, 9);
		  this.hud = Bukkit.createInventory(player, hudSize, this.homesName + " [" + clicked.getName() + "]");
		  
		  List<ItemStack> items = new ArrayList<ItemStack>();
		  
		  for (int i = 0; i < homes.length; i++) {
			  ItemStack x = new ItemStack(utils.getMaterial(utils.getMessage("menus.homes.material")));
			  ItemMeta xM = x.getItemMeta();
			  xM.setDisplayName(utils.getMessage("menus.homes.home.title").replaceAll("%sender%", player.getName()).replaceAll("%target%", clicked.getName()).replaceAll("%home%", homes[i]));
			  List<String> l = new ArrayList<String>();
			//TODO: MAKE THIS CUSTOMIZABLE
			  l.add("Se téléporter à la maison "+"["+homes[i]+"]"+" de "+clicked.getName()+".");
			  xM.setLore(l);
			  x.setItemMeta(xM);
			  items.add(x);
		  }

		    ItemStack head = new ItemStack(Material.SKULL_ITEM);
		    SkullMeta m = (SkullMeta) head.getItemMeta();
		    head.setDurability((short) 3);
		    m.setDisplayName(utils.getMessage("menus.back.title").replaceAll("%target%", clicked.getName()).replaceAll("%sender%", player.getName()));
		    List<String> lore = new ArrayList<String>();
		  //TODO: MAKE THIS CUSTOMIZABLE
		    lore.add("Clique sur la tête pour");
		    lore.add("retourner à la liste des joueurs");
		    m.setLore(lore);
		    m.setOwner(clicked.getName());
		    head.setItemMeta(m);
		    
		    hud.setContents(items.toArray(new ItemStack[items.size()]));
		    hud.setItem(hud.getSize()-1, head);
		  
		  player.openInventory(this.hud);
 		}
 			
		 
	  }
	
	  public void Online(Player player) {
	   if (utils.hasPermission(player, "guis.main")) {
	    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);

	    ItemStack i = new ItemStack(Material.SKULL_ITEM);
	    SkullMeta m = (SkullMeta) i.getItemMeta();
	    i.setDurability((short) 3);
	if (players.length <= 54) {
	    this.ghost = Bukkit.createInventory(player, 54, this.guiName + " [" + Integer.toString(players.length) + "]");
	    for (int z = 0; z < players.length; z++) {
	      Damageable d = players[z];
	      m.setOwner(players[z].getName());
	      m.setDisplayName(ChatColor.GREEN + players[z].getName());
	      if (ess.getUser(players[z]) != null) {
	    	//TODO: MAKE THIS CUSTOMIZABLE
		      ArrayList<String> nlore = new ArrayList<String>();
		      nlore.add(ChatColor.DARK_GRAY + "* UUID: " + ChatColor.DARK_BLUE + players[z].getUniqueId());
		      nlore.add(ChatColor.DARK_GRAY + "* Niveau: " + ChatColor.AQUA + players[z].getLevel());
		      nlore.add(ChatColor.DARK_GRAY + "* Vie: " + ChatColor.AQUA + Math.round(d.getHealth()) + ChatColor.GRAY + " / " + ChatColor.AQUA + Math.round(d.getMaxHealth()));
		      nlore.add(ChatColor.DARK_GRAY + "* Nourriture: " + ChatColor.AQUA + Math.round(players[z].getFoodLevel())  + ChatColor.GRAY + " / " + ChatColor.AQUA +"20");
		      nlore.add(ChatColor.DARK_GRAY + "* Gamemode: " + ChatColor.AQUA + players[z].getGameMode().name());
		      if (ess.getUser(players[z]).isJailed() == true) nlore.add(ChatColor.DARK_GRAY + "* Jail: " + (ess.getUser(players[z]).isJailed() == true ? ChatColor.GREEN : ChatColor.RED) + Boolean.toString(ess.getUser(players[z]).isJailed()).toUpperCase() + ChatColor.AQUA + (ess.getUser(players[z]).isJailed() ? " ["+ess.getUser(players[z]).getJail().toUpperCase()+"]" : ""));
		      if (ess.getUser(players[z]).isMuted() == true) nlore.add(ChatColor.DARK_GRAY + "* Mute: " + (ess.getUser(players[z]).isMuted() == true ? ChatColor.GREEN : ChatColor.RED) + Boolean.toString(ess.getUser(players[z]).isMuted()).toUpperCase());
		      if (apiManager.isVaultEnabled) nlore.add(ChatColor.DARK_GRAY + "* Argent: " + ChatColor.GOLD + utils.getPlayerMoney(players[z]));
		      nlore.add(ChatColor.DARK_GRAY + "* Position: ");
		      nlore.add(ChatColor.GRAY + "   - World: " + ChatColor.AQUA + players[z].getLocation().getWorld().getName().toUpperCase());
		      nlore.add(ChatColor.GRAY + "   - X: " + ChatColor.AQUA + players[z].getLocation().getBlockX());
		      nlore.add(ChatColor.GRAY + "   - Y: " + ChatColor.AQUA + players[z].getLocation().getBlockY());
		      nlore.add(ChatColor.GRAY + "   - Z: " + ChatColor.AQUA + players[z].getLocation().getBlockZ());
		      nlore.add(ChatColor.DARK_GRAY + "* Connexions: ");
		      nlore.add(ChatColor.GRAY + "   - Première: " + ChatColor.AQUA + pl.getConfig().getString("players."+players[z].getUniqueId()+".first"));
		      nlore.add(ChatColor.GRAY + "   - Dernière: " + ChatColor.AQUA + pl.getConfig().getString("players."+players[z].getUniqueId()+".last"));
		      if (apiManager.isAutorankEnabled) nlore.add(ChatColor.GRAY + "   - Temps: " + ChatColor.AQUA + utils.getStringTime((autorankAPI.getGlobalPlayTime(players[z].getUniqueId()) == -1 ? autorankAPI.getLocalPlayTime(players[z].getUniqueId()) : autorankAPI.getGlobalPlayTime(players[z].getUniqueId()))));
		      m.setLore(nlore);
	      }
	      i.setItemMeta(m);
	      this.ghost.setItem(z, i);
	    }
	    player.openInventory(this.ghost);
	  } else {
		  player.sendMessage(utils.getPluginTag() + utils.getMessage("plugin.messages.errors.tooMuchPlayers"));
	  }
		 
	 }
	}

}
