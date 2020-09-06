package com.atlasplugins.atlaslobby.object;

public class Preferences {

	
	private boolean visibility;
	private boolean chat;
	private boolean tell;
	private boolean fly;
	private boolean anuncios;
	
	public Preferences() {
		super();
		this.visibility = true;
		this.chat = true;
		this.tell = true;
		this.fly = true;
		this.anuncios = true;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public boolean isChat() {
		return chat;
	}

	public boolean isTell() {
		return tell;
	}

	public boolean isFly() {
		return fly;
	}

	public boolean isAnuncios() {
		return anuncios;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public void setChat(boolean chat) {
		this.chat = chat;
	}

	public void setTell(boolean tell) {
		this.tell = tell;
	}

	public void setFly(boolean fly) {
		this.fly = fly;
	}

	public void setAnuncios(boolean anuncios) {
		this.anuncios = anuncios;
	}
	
}
