package kuroodo.discordbot.chatcommands;

import java.io.File;
import java.io.FilenameFilter;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.client.handlers.ChatCommandHandler;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.enums.ECommands;
import kuroodo.discordbot.enums.ECommands.AdminCommands;
import kuroodo.discordbot.enums.ECommands.ModeratorCommands;
import kuroodo.discordbot.helpers.ChatHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.JDAInfo;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandHelp extends ChatCommand {

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		String message = event.getMessage().getRawContent();

		if (message.startsWith("!help")) {
			// Print out commands
			if (commandParameters.isEmpty()) {
				printUserCommands();
				if (ChatHelper.isUserAdmin(event.getAuthor())) {
					printAdminCommands();
				} else if (ChatHelper.isUserModerator(event.getAuthor())) {
					printModeratorCommands();
				}
			} else {
				sendCommandInfo();
			}
		} else if (message.equals("!info")) {
			sendBotInfo();
		}

	}

	private void printUserCommands() {
		String commands = "";
		// Gather and print user commands
		for (ECommands e : ECommands.values()) {
			commands = commands + " " + e.name().toLowerCase() + ";";
		}

		sendPrivateMessage(
				"For help with a specific command, use !help [commandname] (!help slap)\n\n");
		sendPrivateMessage("List of commands (don't use the semi-colons): " + commands);
	}

	private void printAdminCommands() {
		String adminCommands = "";
		for (AdminCommands e : AdminCommands.values()) {
			adminCommands = adminCommands + " " + e.name().toLowerCase() + ";";
		}

		for (ModeratorCommands e : ModeratorCommands.values()) {
			adminCommands = adminCommands + " " + e.name().toLowerCase() + ";";
		}

		sendPrivateMessage(
				"For help with a specific command, use !help [commandname] (!help slap))\n\n");
		sendPrivateMessage("List of Admin commands (don't use the semi-colons): " + adminCommands);
	}

	private void printModeratorCommands() {
		String modCommands = "";

		for (ModeratorCommands e : ModeratorCommands.values()) {
			modCommands = modCommands + " " + e.name().toLowerCase() + ";";
		}

		sendPrivateMessage(
				"For help with a specific command, use !help [commandname] (!help slap)\n\n");
		sendPrivateMessage("List of moderator commands (don't use the semi-colons): " + modCommands);
	}

	private void sendCommandInfo() {
		String commandWord = "";
		// If command name has a !
		if (commandParameters.substring(0, 1).equals("!")) {
			commandWord = commandParameters;
		} else {
			commandWord = "!" + commandParameters;
		}

		if (ChatCommandHandler.getCommands().contains(ChatCommandHandler.getCommand(commandWord))) {
			ChatCommand command;
			try {
				command = (ChatCommand) ChatCommandHandler.getCommand(commandWord).newInstance();
				System.out.println(userHasAccessToCommand(command));

				if (userHasAccessToCommand(command)) {
					sendPrivateMessage(command.info());
				} else {
					return;
				}

			} catch (InstantiationException | IllegalAccessException | NullPointerException e) {
				printMiscCommands(commandWord);
				e.printStackTrace();
			}
		} else {
			printMiscCommands(commandWord);
		}
	}

	private void printMiscCommands(String commandWord) {
		switch (commandWord) {
		case "!join":
		case "!play":
		case "!stop":
		case "!pause":
		case "!restart":
			sendPrivateMessage("A hidden command, these commands are for using the audio player\n\n"
					+ "!join [voicechannelname] makes bot join a specific voice channel. Example: !join General\n"
					+ "!play [file/url] [filelocation/url] OR !play makes the bot play an audio file from 2 different sources. "
					+ "Audio files that are not from a url are currently only supported by the system running the bot\n"
					+ "Example: !play url https://someurl.com/audiofile.mp3 OR !play file c:\\somefilelocation\\audiofile.mp3\n"
					+ "When trying to play a file after it's beened paused, just use !play\n\n"
					+ "!stop, !pause, !restart. !stop stops the audio stream, use !restart to start the stream again.\n"
					+ "!pause pauses the audio. Use !play to start playing again\n"
					+ "!restart plays the audio stream from the beginning");
			break;
		case "!soundboard":
			String sounds = "";

			FilenameFilter mp3Filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(".mp3")) {
						// filters files whose extension is .mp3
						return true;
					} else {
						return false;
					}
				}
			};

			String filePath = "sounds/";
			File dir = new File(filePath);
			File[] files = dir.listFiles(mp3Filter);

			if (files.length == 0) {
				System.out.println("No MP3 files found");
			} else {
				for (File file : files) {
					sounds = sounds + "!" + file.getName().toLowerCase().replaceFirst("[.][^.]+$", "") + " ";
				}
			}
			sendPrivateMessage("A hidden command, these commands are for playing special sounds with the audioplayer."
					+ " (see more: !help play)" + "\nUsage: !soundname (example !bradberry)\n" + "List of sounds\n\n"
					+ sounds);
			break;
		default:
			break;
		}
	}

	private void sendBotInfo() {
		String message = "```xl\n";

		message = message + "name: SwagBot";
		message = message + "\nversion: " + Init.VERSION;
		message = message + "\ncreator: Kuroodo";

		if (!JSonReader.getSourceCodeLink().isEmpty()) {
			message = message + "\nSource Code: " + JSonReader.getSourceCodeLink();
		}

		message = message + "\napi: JDA " + JDAInfo.VERSION + " https://github.com/DV8FromTheWorld/JDA";
		message = message + "\nframework: LibGDX https://libgdx.badlogicgames.com/";
		message = message + "\ntools: Minimal Json https://github.com/ralfstx/minimal-json/blob/master/README.md/";
		message = message + "\n```";
		sendMessage(message);
	}

	@Override
	public String info() {
		return "This command gives useful information and stuff";
	}

	private boolean userHasAccessToCommand(ChatCommand command) {
		if (command.isAdminCommand() && !ChatHelper.isUserAdmin(event.getAuthor())) {
			return false;
		} else if (isModCommand
				&& (!ChatHelper.isUserModerator(event.getAuthor()) && !ChatHelper.isUserAdmin(event.getAuthor()))) {
			return false;
		}

		return true;
	}

}
