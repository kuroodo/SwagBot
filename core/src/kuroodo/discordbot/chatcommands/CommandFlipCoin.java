package kuroodo.discordbot.chatcommands;

import java.util.Random;

import kuroodo.discordbot.entities.ChatCommand;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandFlipCoin extends ChatCommand {
	private Random rand;

	public CommandFlipCoin() {
		super();
		rand = new Random();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		rand.setSeed(System.nanoTime());
		int x = rand.nextInt(2);

		if (x == 0) {
			sendMessage(
					"```css\n" + event.getAuthor().getAsMention() + " flips a coin and it lands on " + "tails\n ```");
		} else {
			sendMessage(
					"```css\n" + event.getAuthor().getAsMention() + " flips a coin and it lands on " + "heads\n```");
		}

		event.getMessage().deleteMessage();
	}

	@Override
	public String info() {
		return "Flip a coin. Usage: !flipcoin";
	}

}