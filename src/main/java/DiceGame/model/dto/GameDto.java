package DiceGame.model.dto;


import lombok.*;

@Getter 
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameDto {
	
	private int diceA;
	private int diceB;
	private boolean win;

}
