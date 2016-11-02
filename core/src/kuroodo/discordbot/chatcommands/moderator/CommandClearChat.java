package kuroodo.discordbot.chatcommands.moderator;

import java.util.ArrayList;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandClearChat extends ChatCommand {
	// TODO: Properly implement this feature and make code clean
	public CommandClearChat() {
		isModCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		Init.getJDA().getAccountManager().setGame("Busy...");
		Init.getJDA().getAccountManager().setIdle(true);

		MessageHistory messageHistory1 = getMsgHistory();
		MessageHistory messageHistory2 = getMsgHistory();
		System.out.println("clear: " + commandParameters);

		if (commandParameters.equals("all")) {
			int totalMessages = messageHistory1.retrieveAll().size();
			final int deleteLimit = 100;
			if (totalMessages > deleteLimit) {
				// int timesToLoop = totalMessages / deleteLimit;
				// for (int loopCount = 0; loopCount < timesToLoop; loopCount++)
				// {
				// try {
				// event.getChannel().deleteMessages(messageHistory2.retrieve(deleteLimit));
				// messageHistory2 = getMsgHistory();
				// } catch (RateLimitedException e) {
				// System.out.println("Got rate limited while deleting.");
				// loopCount--;
				// }
				//
				// }

				event.getChannel().deleteMessages(messageHistory2.retrieve(deleteLimit));
			} else {
				event.getChannel().deleteMessages(messageHistory2.retrieveAll());
			}

			// for (Message msg : messageHistory.retrieveAll()) {
			// msg.deleteMessage();
			// deletedMsgCount++;
			// }

			sendMessage("Deleted " + totalMessages + " messages!");
		} else {
			User user = JDAHelper.getUserByID(commandParams);

			if (user == null) {
				user = JDAHelper.getUserByUsername(commandParams);

				if (user == null) {
					if (!event.getMessage().getMentionedUsers().isEmpty()) {
						user = event.getMessage().getMentionedUsers().get(0);
					}
				}
			}

			if (user != null) {
				System.out.println("In");
				ArrayList<Message> messages = new ArrayList<>();
				for (Message msg : messageHistory1.retrieveAll()) {
					if (msg.getAuthor() == user) {
						messages.add(msg);
					}
				}

				event.getChannel().deleteMessages(messages);

			}
		}
		System.out.println("out");
		Init.getJDA().getAccountManager().setGame("Type !help For Help (;");
		Init.getJDA().getAccountManager().setIdle(false);
	}

	private MessageHistory getMsgHistory() {
		return new MessageHistory(event.getChannel());
	}

	// Used for none-command purposes
	public void executeCommand(String commandParams, TextChannel channel) {
		MessageHistory messageHistory = new MessageHistory(channel);
		if (commandParams.equals("all")) {
			for (Message msg : messageHistory.retrieveAll()) {
				msg.deleteMessage();
			}
		}
	}

	@Override
	public String info() {
		return "Clear the chat (or a specific users messages) for the channel the command was sent on.\n"
				+ "Usage: !clearchat all/username \nExample: !clearchat all or !clearchat somefaggot123";
	}

}
