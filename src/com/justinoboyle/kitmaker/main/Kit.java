package com.justinoboyle.kitmaker.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Kit {

    private List<ItemStack> items = new ArrayList<ItemStack>();

    private String name;

    private Inventory i;

    public static final List<Kit> kits = new ArrayList<Kit>();

    public Kit() {
        kits.add(this);
    }

    public static Kit getKit(String name) {
        new File("./plugins/KitMaker/kits/").mkdirs();
        File f = new File("./plugins/KitMaker/kits/" + name + ".kit");
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        if (!f.exists()) {
            Kit k = new Kit();
            k.setName(name);
            k.getInventory();
            return k;
        }
        return fromFile(name);
    }

    public static void addKits() {
        File f = new File("./plugins/KitMaker/kits/");
        f.mkdirs();
        kits.clear();
        for (final File f2 : f.listFiles())
            getKit(FilenameUtils.removeExtension(f2.getName()));
    }

    public static Kit fromFile(String name) {
        name = name.toLowerCase();
        name = name.replace("/", "");
        name = name.replace(".", "");

        Kit k = new Kit();
        k.setName(name);

        new File("./plugins/KitMaker/kits/").mkdirs();

        File f = new File("./plugins/KitMaker/kits/" + name + ".kit");

        if (!f.exists())
            return k;

        Scanner sc = new Scanner(System.in);

        String s = sc.nextLine();

        sc.close();

        ItemStack[] is = CardboardBox.fromBase64(s).getContents();
        for (ItemStack i2 : is)
            k.items.add(i2);

        return k;

    }

    public Inventory getInventory() {
        if (i == null) {
            i = Bukkit.createInventory(null, 9 * 4, "Items");
            for (ItemStack i2 : items)
                if (i2 != null)
                    i.addItem(i2);
        }
        return i;
    }

    public void sync() {
        items.clear();
        for (ItemStack i2 : i.getContents())
            items.add(i2);
        i = null;
        getInventory();
        save();
    }

    public void save() {
        new File("./plugins/KitMaker/kits/").mkdirs();

        File f = new File("./plugins/KitMaker/kits/" + name + ".kit");
        if (f.exists())
            f.delete();

        List<String> lines = new ArrayList<String>();

        lines.add(CardboardBox.toBase64(this.getInventory()));

        try {
            PrintWriter pw = new PrintWriter(f);

            for (String s3 : lines)
                pw.write(s3);

            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not write to file " + f.getAbsolutePath());
        }

    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
