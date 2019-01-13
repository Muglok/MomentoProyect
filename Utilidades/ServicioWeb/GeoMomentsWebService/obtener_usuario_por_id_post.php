<?php
/**
 * Insertar un nuevo alumno en la base de datos
 */

require 'Usuarios.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Decodificando formato Json
    $body = json_decode(file_get_contents("php://input"), true);

    // Insertar Alumno
    $retorno = Usuarios::getById(
        $body['id']);


        if ($retorno) 
        {
                $usuario["estado"] = 1;		// cambio "1" a 1 porque no coge bien la cadena.
                $usuario["usuario"] = $retorno;
                // Enviar objeto json del usuario
                print json_encode($usuario);
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