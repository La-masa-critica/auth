package LMC.auth.services;

import LMC.auth.models.AuthResponse;
import LMC.auth.models.LoginRequest;
import LMC.auth.models.RegisterRequest;
import LMC.auth.models.Role;
import LMC.auth.models.entities.Credentials;
import LMC.auth.models.entities.User;
import LMC.auth.repositories.CredentialsRepository;
import LMC.auth.repositories.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserRespository userRespository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;



    public AuthResponse login(LoginRequest request) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = this.credentialsRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = this.jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
        User userSaved = this.userRespository.save(user);


        Credentials credentials = Credentials.builder()
                .role(Role.USER)
                .username(request.getUsername())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .userId(userSaved.getId())
                .build();

        this.credentialsRepository.save(credentials);
        return AuthResponse.builder()
                .token(this.jwtService.getToken(credentials))
                .build();
    }
}
