package kuroodo.discordbot.trivia.leaderboards;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import kuroodo.discordbot.trivia.leaderboards.xml.PlayerBoardConfigurator;

public class RankSorter {
	// TODO: Refactor and improve code/make more efficient
	public static String getPlayerRank(String userID) {
		String boardRank = "ERROR";

		ArrayList<PlayerData> playerData = new ArrayList<>();

		FilenameFilter xmlFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		};

		String filePath = "trivia/scores";
		File dir = new File(filePath);
		File[] files = dir.listFiles(xmlFilter);
		if (files != null) {
			if (files.length != 0) {
				for (File file : files) {
					playerData.add(new PlayerData(
							PlayerBoardConfigurator.getPlayerIDByFile(Gdx.files.internal(file.getPath()))));

				}
			}
		}

		ArrayList<PlayerData> leaderBoard = new ArrayList<>();
		if (!playerData.isEmpty()) {
			for (PlayerData data : playerData) {
				if (leaderBoard.isEmpty()) {
					leaderBoard.add(data);
				} else if (data != leaderBoard.get(0) && data.getScore() >= leaderBoard.get(0).getScore()) {
					leaderBoard.add(0, data);
				} else {
					leaderBoard.add(data);
				}
			}

			for (PlayerData data : leaderBoard) {
				if (data.getUserId().equals(userID)) {
					boardRank = String.valueOf(leaderBoard.indexOf(data) + 1);
				}

			}
		}

		return boardRank;
	}

	public static class PlayerData {
		private String userId;
		private int score;

		public PlayerData(String userId) {
			this.userId = userId;
			score = Integer.parseInt(PlayerBoardConfigurator.getPlayerTotalScore(userId));
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

	}
}
