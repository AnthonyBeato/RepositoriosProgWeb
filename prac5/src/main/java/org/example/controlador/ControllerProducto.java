package org.example.controlador;

import Utilidad.ControllerBase;
import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import jakarta.servlet.MultipartConfigElement;
import org.example.encapsulacion.*;
import org.example.servicios.ServiciosComentario;
import org.example.servicios.ServiciosFoto;
import org.example.servicios.ServiciosProducto;
import org.example.servicios.ServiciosUsuario;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;





public class ControllerProducto extends ControllerBase {
    ServiciosProducto serviciosProducto = ServiciosProducto.getInstancia();

    ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstancia();
    ServiciosComentario serviciosComentario = ServiciosComentario.getInstancia();

    ServiciosFoto serviciosFoto = ServiciosFoto.getInstancia();
//    private final RepositorioFotos repositorioFotos;
//    private final RepositorioProducto repositorioProducto;


    public ControllerProducto(Javalin app){
        super(app);
    }

    @Override
    public void aplicarDireccionamiento() {
        app.routes(() -> {
           path("/", () -> {
               get("/",ctx -> {
                   CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                   Map<String, Object> modelo = new HashMap<>();
                   //List<Producto> listaProductos = serviciosProducto.getListaProductos();


                   int actualPage = 1;
                   String page = ctx.req().getParameter("page");
                   if(page != null){
                       try{
                           actualPage = Integer.parseInt(page);
                       }catch (NumberFormatException e){
                           ctx.redirect("/");
                       }
                   }

                   int cantProductos = serviciosProducto.findAll().size();
                   int cantProdXPage = 10;
                   int totalPages = (int) Math.ceil((double) cantProductos/cantProdXPage);

                   int indiceIni = (actualPage - 1) * cantProdXPage;
                   int indiceFinal = Math.min(indiceIni + cantProdXPage, cantProductos);




                   modelo.put("titulo", "Lista de compras");

                   List<Producto> listaProductos = serviciosProducto.findAll()
                           .subList(indiceIni, indiceFinal)
                           .stream()
                           .filter(a -> a.getCantidad() > 0)
                           .toList();


                   modelo.put("productos", listaProductos);
                       modelo.put("actualPage", actualPage);
                       modelo.put("totalPages", totalPages);
                       modelo.put("cantProdXPage", cantProdXPage);
                       modelo.put("cantProductos", cantProductos);


                   modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());


                   //Guardar el nombre de Usuario en header
                   modelo.put("session", ctx.sessionAttributeMap());
                   ctx.render("/templates/vista/index.html", modelo);
               });
           }) ;
        });

