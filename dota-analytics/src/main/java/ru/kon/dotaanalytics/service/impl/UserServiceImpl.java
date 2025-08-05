package ru.kon.dotaanalytics.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kon.dotaanalytics.dto.request.LoginRequest;
import ru.kon.dotaanalytics.dto.request.RegisterRequest;
import ru.kon.dotaanalytics.dto.response.JwtResponse;
import ru.kon.dotaanalytics.entity.Role;
import ru.kon.dotaanalytics.entity.User;
import ru.kon.dotaanalytics.entity.enums.ERole;
import ru.kon.dotaanalytics.exception.ResourceNotFoundException;
import ru.kon.dotaanalytics.exception.UserAlreadyExistsException;
import ru.kon.dotaanalytics.repository.RoleRepository;
import ru.kon.dotaanalytics.repository.UserRepository;
import ru.kon.dotaanalytics.security.jwt.JwtUtils;
import ru.kon.dotaanalytics.security.service.UserDetailsImpl;
import ru.kon.dotaanalytics.service.UserService;
import ru.kon.dotaanalytics.util.ErrorMessages;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public void registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByNickname(registerRequest.nickname())) {
            throw new UserAlreadyExistsException(
                String.format(ErrorMessages.NICKNAME_ALREADY_EXISTS, registerRequest.nickname()));
        }
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new UserAlreadyExistsException(
                String.format(ErrorMessages.EMAIL_ALREADY_EXISTS, registerRequest.email()));
        }

        User user = new User(
            null,
            registerRequest.nickname(),
            registerRequest.email(),
            passwordEncoder.encode(registerRequest.password()),
            new HashSet<>()
        );

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.ROLE_NOT_FOUND, ERole.ROLE_USER.name())));

        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    @Override
    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getNickname(),
            userDetails.getEmail(),
            roles
        );
    }
}
