package LMC.auth.controllers;

import LMC.auth.dto.AuthResponse;
import LMC.auth.dto.LoginRequest;
import LMC.auth.dto.RegisterRequest;
import LMC.auth.dto.ValidateDTO;
import LMC.auth.services.AuthService;
import LMC.auth.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ValidateDTO> validateToken(@RequestBody String token) {
        ValidateDTO validateDTO = authService.validateToken(token);
        if (validateDTO.isValid()) {
            return ResponseEntity.ok(validateDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validateDTO);
    }
}
