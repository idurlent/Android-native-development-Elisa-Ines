package fr.ines.elisa.disney

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase
        auth = FirebaseAuth.getInstance()

        // Vues
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)


        loginButton.setOnClickListener {

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Vérification champs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Remplis tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {


                        val error = task.exception.toString()

                        val message = when {
                            error.contains("no user record", true) ->
                                "Aucun compte avec cet email"

                            error.contains("password is invalid", true) ->
                                "Mot de passe incorrect"

                            error.contains("badly formatted", true) ->
                                "Email invalide"

                            else -> "Erreur lors de la connexion"
                        }

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
        }


        registerButton.setOnClickListener {

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Remplis tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {

                        val error = task.exception.toString()

                        val message = when {
                            error.contains("already in use", true) ->
                                "Email déjà utilisé"

                            error.contains("password should be at least", true) ->
                                "Mot de passe trop court (min 6 caractères)"

                            else -> "Erreur lors de l'inscription"
                        }

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}