package org.example.BaseDatos;

import org.h2.tools.Server;

import java.sql.SQLException;


public class Bootstrap {

    private static Bootstrap instance;

    private Bootstrap(){

    }

    public static Bootstrap getInstance(){
        if(instance == null){
            instance=new Bootstrap();
            instance=new Bootstrap();
        }
        return instance;
    }

    public void startDb() {
        try {
            //Modo servidor H2.
            Server.createTcpServer("-tcpPort",
                    "9092",
                    "-tcpAllowOthers",
                    "-tcpDaemon",
                    "-ifNotExists").start();
            //Abriendo el cliente web. El valor 0 representa puerto aleatorio.
            String status = Server.createWebServer("-trace", "-webPort", "0").start().getStatus();
            //
            System.out.println("Status Web: "+status);
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }
    }

    public void init(){
        startDb();
    }

}