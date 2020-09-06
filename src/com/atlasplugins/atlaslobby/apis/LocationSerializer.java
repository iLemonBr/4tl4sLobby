package com.atlasplugins.atlaslobby.apis;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {

	
	 public static String getSerializedLocation(Location loc) { 
	        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch()
	                + ";" + loc.getWorld().getUID();
	     }
	  
	    public static Location getDeserializedLocation(String s) {
	    	if (s.equalsIgnoreCase("null")) return null;
	            String [] parts = s.split(";"); 
	            double x = Double.parseDouble(parts[0]);
	            double y = Double.parseDouble(parts[1]);
	            double z = Double.parseDouble(parts[2]);
	            float yaw = Float.parseFloat(parts[3]);
	            float pitch = Float.parseFloat(parts[4]);
	            UUID u = UUID.fromString(parts[5]);
	            World w = Bukkit.getServer().getWorld(u);
	            return new Location(w, x, y, z, yaw, pitch); 
	    }
	    
}
