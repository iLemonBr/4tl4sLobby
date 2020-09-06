package com.atlasplugins.atlaslobby.captcha;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.atlasplugins.atlaslobby.apis.Heads;
import com.atlasplugins.atlaslobby.apis.ItemComposer;

public class CaptchaGenerator{

	public static HashMap<String,Integer> correctSlot = new HashMap<String,Integer>();
	public final int INVENTORY_SIZE = 27;
	
	public CaptchaGenerator(Player p) {
		Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE,"Clique no bloco azul");
		Random r = new Random();
		int correctSlot = r.nextInt(INVENTORY_SIZE);
		for(int i = 0; i<INVENTORY_SIZE; i++) {
			if(i != correctSlot) {
			inv.setItem(i, new ItemComposer(Heads.CINZA.clone()).setName("§f").toItemStack());
			}else {
				inv.setItem(i, new ItemComposer(Heads.BLUE).setName("§f").toItemStack());
			}
		}
		CaptchaGenerator.correctSlot.put(p.getName(), correctSlot);
		p.openInventory(inv);
	}
	
}