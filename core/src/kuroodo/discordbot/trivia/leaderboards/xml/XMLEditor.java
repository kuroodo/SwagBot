package kuroodo.discordbot.trivia.leaderboards.xml;

import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

public class XMLEditor {
	public static void generatePlayerBoardStructure(String userID, String totalScore, String totalAnswers, String personalMessage) {

		FileWriter fileWriter;

		XmlWriter xmlWriter;

		try {
			fileWriter = new FileWriter("trivia/scores/player_" + userID + ".xml", false);
			xmlWriter = new XmlWriter(fileWriter);

			try {
				xmlWriter.element(userID).element("data").attribute("totalScore", totalScore)
						.attribute("totalAnswers", totalAnswers).text(personalMessage).pop().pop().flush();
				xmlWriter.close();
			} catch (IOException e) {
				System.out.println("catch");
			}
		} catch (IOException e) {
			System.out.println("catch2");
		}
	}
}