        app.routes(() -> {
            path("/Seguridad/Productos", () -> {
                get("/",ctx -> {
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    Map<String, Object> modelo = new HashMap<>();
                    List<Producto> listaProductos = serviciosProducto.findAll();

                    //String query = ctx.queryParam("q");
                    //int page = ctx.pathParamAsClass("page" , Integer.class).get()-1;
//                    List<Producto> listaProductos;
//                    if(query != null) {
//                        listaProductos = serviciosProducto.getPageSearch(page, query).stream().filter(a -> a.getCantidad() > 0).toList();
//                    } else {
//                        listaProductos = serviciosProducto.getPage(page).stream().filter(a -> a.getCantidad() > 0).toList();
//                    }

                    modelo.put("titulo", "Gestion de productos");
                    modelo.put("productos", listaProductos);
                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());
                    //modelo.put("paginas", serviciosProducto.getPageCount());


                    //Guardar el nombre de Usuario en header
                    modelo.put("session", ctx.sessionAttributeMap());

                    Usuario usuarioActual = ctx.sessionAttribute("usuario");


                    if(usuarioActual != null && usuarioActual.getUsuario().equals("admin")){
                        ctx.render("/templates/vista/gestionProductos.html", modelo);
                    }else{
                        ctx.render("/templates/vista/error.html");
                    }

                });

                //Listado de productos
//                get(ctx -> {
//                    //List<Producto> listaProductos = serviciosProducto.getListaProductos();
//                    //String query = ctx.queryParam("q");
//                    //int page = ctx.pathParamAsClass("page" , Integer.class).get()-1;
//
//                    List<Producto> listaProductos = serviciosProducto.getListaProductos().;
//
////                    List<Producto> listaProductos;
////                    if(query != null) {
////                        listaProductos = serviciosProducto.getPageSearch(page, query).stream().filter(a -> a.getCantidad() > 0).toList();
////                    } else {
////                        listaProductos = serviciosProducto.getPage(page).stream().filter(a -> a.getCantidad() > 0).toList();
////                    }
//
//
//                    Map<String, Object> modelo = new HashMap<>();
//                    modelo.put("titulo", "Lista de productos");
//                    modelo.put("productos", listaProductos);
//                    modelo.put("paginas", serviciosProducto.getPageCount());
//
//                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
//                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());
//
//                    //Guardar el nombre de Usuario en header
//                    modelo.put("session", ctx.sessionAttributeMap());
//
//                    ctx.render("/templates/vista/index.html", modelo);
//                });

                //Formulario crear
                get("/Crear/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());

                    //Guardar el nombre de Usuario en header
                    modelo.put("session", ctx.sessionAttributeMap());
                    ctx.render("/templates/vista/crearProducto.html", modelo);
                });
                //Crear producto
                post("/Crear/", ctx -> {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/templates/img");
                    ctx.attribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

                    String identificador = UUID.randomUUID().toString();
                    String nombre = ctx.formParam("nombre");
                    double precio = ctx.formParamAsClass("precio", Double.class).get();
                    String descripcion = ctx.formParam("descripcion");

                    Producto tempProducto = new Producto(identificador, nombre, precio, 1, descripcion, null, null);

                    //Guardar foto:
                    List<UploadedFile> fotos = ctx.uploadedFiles("fotos");

                    serviciosProducto.crear(tempProducto);
                    for(UploadedFile foto: fotos){
                        String nombreFoto = foto.filename();
                        String mimeType = foto.contentType();
                        byte[] fotoBytes = foto.content().readAllBytes();
                        String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);


                        Foto nuevaFoto = new Foto(nombreFoto, mimeType, fotoBase64, tempProducto);
                        serviciosFoto.crear(nuevaFoto);
                    }

                    tempProducto.setFotos(serviciosFoto.getFotosByProducto(tempProducto.getIdProducto()));
                    serviciosProducto.editar(tempProducto);


                    ctx.redirect("/Seguridad/Productos");
                });

                //Formulario modificar
                get("/Modificar/{idProducto}",ctx -> {
                    //Producto producto = serviciosProducto.getProductByID(ctx.pathParam("idProducto"));
                    Producto producto = serviciosProducto.find(ctx.pathParam("idProducto"));

                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    Map<String, Object> modelo = new HashMap<>();

                    modelo.put("titulo", "Modificar productos");
                    modelo.put("idProducto", producto.getIdProducto());
                    modelo.put("nombre", producto.getNombre());
                    modelo.put("descripcion", producto.getDescripcion());
                    modelo.put("precio", producto.getPrecio());
                    //modelo.put("cantidad", producto.getCantidad());
                    modelo.put("action", ("/Seguridad/Productos/Modificar/".concat(producto.getIdProducto())));
                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());

                    List<Foto> fotosProducto = producto.getFotos();
                    modelo.put("fotosProducto", fotosProducto);


                    //Guardar el nombre de Usuario en header
                    modelo.put("session", ctx.sessionAttributeMap());
                    ctx.render("/templates/vista/editarProducto.html", modelo);
                });
                //Actualizar producto
                post("/Modificar/{idProducto}",ctx -> {
                    String identificador = ctx.pathParam("idProducto");
                    String nombre = ctx.formParam("nombre");
                    double precio = ctx.formParamAsClass("precio", Double.class).get();
                    String descripcion = ctx.formParam("descripcion");

                    Producto productoActual = serviciosProducto.find(identificador);

                    Producto tempProducto = new Producto(identificador, nombre, precio, 1, descripcion, productoActual.getFotos(), productoActual.getComentarios());


                    //Guardar nuevas fotos:
//                    List<UploadedFile> fotos = ctx.uploadedFiles("fotos");
//                    for(UploadedFile foto: fotos){
//                        String nombreFoto = foto.filename();
//                        String mimeType = foto.contentType();
//                        byte[] fotoBytes = foto.content().readAllBytes();
//                        String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
//
//                        ServiciosFoto.getInstancia().crear(new Foto(nombreFoto, mimeType, fotoBase64, tempProducto));
//                    }


                    //Actualizar en la BD
                    serviciosProducto.editar(tempProducto);



                    ctx.redirect("/Seguridad/Productos");
                });

