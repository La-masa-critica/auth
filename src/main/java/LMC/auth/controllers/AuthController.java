package LMC.auth.controllers;

import LMC.auth.models.AuthResponse;
import LMC.auth.models.LoginRequest;
import LMC.auth.models.RegisterRequest;
import LMC.auth.services.AuthService;
import LMC.auth.services.JwtService;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(this.authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(this.authService.login(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        try {
            // Extraer el username del token
            String username = jwtService.getUsernameFromToken(token);

            // Cargar los detalles del usuario desde el repositorio
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Verificar la validez del token
            if (jwtService.isTokenValid(token, userDetails)) {
                return ResponseEntity.ok(1);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
        }
    }
}
