package net.psammead.linky.plugin.kick;

import java.util.ArrayList;

import net.psammead.linky.Command;
import net.psammead.linky.CommandBase;
import net.psammead.linky.PluginBase;
import net.psammead.linky.irc.ConnectionHandler;
import net.psammead.linky.irc.ConnectionHandlerAdapter;
import net.psammead.linky.irc.Routing;

public class KickPlugin extends PluginBase {
	
	private KickModel kickModel;
	
	public KickPlugin() {
	}
	
	@Override
	public void init() {
		kickModel = new KickModel();
	}
	
	@Override
	public void afterLoad() {
		context.registerPersistent(kickModel);
	}
	
	@Override
	public void beforeUnload() {
		context.unregisterPersistent(kickModel);
	}
	
	@Override
	public String status() {
		return context.message("status", kickModel.getKickCount());
	}
	
	@Override
	public ConnectionHandler handler() {
		return new ConnectionHandlerAdapter() {
			
			@Override
			public void onPrivMsg(Routing routing, String message) {
				check(routing, message);
			}
			
			@Override
			public void onAction(Routing routing, String action) {
				check(routing, action);
			}
			
			private void check(Routing routing, String message) {
				if(!routing.target.channelFlag) {
					return;
				}
				String nick = routing.source.nick;
				String login = routing.source.login;
				String host = routing.source.host;
				String complete = nick + "!" + login + "@" + host;
				if(kickModel.getNicks().contains(nick)) {
					Kick kick = kickModel.getKick(nick);
					ArrayList<String> channels = kick.getChannels();
					//if(channels.contains())
					//routing.connection.kick(kick.getChannel(), nick);

					return;
				}
				for(String aNick : kickModel.getNicks()) {
					if(complete.matches(aNick)) {
						Kick kick = kickModel.getKick(aNick);

						//routing.connection.kick(kick.getChannel(), nick);

						break;
					}
				}
			}
		};
	}
	
	@Override
	public Command[] commands() {
		return new Command[] {
			new CommandBase("addKick") {				
				@Override
				public void execute(Routing routing, Object[] args) {
					cmdAddKick(routing, (String)args[0], (String)args[1]);					
				}
			},
			new CommandBase("delKick") {
				@Override
				public void execute(Routing routing, Object[] args) {
					cmdDelKick(routing, (String)args[0]);
				}				
			}
		};
	}
	
	private void cmdAddKick(Routing routing, String nick, String channel) {
		boolean success = kickModel.addKick(nick, channel);
		if(success) {
			routing.reply(context.message("addKickDone", nick));
		}
		else {
			routing.reply(context.message("kickAlreadyExisting", nick));
		}
	}
	
	private void cmdDelKick(Routing routing, String nick) {
		boolean success = true;// = kickModel.deleteKick(nick);
		if(success) {
			routing.reply(context.message("delKickDone", nick));
		}
		else {
			routing.reply(context.message("kickDoesNotExist", nick));
		}
	}
}
