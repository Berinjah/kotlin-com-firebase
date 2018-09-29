package com.example.aluno.listafilmes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    private lateinit var etLoginUsuario:EditText
    private lateinit var etLoginSenha:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnCriarConta:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        //findViewById vincula as variáveis ao xml da activity
        etLoginUsuario = findViewById(R.id.etLoginUsuario)
        etLoginSenha = findViewById(R.id.etLoginSenha)
        btnLogin =  findViewById(R.id.btnLogin)
        btnCriarConta = findViewById(R.id.btnCriarConta)

        btnLogin.setOnClickListener {//ao clicar no botão, são passados os parâmetros e o método é utilizado, o mesmo vale par ao btnCriarConta
            view -> signIn(view, etLoginUsuario.text.toString(), etLoginSenha.text.toString())
        }

        btnCriarConta.setOnClickListener {
            view -> createUser(view, etLoginUsuario.text.toString(), etLoginSenha.text.toString())
        }

        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser //recebe o usuário atual para que não seja necessário logar todas as vezes que for utilizar o app
            if(user != null){
                //intent é utilizadop para acessar outra activity
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if(mAuthListener != null){ //remove para poupar recursos
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }
    fun signIn(view: View, email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(!it.isSuccessful) showMessage(view, message = "Erro: ${it.exception?.message}")
        }
    }

    fun createUser(view: View, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(!it.isSuccessful) showMessage(view, message = "Erro: ${it.exception?.message}")
        }
    }

    fun showMessage(view:View, message: String){/*mostra uma mensagem em uma snackbar,
                                                utilizado para exibir os erros*/
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}
