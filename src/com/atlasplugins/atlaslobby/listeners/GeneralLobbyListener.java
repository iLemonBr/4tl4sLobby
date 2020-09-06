package com.atlasplugins.atlaslobby.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.LobbyAPI;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class GeneralLobbyListener implements Listener{

	@EventHandler
	public void onArmorStandConnection(PlayerInteractAtEntityEvent event) {
		if(event.getRightClicked() != null && event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
			if(event.getRightClicked().hasMetadata("hidder")) {
				Player p = event.getPlayer();
				String server = event.getRightClicked().getMetadata("hidder").get(0).asString();
				LobbyAPI.connectPlayerToTheServer(p, server);
			}
		}
	}
	
	@EventHandler
	public void onNPCConnection(NPCRightClickEvent event) {
		NPC npc = event.getNPC();
		if(npc.getEntity().getPassenger()!=null && npc.getEntity().getPassenger().hasMetadata("hidder")) {
			String server = npc.getEntity().getPassenger().getMetadata("hidder").get(0).asString();
			LobbyAPI.connectPlayerToTheServer(event.getClicker(), server);
		}
	}
	
	@EventHandler
	public void onDamageByVoid(EntityDamageEvent event) {
		if(event.getCause().equals(DamageCause.VOID)) {
			if(event.getEntityType().equals(EntityType.PLAYER)) {
				if(LobbyCore.getSpawn()!=null) {
					event.setDamage(0.0);
					event.setCancelled(true);
					Player p = (Player)event.getEntity();
					p.teleport(LobbyCore.getSpawn());
				}
			}
		}
	}
	
	@EventHandler
	public void food(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
		event.setCancelled(true);
	}
	
	@EventHandler
	public void aindaNaoAutenticado(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if(!Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
			event.getPlayer().teleport(p.getLocation().clone().setDirection(event.getTo().getDirection()));
		}else {
			AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName().toLowerCase());
			if(!atlasPlayer.isLogged()) {
				event.getPlayer().teleport(p.getLocation().clone().setDirection(event.getTo().getDirection()));
			}
		}
	}
	@EventHandler
	public void fallDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			if(event.getCause().equals(DamageCause.FALL) || event.getCause().equals(DamageCause.FALLING_BLOCK)) {
				event.setCancelled(true);
				event.setDamage(0);
			}
		}
	}
}
