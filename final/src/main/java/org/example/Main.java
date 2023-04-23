package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import org.example.BaseDatos.Bootstrap;
import org.example.controlador.ControllerAcortador;
import org.example.controlador.ControllerQRCode;
import org.example.controlador.ControllerSeguridad;
import org.example.controlador.ControllerURL;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosUsuario;
import org.example.encapsulacion.JwtUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    private static final Map<String, WsConnectContext> users = new ConcurrentHashMap<>();

    private static String connectionMethod = "";
    public static void main(String[] args) {
        //Configuración de Hibernate
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Bootstrap.getInstance().init();

        ServiciosUsuario.getInstancia().crear(new Usuario("admin", "admin", "admin", true));

        //Configuración de javalin
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.aliasCheck = null;
                staticFileConfig.precompress = false;
            });
        });

//        app.cfg.accessManager((handler, ctx, permittedRoles) -> {
//            Set<String> allowedRoles = new HashSet<>();
//            permittedRoles.stream().map(Object::toString).forEach(allowedRoles::add);
//
//            String userRole = "";
//            String token = ctx.header("Authorization");
//            if (token != null) {
//                try {
//                    Usuario usuario = JwtUtil.verificarToken(token);
//                    userRole = usuario.getRol().toString();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("Error al validar el token: " + e.getMessage());
//                }
//            } else {
//                System.out.println("No se proporcionó un token en la cabecera 'Authorization'");
//            }
//
//            if (allowedRoles.contains(userRole)) {
//                handler.handle(ctx);
//            } else {
//                System.out.println("El rol del usuario no está permitido: " + userRole);
//                ctx.status(401).json("Unauthorized");
//            }
//        });


        //Controladoras
        new ControllerSeguridad(app).aplicarDireccionamiento();
        new ControllerURL(app).aplicarDireccionamiento();
        new ControllerAcortador(app).aplicarDireccionamiento();
        new ControllerQRCode(app).aplicarDireccionamiento();

        //Iniciar
        app.start(7000);

        entityManager.close();
        entityManagerFactory.close();

        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Conexión entrante: " + ctx.getSessionId());
                String userId = getUserIdFromSession(ctx);
                if (userId != null) {
                    users.put(userId, ctx);
                    broadcastUserCount();
                }
            });

            ws.onClose(ctx -> {
                System.out.println("Conexión cerrada: " + ctx.getSessionId());
                String userId = getUserIdFromSession(ctx);
                if (userId != null) {
                    users.remove(userId);
                    broadcastUserCount();
                }
            });

            ws.onError(ctx -> {
                System.out.println("Error en conexión: " + ctx.getSessionId());
                String userId = getUserIdFromSession(ctx);
                if (userId != null) {
                    users.remove(userId);
                }
            });
        });

    }

    private static String getUserIdFromSession(WsContext ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");
        if (usuario != null) {
            return usuario.getIdUsuario();
        }
        return null;
    }

    private static void broadcastUserCount() {
        users.values().forEach(user -> {
            user.send(Integer.toString(users.size()));
        });
    }

    public static String getConnectionMethod(){
        return connectionMethod;
    }

}