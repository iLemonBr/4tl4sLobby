package com.atlasplugins.atlaslobby.apis;

import org.bukkit.inventory.*;
import org.bukkit.enchantments.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;

public class ItemBuilderCustom
{
    private ItemStack is;
    
    public ItemBuilderCustom(final Material m) {
        this(m, 1);
    }
    
    public ItemBuilderCustom(final ItemStack is) {
        this.is = is;
    }
    
    public ItemBuilderCustom(final Material m, final int quantia) {
        this.is = new ItemStack(m, quantia);
    }
    
    public ItemBuilderCustom(final Material m, final int quantia, final byte durabilidade) {
        this.is = new ItemStack(m, quantia, (short)durabilidade);
    }
    
    public ItemBuilderCustom(final Material m, final int quantia, final int durabilidade) {
        this.is = new ItemStack(m, quantia, (short)durabilidade);
    }
    
    public ItemBuilderCustom clone() {
        return new ItemBuilderCustom(this.is);
    }
    
    public ItemBuilderCustom setDurability(final short durabilidade) {
        this.is.setDurability(durabilidade);
        return this;
    }
    
    public ItemBuilderCustom setAmount(final int amount) {
        this.is.setAmount(amount);
        final ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom setDurability(final int durabilidade) {
        this.is.setDurability((short)Short.valueOf(new StringBuilder().append(durabilidade).toString()));
        return this;
    }
    
    public ItemBuilderCustom setName(final String nome) {
        final ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(nome);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom addUnsafeEnchantment(final Enchantment ench, final int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }
    
    public ItemBuilderCustom removeEnchantment(final Enchantment ench) {
        this.is.removeEnchantment(ench);
        return this;
    }
    
    public ItemBuilderCustom setSkullOwner(final String dono) {
        try {
            final SkullMeta im = (SkullMeta)this.is.getItemMeta();
            im.setOwner(dono);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (ClassCastException ex) {}
        return this;
    }
    
    public ItemBuilderCustom addEnchant(final Enchantment ench, final int level) {
        final ItemMeta im = this.is.getItemMeta();
        im.addEnchant(ench, level, true);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom addEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments((Map)enchantments);
        return this;
    }
    
    public ItemBuilderCustom setInfinityDurability() {
        this.is.setDurability((short)32767);
        return this;
    }
    
    public ItemBuilderCustom addItemFlag(final ItemFlag flag) {
        final ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(new ItemFlag[] { flag });
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom setLore(final String... lore) {
        final ItemMeta im = this.is.getItemMeta();
        im.setLore((List)Arrays.asList(lore));
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom setLore(final List<String> lore) {
        final ItemMeta im = this.is.getItemMeta();
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom removeLoreLine(final String linha) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        if (!lore.contains(linha)) {
            return this;
        }
        lore.remove(linha);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom removeLoreLine(final int index) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        if (index < 0 || index > lore.size()) {
            return this;
        }
        lore.remove(index);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom addLoreLine(final String linha) {
        final ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<String>();
        if (im.hasLore()) {
            lore = new ArrayList<String>(im.getLore());
        }
        lore.add(linha);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom addLores(final List<String> linha) {
        final ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<String>();
        if (im.hasLore()) {
            lore = new ArrayList<String>(im.getLore());
        }
        for (final String s : linha) {
            lore.add(s);
        }
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom addLoreLine(final String linha, final int pos) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        lore.set(pos, linha);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }
    
    public ItemBuilderCustom setDyeColor(final DyeColor cor) {
        this.is.setDurability((short)cor.getData());
        return this;
    }
    
    @Deprecated
    public ItemBuilderCustom setWoolColor(final DyeColor cor) {
        if (!this.is.getType().equals((Object)Material.WOOL)) {
            return this;
        }
        this.is.setDurability((short)cor.getData());
        return this;
    }
    
    public ItemBuilderCustom setLeatherArmorColor(final Color cor) {
        try {
            final LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
            im.setColor(cor);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (ClassCastException ex) {}
        return this;
    }
    
    public ItemStack toItemStack() {
        return this.is;
    }
}
