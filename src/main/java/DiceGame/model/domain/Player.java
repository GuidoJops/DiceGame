package DiceGame.model.domain;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter 
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "players")
public class Player {
	
	@Id
	private String id;

	@NotBlank(message = "Name must be at least one character.")
	private String name;

	//e-mail as 'userName'
	@Email(message = "Username must be an e-mail format. 'xx@mail.com'")
	@NotBlank (message = "Username must be at least one character.")
	private String userName;

	@JsonIgnore
	@NotBlank (message = "Password must be at least one character.")
	private String password;

	private Date registDate;
	private double winSuccess;
	private int victories;
	private List<Game> games;
	private List<Role> roles;


	public Player(String name, String userName, String password) {
		this.name = name;
		this.userName = userName;
		this.password = password;
		registDate = new Date();
		winSuccess = 0;
		victories = 0;
		games = new ArrayList<Game>();
		roles = Arrays.asList(
				new Role(2L,ERole.ROLE_USER)); // User Role by default
	}
	

	public void updateWinSuccess() {
		setWinSuccess(winSuccessCalculator());
	}

	public double winSuccessCalculator() {
		int totalGames = games.size() + 1;
		double result = 0;
		
		if (totalGames >= 1) {
			result = victories / (double) totalGames * 100;
		}
		return Math.round(result * 10d) / 10d;
	}
	
	public void resetPlayer() {
		winSuccess = 0;
		victories = 0;
		games = new ArrayList<>();
		
	}

}
