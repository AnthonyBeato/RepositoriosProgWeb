package org.example.grpc;

import acortadorrn.AcortadorRn;
import acortadorrn.ServicioCreacionGrpc;
import acortadorrn.ServicioListadoGrpc;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Base64;
import java.util.List;

import com.google.gson.Gson;
import org.example.encapsulacion.Acortador;
import org.example.encapsulacion.Usuario;
import org.example.servicios.ServiciosAcortador;
import org.example.servicios.ServiciosUsuario;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Scanner;




public class ClienteGRPC {
    public static void main(String[] args) {

        // Crear canal de comunicación con el servidor
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

        // Crear stubs para los servicios de listado y creación de URLs
        ServicioListadoGrpc.ServicioListadoBlockingStub stubListado = ServicioListadoGrpc.newBlockingStub(channel);
        ServicioCreacionGrpc.ServicioCreacionBlockingStub stubCreacion = ServicioCreacionGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;
        String opcion = "";
        //Desplegar menú de opciones al usuario por consola
        while(continuar){
            System.out.println("\n\n------------------------------------------------------");
            System.out.println("Bienvenido al cliente GRPC para LinkZip!");
            System.out.println("\tOpciones: ");
            System.out.println("1. Listar url por usuario");
            System.out.println("2. Acortar una URL");
            System.out.println("3. Salir");

            System.out.println("\nDigita la opcion: ");
            opcion = scanner.nextLine();


            //Switchcase para mostrar menu visual en consola
            switch (opcion) {
                case "1" -> {
                    System.out.println("Listado de URL por usuario");
                    System.out.println("\nDigite el usuario al que quiere seleccionar: ");
                    String user = scanner.nextLine();
                    Usuario usuario = buscarUsuarioPorUser(user);

                    // Obtener listado de URLs para un usuario
                    AcortadorRn.ListarURLsRequest requestListado = AcortadorRn.ListarURLsRequest.newBuilder().setIdUsuario(usuario.getIdUsuario()).build();
                    AcortadorRn.ListarURLsResponse responseListado = stubListado.listarURLs(requestListado);

                    String usuario_request_listado = requestListado.getIdUsuario();

                    System.out.println("Listado de URLs para el usuario " + usuario_request_listado + ":");

                    // Creamos una instancia de Gson
                    Gson gson = new Gson();

                    String json = responseListado.getJson();

                    // Convertimos la cadena JSON a una lista de objetos Acortador
                    Type tipoListaAcortadores = new TypeToken<List<AcortadorRn.URL>>(){}.getType();
                    List<AcortadorRn.URL> listaDeAcortadores = gson.fromJson(json, tipoListaAcortadores);

                    for (AcortadorRn.URL url : listaDeAcortadores) {
                        System.out.println("Iterando en la URL: " + url);
                        System.out.println("URL: " + url.getUrlCompleta() + ", Fecha de creación: " + url.getFechaCreacion());
                    }

                }
                case "2" -> {
                    System.out.println("\nAcortar una URL");

                    String urloriginal = scanner.nextLine();


                    // Crear solicitud para el servicio de creación de URLs
                    JsonObject jsonRequestCreacion = new JsonObject();
                    jsonRequestCreacion.addProperty("id_usuario", "invitado");
                    jsonRequestCreacion.addProperty("url_original", urloriginal);
                    jsonRequestCreacion.addProperty("url_acortada", "");
                    jsonRequestCreacion.addProperty("imagen_previa", "");

                    // Obtener imagen previa de la URL original
                    try {
                        URL url = new URL(urloriginal);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setDoOutput(true);

                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                        jsonRequestCreacion.addProperty("imagen_previa", base64Image);

                    } catch (IOException e) {
                        System.out.println("Error al obtener la imagen previa: " + e.getMessage());
                    }

                    Gson gson2 = new Gson();
                    String json2 = gson2.toJson(jsonRequestCreacion);
                    AcortadorRn.CrearURLRequest requestCreacion = AcortadorRn.CrearURLRequest.newBuilder().setJson(json2).build();

                    // Realizar llamada al servicio de creación de URLs
                    AcortadorRn.CrearURLResponse responseCreacion = stubCreacion.crearURL(requestCreacion);

                    System.out.println("URL creada para el usuario " + responseCreacion.getIdUsuario() + ":");
                    System.out.println("URL completa: " + responseCreacion.getUrlCompleta());
                    System.out.println("URL acortada: " + responseCreacion.getUrlAcortada());
                    System.out.println("Fecha de creación: " + responseCreacion.getFechaCreacion().toString());

                    // Obtener imagen previa en base64
                    String urlImagen = responseCreacion.getUrlAcortada();
                    String imagenPreviaBase64 = obtenerImagenPreviaBase64(urlImagen);
                    if (imagenPreviaBase64 != null) {
                        System.out.println("Imagen previa en base64: " + imagenPreviaBase64);
                    } else {
                        System.out.println("No se pudo obtener la imagen previa.");
                    }

                }
                case "3" -> {
                    continuar = false;
                }
                default -> System.out.println("Opción inválida, selecciona una opción del 1 al 3");
            }
        }

        // Imprimir la respuesta del listado de URLs
        //System.out.println("\nListado de URLs:");
        //System.out.println(responseListado.getJson()); // la respuesta ahora es un JSON


        // Cerrar el canal
        channel.shutdown();
    }

    private static String obtenerImagenPreviaBase64(String url) {
        try {
            // Conectar con la URL
            URL urlObj = new URL("https://api.apiflash.com/v1/urltoimage?access_key=API_KEY&url=" + url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");

            // Leer la respuesta en formato base64
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Convertir la respuesta a JSON
            JsonObject responseJson = new JsonParser().parse(content.toString()).getAsJsonObject();

            // Obtener el contenido en base64
            String contentBase64 = responseJson.get("content").getAsString();

            return contentBase64;

        } catch (Exception e) {
            return null;
        }
    }

    private static Usuario buscarUsuarioPorUser(String username) {

        //Usuario usuario = ServiciosUsuario.getInstancia().getUsuariotByUser(username);
        Usuario usuario = null;
        List<Usuario> listaUsuarios = ServiciosUsuario.getInstancia().findAll();
        System.out.println("  creó la lista de Usuarios");
        for (Usuario user: listaUsuarios) {
            if(user.getUsuario().equalsIgnoreCase(username)){
                usuario = user;
                System.out.println("  encontró el user! ");
            }
        }

        if (usuario != null) {
            System.out.println("El usuario encontrado es: " + usuario.getUsuario());
        } else {
            System.out.println("El usuario no se encontró en la base de datos.");
        }

        return usuario;
    }

}
