<?php
/**
 * Insertar un nuevo alumno en la base de datos
 */

require 'Usuarios.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);

    // Insertar Alumno
    $retorno = Usuarios::insert2(
        $body['nombre'],
        $body['contrasenya'],
        $body['userpass'],
        $body['telefono']
    );

    if ($retorno) {
        $json_string = json_encode(array("estado" => 1,"mensaje" => "Inserción correcta"));
		echo $json_string;
    } else {
        $json_string = json_encode(array("estado" => 2,"mensaje" => "No se creó el registro"));
		echo $json_string;
    }
}

?>