package com.atlasplugins.atlaslobby.apis;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.atlasplugins.atlaslobby.Main;


public class Hidder {

	public static Entity addMetaDados(Entity p, String server) {
		ArmorStand entidade = p.getWorld().spawn(p.getLocation(), ArmorStand.class);
		entidade.setMetadata("hidder", new FixedMetadataValue(Main.getPlugin(Main.class), server));
		entidade.setVisible(false);
		entidade.setBasePlate(false);
		p.setPassenger(entidade);
		return entidade;
	}
	
	public static void hideNPCNameTag(Player from, Entity p) {
		Player npc = (Player)p;
		Scoreboard score = from.getScoreboard();
		Team team = null;
		for(Team t : score.getTeams()) {
			if(t.getName().equalsIgnoreCase("NPCs")) {
				team = t;
			}
		}
		if(team == null) {
			team = score.registerNewTeam("NPCs");
		}
		team.addEntry(npc.getName());
		team.setNameTagVisibility(NameTagVisibility.NEVER);
		from.setScoreboard(score);
	}
}
