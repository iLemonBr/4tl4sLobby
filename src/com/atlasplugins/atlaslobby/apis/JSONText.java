package com.atlasplugins.atlaslobby.apis;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import java.util.ArrayList;
import net.md_5.bungee.api.chat.BaseComponent;
import java.util.List;
import net.md_5.bungee.api.chat.TextComponent;

public class JSONText
{
    private TextComponent component;
    private TextComponent textComponent;
    private List<BaseComponent> baseComponent;
    
    public JSONText() {
        this.baseComponent = new ArrayList<BaseComponent>();
        this.component = new TextComponent("");
    }
    
    public JSONText(final String prefix) {
        this.baseComponent = new ArrayList<BaseComponent>();
        this.component = new TextComponent(prefix);
    }
    
    public JSONText prefix(final String prefix) {
        this.text(prefix);
        return this;
    }
    
    public JSONText text(final String text) {
        this.textComponent = new TextComponent(TextComponent.fromLegacyText(text));
        return this;
    }
    
    public JSONText hoverText(final String text) {
        final BaseComponent[] hover = { new TextComponent(text) };
        this.textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
        return this;
    }
    
    public JSONText clickOpenURL(final String url) {
        this.textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.valueOf(url.startsWith("http") ? "" : "https://") + url));
        return this;
    }
    
    public JSONText clickRunCommand(final String command) {
        this.textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }
    
    public JSONText clickSuggest(final String suggest) {
        this.textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggest));
        return this;
    }
    
    public JSONText next() {
        if (this.textComponent == null) {
            return this;
        }
        this.baseComponent.add((BaseComponent)this.textComponent);
        return this;
    }
    
    public void send(final Player player) {
        this.component.setExtra((List)this.baseComponent);
        player.spigot().sendMessage((BaseComponent)this.component);
    }
    
    public void send(final CommandSender sender) {
        this.component.setExtra((List)this.baseComponent);
        sender.sendMessage(this.component.getText());
    }
    
    public void sendEveryOne() {
        this.component.setExtra((List)this.baseComponent);
        for (final Player everyone : Bukkit.getOnlinePlayers()) {
            everyone.spigot().sendMessage((BaseComponent)this.component);
        }
    }
}
