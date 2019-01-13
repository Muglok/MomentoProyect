<?php

/**
 * Representa el la estructura de las Alumnoss
 * almacenadas en la base de datos
 */
require 'Database.php';

class Usuarios
{
    function __construct()
    {
    }

    /**
     * Retorna en la fila especificada de la tabla 'Alumnos'
     *
     * @param $idAlumno Identificador del registro
     * @return array Datos del registro
     */
    public static function getAll()
    {
        $consulta = "SELECT * FROM usuarios";
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
	
	public static function getAllMomentos()
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

    /**
     * Obtiene los campos de un Alumno con un identificador
     * determinado
     *
     * @param $idAlumno Identificador del alumno
     * @return mixed
     */
    public static function getById($id)
    {
        // Consulta de la tabla Alumnos
        $consulta = "SELECT id,
                            nombre,
                            contrasenya,
                            userpass,
                            telefono
                             FROM usuarios
                             WHERE id = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($id));
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
    
    //-----------------------------------------------------------------------
    public static function updateUserPass(
        $nombre,
        $contrasenya,       
        $id
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE usuarios" .
            " SET nombre=?,           
             contrasenya=? " .
            "WHERE id=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($nombre, $contrasenya,$id));

        return $cmd;
    }


    //-----------------------------------------------------------------------

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
    
       public static function insert2(       
        $nombre,
        $contransenya,
        $userpass,
        $telefono
    )
    {
        // Sentencia INSERT
        $comando = "INSERT INTO usuarios ( " .
            "nombre," .
            " contrasenya,
            userpass,telefono)" .
            " VALUES( ?,?,?,?)";

        // Preparar la sentencia
        $sentencia = Database::getInstance()->getDb()->prepare($comando);

        return $sentencia->execute(
            array(
                $nombre,
                $contransenya,
                $userpass,
                $telefono
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

 //------------------ update usuario, actualizar telefono --------------------------
     public static function updateTelefonoUsuario(
        $telefono,       
        $id
    )
    {
        // Creando consulta UPDATE
        $consulta = "UPDATE usuarios" .
            " SET telefono=?" .
            "WHERE id=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($telefono,$id));

        return $cmd;
    }

    // ----------------------------------------------------------------------
}
?>