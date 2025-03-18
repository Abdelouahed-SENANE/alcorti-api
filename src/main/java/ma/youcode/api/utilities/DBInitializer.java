package ma.youcode.api.utilities;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.models.users.Admin;
import ma.youcode.api.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            Admin admin = Admin.builder()
                    .cin("HH1200")
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@mail.com")
                    .password(passwordEncoder.encode("admin2024"))
                    .active(true)
                    .isEmailVerified(true)
                    .build();
            userRepository.save(admin);
        }
    }
}
