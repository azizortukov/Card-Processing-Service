package uz.anas.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.anas.card.entity.User;
import uz.anas.card.entity.enums.RoleName;
import uz.anas.card.exceptions.BadRequestException;
import uz.anas.card.model.dto.LoginDTO;
import uz.anas.card.model.dto.SignUpDTO;
import uz.anas.card.model.dto.TokenDTO;
import uz.anas.card.repo.RoleRepository;
import uz.anas.card.repo.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> checkLoginDetails(LoginDTO loginDto) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));
            String token = jwtService.genToken((UserDetails) auth.getPrincipal());

            return ResponseEntity.ok(new TokenDTO(token));
        } catch (AuthenticationException e) {
            throw new UsernameNotFoundException("Email or password is incorrect. Please try again!");
        }
    }

    @Override
    public HttpEntity<?> singUp(SignUpDTO signUpDTO) {
        var userOptional = userRepository.findByEmail(signUpDTO.email());
        if (userOptional.isPresent()) {
            throw new BadRequestException("User has already signed up. Please, login!");
        }
        userRepository.save(User.builder()
                .email(signUpDTO.email())
                .password(passwordEncoder.encode(signUpDTO.password()))
                .fullName(signUpDTO.fullName())
                .roles(List.of(roleRepository.findByName(RoleName.ROLE_CLIENT)))
                .build());
        return ResponseEntity.noContent().build();
    }

}