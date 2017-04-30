package kuroodo.discordbot.trivia.leaderboards.xml;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class PlayerBoardConfigurator {

	public static String getPlayerTotalScore(String userID) {
		XmlReader xmlReader = new XmlReader();
		try {
			Element root = xmlReader.parse(Gdx.files.internal("trivia/scores/player_" + userID + ".xml"));
			Element data = root.getChildByName("data");

			return data.getAttribute("totalScore");
		} catch (IOException e) {
			return "ERROR";
		}
	}

	public static String getplayerTotalAnswers(String userID) {
		XmlReader xmlReader = new XmlReader();
		try {
			Element root = xmlReader.parse(Gdx.files.internal("trivia/scores/player_" + userID + ".xml"));
			Element data = root.getChildByName("data");

			return data.getAttribute("totalAnswers");
		} catch (IOException e) {
			return "ERROR";
		}
	}

	public static String getPlayerPersonamMessage(String userID) {
		XmlReader xmlReader = new XmlReader();
		try {
			Element root = xmlReader.parse(Gdx.files.internal("trivia/scores/player_" + userID + ".xml"));
			Element data = root.getChildByName("data");

			return data.getText();
		} catch (IOException e) {
			return "ERROR";
		}
	}

	public static String getPlayerIDByFile(FileHandle file) {
		XmlReader xmlReader = new XmlReader();

		try {
			Element root = xmlReader.parse(file);

			return root.getName();
		} catch (IOException e) {
			return "ERROR";
		}
	}

	public static boolean containsPlayerData(String userID) {
		FileHandle file = new FileHandle("trivia/scores/player_" + userID + ".xml");
		System.out.println("Does player data exist? " + file.exists());
		return file.exists();
	}

	public static void changePlayerData(String userID, EXmlAttributes attribute, String newData) {
		String totalScore = getPlayerTotalScore(userID);
		String totalAnswers = getplayerTotalAnswers(userID);
		String personalMessage = getPlayerPersonamMessage(userID);

		switch (attribute) {
		case SCORE:
			totalScore = newData;
			break;
		case ANSWERS:
			totalAnswers = newData;
			break;
		case PERSONAL_MESSAGE:
			personalMessage = newData;
			break;
		default:
			System.out.println("ERROR UPDATING TRIVIA INFO FOR PLAYER: " + userID + " TRIED TO UPDATE: " + attribute
					+ " WITH VALUE OF: " + newData);
		}

		XMLEditor.generatePlayerBoardStructure(userID, totalScore, totalAnswers, personalMessage);
	}

	public static void createNewPlayerData(String userID) {
		String totalScore = "0", totalAnswers = "0", personalMessage = "";
		XMLEditor.generatePlayerBoardStructure(userID, totalScore, totalAnswers, personalMessage);
	}
}
