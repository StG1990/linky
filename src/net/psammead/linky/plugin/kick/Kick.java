package net.psammead.linky.plugin.kick;

import java.util.ArrayList;

public class Kick {
	
	private String nick;
	private ArrayList<String> channels;
	
	public Kick() {
		nick = null;
		channels = new ArrayList<String>();
	}
	
	public Kick(String nick, String channel) {
		this.nick = nick;
		channels.add(channel);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public ArrayList<String> getChannels() {
		return channels;
	}

	public void addChannel(String channel) {
		channels.add(channel);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((channels == null) ? 0 : channels.hashCode());
		result = prime * result + ((nick == null) ? 0 : nick.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kick other = (Kick) obj;
		if (channels == null) {
			if (other.channels != null)
				return false;
		} else if (!channels.equals(other.channels))
			return false;
		if (nick == null) {
			if (other.nick != null)
				return false;
		} else if (!nick.equals(other.nick))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kick [nick=" + nick + ", channels=" + channels + "]";
	}
	
}
