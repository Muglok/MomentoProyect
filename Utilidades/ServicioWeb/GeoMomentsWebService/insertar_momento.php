<?php
/**
 * Insertar un nuevo momento en la base de datos
 */

require 'Momentos.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);

    // Insertar Alumno
    $retorno = Momentos::insertMomento(
        $body['titulo'],
        $body['descripcion'],
        $body['cancion'],
        $body['latitud'],
        $body['longitud'],
        $body['fecha'],
        $body['hora'],
        $body['idusuario'],
        $body['compartido']
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