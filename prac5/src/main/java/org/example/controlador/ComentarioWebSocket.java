package org.example.controlador;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import org.example.Main;
import org.example.encapsulacion.Usuario;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComentarioWebSocket  extends Main {

    private static ConcurrentHashMap<String, WsContext> userWsMap = new ConcurrentHashMap<>();

    public static void onConnect(WsConnectContext ctx) {
        String userId = getUserIdFromSession(ctx);
        if (userId != null) {
            userWsMap.put(userId, ctx);
        }
    }



    public static void onDisconnect(WsCloseContext ctx) {
        String userId = getUserIdFromSession(ctx);
        if (userId != null) {
            userWsMap.remove(userId);
        }
    }

    public static void comentarioEliminado(String comentarioId) {
        Main.broadcastMessage("Comentario eliminado: " + comentarioId);
    }

    private static String getUserIdFromSession(WsContext ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");
        if (usuario != null) {
            return usuario.getIdUsuario();
        }
        return null;
    }
}