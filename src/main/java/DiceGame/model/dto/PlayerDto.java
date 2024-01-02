package DiceGame.model.dto;


import java.time.LocalDateTime;
import lombok.*;

@Getter 
@Setter 
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
	
	private String id;
	private String name;
	private String userName; // E-mail
	private LocalDateTime registrationDate;
	private double winSuccess;

}
