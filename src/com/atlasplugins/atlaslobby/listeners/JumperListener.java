package com.atlasplugins.atlaslobby.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.atlasplugins.atlaslobby.apis.ParticleEffect;
import com.atlasplugins.atlaslobby.apis.Sounds;

public class JumperListener implements Listener{

	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if(p.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.SLIME_BLOCK)) {
			ParticleEffect.VILLAGER_HAPPY.display(0.5f, 0.2f, 0.5f, 1.0f, 10, p.getLocation().add(0, -1, 0).getBlock().getLocation(), 20.0);
			p.setVelocity(p.getVelocity().multiply(3.0));
			Sound sonido = Sounds.getSound(Sounds.Sound_1_7.FIREWORK_LAUNCH, Sounds.Sound_1_9.ENTITY_FIREWORK_LAUNCH);
			p.playSound(p.getLocation(), sonido, 2.0f, 2.0f);
		}
	}
}
