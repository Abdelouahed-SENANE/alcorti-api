package ma.youcode.api.security.services;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LogManager.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String cinOrEmail) throws UsernameNotFoundException {

        return userRepository.findByCinOrEmail(cinOrEmail , cinOrEmail)
                .map(user -> {
                    user.setLoggedAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .map(UserSecurity::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
