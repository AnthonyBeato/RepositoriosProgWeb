package org.example.BaseDatos;


import org.example.encapsulacion.CarroCompras;
import org.example.servicios.ServiciosFoto;
import org.postgresql.ds.PGSimpleDataSource;

import javax.persistence.EntityManager;
import java.sql.*;

public class BDCockroach extends CarroCompras{

    private static BDCockroach instancia;
    public static BDCockroach getInstance() throws SQLException {
        if (instancia == null){
            instancia = new BDCockroach();
        }
        return instancia;
    }

    public Connection getConnection() throws SQLException{
        PGSimpleDataSource ds = new PGSimpleDataSource();
        String url = System.getenv("JDBC_DATABASE_URL");
        if (url != null){
            ds.setUrl(url);
        } else{
            ds.setUrl("jdbc:postgresql://stony-buzzard-9215.7tt.cockroachlabs.cloud:26257/defaultdb?sslmode=require&password=HTQHIg_wI0ueQf0bVAJ4wQ&user=quickbuy");
        }
        Connection connection = ds.getConnection();

        // Creamos la tabla ventas si no existe
        String sql = "CREATE TABLE IF NOT EXISTS ventas (id UUID PRIMARY KEY, cantidad INTEGER, total DOUBLE, fecha TIMESTAMP DEFAULT NOW())";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla ventas: " + e.getMessage());
        }

        return connection;
    }

    public void insertarVenta(CarroCompras carroCompras) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO ventas (id, cantidad, total) VALUES (?, ?,?)");

            stmt.setObject(1, carroCompras.getIdCarroCompra());
            stmt.setInt(2, carroCompras.getCantidadCarroCompra());
            stmt.setDouble(3, carroCompras.getMontoCarroCompra());
            stmt.executeUpdate();

        }
    }
}
