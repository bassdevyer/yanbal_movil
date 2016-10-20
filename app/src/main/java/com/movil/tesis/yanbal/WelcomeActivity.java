package com.movil.tesis.yanbal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.movil.tesis.yanbal.model.Consultora;
import com.movil.tesis.yanbal.util.RequestType;
import com.movil.tesis.yanbal.util.UrlUtil;

import org.json.JSONObject;

import static android.view.View.GONE;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    private Consultora loggedConsultant;

    private EditText usernameEditText;
    private EditText passWordEditText;

    private static final int REGISTER_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        inflateViews();
    }

    private void inflateViews() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passWordEditText = (EditText) findViewById(R.id.passwordEditText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    authenticate(usernameEditText.getText().toString(), passWordEditText.getText().toString());
                }
            });
        }

        TextView createAcountTextView = (TextView) findViewById(R.id.createAcountTextView);
        if (createAcountTextView != null) {
            createAcountTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerConsultantIntent = new Intent(WelcomeActivity.this, RegisterConsultantActivity.class);
                    startActivity(registerConsultantIntent);
                }
            });
        }
    }


    public void authenticate(String username, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Autenticando");
        //JsonObjectRequest(String url, JSONObject jsonRequest, Listener<JSONObject> listener, Response.ErrorListener errorListener)
        String url = UrlUtil.getInstance(this).getUrl(RequestType.LOGIN, username, password);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response != null) {
                    loggedConsultant = new Gson().fromJson(response.toString(), Consultora.class);
                    showWelcomeMessage();
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
                }
                if (error instanceof ParseError) {
                    AlertDialog incorrectCredentialsAlertDialog = new AlertDialog.Builder(WelcomeActivity.this).create();
                    incorrectCredentialsAlertDialog.setMessage("Su usuario y/o contraseña son incorrectos");
                    incorrectCredentialsAlertDialog.show();
                    cleanCredentialsText();
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

    private void cleanCredentialsText() {
        usernameEditText.getText().clear();
        passWordEditText.getText().clear();
    }

    private void showWelcomeMessage() {
        LinearLayout loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
        if (loginLayout != null) {
            loginLayout.setVisibility(GONE);
        }
        LinearLayout welcomeLinearLayout = (LinearLayout) findViewById(R.id.welcomeLinearLayout);
        if (welcomeLinearLayout != null) {
            welcomeLinearLayout.setVisibility(View.VISIBLE);
            setWelcomeMessage();
        }
    }

    private void setWelcomeMessage() {
        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        if (welcomeTextView != null) {
            welcomeTextView.setText(getString(R.string.welcomeMessage, loggedConsultant.getNombresConsultora()));
            openMainAcvitity();
        }
    }

    private void openMainAcvitity() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainActivityIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }

        }, 2000L);
    }
}
