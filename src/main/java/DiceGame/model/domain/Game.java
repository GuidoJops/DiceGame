package DiceGame.model.domain;


import lombok.*;

@Getter 
@Setter
@ToString
public class Game {
	
	private int diceA;
	private int diceB;
	private boolean win;
	
	public Game() {
		diceA = (int)(Math.random()*6)+1;
		diceB = (int)(Math.random()*6)+1;
		win = isWin();
	}

	public boolean isWin() {
		return diceA + diceB == 7;
	}
}
