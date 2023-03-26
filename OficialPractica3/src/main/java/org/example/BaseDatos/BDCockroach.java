package org.example.BaseDatos;


import org.example.encapsulacion.VentaProductos;
import org.example.servicios.ServiciosFoto;
import org.postgresql.ds.PGSimpleDataSource;

import javax.persistence.EntityManager;
import javax.sound.midi.Soundbank;
import java.sql.*;

public class BDCockroach extends VentaProductos{

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
        String sql = "CREATE TABLE IF NOT EXISTS ventas (id UUID PRIMARY KEY,nombre varchar, cantidad INTEGER, total INTEGER, fecha TIMESTAMP DEFAULT NOW())";
        try (Statement stmt = connection.createStatement()) {

            stmt.execute(sql);
        } catch (SQLException e) {

            System.err.println("Error al crear la tabla ventas: " + e.getMessage());
        }

        return connection;
    }

    public void insertarVenta(VentaProductos ventaProductos) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO ventas (id, nombre, cantidad, total, fecha) VALUES (?,?,?,?,?)");

            System.out.println("ID de la venta "+ ventaProductos.getIdVentas() + " monto " + ventaProductos.getTotal());
            stmt.setObject(1, ventaProductos.getIdVentas());
            stmt.setString(2, ventaProductos.getNombreCliente());
            stmt.setInt(3, ventaProductos.getProductos().size());
            stmt.setFloat(4, (float) ventaProductos.getTotal());
            stmt.setDate(5, (Date) ventaProductos.getFechaCompra());
            stmt.executeUpdate();
            stmt.close();
        }catch (SQLException ex) {
            System.out.println("Error al insertar el CarritoItem: " + ex.getMessage());
        }
    }
}
