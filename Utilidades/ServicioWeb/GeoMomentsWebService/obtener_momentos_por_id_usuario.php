<?php
/**
 * Insertar un nuevo alumno en la base de datos
 */

require 'Momentos.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);

    // Insertar Alumno
    $retorno = Momentos::getMomentosById(
        $body['idusuario']);


        if ($retorno) 
        {
                $momento["estado"] = 1;		// cambio "1" a 1 porque no coge bien la cadena.
                $momento["momentos"] = $retorno;
                // Enviar objeto json del momento
                print json_encode($momento);
        } 
        else 
        {
        // Enviar respuesta de error general
        print json_encode(
                array(
                'estado' => '2',
                'mensaje' => 'No se obtuvo el registro'
                )
        );
        }    
}

?>