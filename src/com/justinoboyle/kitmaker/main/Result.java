package com.justinoboyle.kitmaker.main;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Result implements Serializable {

    private static final long serialVersionUID = -6099160460801738862L;

    private boolean isCommand;
    private boolean isLegendary;
    private ItemStack display;
    private String command;
    private int percentage;
    private String name;

    public Result(boolean isCommand, ItemStack display, String command, int percentage, String name, boolean isLegendary) {
        super();
        this.isCommand = isCommand;
        this.display = display;
        this.command = command;
        this.percentage = percentage;
        this.name = name;
        this.isLegendary = isLegendary;
    }

    public Result(String name) {
        super();
        this.isCommand = KitMaker.instance.getConfig().getBoolean("Results." + name + ".isCommand");
        this.isLegendary = KitMaker.instance.getConfig().getBoolean("Results." + name + ".isLegendary");
        this.display = (ItemStack) KitMaker.instance.getConfig().get("ResultItems." + name);
        this.command = KitMaker.instance.getConfig().getString("Results." + name + ".command");
        this.percentage = KitMaker.instance.getConfig().getInt("Results." + name + ".percentage");

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + ((display == null) ? 0 : display.hashCode());
        result = prime * result + (isCommand ? 1231 : 1237);
        result = prime * result + percentage;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Result other = (Result) obj;
        if (command == null) {
            if (other.command != null)
                return false;
        } else if (!command.equals(other.command))
            return false;
        if (display == null) {
            if (other.display != null)
                return false;
        } else if (!display.equals(other.display))
            return false;
        if (isCommand != other.isCommand)
            return false;
        if (percentage != other.percentage)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Result [isCommand=" + isCommand + ", display=" + display + ", command=" + command + ", percentage=" + percentage + "]";
    }

    public boolean isCommand() {
        return isCommand;
    }

    public void setCommand(boolean isCommand) {
        this.isCommand = isCommand;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public void setDisplay(ItemStack display) {
        this.display = display;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void saveItemStack() {
        KitMaker.instance.getConfig().set("ResultItems." + this.name, this.display);
    }

    public void save() {
        KitMaker.instance.getConfig().set("Results." + name + ".isCommand", this.isCommand);
        KitMaker.instance.getConfig().set("Results." + name + ".isLegendary", isLegendary);
        saveItemStack();
        KitMaker.instance.getConfig().set("Results." + name + ".command", command);
        KitMaker.instance.getConfig().set("Results." + name + ".percentage", percentage);
        KitMaker.instance.getConfig().set("Results." + name + ".percentage", percentage);

        KitMaker.instance.saveConfig();
    }

    public void giveReward(Player p) {
        if (this.isLegendary) {
            Bukkit.broadcastMessage("§6§lCRATES: §3" + p.getName() + " &7received a &3Legendary &7item!".replace("&", "§"));
            for (Player ps : p.getWorld().getPlayers()) {
                ps.playSound(ps.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
            }
        }
        if (this.isCommand()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), this.getCommand().replace("[player]", p.getName()));
            return;
        }

        if (this.getCommand().equals("giveitem")) {
            if (p.getInventory().firstEmpty() == -1) {
                p.getWorld().dropItemNaturally(p.getLocation(), this.getDisplay());
                p.sendMessage("§6§lCRATES: §7Your inventory is full, so your reward has been placed on the ground.".replace("&", "§"));
                return;
            }
            p.getInventory().addItem(this.getDisplay());
            p.updateInventory();
        }

    }

}
