-- ============================================================
-- ON TOUR - Script completo de base de datos
-- Seminario de Practica de Informatica - AP2
-- Autor: Valentin Ragusa
-- ============================================================

-- ============================================================
-- SECCION 1: Creacion de la base de datos y las tablas
-- ============================================================

CREATE DATABASE IF NOT EXISTS ontour;
USE ontour;

CREATE TABLE shows (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_venue VARCHAR(150) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    fecha DATE NOT NULL,
    hora_llegada TIME,
    hora_soundcheck TIME,
    hora_show TIME,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_show_venue_fecha UNIQUE (nombre_venue, fecha)
);

CREATE TABLE riders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    show_id INT NOT NULL,
    tipo ENUM('tecnico', 'hospitality') NOT NULL,
    fecha_carga DATE NOT NULL,
    estado ENUM('pendiente', 'en_negociacion', 'confirmado') NOT NULL DEFAULT 'pendiente',
    FOREIGN KEY (show_id) REFERENCES shows(id) ON DELETE CASCADE,
    CONSTRAINT uq_rider_show_tipo UNIQUE (show_id, tipo)
);

CREATE TABLE contrariders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rider_id INT NOT NULL,
    version INT NOT NULL,
    fecha_carga DATE NOT NULL,
    descripcion TEXT,
    disponibilidad ENUM('confirmado', 'alternativa', 'no_disponible') NOT NULL,
    FOREIGN KEY (rider_id) REFERENCES riders(id) ON DELETE CASCADE,
    CONSTRAINT uq_contrarider_version UNIQUE (rider_id, version)
);

CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    tipo ENUM('transporte', 'alojamiento', 'tecnico') NOT NULL,
    contacto VARCHAR(150),
    CONSTRAINT uq_proveedor_nombre_tipo UNIQUE (nombre, tipo)
);

CREATE TABLE show_proveedor (
    show_id INT NOT NULL,
    proveedor_id INT NOT NULL,
    PRIMARY KEY (show_id, proveedor_id),
    FOREIGN KEY (show_id) REFERENCES shows(id) ON DELETE CASCADE,
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id) ON DELETE CASCADE
);

CREATE TABLE integrantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    documento_identidad VARCHAR(50) NOT NULL,
    numero_pasajero_frecuente VARCHAR(50),
    CONSTRAINT uq_integrante_documento UNIQUE (documento_identidad)
);

CREATE TABLE viaticos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    show_id INT NOT NULL,
    integrante_id INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    pagado BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (show_id) REFERENCES shows(id) ON DELETE CASCADE,
    FOREIGN KEY (integrante_id) REFERENCES integrantes(id) ON DELETE CASCADE,
    CONSTRAINT uq_viatico_show_integrante_fecha UNIQUE (show_id, integrante_id, fecha)
);

CREATE TABLE habitaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    show_id INT NOT NULL,
    tipo ENUM('individual', 'doble', 'triple') NOT NULL,
    numero_habitacion VARCHAR(20) NOT NULL,
    FOREIGN KEY (show_id) REFERENCES shows(id) ON DELETE CASCADE,
    CONSTRAINT uq_habitacion_show_numero UNIQUE (show_id, numero_habitacion)
);

CREATE TABLE habitacion_integrante (
    habitacion_id INT NOT NULL,
    integrante_id INT NOT NULL,
    PRIMARY KEY (habitacion_id, integrante_id),
    FOREIGN KEY (habitacion_id) REFERENCES habitaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (integrante_id) REFERENCES integrantes(id) ON DELETE CASCADE
);

CREATE TABLE documentos_integrante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    integrante_id INT NOT NULL,
    tipo_documento VARCHAR(50) NOT NULL,
    archivo VARCHAR(255),
    fecha_carga DATE NOT NULL,
    FOREIGN KEY (integrante_id) REFERENCES integrantes(id) ON DELETE CASCADE,
    CONSTRAINT uq_documento_integrante_tipo UNIQUE (integrante_id, tipo_documento)
);


-- ============================================================
-- SECCION 2: Datos de prueba
-- ============================================================

INSERT INTO shows (nombre_venue, ciudad, pais, fecha, hora_llegada, hora_soundcheck, hora_show) VALUES
('Palacio Vistalegre', 'Madrid', 'España', '2026-10-09', '15:00:00', '18:00:00', '22:00:00'),
('Recinto Ferial', 'Málaga', 'España', '2026-10-15', '14:00:00', '17:30:00', '21:30:00'),
('Lollapalooza', 'Chicago', 'Estados Unidos', '2026-08-01', '10:00:00', '14:30:00', '20:00:00'),
('Ultra Music Festival', 'Miami', 'Estados Unidos', '2026-03-27', '09:00:00', '13:00:00', '19:30:00');

INSERT INTO integrantes (nombre, documento_identidad, numero_pasajero_frecuente) VALUES
('Martin Gimenez', '30111222', 'AA123456'),
('Lucas Fernandez', '29888777', 'BA654321'),
('Sofia Alvarez', '31222333', NULL),
('Diego Torres', '28999111', 'LT998877');

INSERT INTO proveedores (nombre, tipo, contacto) VALUES
('TransEuro Logistica', 'transporte', '+34 600 111 222'),
('Hotel Riviera Group', 'alojamiento', 'reservas@hotelriviera.com'),
('SoundPro Rental', 'tecnico', 'contacto@soundpro.com'),
('StageLight Solutions', 'tecnico', 'info@stagelight.com');

