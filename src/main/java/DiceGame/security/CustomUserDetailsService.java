package DiceGame.security;

import DiceGame.model.domain.Player;
import DiceGame.repository.IPlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//UserDetailService -> Administrador de credenciales de Usuario
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IPlayerRepository userRepo;

    public CustomUserDetailsService(IPlayerRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player user = userRepo.findByUserName(username).orElseThrow(()->
                new UsernameNotFoundException("No se encontró un usuario con userName:" + username));

        return new CustomUserDetails(user);
    }


}
