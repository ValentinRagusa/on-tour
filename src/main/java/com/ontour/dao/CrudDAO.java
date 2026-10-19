package com.ontour.dao;

import java.util.List;

// Interfaz generica con las operaciones CRUD comunes a todos los DAOs de entidades.
// T es el tipo de objeto del modelo que maneja cada DAO (Show, Rider, Proveedor, etc.)
public interface CrudDAO<T> {

    // Insertar un nuevo registro
    void insertar(T objeto);

    // Consultar todos los registros
    List<T> consultarTodos();

    // Modificar un registro existente
    void modificar(T objeto);

    // Eliminar un registro por ID
    void eliminar(int id);

    // Mostrar un resumen liviano (id + dato principal) para elegir un registro en otro formulario
    void mostrarResumen();
}
