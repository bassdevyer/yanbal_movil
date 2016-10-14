package com.movil.tesis.yanbal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.movil.tesis.yanbal.model.Consultora;

import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    private Consultora loggedConsultant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        inflateViews();
    }

    private void inflateViews() {
        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final EditText passWordEditText = (EditText) findViewById(R.id.passwordEditText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    authenticate(usernameEditText.getText().toString(), passWordEditText.getText().toString());
                }
            });
        }
    }


    public void authenticate(String username, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Autenticando");
        //JsonObjectRequest(String url, JSONObject jsonRequest, Listener<JSONObject> listener, Response.ErrorListener errorListener)
        String url = "http://192.168.1.8:8080/yanbalWs/authenticate/" + username + "/" + password;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response != null) {
                    loggedConsultant = new Gson().fromJson(response.toString(), Consultora.class);
                    Toast.makeText(WelcomeActivity.this, "Bienvenida, " + loggedConsultant.getNombres(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(WelcomeActivity.this).create();
                    alertDialog.setMessage("Error contactando al servidor");
                    alertDialog.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(WelcomeActivity.this).create();
                    alertDialog.setMessage("Error");
                    alertDialog.show();
                }
            }
        });
        progressDialog.show();
        AppSingleton.getInstance(this).addToRequestQueue(request, null);


    }
}
