package com.example.miapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {

    // Para el registro: inserta un usuario nuevo
    @Insert
    suspend fun insertar(usuario: Usuario)

    // Para el login: busca un usuario que coincida con usuario Y password
    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND password = :password LIMIT 1")
    suspend fun login(usuario: String, password: String): Usuario?

    // Para el registro: verifica si el nombre de usuario ya existe
    @Query("SELECT * FROM usuarios WHERE usuario = :usuario LIMIT 1")
    suspend fun buscarPorUsuario(usuario: String): Usuario?
}