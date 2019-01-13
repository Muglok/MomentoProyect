<?php

/**
 * Representa el la estructura de las Alumnoss
 * almacenadas en la base de datos
 */
require 'Database.php';

class Momentos
{
    function __construct()
    {
    }
	
	public static function getMomentos()
    {
        $consulta = "SELECT * FROM momentos";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
    }
    
    public static function getMomentosCompartidos()
    {
        $consulta = "SELECT * FROM momentos where compartido = 1";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            return false;
        }
    }

    /**
     * Obtiene los campos de un Alumno con un identificador
     * determinado
     *
     * @param $idAlumno Identificador del alumno
     * @return mixed
     */
    public static function getMomentosById($idusuario)
    {
        // Consulta de la tabla Alumnos
        $consulta = "SELECT * FROM momentos
                             WHERE idusuario = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($idusuario));
            // Capturar primera fila del resultado
            return $comando->fetchAll(PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            // Aqu� puedes clasificar el error dependiendo de la excepci�n
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

    //-------------------------------------------------------------------------------

    public static function getByUserPass($nombre,$contrasenya)
    {
        // Consulta de la tabla Alumnos
        $consulta = "SELECT id,
                            nombre,
                            contrasenya,
                            userpass,
                            telefono
                             FROM usuarios
                             WHERE nombre = ? and contrasenya = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($nombre,$contrasenya));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;

        } catch (PDOException $e) {
            // Aqu� puedes clasificar el error dependiendo de la excepci�n
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

    //-------------------------------------------------------------------------------

    /**
     * Actualiza un registro de la bases de datos basado
     * en los nuevos valores relacionados con un identificador
     *
     * @param $idAlumno      identificador
     * @param $nombre      nuevo nombre
     * @param $direccion nueva direccion
     
     */
    public static function update(
        $id,
        $nombre,
        $contrasenya,
        $userpass
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE usuarios" .
            " SET nombre=?,
             contrasenya=?,
             userpass=? " .
            "WHERE id=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($nombre, $contrasenya,$userpass,$id));

        return $cmd;
    }

    //------------------ update momento, compartir --------------------------
    public static function updateMomentoCompartir(
        $compartido,       
        $id
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE momentos" .
            " SET compartido=?" .
            "WHERE id=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($compartido,$id));

        return $cmd;
    }
    // ----------------------------------------------------------------------
    
     //------------------ update momento, modificar --------------------------
     public static function updateMomentoModificar(
        $titulo,
        $descripcion,
        $cancion,       
        $id
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE momentos" .
                " SET titulo=?,
                descripcion=?,
                cancion=? " .
                "WHERE id=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($titulo,$descripcion,$cancion,$id));

        return $cmd;
    }

    // ----------------------------------------------------------------------
    
    /**
     * Insertar un nuevo Alumno
     *
     * @param $nombre      nombre del nuevo registro
     * @param $direccion direcci�n del nuevo registro
     * @return PDOStatement
     */
    public static function insert(       
        $nombre,
        $contransenya,
        $userpass
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO usuarios ( " .
            "nombre," .
            " contrasenya,
            userpass)" .
            " VALUES( ?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(
            array(
                $nombre,
                $contransenya,
                $userpass
            )
        );

    }

    //------------------------- Insertar Momento ----------------------------
    public static function insertMomento(       
        $titulo,
        $descripcion,
        $cancion,
        $latitud,
        $longitud,
        $fecha,
        $hora,
        $idusuario,
        $compartido
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO momentos ( " .
            "titulo," .
            " descripcion, cancion, latitud, longitud, fecha, hora, idusuario,compartido)" .
            " VALUES(?,?,?,?,?,?,?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(
            array(
                $titulo,
                $descripcion,
                $cancion,
                $latitud,
                $longitud,
                $fecha,
                $hora,
                $idusuario,
                $compartido
            )
        );

    }

    //----------------------------------------------------------------------
	
    /**
     * Eliminar el registro con el identificador especificado     
     * @param $idAlumno identificador de la tabla Alumnos
     * @return bool Respuesta de la eliminaci�n
     */
    public static function delete($id)
    {
        // Sentencia DELETE
        $comando = "DELETE FROM usuarios WHERE id = ?";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(array($id));
    }
}

?>