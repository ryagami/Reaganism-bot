package ircbot;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.PircBot;

public class IRCbot extends PircBot {

	static String greetings_string;
	static String partings_string;
	static ArrayList<String> greetings = new ArrayList<String>();
	static ArrayList<String> partings = new ArrayList<String>();
	
	static ArrayList<Command> commands;
	
	
	
	static {
		final Charset ENCODING = StandardCharsets.UTF_8;
		String iFile = "greetings.txt";
		Path iPath = Paths.get(iFile);
		try (Scanner sc = new Scanner(iPath, ENCODING.name())) {
			greetings_string = sc.nextLine();
			partings_string = sc.nextLine();
			greetings = new ArrayList<String>(Arrays.asList(greetings_string.split(", ")));
			partings = new ArrayList<String>(Arrays.asList(partings_string.split(", ")));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public IRCbot(String name) {
		this.setName(name);
		this.setAutoNickChange(true);
	}
	
	@Override
	protected void onJoin(String channel, String sender, String login, String hostname) {
		super.onJoin(channel, sender, login, hostname);
		if (sender.equalsIgnoreCase(this.getNick()))
			sendMessage(channel, greetings.get(new Random().nextInt(greetings.size())) + "!");
		else
			sendMessage(channel, greetings.get(new Random().nextInt(greetings.size())) + ", " + sender + "!");
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		super.onMessage(channel, sender, login, hostname, message);
		if (message.indexOf(".stahp") == 0) {
			quitServer(partings.get(new Random().nextInt(partings.size())) + "!");
			
			final Charset KODIRANJE = StandardCharsets.UTF_8;
			String oFile = "greetings.txt";
			Path oPath = Paths.get(oFile);
			try {
				Files.write(oPath, Arrays.asList(greetings_string), KODIRANJE);
				Files.write(oPath, Arrays.asList(partings_string), KODIRANJE, StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		
		else
		if (message.indexOf(".help") == 0) {
			sendMessage(channel, "This is the help command.");
			sendMessage(channel, ".help [command name] - Shows help for .[command name]");
			sendMessage(channel, "List of commands:");
		}
		
		else
		if (message.indexOf(".test") == 0)
			sendMessage(channel, "rya iz dum lol");
		
		else
		if (message.indexOf(this.getNick()) >= 0) {
			if (message.indexOf("") >= 0)
				sendMessage(channel, greetings.get(new Random().nextInt(greetings.size())) + ", " + sender + "!");
		}
		
		else
		if (Pattern.compile("good night", Pattern.CASE_INSENSITIVE).matcher(message).find()) {
			
		}
		
		else
		if (Pattern.compile("([=:;xX].?[(<]|D.?[=:;xX])").matcher(message).find()) {
			Random rand = new Random();
			switch (rand.nextInt(4)) {
			case 0:
				sendMessage(channel, "What's wrong, fren?");
				break;
			case 1:
				sendMessage(channel, "We love you, fren! <3");
				break;
			case 2:
				sendAction(channel, "snekhuggles " + sender);
				break;
			case 3:
			default:
				sendMessage(channel, "Here! Have a snek pic! http://i.imgur.com/gZp2pUl.jpg");
				break;
			}
		}
		
//		else
//		if (Pattern.compile("[xX]D").matcher(message).find()) {
//			sendMessage(channel, "lol");
//		}
		
		else 
		if (Pattern.compile("([=:;].?[)>D])").matcher(message).find()) {
			Random rand = new Random();
			switch (rand.nextInt(4)) {
			case 0:
			case 1:
			case 2:
				sendMessage(channel, "<3");
				break;
			case 3:
			default:
				sendAction(channel, "hugs everyone");
				break;
			}
		}
		
		else
		if (message.indexOf("<_<") >= 0) {
			sendMessage(channel, ">_>");
		}
		
		else
		if (message.indexOf(">_>") >= 0) {
			sendMessage(channel, "<_<");
		}
		
		else
		if (message.equalsIgnoreCase("time")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			sendMessage(channel, sender + ": " + sdf.format(Calendar.getInstance().getTime()) + "Z");
		}
		
		else {
			Pattern greet_regex = Pattern.compile(".add -g (.+)");	
			Pattern part_regex = Pattern.compile(".add -p (.+)");
			Matcher greet_matcher = greet_regex.matcher(message);
			Matcher part_matcher = part_regex.matcher(message);
			if (greet_matcher.find()) {
				String new_greet = greet_matcher.group(1);
				greetings.add(new_greet);
				greetings_string += ", " + new_greet;
				sendMessage(channel, "Added " + new_greet + " to greetings!");
			}
			if (part_matcher.find()) {
				String new_part = part_matcher.group(1);
				partings.add(new_part);
				partings_string += ", " + new_part;
				sendMessage(channel, "Added " + new_part + " to partings!");
			}
		}
	}
	
	@Override
	protected void onPrivateMessage(String sender, String login, String hostname, String message) {
		super.onPrivateMessage(sender, login, hostname, message);
		onMessage(sender, sender, login, hostname, message);
	}
	
	@Override
	protected void onAction(String sender, String login, String hostname, String target, String action) {
		super.onAction(sender, login, hostname, target, action);
		if (target.equalsIgnoreCase(this.getNick()))
			target = sender;
		if (Pattern.compile("[=:].?[(]").matcher(action).find()) {
			Random rand = new Random();
			switch(rand.nextInt(4)) {
			case 0:
				sendMessage(target, "What's wrong, fren?");
				break;
			case 1:
				sendMessage(target, "We love you, fren! <3");
				break;
			case 2:
				sendAction(target, "snekhuggles " + sender);
				break;
			case 3:
			default:
				sendMessage(target, "Here! Have a snek pic! http://i.imgur.com/gZp2pUl.jpg");
				break;
			}
		}
		
//		else
//		if (Pattern.compile("[xX]D").matcher(action).find()) {
//			sendMessage(target, "lol");
//		}
		
		else 
		if (Pattern.compile("([=:;].?[)>D])").matcher(action).find()) {
			Random rand = new Random();
			switch (rand.nextInt(4)) {
			case 0:
			case 1:
			case 2:
				sendMessage(target, "<3");
				break;
			case 3:
			default:
				sendAction(target, "hugs everyone");
				break;
			}
		}
		
		else
		if (action.indexOf("<_<") >= 0) {
			sendMessage(target, ">_>");
		}
		
		else
		if (action.indexOf(">_>") >= 0) {
			sendMessage(target, "<_<");
		}
	}
	
}
