package com.example.confeccionesbrenda.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.confeccionesbrenda.data.models.User

/**
 * DAO para la entidad User. Proporciona los métodos para interactuar con la tabla 'users'.
 */
@Dao
interface UserDao {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * Si el usuario (basado en su email) ya existe, la operación se ignora.
     * @param user El objeto User a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    /**
     * Busca un usuario por su email.
     * @param email El email del usuario a buscar.
     * @return El objeto User si se encuentra, o null si no existe.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): User?
}
