package org.example.servicios;

import org.example.encapsulacion.URL;

import java.util.List;

public interface UrlService {
    List<URL> findAll();
    URL getProductByID(String id);
    URL getURLByID(String id);
}