                //Eliminar producto
                post("/Eliminar/{idProducto}", ctx -> {
                    String identificador = ctx.pathParam("idProducto");
                    //serviciosProducto.deleteProducto(identificador);

                    //Eliminar de la BD
                    serviciosProducto.eliminar(identificador);
                    ctx.redirect("/Seguridad/Productos");
                });



//                post("/producto/{idProducto}", ctx -> {
//                    ctx.redirect("/");
//                });
            }) ;

            path("/producto", () -> {
                get("/{idProducto}", ctx -> {
                    Producto producto = serviciosProducto.find(ctx.pathParam("idProducto"));
                    CarroCompras carroCompras = ctx.sessionAttribute("carroCompras");
                    Map<String, Object> modelo = new HashMap<>();


                    modelo.put("titulo", "Modificar productos");
                    modelo.put("idProducto", producto.getIdProducto());
                    modelo.put("nombre", producto.getNombre());
                    modelo.put("descripcion", producto.getDescripcion());
                    modelo.put("precio", producto.getPrecio());
                    //modelo.put("cantidad", producto.getCantidad());
                    //modelo.put("action", ("/Seguridad/Productos/Modificar/".concat(producto.getIdProducto())));
                    modelo.put("cantidadProdCarrito", carroCompras.getCantidadCarroCompra());

                    //List<Foto> fotosProducto = serviciosFoto.getFotosByProducto(producto);
                    modelo.put("fotosProducto", producto.getFotos());
                    //modelo.put("fotosProducto", serviciosFoto.getFotosByProducto(producto.getIdProducto()));

                    modelo.put("comentarios", ServiciosComentario.getInstancia().obtenerComentariosPorProducto(producto.getIdProducto()));



                    //Guardar el nombre de Usuario en header
                    modelo.put("session", ctx.sessionAttributeMap());

                    System.out.println("Llegó a detalleProducto");
                    ctx.render("/templates/vista/detalleProducto.html", modelo);
                });
                post("/{idProducto}/comentario", ctx -> {
                    Producto producto = serviciosProducto.find(ctx.pathParam("idProducto"));

                    Usuario usuario = ctx.sessionAttribute("usuario");

                    if(ctx.sessionAttribute("usuario") != null){
                        String comentario = ctx.formParam("comentario");

                        Comentario nuevoComentario = new Comentario(comentario, producto, usuario);
                        serviciosComentario.crear(nuevoComentario);


                        ctx.redirect("/producto/" + producto.getIdProducto());
                    } else {
                        ctx.render("/templates/vista/error.html");
                    }
                });

                post("/{idProducto}/comentario/Eliminar/{idComentario}", ctx -> {
                    Producto producto = serviciosProducto.find(ctx.pathParam("idProducto"));
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    String identificador = ctx.pathParam("idComentario");

                    if(usuario.getUsuario().equalsIgnoreCase("admin")){
                        //Eliminar de la BD
                        serviciosComentario.eliminar(identificador);
                        ctx.redirect("/producto/" + producto.getIdProducto());
                    } else {
                        ctx.render("/templates/vista/error.html");
                    }
                });
            });
        });
    }
}
