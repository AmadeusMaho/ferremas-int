-- Crear esquema
FLUSH PRIVILEGES;
DROP DATABASE IF EXISTS ferremas;
CREATE DATABASE IF NOT EXISTS ferremas;
DROP USER IF EXISTS 'admin'@'%';
-- Creamos usuario xxx lo puede reemplazar por un nombre de su usuario
CREATE USER 'admin'@'%' IDENTIFIED BY '123';
GRANT ALL PRIVILEGES ON ferremas.* TO 'admin'@'%';


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

INSERT INTO cliente values (1, "Juan", "Pérez", 12345678, "1", "juan@gmail.com", "+56912345678");
INSERT INTO usuario values (1, "juan.perez", "clave123", "juan@gmail.com", 2);

INSERT INTO usuario values (2, "admin", "clave123", "admin@ferremas.com", 1);
INSERT INTO usuario values (3, "bodeguero", "clave123", "bodeguero@ferremas.com", 3);
INSERT INTO usuario values (4, "contador", "clave123", "contador@ferremas.com", 4);

INSERT INTO producto values (1, "Martillo", "Martillo mango de fibra de vidrio y cabeza de acero forjado.", "https://rgm.vtexassets.com/arquivos/ids/156235-1200-auto?v=638554617786370000&width=1200&height=auto&aspect=true", 8490, 30);
INSERT INTO producto values (2, "Serrucho", "Serrucho 20 Madera.", "https://cdnx.jumpseller.com/ferreteria-sur/image/23367127/thumb/1079/1079?1649782150", 7500, 15);
INSERT INTO producto values (3, "Clavo De 5", "Clavo De 5, bolsa de 1 kilo.", "https://construplaza.cl/media/catalog/product/C/L/CLAVO004008_20230415133802.jpg?optimize=medium&bg-color=255,255,255&fit=bounds&height=420&width=600&canvas=600:420", 1990, 200);
INSERT INTO producto values (4, "Alicate Universal", "Alicate Universal 8 Pulgadas.", "https://www.weitzler.cl/bitobee/wp-content/uploads/2022/11/65002100003-510x510.jpg", 4620, 50);
INSERT INTO producto values (5, "Huincha De Medir", "Cinta Huincha De Medir Metrica De Acero 10m.", "https://cdnx.jumpseller.com/ferroelectronic/image/42183146/thumb/1079/1079?1700156903", 10350, 70);
INSERT INTO producto values (6, "Juego de Destornilladores", "Juego de Destornilladores de Presición, 6 piezas.", "https://eccommerce-ferreteria-cl.s3.us-east-2.amazonaws.com/imagenes/productos/1270113107.jpg", 9990, 60);
INSERT INTO producto values (7, "Nivel de aluminio", "Nivel de aluminio 12 Pulgadas.", "https://media.falabella.com/sodimacCL/5433967_01/w=1500,h=1500,fit=pad", 8154, 120);
INSERT INTO producto values (8, "Llave Punta Corona", "Llave Punta Corona 24 mm.", "https://www.miferreteria.cl/wp-content/uploads/2021/04/llave-punta-corona-force.jpg", 6400, 50);
INSERT INTO producto values (9, "Esmeril Angular Eléctrico", "Esmeril Angular Eléctrico 4,5 pulgadas 820W + 2 discos.", "https://media.falabella.com/sodimacCL/3185222_01/w=1500,h=1500,fit=pad", 25490, 60);

INSERT INTO venta values (1, '2025-06-01', 1, "VD");
INSERT INTO detalle_venta values (1, 1, 6, 1, 9990.00);

INSERT INTO direccion values (1, '', 'Av. Américo Vespucio 1501, Cerrillos, Región Metropolitana', 1);

INSERT INTO despacho values (1, 1, 1, '2025-06-01', null);