INSERT INTO riders (show_id, tipo, fecha_carga, estado) VALUES
(1, 'tecnico', '2026-09-01', 'confirmado'),
(1, 'hospitality', '2026-09-01', 'confirmado'),
(2, 'tecnico', '2026-09-05', 'en_negociacion'),
(2, 'hospitality', '2026-09-05', 'pendiente'),
(3, 'tecnico', '2026-07-01', 'en_negociacion'),
(3, 'hospitality', '2026-07-01', 'confirmado'),
(4, 'tecnico', '2026-02-20', 'confirmado'),
(4, 'hospitality', '2026-02-20', 'pendiente');

INSERT INTO contrariders (rider_id, version, fecha_carga, descripcion, disponibilidad) VALUES
(1, 1, '2026-09-05', 'Se ofrece amplificador equivalente a la marca solicitada', 'alternativa'),
(1, 2, '2026-09-10', 'Confirmado el backline completo solicitado', 'confirmado'),
(3, 1, '2026-09-08', 'Confirmada consola solicitada', 'confirmado'),
(5, 1, '2026-07-05', 'No cuentan con el modelo de consola solicitado', 'no_disponible'),
(5, 2, '2026-07-10', 'Se ofrece consola alternativa de gama similar', 'alternativa'),
(7, 1, '2026-02-22', 'Confirmado backline completo', 'confirmado');

INSERT INTO show_proveedor (show_id, proveedor_id) VALUES
(1, 1), (1, 2), (1, 3),
(2, 1), (2, 3),
(3, 3), (3, 4),
(4, 1), (4, 4);

INSERT INTO habitaciones (show_id, tipo, numero_habitacion) VALUES
(1, 'doble', '305'),
(1, 'individual', '306'),
(2, 'triple', '410'),
(3, 'doble', '512'),
(4, 'doble', '118');

INSERT INTO habitacion_integrante (habitacion_id, integrante_id) VALUES
(1, 1), (1, 2),
(2, 3),
(3, 1), (3, 2), (3, 4),
(4, 3), (4, 4),
(5, 1), (5, 4);

INSERT INTO viaticos (show_id, integrante_id, monto, fecha, pagado) VALUES
(1, 1, 150.00, '2026-10-09', TRUE),
(1, 2, 150.00, '2026-10-09', FALSE),
(2, 1, 150.00, '2026-10-15', TRUE),
(3, 3, 200.00, '2026-08-01', TRUE),
(4, 4, 200.00, '2026-03-27', FALSE);

INSERT INTO documentos_integrante (integrante_id, tipo_documento, archivo, fecha_carga) VALUES
(1, 'DNI', 'dni_martin.pdf', '2026-01-10'),
(1, 'Pasaporte', 'pasaporte_martin.pdf', '2026-01-10'),
(2, 'DNI', 'dni_lucas.pdf', '2026-01-11'),
(2, 'Pasaporte', 'pasaporte_lucas.pdf', '2026-01-11'),
(3, 'DNI', 'dni_sofia.pdf', '2026-01-12'),
(4, 'DNI', 'dni_diego.pdf', '2026-01-13'),
(4, 'Pasaporte', 'pasaporte_diego.pdf', '2026-01-13');


-- ============================================================
-- SECCION 3: Consultas SQL de presentacion
-- ============================================================

-- Consulta 1: JOIN simple
-- Listar todos los riders de un show junto con el nombre del venue
SELECT s.nombre_venue, r.tipo, r.estado, r.fecha_carga
FROM riders r
JOIN shows s ON r.show_id = s.id
WHERE s.id = 1;

-- Consulta 2: JOIN con historial de versiones
-- Ver todas las versiones de contrarider de un rider especifico, ordenadas
SELECT c.version, c.fecha_carga, c.disponibilidad, c.descripcion
FROM contrariders c
WHERE c.rider_id = 1
ORDER BY c.version;

-- Consulta 3: Relacion N:N (proveedores por show)
-- Listar todos los proveedores que trabajan en un show determinado
SELECT s.nombre_venue, p.nombre, p.tipo, p.contacto
FROM show_proveedor sp
JOIN shows s ON sp.show_id = s.id
JOIN proveedores p ON sp.proveedor_id = p.id
WHERE s.id = 1;

-- Consulta 4: Relacion N:N (integrantes por habitacion)
-- Listar todos los integrantes alojados en una habitacion especifica
SELECT h.numero_habitacion, h.tipo, i.nombre, i.documento_identidad
FROM habitacion_integrante hi
JOIN habitaciones h ON hi.habitacion_id = h.id
JOIN integrantes i ON hi.integrante_id = i.id
WHERE h.id = 3;

-- Consulta 5: Filtro con condicion
-- Listar los viaticos que todavia no fueron pagados
SELECT s.nombre_venue, i.nombre, v.monto, v.fecha
FROM viaticos v
JOIN shows s ON v.show_id = s.id
JOIN integrantes i ON v.integrante_id = i.id
WHERE v.pagado = FALSE;

-- Consulta 6: Agregacion (COUNT + GROUP BY)
-- Contar cuantos shows tiene asignado cada proveedor
SELECT p.nombre, p.tipo, COUNT(sp.show_id) AS cantidad_shows
FROM proveedores p
LEFT JOIN show_proveedor sp ON p.id = sp.proveedor_id
GROUP BY p.id, p.nombre, p.tipo
ORDER BY cantidad_shows DESC;
