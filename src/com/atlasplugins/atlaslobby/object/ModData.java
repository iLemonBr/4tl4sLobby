package com.atlasplugins.atlaslobby.object;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ModData {

	
    private final Map<String, String> mods;
    
    public ModData(Map<String, String> mods) {
        this.mods = mods;
    }
    
    public Set<String> getMods() {
        return Collections.unmodifiableSet((Set<? extends String>)this.mods.keySet());
    }
    
    public Map<String, String> getModsMap() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.mods);
    }
    
}
