<?php
/**
 * Obtiene todas las alumnos de la base de datos
 */

require 'Momentos.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    // Manejar petici�n GET
    $usuarios = Momentos::getMomentosCompartidos();

    if ($usuarios) {

        $datos["estado"] = 1;
        $datos["momentos"] = $usuarios;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}



