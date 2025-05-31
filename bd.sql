-- Crear esquema
DROP DATABASE IF EXISTS ferremas;
CREATE DATABASE IF NOT EXISTS ferremas;
DROP USER IF EXISTS 'admin'@'%';
-- Creamos usuario xxx lo puede reemplazar por un nombre de su usuario
CREATE USER 'admin'@'%' IDENTIFIED BY '123';
GRANT ALL PRIVILEGES ON ferremas.* TO 'admin'@'%';
FLUSH PRIVILEGES;

USE ferremas;

CREATE TABLE cliente (
    cliente_id INTEGER NOT NULL auto_increment,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    run INTEGER(8) NOT NULL,
    dv VARCHAR(1) NOT NULL,
    email VARCHAR(100),
    telefono VARCHAR(20),
    PRIMARY KEY (cliente_id)
);

CREATE TABLE producto (
    producto_id INTEGER NOT NULL auto_increment,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    imagen VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL,
    PRIMARY KEY (producto_id)
);

CREATE TABLE venta (
    venta_id INTEGER NOT NULL auto_increment,
    fecha DATE NOT NULL,
    cliente_id INTEGER NOT NULL,
    medio_pago VARCHAR(255) NOT NULL,
    PRIMARY KEY (venta_id),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id)
);

CREATE TABLE detalle_venta (
	detalle_venta_id INTEGER NOT NULL PRIMARY KEY auto_increment,    
	venta_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unit DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES venta(venta_id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(producto_id)
);

CREATE TABLE usuario (
    usuario_id INTEGER NOT NULL PRIMARY KEY auto_increment,
    username VARCHAR(50) NOT NULL,
    clave VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL,
    tipo_usuario INTEGER NOT NULL
);


CREATE TABLE direccion (
    direccion_id INTEGER NOT NULL auto_increment,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    cliente_id INTEGER NOT NULL,
    PRIMARY KEY (direccion_id),
    CONSTRAINT fk_direccion_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id)
);

CREATE TABLE despacho (
    despacho_id INTEGER NOT NULL auto_increment,
    direccion_id INTEGER NOT NULL,
    venta_id INTEGER NOT NULL UNIQUE,
    fecha_despacho DATE,
    fecha_recibido DATE,
    PRIMARY KEY (despacho_id),
    CONSTRAINT fk_despacho_venta FOREIGN KEY (venta_id) REFERENCES venta(venta_id),
    CONSTRAINT fk_despacho_direccion FOREIGN KEY (direccion_id) REFERENCES direccion(direccion_id)
);

INSERT INTO cliente values (1, "mauri", "gatica", 12345678, "5", "ajns@jdfg.com", "+65892115");

INSERT INTO usuario values (1, "mauri", "123", "ajns@jdfg.com", 1);