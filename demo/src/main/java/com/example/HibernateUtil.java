package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase que mantén unha única referencia ó xestor de entidades da aplicación.
 * Ademáis, proporciona os métodos de obter esta referencia ou pechar o xestor si está aberto.
 */
public class HibernateUtil {

    //Mantemos a variable global estática e final do xestor de entidades único 
    private static final EntityManagerFactory xestorEntidades =
        Persistence.createEntityManagerFactory("dragolandiaServizo");

    public static EntityManager getEntityManager() {
        return xestorEntidades.createEntityManager();
    }
   
    public static void close() {
        if (xestorEntidades.isOpen()) {
            xestorEntidades.close();
        }
    }

}

