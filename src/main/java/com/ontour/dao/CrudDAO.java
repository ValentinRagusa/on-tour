package com.ontour.dao;

import java.util.List;

/**
 * Interfaz generica con las operaciones CRUD que comparten los DAOs de entidades.
 * <p>
 * T es la clase del modelo que maneja cada DAO. La implementan ShowDAO, RiderDAO,
 * ContrariderDAO, ProveedorDAO, IntegranteDAO, ViaticoDAO, HabitacionDAO y
 * DocumentoIntegranteDAO.
 * <p>
 * Como todos cumplen el mismo contrato, en Main se pueden tratar igual: el metodo
 * listar(CrudDAO) sirve para cualquier entidad y el "Resumen general" recorre un
 * arreglo CrudDAO[] sin saber que DAO concreto hay en cada posicion.
 * <p>
 * ShowProveedorDAO y HabitacionIntegranteDAO no la implementan porque manejan tablas
 * de union: no tienen un id propio ni se modifican, solo se asigna o se quita la relacion.
 *
 * @param <T> clase del modelo (Show, Rider, Proveedor, etc.)
 */
public interface CrudDAO<T> {

    /** Guarda un registro nuevo en la base. */
    void insertar(T objeto);

    /** Devuelve todos los registros de la tabla como objetos del modelo. */
    List<T> consultarTodos();

    /** Actualiza un registro existente, identificado por su id. */
    void modificar(T objeto);

    /** Elimina el registro con ese id. */
    void eliminar(int id);

    /** Muestra id + dato principal de cada registro, para elegir uno desde otro formulario. */
    void mostrarResumen();
}
