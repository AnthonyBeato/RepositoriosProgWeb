package org.example.encapsulacion;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.servicios.ServiciosUsuario;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "9C0640B59CA77B0D40F78C3FF3B3AC2C";

    public static String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsuario())
                .claim("role", usuario.isAdmin() ? "admin" : "user")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1))))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public static Usuario validarToken(String token) throws JwtException {
        String username = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        // Busca al usuario por su nombre de usuario en la base de datos
        ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();
        return serviciosUsuario.getUsuariotByUser(username);
    }


}

