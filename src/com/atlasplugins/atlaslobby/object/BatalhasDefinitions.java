package com.atlasplugins.atlaslobby.object;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.ItemComposer;

public class BatalhasDefinitions {

	
	private static ItemStack helmet;
	private static ItemStack chestplate;
	private static ItemStack leggings;
	private static ItemStack boots;
	private static ArrayList<ItemStack> additionalItens = new ArrayList<ItemStack>();
	
	public BatalhasDefinitions(Main core) {
		super();
		for(String item : core.getConfig().getConfigurationSection("Batalhas.ItensBatalhar").getKeys(false)) {
			if(item.equalsIgnoreCase("HELMET")) {
				helmet = new ItemComposer(Material.getMaterial(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".ID")))
						.setDurability((short)core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Data")).setAmount(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Quantity")).toItemStack();
			}else if(item.equalsIgnoreCase("CHESTPLATE")) {
				chestplate = new ItemComposer(Material.getMaterial(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".ID")))
						.setDurability((short)core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Data")).setAmount(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Quantity")).toItemStack();
			}else if(item.equalsIgnoreCase("LEGGINGS")) {
				leggings = new ItemComposer(Material.getMaterial(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".ID")))
						.setDurability((short)core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Data")).setAmount(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Quantity")).toItemStack();
			}else if(item.equalsIgnoreCase("BOOTS")) {
				boots = new ItemComposer(Material.getMaterial(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".ID")))
						.setDurability((short)core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Data")).setAmount(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Quantity")).toItemStack();
			}else {
				additionalItens.add(new ItemComposer(Material.getMaterial(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".ID")))
						.setDurability((short)core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Data")).setAmount(core.getConfig().getInt("Batalhas.ItensBatalhar."+item+".Quantity")).toItemStack());
			}
		}
	}
	
	public static void giveItems(Player p) {
		if(helmet!=null) {
			p.getInventory().setHelmet(helmet);
		}
		if(chestplate!=null) {
			p.getInventory().setChestplate(chestplate);
		}
		if(leggings!=null) {
			p.getInventory().setLeggings(leggings);
		}
		if(boots!=null) {
			p.getInventory().setBoots(boots);
		}
		if(additionalItens.size() > 0) {
			additionalItens.forEach(item->{
				p.getInventory().addItem(item);
			});
		}
	}
	
	public static void clearItems(Player p) {
		p.getInventory().clear();
	}
	
	
}
