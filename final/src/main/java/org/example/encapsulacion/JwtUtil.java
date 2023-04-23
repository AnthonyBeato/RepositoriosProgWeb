package org.example.encapsulacion;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosUsuario;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "9C0640B59CA77B0D40F78C3FF3B3AC2C";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final int EXPIRATION_TIME = 86400000; // 24 horas

    public static String generarToken(Usuario usuario) {
        return JWT.create()
                .withClaim("idUsuario", usuario.getIdUsuario())
                .withClaim("rol", usuario.getRol())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    public static Usuario verificarToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String idUsuario = decodedJWT.getClaim("idUsuario").asString();
            String rol = decodedJWT.getClaim("rol").asString();
            Usuario usuario = ServiciosUsuario.getInstancia().getUsuarioByID(idUsuario);
            if (usuario != null && usuario.getRol().equals(rol)) {
                return usuario;
            } else {
                return null;
            }
        } catch (JWTVerificationException e) {
            return null;
        }
    }


}

