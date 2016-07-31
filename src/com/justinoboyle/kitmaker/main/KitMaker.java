package com.justinoboyle.kitmaker.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class KitMaker extends JavaPlugin {

    public static KitMaker instance;

    private static final Listener[] listeners = new Listener[] {};
    
    public static final List<ResultList> results = new ArrayList<ResultList>();

    public void onEnable() {
        instance = this;
        registerListeners();
        new BukkitRunnable(){
            public void run(){
//                Kit.addKits();
                System.out.println("Done with kits.");
            }
        }.runTaskAsynchronously(this);
//        Kit.addKits();
    }

    public void onDisable() {
    }
    
    public void resetupChests() {

        String s = "";

        try {
            if (KitMaker.instance.getConfig().getString("loadedchests") != null) {
                s = KitMaker.instance.getConfig().getString("loadedchests");
            }
        } catch (Exception ex) {

        }

        for (String s2 : s.split(",")) {

            String location = KitMaker.instance.getConfig().getString("TreasureChests." + s2 + ".location");

            String particle = KitMaker.instance.getConfig().getString("TreasureChests." + s2 + ".particle");

            String results = KitMaker.instance.getConfig().getString("TreasureChests." + s2 + ".results");

            String name = KitMaker.instance.getConfig().getString("TreasureChests." + s2 + ".name");

            boolean shouldShootFirework = KitMaker.instance.getConfig().getBoolean("TreasureChests." + s2 + ".shouldShootFirework");

            String displayName = KitMaker.instance.getConfig().getString("TreasureChests." + s2 + ".displayName");

            results.add(c);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return false;

        Player p = (Player) sender;

        if (!p.isOp())
            return false;

        if (args.length == 0)
            return false;

        if (label.equalsIgnoreCase("kit")) {
            Kit k = Kit.getKit(args[0]);
            for (ItemStack i : k.getItems()) {
                if (i != null)
                    p.getInventory().addItem(i);
            }
            p.updateInventory();
            return false;
        }

        String name = args[0].toLowerCase();

        Kit k = Kit.getKit(name);
        if (args.length > 1)
            if (args[1].equals("save") || args[1].equals("sync")) {
                k.sync();
                return false;
            }

        p.openInventory(k.getInventory());

        return false;
    }

    private void registerListeners() {
        for (Listener l : listeners)
            Bukkit.getPluginManager().registerEvents(l, this);
    }
}
