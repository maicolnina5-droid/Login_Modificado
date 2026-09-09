package com.example.miapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.miapp.data.AppDatabase
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsuario: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvMensaje: TextView
    private lateinit var tvIrRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvMensaje = findViewById(R.id.tvMensaje)
        tvIrRegistro = findViewById(R.id.tvIrRegistro)

        val dao = AppDatabase.getDatabase(applicationContext).usuarioDao()

        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isEmpty() || password.isEmpty()) {
                mostrarMensaje("Todos los campos son obligatorios")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val usuarioEncontrado = dao.login(usuario, password)

                if (usuarioEncontrado != null) {
                    mostrarMensaje("¡Bienvenido ${usuarioEncontrado.usuario}!", esError = false)

                    btnLogin.postDelayed({
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 1000)
                } else {
                    mostrarMensaje("Usuario o contraseña incorrectos")
                }
            }
        }

        tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun mostrarMensaje(texto: String, esError: Boolean = true) {
        tvMensaje.text = texto
        tvMensaje.visibility = TextView.VISIBLE
        val color = if (esError)
            android.R.color.holo_red_dark
        else
            android.R.color.holo_green_dark
        tvMensaje.setTextColor(resources.getColor(color))
    }
}