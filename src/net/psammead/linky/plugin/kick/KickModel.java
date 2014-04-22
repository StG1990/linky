package net.psammead.linky.plugin.kick;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.psammead.linky.persistence.Persistent;

public class KickModel implements Persistent {
	
	private Map<String, Kick> kicks;
	
	public KickModel() {
		kicks = new HashMap<String, Kick>();
	}
	
	public Set<String> getNicks() {
		return Collections.unmodifiableSet(kicks.keySet());
	}
	
	public Kick getKick(String nick) {
		return kicks.get(nick);
	}
	
	public Collection<Kick> getAllKicks() {
		return Collections.unmodifiableCollection(kicks.values());
	}
	
	public Set<String> getKickNicks() {
		return Collections.unmodifiableSet(kicks.keySet());
	}
	
	public int getKickCount() {
		return kicks.size();
	}
	
	public boolean addKick(String nick, String channel) {
		if(kicks.containsKey(nick)) {
			if(kicks.get(nick).getChannels().contains(channel)) {
				return false;
			}
			kicks.get(nick).addChannel(channel);
			return true;
		}
		String result;
		if(nick.contains("@")) {
			result = parseRegEx(nick);
		}
		else {
			result = nick;
		}
		Kick kick = new Kick();
		kick.setNick(result);
		kick.addChannel(channel);
		kicks.put(kick.getNick(), kick);
		return true;
	}
	
	public boolean deleteKick(String nick, String channel) {
		ArrayList<String> channels = kicks.get(nick).getChannels();
		if(!kicks.containsKey(nick) || !channels.contains(channel)) {
			return false;
		}

		String result;
		if(nick.contains("@")) {
			result= parseRegEx(nick);
		}
		else {
			result = nick;
		}
		
		if(channels.size() > 1) {
			channels.remove(channel);
		}
		else {
			kicks.remove(result);
		}
		return true;
	}
	
	private String parseRegEx(String nick) {
		ArrayList<String> string = new ArrayList<String>();
		
		int em = nick.indexOf("!");
		int at = nick.indexOf("@");
		
		string.add(nick.substring(0, em));
		string.add(nick.substring(em, em+1));
		string.add(nick.substring(em+1, at));
		string.add(nick.substring(at, at+1));
		string.add(nick.substring(at+1, nick.length()));
		
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < string.size(); i++) {
			StringBuffer internalBuffer = new StringBuffer();
			String org = string.get(i);			
			while(org.contains("*")) {
				
				int a = org.indexOf("*");
				if(a != 0) {
					internalBuffer.append("(" + org.substring(0, a) + ")");
					org = org.substring(a, org.length());
				}
				internalBuffer.append("(." + org.substring(0, 1) + ")");
				org = org.substring(1, org.length());				
			}
			while(org.contains("\\p{Punct}")) {
				
				int a = org.indexOf("\\p{Punct}");
				
				if(a != 0) {
					internalBuffer.append("(" + org.substring(0, a) + ")");
					org = org.substring(a, org.length());
				}
				internalBuffer.append("(\\" + org.substring(0, 1) + ")");
				org = org.substring(1, org.length());		
			}
			if(internalBuffer.length() == 0 || !org.isEmpty()) {
				internalBuffer.append("(" + org + ")");
			}
			buffer.append(internalBuffer.toString());			
		}
				
		return buffer.toString();
	}
	
	@Override
	public String getPersistentName() {
		return "kick";
	}

	@Override
	public Object getPersistentModel() {
		return kicks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPersistentModel(Object model) {
		kicks = (Map<String, Kick>)model;
	}

}
