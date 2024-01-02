package DiceGame.utils;

import DiceGame.repository.IRoleRepository;
import DiceGame.model.domain.ERole;
import DiceGame.model.domain.Player;
import DiceGame.model.domain.Role;
import DiceGame.repository.IPlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private IRoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IPlayerRepository playerRepo;

    @Override
    public void run(String... args) throws Exception {

        //Roles and an Admin User are added the first time the program runs
        if (roleRepo.count() == 0) {
            roleRepo.saveAll(List.of(
                    new Role(1L, ERole.ROLE_ADMIN),
                    new Role(2L, ERole.ROLE_USER)
            ));
            log.info("USER and ADMIN roles created.");
        }
        if (playerRepo.count() == 0) {
            Player adminPlayer = new Player("DEFAULT-ADMIN",
                    "admin@admin.com",
                    passwordEncoder.encode("admin"));
            adminPlayer.setRoles(roleRepo.findAll());
            playerRepo.save(adminPlayer);
            log.info("'admin' user created.");
        }
    }
}
