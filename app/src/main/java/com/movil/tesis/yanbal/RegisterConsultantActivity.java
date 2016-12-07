package com.movil.tesis.yanbal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterConsultantActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Spinner consultantIdTypeSpinner;
    private EditText consultantName;
    private EditText consultantLastName;
    private EditText consultantEmail;
    private EditText consultantPassword;
    private EditText consultantPasswordConfirmation;
    private EditText consultantIdentification;
    private EditText consultantPhone;
    private EditText consultantMobile;
    private Spinner consultantGenreSpinner;
    private EditText consultantSecurityCode;

    private TextView consultantBirthDateTextView;

    private static final String TAG = "RegisterConsultantActiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_consultant);

        inflateViews();
    }

    private void inflateViews() {
        consultantIdTypeSpinner = (Spinner) findViewById(R.id.consultantIdType);
        consultantName = (EditText) findViewById(R.id.consultantName);
        consultantLastName = (EditText) findViewById(R.id.consultantLastname);
        consultantEmail = (EditText) findViewById(R.id.consultantEmail);
        consultantPassword = (EditText) findViewById(R.id.consultantPassword);
        consultantPasswordConfirmation = (EditText) findViewById(R.id.consultantPasswordConfirmation);
        consultantIdentification = (EditText) findViewById(R.id.consultantIdentification);
        consultantPhone = (EditText) findViewById(R.id.consultantPhone);
        consultantMobile = (EditText) findViewById(R.id.consultantMobile);
        consultantGenreSpinner = (Spinner) findViewById(R.id.consultantGenre);
        consultantSecurityCode = (EditText) findViewById(R.id.consultantSecurityCode);

        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    submitRegister();
                }
            }
        });

        Button selectBirthDateButton = (Button) findViewById(R.id.selectBirthDateButton);
        if (selectBirthDateButton != null) {
            selectBirthDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new BirthDateSelectionDatePicker();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
            });
        }

        consultantBirthDateTextView = (TextView) findViewById(R.id.consultantBirthDateTextView);

    }

    private boolean validateFields() {
        boolean outcome = true;
        if (TextUtils.isEmpty(consultantIdentification.getText())) {
            consultantIdentification.setError("El campo cédula es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(consultantName.getText())) {
            consultantName.setError("El campo nombre es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(consultantLastName.getText())) {
            consultantLastName.setError("El campo apellido es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(consultantEmail.getText())) {
            consultantEmail.setError("El campo email es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(consultantPassword.getText())) {
            consultantPassword.setError("El campo contraseña es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(consultantPhone.getText())) {
            consultantPhone.setError("El campo teléfono es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(consultantMobile.getText())) {
            consultantMobile.setError("El campo celular es obligatorio");
            outcome = false;
        }
        if (consultantBirthDateTextView.getText().toString().equals("Ninguna")) {
            consultantBirthDateTextView.setError("La fecha de nacimiento es obligatoria");
            outcome = false;
        }
        if (TextUtils.isEmpty(consultantSecurityCode.getText())) {
            consultantSecurityCode.setError("El campo código de registro es obligatorio");
            outcome = false;
        }

        if (TextUtils.isEmpty(consultantPasswordConfirmation.getText())) {
            consultantPasswordConfirmation.setError("Por favor confirme su contraseña");
            outcome = false;
        } else {
            if (!consultantPassword.getText().toString().equals(consultantPasswordConfirmation.getText().toString())) {
                consultantPasswordConfirmation.setError("Las contraseñas no coinciden");
                outcome = false;
            }
        }
        return outcome;
    }

    private void submitRegister() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando");
        // getUrl(RequestType requestType, String username, String password, String code, String consultantId, String campaign, String week, String securityCode)
        String url = UrlUtil.getInstance(this).getUrl(RequestType.CONSULTANT_REGISTER, null, null, null, null, null, null, consultantSecurityCode.getText().toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Consultora createdConsultora = new Gson().fromJson(response.toString(), Consultora.class);
                Toast.makeText(RegisterConsultantActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterConsultantActivity.this).create();
                    alertDialog.setMessage("Error contactando al servidor");
                    alertDialog.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterConsultantActivity.this).create();
                    alertDialog.setMessage("Error registrando la consultora. Verifique que la información y el código de seguridad sean los correctos.");
                    alertDialog.show();
                }
            }
        }
        ) {
            @Override
            public byte[] getBody() {
                Consultora outcome = new Consultora();
                outcome.setIdentificacionConsultora(consultantIdentification.getText().toString());
                outcome.setTipoIdentificacionConsultora(consultantIdTypeSpinner.getSelectedItem().toString());
                outcome.setNombresConsultora(consultantName.getText().toString());
                outcome.setApellidosConsultora(consultantLastName.getText().toString());
                outcome.setEmailConsultora(consultantEmail.getText().toString());
                outcome.setTelefonoConsultora(consultantPhone.getText().toString());
                outcome.setCelularConsultora(consultantMobile.getText().toString());
                outcome.setGeneroConsultora(consultantGenreSpinner.getSelectedItem().toString());
                outcome.setPassword(consultantPassword.getText().toString());
                outcome.setFechaNacimientoConsultora(consultantBirthDateTextView.getText().toString());
                return new Gson().toJson(outcome).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        progressDialog.show();
        AppSingleton.getInstance(this).addToRequestQueue(request, null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        consultantBirthDateTextView.setText(year + "/" + month + "/" + dayOfMonth);
    }
}
