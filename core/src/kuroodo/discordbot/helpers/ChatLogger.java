package kuroodo.discordbot.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ChatLogger {
	private static int messageCount = 0;

	public ChatLogger() {

	}

	public static void logMessage(String message) {
		if (!Gdx.files.local("TEMPmessageHistory.txt").exists()) {
			System.out.println("CREATING NEW MESSAGE HISTORY FILE");
		}

		FileHandle logFile = Gdx.files.local("TEMPmessageHistory.txt");

		logFile.writeString(message, true);
		logFile.writeString("\n", true);

		messageCount++;
	}

	public static void closeLog() {
		if (!Gdx.files.local("TEMPmessageHistory.txt").exists()) {
			System.out.println("CANNOT CLOSE LOG. FILE DOESN'T EXIST");
		} else {
			FileHandle logFile = Gdx.files.local("TEMPmessageHistory.txt");
			logFile.writeString("\n\n", true);
			logFile.writeString("Total Messages: " + messageCount, true);

			int fileInstances = 1;
			do {
				if (Gdx.files.local("messageHistory" + fileInstances + ".txt").exists()) {
					fileInstances++;
				} else {
					break;
				}
			} while (true);

			logFile.moveTo(Gdx.files.local("messageHistory" + fileInstances + ".txt"));
		}
	}
}
