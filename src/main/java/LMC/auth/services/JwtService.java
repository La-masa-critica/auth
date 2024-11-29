package LMC.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String SECRET_KEY = "bbc58af422f570589f842991431ba451cf44e05e04674dbfe29687f4c881b5fa";

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);

    }

    public String getToken(HashMap<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder().setClaims(extraClaims).setSubject(user.getUsername())
                .claims(extraClaims.entrySet().stream()
                        .collect(Collectors.toMap(HashMap.Entry::getKey, HashMap.Entry::getValue)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getkey(), SignatureAlgorithm.HS256).compact();

    }

    public String generateServiceToken() {
        return Jwts.builder().setSubject("service")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 3600 * 1000 * 7))
                .signWith(getkey(), SignatureAlgorithm.HS256).compact();
    }

    public Key getkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token); // Valida firma y expiración
            return true;
        } catch (Exception e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

    Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(getkey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
