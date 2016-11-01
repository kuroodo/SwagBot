package kuroodo.discordbot.games.tictactoe;

public class BoardSlot {
	private char currentSymbol;
	private char startingSymbol;

	private boolean marked = false;

	public BoardSlot(char symbol) {
		currentSymbol = symbol;
		startingSymbol = symbol;
	}

	public void markAs(char symbol) {
		marked = true;
		currentSymbol = symbol;
	}

	public char getCurrentSymbol() {
		return currentSymbol;
	}

	public boolean isMarkedAs(char mark) {
		return currentSymbol == mark;
	}

	public boolean isMarked() {
		return marked;
	}

	public char getStartSymbol() {
		return startingSymbol;
	}

	public void resetToStart() {
		marked = false;
		currentSymbol = startingSymbol;
	}
}
