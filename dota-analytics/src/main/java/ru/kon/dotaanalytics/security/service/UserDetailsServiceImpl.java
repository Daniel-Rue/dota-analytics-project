package ru.kon.dotaanalytics.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kon.dotaanalytics.entity.User;
import ru.kon.dotaanalytics.repository.UserRepository;
import ru.kon.dotaanalytics.util.ErrorMessages;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND_BY_EMAIL, email))
            );

        return new UserDetailsImpl(user);
    }
}
