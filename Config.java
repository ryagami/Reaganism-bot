package ircbot;

public class Config {
	
	public static String channel = "#ssss";
	public static String server = "irc.quakenet.org";
	public static int port = 6667;
	public static String botname = "Andryaid";

	public static void main(String[] args) throws Exception {
		
		IRCbot bot = new IRCbot(botname);
		bot.setVerbose(true);
		bot.connect(server, port);
		bot.joinChannel(channel);
		
	}

}
