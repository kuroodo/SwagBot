package kuroodo.discordbot.trivia;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import kuroodo.discordbot.trivia.leaderboards.xml.EXmlAttributes;
import kuroodo.discordbot.trivia.leaderboards.xml.PlayerBoardConfigurator;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class TriviaSession extends JDAListener {

	public static boolean isSessionActive = false;

	private TextChannel triviaChannel;
	private String questionPretext, question, answer;
	private boolean isAnswered = false;

	private XmlReader xmlReader;
	private Element root;

	public TriviaSession(Member member) {
		listenerName = "trivia";

		setUpTrivia(member);

		Timer.schedule(new Task() {

			@Override
			public void run() {
				if (!isAnswered) {
					triviaChannel.sendMessage("Times up! Nobody got the answer...\nThe answer was: '" + answer + "'\n")
							.queue();
					endTriviaSession();
				}
			}
		}, 15);
	}

	private void setUpTrivia(Member member) {
		triviaChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("triviachannel"));

		if (triviaChannel != null) {
			isSessionActive = true;

			xmlReader = new XmlReader();

			try {
				root = xmlReader.parse(Gdx.files.internal("trivia/trivia.xml"));

				Random rand = new Random(System.nanoTime());
				int elementIndex = rand.nextInt(root.getChildCount());

				Element triviaQuestion = root.getChild(elementIndex);
				questionPretext = triviaQuestion.getAttribute("pretext");
				question = triviaQuestion.getText();
				answer = triviaQuestion.getAttribute("answer");

				triviaChannel
						.sendMessage("--- " + member.getAsMention()
								+ " started a new round of faggot Trivia. **You have 15 seconds. Get ready to answer!** ---")
						.queue();
				triviaChannel.sendMessage(questionPretext + "\n\n" + question).queue();
			} catch (IOException e) {
				endTriviaSession();
				System.out.println("TRIVIA ERROR");
				triviaChannel.sendMessage("Got an error. Could not read trivia question XML file :(").queue();
			}
		}

	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		if (event.getChannel() == triviaChannel && event.getAuthor() != Init.getJDA().getSelfUser()) {

			checkUserBoardExists(event.getMember());

			if (event.getMessage().getRawContent().toLowerCase().equals(answer.toLowerCase())) {
				if (triviaChannel != null) {
					isAnswered = true;
					manageMemberStats(event.getMember().getUser().getId());

					triviaChannel.sendMessage(event.getAuthor().getAsMention() + " Answered correctly!\nAnswer: '"
							+ answer + "'\n" + event.getMember().getEffectiveName() + "'s ScoreBoard: ```"
							+ getMemberData(event.getMember()) + "```").queue();

					endTriviaSession();
				} else {
					endTriviaSession();
				}
			}
		}

	}

	private String getMemberData(Member member) {
		int totalScore = Integer.parseInt(PlayerBoardConfigurator.getPlayerTotalScore(member.getUser().getId()));
		int totalAnswers = Integer.parseInt(PlayerBoardConfigurator.getplayerTotalAnswers(member.getUser().getId()));

		return "Score: " + totalScore + ", Answers: " + totalAnswers;
	}

	private void manageMemberStats(String userID) {
		int totalAnswers = Integer.parseInt(PlayerBoardConfigurator.getplayerTotalAnswers(userID));
		int totalScore = Integer.parseInt(PlayerBoardConfigurator.getPlayerTotalScore(userID));

		totalAnswers += 1;
		totalScore += 1;

		PlayerBoardConfigurator.changePlayerData(userID, EXmlAttributes.ANSWERS, String.valueOf(totalAnswers));
		PlayerBoardConfigurator.changePlayerData(userID, EXmlAttributes.SCORE, String.valueOf(totalScore));
	}

	private void checkUserBoardExists(Member member) {
		if (!PlayerBoardConfigurator.containsPlayerData(member.getUser().getId())) {
			PlayerBoardConfigurator.createNewPlayerData(member.getUser().getId());
		}
	}

	private void endTriviaSession() {
		isSessionActive = false;
		Init.removeListener(this);
	}

	@Override
	public void update(float delta) {
	}

}