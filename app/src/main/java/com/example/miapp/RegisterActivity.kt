package com.example.miapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.miapp.data.AppDatabase
import com.example.miapp.data.Usuario
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNuevoUsuario: TextInputEditText
    private lateinit var etNuevoPassword: TextInputEditText
    private lateinit var etConfirmarPassword: TextInputEditText
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var tvMensajeRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNuevoUsuario = findViewById(R.id.etNuevoUsuario)
        etNuevoPassword = findViewById(R.id.etNuevoPassword)
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        tvMensajeRegistro = findViewById(R.id.tvMensajeRegistro)

        val dao = AppDatabase.getDatabase(applicationContext).usuarioDao()

        btnRegistrar.setOnClickListener {
            val usuario = etNuevoUsuario.text.toString().trim()
            val password = etNuevoPassword.text.toString().trim()
            val confirmar = etConfirmarPassword.text.toString().trim()

            if (usuario.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
                mostrarMensaje("Todos los campos son obligatorios")
                return@setOnClickListener
            }

            if (password != confirmar) {
                mostrarMensaje("Las contraseñas no coinciden")
                return@setOnClickListener
            }

            if (password.length < 6) {
                mostrarMensaje("La contraseña debe tener al menos 6 caracteres")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existente = dao.buscarPorUsuario(usuario)
                if (existente != null) {
                    mostrarMensaje("Ese nombre de usuario ya está registrado")
                    return@launch
                }

                dao.insertar(Usuario(usuario = usuario, password = password))

                mostrarMensaje("¡Cuenta creada! Ya puedes iniciar sesión", esError = false)

                btnRegistrar.postDelayed({
                    finish()
                }, 1500)
            }
        }
    }

    private fun mostrarMensaje(texto: String, esError: Boolean = true) {
        tvMensajeRegistro.text = texto
        tvMensajeRegistro.visibility = TextView.VISIBLE
        val color = if (esError)
            android.R.color.holo_red_dark
        else
            android.R.color.holo_green_dark
        tvMensajeRegistro.setTextColor(resources.getColor(color))
    }
}