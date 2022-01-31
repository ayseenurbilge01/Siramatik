package com.example.siramatik

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var outputstring: String?
        val AES = "AES"

        var sharedpreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("loginusername"," ")!=" ")
        {
            outputstring = encrypt(editId.getText().toString(), editPassword.getText().toString())
            val intent = Intent(this, MainActivity::class.java)
            val args = Bundle()
            args.putString("Id", outputstring)
            intent.putExtras(args)
            startActivity(intent)
            finish()
        }
        btngiris.setOnClickListener {
            if (editId.text.isEmpty() || editPassword.text.isEmpty())
                Toast.makeText(this, "Lütfen boş alan bırakmayınız", Toast.LENGTH_SHORT).show()
            else {
                outputstring =
                    encrypt(editId.getText().toString(), editPassword.getText().toString())
                val rq = Volley.newRequestQueue(this)
                val url = "http://10.0.2.2:5000/api/giriskontrol"
                val stringRequest =  object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener { response ->
                        try {
                            val strRes = response.toString()
                            if (strRes.equals("true")) {
                                var editor = sharedpreferences.edit()

                                editor.putString("loginusername", editId.getText().toString())

                                editor.commit()

                                val intent = Intent(this, MainActivity::class.java)

                                intent.putExtra("Id", outputstring)
                                startActivity(intent)
                                finish()

                            } else
                                Toast.makeText(applicationContext, strRes, Toast.LENGTH_SHORT).show()

                        } catch (ex: JSONException) {
                        }
                    }, object : Response.ErrorListener {
                        override fun onErrorResponse(volleyError: VolleyError) {
                            Toast.makeText(applicationContext, "error,"+volleyError.toString()+"", Toast.LENGTH_LONG).show()

                        }
                    })
                 {
                        override fun getParams(): Map<String, String> {
                        val params: HashMap<String, String> = HashMap()
                        params.put("Id", outputstring.toString())
                        return params
                    }
                }
                rq.add(stringRequest)
            }

        }
        var boolPassword =true
        imageViewShowPassword.setOnClickListener{
                if (boolPassword) {
                    editPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    imageViewShowPassword.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_eye_off))
                } else {
                    editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    imageViewShowPassword.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_p_eye))
                }
                boolPassword = !boolPassword

        }


    }




    @Throws(Exception::class)
    private fun encrypt(Data: String, Password: String): String? {
        val key = generateKey(Password)

        val AES="AES"
        val c = Cipher.getInstance(AES)
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(Data.toByteArray())
        var encryptedValue: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedValue = Base64.getEncoder().encodeToString(encVal)
        }
        return encryptedValue
    }

    @Throws(Exception::class)
    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray(StandardCharsets.UTF_8)
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }
}