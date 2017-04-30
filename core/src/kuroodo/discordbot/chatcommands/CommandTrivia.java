package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import kuroodo.discordbot.trivia.TriviaSession;
import kuroodo.discordbot.trivia.leaderboards.RankSorter;
import kuroodo.discordbot.trivia.leaderboards.xml.PlayerBoardConfigurator;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandTrivia extends ChatCommand {

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (event.getChannel() != JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("triviachannel")))
			return;

		if (commandParams.isEmpty()) {
			if (!TriviaSession.isSessionActive) {
				Init.addNewListener(new TriviaSession(event.getMember()));
			}
		} else {
			switch (commandParams.toLowerCase()) {
			case "myscore":
				String message = event.getAuthor().getAsMention() + " Your score is: **"
						+ PlayerBoardConfigurator.getPlayerTotalScore(event.getAuthor().getId()) + "**";
				message = message + " (Rank #" + RankSorter.getPlayerRank(event.getAuthor().getId()) + ")";
				sendMessage(message);
				break;
			}
		}
	}

	@Override
	public String info() {
		return "Play a game of Trivia based on video games! The command is only accessable via a specified trivia text channel\nUsage: !trivia\nAvailable parameters: myscore (example: !trivia myscore)";
	}

}
