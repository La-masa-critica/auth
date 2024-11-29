package LMC.auth.services;

import LMC.auth.dto.AuthResponse;
import LMC.auth.dto.LoginRequest;
import LMC.auth.dto.RegisterRequest;
import LMC.auth.dto.ValidateDTO;
import LMC.auth.models.AuthData;
import LMC.auth.models.Profile;
import LMC.auth.models.Role;
import LMC.auth.models.SecurityUser;
import LMC.auth.repositories.AuthDataRepository;
import LMC.auth.repositories.ProfileRepository;
import LMC.auth.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final ProfileRepository profileRepository;
    private final AuthDataRepository authDataRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        // Buscar usuario y crear SecurityUser
        AuthData authData = authDataRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityUser securityUser = new SecurityUser(authData);

        // Generar token
        String token = jwtService.getToken(securityUser);

        return AuthResponse.builder()
                .token(token)
                .profile(authData.getProfile())
                .role(authData.getRole().getName())
                .build();
    }

    public ValidateDTO validateToken(String token) {
        try {
            // Extract username from token
            String username = jwtService.getUsernameFromToken(token);
            System.out.println("Username: " + username);
            if (username == null) {
                return ValidateDTO.builder().isValid(false).build();
            }
            if (username.equals("service")) {
                if (jwtService.validateToken(token)) {
                    return ValidateDTO.builder()
                            .isValid(true)
                            .role("ROLE_SERVICE")
                            .build();
                }
            }
            // Load user details from repository
            SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(username);

            Long expiration = jwtService.getAllClaims(token).getExpiration().getTime();

            // Verify token validity
            if (jwtService.isTokenValid(token, securityUser)) {
                return ValidateDTO.builder().isValid(true)
                        .userId(securityUser.getProfile().getId())
                        .role(securityUser.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .filter(authority -> authority.contains("ROLE_")).findFirst().get())
                        .expiration(expiration)
                        .permissions(securityUser.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .filter(authority -> !authority.contains("ROLE_"))
                                .map(String::toUpperCase)
                                .collect(Collectors.toSet())
                        ).build();
            }
        } catch (Exception e) {
            // Handle exception if needed
            return ValidateDTO.builder().isValid(false).message(e.getMessage()).build();
        }
        return ValidateDTO.builder().isValid(false).build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el username ya existe
        if (authDataRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Crear y guardar el Profile
        Profile profile = Profile.builder().name(request.getName())
                .email(request.getEmail()).phone(request.getPhone()).build();

        // Buscar el rol por defecto (asumiendo que existe un rol 'USER')
        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // Crear AuthData
        AuthData authData = AuthData.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .profile(profile).role(defaultRole).enabled(true).build();

        profile.setAuthData(authData);
        profileRepository.save(profile);

        SecurityUser securityUser = new SecurityUser(authData);
        String token = jwtService.getToken(securityUser);

        return AuthResponse.builder()
                .token(token)
                .profile(authData.getProfile())
                .role(authData.getRole().getName())
                .build();
    }
}
