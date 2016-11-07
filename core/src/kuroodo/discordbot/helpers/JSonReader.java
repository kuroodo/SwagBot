/**
 * Minimal-Json https://github.com/ralfstx/minimal-json/blob/master/README.md
 * Using under the MIT license https://opensource.org/licenses/MIT
 */
package kuroodo.discordbot.helpers;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class JSonReader {
	public static String getLogin() {
		String botToken = "";

		Reader reader;
		try {
			reader = new FileReader("config.json");
			JsonObject object = Json.parse(reader).asObject();

			botToken = object.get("bottoken").asString();
		} catch (IOException e) {
			System.out.println("Could not get bot token!");
		}

		return botToken;
	}

	public static boolean getIsDevMode() {
		boolean isDevMode = false;

		Reader reader;
		try {
			reader = new FileReader("config.json");
			JsonObject object = Json.parse(reader).asObject();

			isDevMode = object.get("devmode").asBoolean();
		} catch (IOException | NullPointerException e) {
			System.out.println("Could not read/get dev mode boolean!");
		}

		return isDevMode;
	}

	public static String getSuperUserPassword() {
		String superUserPassword = "";

		Reader reader;
		try {
			reader = new FileReader("config.json");
			JsonObject object = Json.parse(reader).asObject();

			superUserPassword = object.get("superuserpass").asString();
		} catch (IOException | NullPointerException e) {
			System.out.println("Could not read/get super user password!");
		}

		return superUserPassword;
	}

	public static String getSourceCodeLink() {
		String sourceLink = "";

		Reader reader;
		try {
			reader = new FileReader("config.json");
			JsonObject object = Json.parse(reader).asObject();

			sourceLink = object.get("sourcecode").asString();
		} catch (IOException e) {
			System.out.println("Could not get source code link!");
		}

		return sourceLink;
	}

}
