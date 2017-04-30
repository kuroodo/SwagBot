package kuroodo.discordbot.trivia.leaderboards.xml;

public class PlayerBoardStructure {
	private int totalScore, totalAnswers, totalMisses;
	private String personalMessage;

	public PlayerBoardStructure(int totalScore, int totalAnswers, int totalMisses, String personalMessage) {
		this.totalScore = totalScore;
		this.totalAnswers = totalAnswers;
		this.totalMisses = totalMisses;
		this.personalMessage = personalMessage;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getTotalAnswers() {
		return totalAnswers;
	}

	public void setTotalAnswers(int totalAnswers) {
		this.totalAnswers = totalAnswers;
	}

	public int getTotalMisses() {
		return totalMisses;
	}

	public void setTotalMisses(int totalMisses) {
		this.totalMisses = totalMisses;
	}

	public String getPersonalMessage() {
		return personalMessage;
	}

	public void setPersonalMessage(String personalMessage) {
		this.personalMessage = personalMessage;
	}

}
