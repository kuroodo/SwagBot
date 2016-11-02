package kuroodo.discordbot.chatcommands;

import java.util.Random;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandMagicBall extends ChatCommand {
	private Random rand;

	// TODO: Move/manage responses via XML/Json file instead of hardcoding
	
	// Total response counts for each response types. Used to generate a random
	// integer with a range of the count
	public int responseCountWhy = 11, responseCountHow = 11, responseCountIs = 16, responseCountWhen = 13,
			responseCountWill = 16, responseCountWhat = 10, responseCountWho = 10, responseCountOther = 10,
			responseCountAre = 11, responseCountBlank = 4;

	public CommandMagicBall() {
		super();
		rand = new Random();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		// Split the users question and get the first word
		String splitString[] = commandParameters.split(" ", 2);
		String firstWord = splitString[0];

		if (!firstWord.isEmpty()) {
			switch (firstWord.toLowerCase()) {
			case "why":
				answerWhy(rand.nextInt(responseCountWhy));
				break;
			case "how":
				answerHow(rand.nextInt(responseCountHow));
				break;
			case "is":
				answerIs(rand.nextInt(responseCountIs));
				break;
			case "when":
				answerWhen(rand.nextInt(responseCountWhen));
				break;
			case "will":
				answerWill(rand.nextInt(responseCountWill));
				break;
			case "what":
				answerWhat(rand.nextInt(responseCountWhat));
				break;
			case "who":
				answerWho(rand.nextInt(responseCountWhat));
				break;
			case "are":
				answerAre(rand.nextInt(responseCountAre));
				break;
			default:
				answerOther(rand.nextInt(responseCountOther));
				break;
			}
		} else {
			answerBlank(rand.nextInt(responseCountBlank));
		}
	}

	private void answerBlank(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Yes!?");
			break;
		case 1:
			sendMessage("What do you want from me");
			break;
		case 2:
			sendMessage("Why did you mention me?");
			break;
		case 3:
			sendMessage("You dare disturb me?");
			break;
		case 4:
			sendMessage("I've been summoned!");
			break;
		}
	}

	private void answerAre(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Yes!");
			break;
		case 1:
			sendMessage("Perhaps in the foreseeable future.");
			break;
		case 2:
			sendMessage("Maybe.");
			break;
		case 3:
			sendMessage("don't count on it!");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("With a little bit of luck perhaps");
			break;
		case 6:
			boolean exit = false;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user.getUsername().equals(Init.getBotName())) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("Possibly");
			break;
		case 8:
			sendMessage("Without a fucking doubt!");
			break;

		case 9:
			sendMessage("Well, not exacyly");
			break;

		case 10:
			sendMessage("Nope");
			break;
		case 11:
			sendMessage("Just like sprinkles on icecream.");
			break;

		}

	}

	private void answerWho(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Idk.");
			break;
		case 1:
			sendMessage("Perhaps in the foreseeable future I'll be able to answer that.");
			break;
		case 2:
			sendMessage("One of us.");
			break;
		case 3:
			sendMessage("Nobody");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("Hitler");
			break;
		case 6:
			boolean exit = false;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user.getUsername().equals(Init.getBotName())) {
					sendMessage((user.getAsMention()));

					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("A pink fluffy unicorn walking on rainbows");
			break;
		case 8:
			sendMessage("Ask again and maybe I'll be in the mood to answer");
			break;

		case 9:
			sendMessage("They're all the same shit");
			break;

		case 10:
			sendMessage("I don't think you'd want to know");
			break;

		}

	}

	private void answerOther(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Yes!");
			break;
		case 1:
			sendMessage("Perhaps in the foreseeable future.");
			break;
		case 2:
			sendMessage("Maybe.");
			break;
		case 3:
			sendMessage("don't count on it!");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("With a little bit of luck perhaps");
			break;
		case 6:
			boolean exit = false;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user.getUsername().equals(Init.getBotName())) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("How about fugg off m8");
			break;
		case 8:
			sendMessage("Without a fucking doubt!");
			break;

		case 9:
			sendMessage("No too bad you're an asshore");
			break;

		case 10:
			sendMessage("I must say no");
			break;

		}

	}

	public void answerWhy(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Idk.");
			break;
		case 1:
			sendMessage("Blame A E S T H E T I C S");
			break;
		case 2:
			sendMessage("Life is simply a bitch");
			break;
		case 3:
			sendMessage("Just because.");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("Do you really want me to answer that?");
			break;
		case 6:
			boolean exit = false;
			int loopCount = 0;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user != Init.getJDA().getSelfInfo() && user != event.getAuthor()) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}

				loopCount++;

				if (loopCount == 10) {
					sendMessage(("I'm out of ideas"));
					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("How about fugg off m8");
			break;
		case 8:
			sendMessage("How the fuck should I know?");
			break;
		case 9:
			sendMessage("No too bad you're an asshore");
			break;
		case 10:
			sendMessage("Blame the jews");
			break;
		case 11:
			sendMessage("Why are you so annoying?");
			break;

		}
	}

	public void answerWhen(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Soon!");
			break;
		case 1:
			sendMessage("Perhaps in the foreseeable future.");
			break;
		case 2:
			sendMessage("Maybe right now");
			break;
		case 3:
			sendMessage("Don't count on it!");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("Never");
			break;
		case 6:
			boolean exit = false;
			int loopCount = 0;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user != Init.getJDA().getSelfInfo() && user != event.getAuthor()) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}

				loopCount++;

				if (loopCount == 10) {
					sendMessage(("I'm out of ideas"));
					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("How about fugg off m8");
			break;
		case 8:
			sendMessage("How the fuck should I know?");
			break;

		case 9:
			sendMessage("No too bad you're an asshore");
			break;

		case 10:
			sendMessage("I SAY NEVER");
			break;
		case 11:
			sendMessage("Give it a few minutes");
			break;
		case 12:
			sendMessage("In the next leap year!");
			break;
		case 13:
			sendMessage("When Half Life 3 comes out!");
			break;

		}
	}

	public void answerHow(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Idk.");
			break;
		case 1:
			sendMessage("Science...BITCH!");
			break;
		case 2:
			sendMessage("El senor Jesu'cristo!");
			break;
		case 3:
			sendMessage("Step 1.Open it. Step 2.Bring it out. Step 3.Make sure its hard. "
					+ "Step 4.Put it in. Step 5.That's how you put frozen food in the microwave :D");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("The same way that Hitler did it");
			break;
		case 6:
			boolean exit = false;
			int loopCount = 0;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user != Init.getJDA().getSelfInfo() && user != event.getAuthor()) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}

				loopCount++;

				if (loopCount == 10) {
					sendMessage(("I'm out of ideas"));
					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("How about fugg off m8");
			break;
		case 8:
			sendMessage("How? You ask How?!?!? THIS IS FUCKING INSANE");
			break;

		case 9:
			sendMessage("No too bad you're an asshore");
			break;

		case 10:
			sendMessage("I must say no");
			break;

		case 11:
			// http://lmgtfy.com/?q=What+The+Hell
			String url = "http://lmgtfy.com/?q=";
			String[] split = commandParameters.split(" ");

			for (String word : split) {
				url = url + word + "+";
			}

			sendMessage(url);
			break;

		}
	}

	public void answerIs(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Yes!");
			break;
		case 1:
			sendMessage("Perhaps in the foreseeable future.");
			break;
		case 2:
			sendMessage("Maybe.");
			break;
		case 3:
			sendMessage("don't count on it!");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("With a little bit of luck perhaps");
			break;
		case 6:
			boolean exit = false;
			int loopCount = 0;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user != Init.getJDA().getSelfInfo() && user != event.getAuthor()) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}

				loopCount++;

				if (loopCount == 10) {
					sendMessage(("I'm out of ideas"));
					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("How about fugg off m8");
			break;
		case 8:
			sendMessage("Without a fucking doubt!");
			break;

		case 9:
			sendMessage("No too bad you're an asshore");
			break;

		case 10:
			sendMessage("I must say no");
			break;
		case 11:
			sendMessage("More than anything");
			break;
		case 12:
			sendMessage("Only if you spread butter over it");
			break;
		case 13:
			sendMessage("It depends really.");
			break;
		case 14:
			sendMessage("You're fucking annoying");
			break;
		case 15:
			sendMessage("Look at the bigger picture!");
			break;
		case 16:
			sendMessage("How the fuck should I know?");
			break;

		}
	}

	public void answerWill(int randomNumber) {
		switch (randomNumber) {
		case 0:
			sendMessage("Yes!");
			break;
		case 1:
			sendMessage("Perhaps in the foreseeable future.");
			break;
		case 2:
			sendMessage("Maybe.");
			break;
		case 3:
			sendMessage("don't count on it!");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("With a little bit of luck perhaps");
			break;
		case 6:
			boolean exit = false;
			int loopCount = 0;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user != Init.getJDA().getSelfInfo() && user != event.getAuthor()) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}

				loopCount++;

				if (loopCount == 10) {
					sendMessage(("I'm out of ideas"));
					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("How about fugg off m8");
			break;
		case 8:
			sendMessage("Without a fucking doubt!");
			break;
		case 9:
			sendMessage("No too bad you're an asshore");
			break;
		case 10:
			sendMessage("I must say no");
			break;
		case 11:
			sendMessage("How the fuck should I know?");
			break;
		case 12:
			sendMessage("More than anything");
			break;
		case 13:
			sendMessage("Only if you spread butter over it");
			break;
		case 14:
			sendMessage("It depends really.");
			break;
		case 15:
			sendMessage("You're fucking annoying");
			break;
		case 16:
			sendMessage("Look at the bigger picture!");
			break;

		}
	}

	private void answerWhat(int randomNumber) {
		// Something fun. If user asked what is love, respond with baby don't
		// hurt me
		String[] splitWord = commandParameters.split(" ");
		int matchedWords = 0;

		for (int wordCount = 0; wordCount < splitWord.length; wordCount++) {
			System.out.println(splitWord[wordCount].toLowerCase());
			if (splitWord[wordCount].toLowerCase().equals("what") || splitWord[wordCount].toLowerCase().equals("is")
					|| splitWord[wordCount].toLowerCase().equals("love")
					|| splitWord[wordCount].toLowerCase().equals("love?")) {
				matchedWords++;
			}

			if (matchedWords == 3) {
				sendMessage("Baby don't hurt me");
				return;
			} else if (wordCount > 2) {
				break;
			}

		}

		switch (randomNumber) {
		case 0:
			sendMessage("Idk.");
			break;
		case 1:
			// http://lmgtfy.com/?q=What+The+Hell
			String url = "http://lmgtfy.com/?q=";
			String[] split = commandParameters.split(" ");

			for (String word : split) {
				url = url + word + "+";
			}

			sendMessage(url);

			break;
		case 2:
			sendMessage("It could be the other way around actually");
			break;
		case 3:
			sendMessage("for fuck sake these questions are getting rediculous!");
			break;
		case 4:
			sendMessage("Look deep into your heart and you will find the answer");
			break;
		case 5:
			sendMessage("21");
			break;
		case 6:
			boolean exit = false;
			do {
				User user = JDAHelper.getRandomOnlineUser();

				if (user.getUsername().equals(Init.getBotName())) {
					sendMessage(("Perhaps " + user.getAsMention() + " has the answer to that?"));

					exit = true;
				}
			} while (!exit);
			break;
		case 7:
			sendMessage("How about fugg off m8");
			break;
		case 8:
			sendMessage("Without a fucking doubt!");
			break;

		case 9:
			sendMessage("I think instead you should be asking yourself, why are you talking to a robot?");
			break;

		case 10:
			sendMessage("Simple, cake!");
			break;

		}

	}

	@Override
	public String info() {
		return "Ask Questions, find out secrets, or predict the future!."
				+ " Usage: !magicball [text] \nExample: !magicball Will it rain today?";
	}

}
