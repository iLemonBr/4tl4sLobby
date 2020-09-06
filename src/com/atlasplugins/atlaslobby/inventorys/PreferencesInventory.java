package com.atlasplugins.atlaslobby.inventorys;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.InventoryHandler;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public class PreferencesInventory {

	
	public static void open(Player p) {
		AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName().toLowerCase());
		InventoryHandler handler = new InventoryHandler("Preferências", 36);
		
		if(atlasPlayer.getPreferences().isVisibility()) {
			handler.item(2, new ItemComposer(Material.SKULL_ITEM).setName("§aVisibilidade de Jogadores")
					.setLore("§7Ativar ou desativar a visibilidade","§7dos jogadores no lobby").toItemStack());
			handler.item(11, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)5).setName("§aVisibilidade de Jogadores")
					.setLore("§7Estado: §aLigado").toItemStack());
		}else {
			handler.item(2, new ItemComposer(Material.SKULL_ITEM).setName("§cVisibilidade de Jogadores")
					.setLore("§7Ativar ou desativar a visibilidade","§7dos jogadores no lobby").toItemStack());
			handler.item(11, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)14).setName("§cVisibilidade de Jogadores")
					.setLore("§7Estado: §cDesligado").toItemStack());
		}
		
		if(atlasPlayer.getPreferences().isChat()) {
			handler.item(3, new ItemComposer(Material.SIGN).setName("§aChat no Lobby")
					.setLore("§7Ativar ou desativar o","§7chat dentro do lobby").toItemStack());
			handler.item(12, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)5).setName("§aChat no Lobby")
					.setLore("§7Estado: §aLigado").toItemStack());
		}else {
			handler.item(3, new ItemComposer(Material.SIGN).setName("§cChat no Lobby")
					.setLore("§7Ativar ou desativar o","§7chat dentro do lobby").toItemStack());
			handler.item(12, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)14).setName("§cChat no Lobby")
					.setLore("§7Estado: §cDesligado").toItemStack());
		}
		
		if(atlasPlayer.getPreferences().isTell()) {
			handler.item(5, new ItemComposer(Material.PAPER).setName("§aMensagens Privadas no Lobby")
					.setLore("§7Ativar ou desativar as mensagens","§7recebidas pelo '/tell'").toItemStack());
			handler.item(14, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)5).setName("§aMensagens Privadas no Lobby")
					.setLore("§7Estado: §aLigado").toItemStack());
		}else {
			handler.item(5, new ItemComposer(Material.PAPER).setName("§cMensagens Privadas no Lobby")
					.setLore("§7Ativar ou desativar as mensagens","§7recebidas pelo '/tell'").toItemStack());
			handler.item(14, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)14).setName("§cMensagens Privadas no Lobby")
					.setLore("§7Estado: §cDesligado").toItemStack());
		}
		
		if(atlasPlayer.getPreferences().isFly()) {
			handler.item(6, new ItemComposer(Material.FEATHER).setName("§aVoar no Lobby")
					.setLore("§7Ativar ou desativar o voô","§7dentro do lobby do servidor.").toItemStack());
			handler.item(15, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)5).setName("§aVoar no Lobby")
					.setLore("§7Estado: §aLigado").toItemStack());
		}else {
			handler.item(6, new ItemComposer(Material.FEATHER).setName("§cVoar no Lobby")
					.setLore("§7Ativar ou desativar o voô","§7dentro do lobby do servidor.").toItemStack());
			handler.item(15, new ItemComposer(Material.STAINED_GLASS_PANE).setDurability((short)14).setName("§cVoar no Lobby")
					.setLore("§7Estado: §cDesligado").toItemStack());
		}
		handler.handler(event->{
			if(event.getSlot() == 11) {
				atlasPlayer.getPreferences().setVisibility((atlasPlayer.getPreferences().isVisibility() ? false : true));
				atlasPlayer.save(true);
				p.getInventory().clear();
				LobbyCore.giveLobbyItems(p);
				open(p);
			}
			if(event.getSlot() == 12) {
				atlasPlayer.getPreferences().setChat((atlasPlayer.getPreferences().isChat() ? false : true));
				atlasPlayer.save(true);
				open(p);
			}
			if(event.getSlot() == 14) {
				atlasPlayer.getPreferences().setTell((atlasPlayer.getPreferences().isTell() ? false : true));
				atlasPlayer.save(true);
				open(p);
			}
			if(event.getSlot() == 15) {
				if(atlasPlayer.getPreferences().isFly()) {
					atlasPlayer.getPreferences().setFly(false);
					p.setAllowFlight(false);
					p.setFlying(false);
				}else {
					atlasPlayer.getPreferences().setFly(true);
					p.setAllowFlight(true);
					p.setFlying(true);
				}
				atlasPlayer.save(true);
				open(p);
			}
		});
		handler.open(p);
	}
}
