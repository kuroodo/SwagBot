package kuroodo.discordbot.chatcommands;

import java.util.Random;

import kuroodo.discordbot.entities.ChatCommand;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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
					event.getAuthor().getAsMention() + " ```css\n" + " flips a coin and it lands on " + "tails\n```");
		} else {
			sendMessage(
					event.getAuthor().getAsMention() + " ```css\n" + " flips a coin and it lands on " + "heads\n```");
		}

		event.getMessage().delete();
	}

	@Override
	public String info() {
		return "Flip a coin. Usage: !flipcoin";
	}

}
