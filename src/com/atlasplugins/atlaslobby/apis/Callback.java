package com.atlasplugins.atlaslobby.apis;

import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public interface Callback<T>
{
    public void execute(T response, AtlasPlayer lobbyUser);
}
