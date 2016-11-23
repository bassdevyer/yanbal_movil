package com.movil.tesis.yanbal;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.movil.tesis.yanbal.model.Cliente;
import com.movil.tesis.yanbal.util.RequestType;
import com.movil.tesis.yanbal.util.UrlUtil;

import org.json.JSONObject;

import java.math.BigDecimal;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


/**
 * Created by mac on 10/18/16.
 */

public class ClientRegisterFragment extends Fragment {

    private EditText clientNameEditText;
    private EditText clientLastnameEditText;
    private Spinner clientIdTypeSpinner;
    private EditText clientIdentificationEditText;
    private EditText clientEmailEditText;
    private EditText clientPhoneEditText;
    private EditText clientMobileEditText;
    private Spinner clientGenreSpinner;
    private TextView clientBirthDateTextView;
    private TextView clientLocationLatitudeTextView;
    private TextView getClientLocationLongitudeTextView;

    private Button submitClientRegisterButton;

    private ImageButton locationImageButton;

    public ClientRegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_client_register, container, false);
        inflateViews(rootView);
        return rootView;
    }

    private void inflateViews(View rootView) {
        locationImageButton = (ImageButton) rootView.findViewById(R.id.clientLocationButton);
        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapActivityIntent = new Intent(getActivity(), MapActivity.class);
                getActivity().startActivityForResult(mapActivityIntent, MainActivity.MAP_LOCATION_REQ_CODE);
            }
        });

        clientNameEditText = (EditText) rootView.findViewById(R.id.clientName);
        clientLastnameEditText = (EditText) rootView.findViewById(R.id.clientLastname);
        clientIdTypeSpinner = (Spinner) rootView.findViewById(R.id.clientIdType);
        clientIdentificationEditText = (EditText) rootView.findViewById(R.id.clientIdentification);
        clientEmailEditText = (EditText) rootView.findViewById(R.id.clientEmail);
        clientPhoneEditText = (EditText) rootView.findViewById(R.id.clientPhone);
        clientMobileEditText = (EditText) rootView.findViewById(R.id.clientMobile);
        clientGenreSpinner = (Spinner) rootView.findViewById(R.id.clientGenre);
        clientBirthDateTextView = (TextView) rootView.findViewById(R.id.clientBirthDateTextView);
        clientLocationLatitudeTextView = (TextView) rootView.findViewById(R.id.clientLocationLatitudeTextView);
        getClientLocationLongitudeTextView = (TextView) rootView.findViewById(R.id.clientLocationLongitudeTextView);

        submitClientRegisterButton = (Button) rootView.findViewById(R.id.registerClientButton);

        submitClientRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    submitRegister();
                }
            }
        });

        Button selectBirthDateButton = (Button) rootView.findViewById(R.id.selectBirthDateButton);
        if (selectBirthDateButton != null) {
            selectBirthDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new BirthDateSelectionDatePicker();
                    newFragment.show(getActivity().getFragmentManager(), "datePicker");
                }
            });
        }
    }

    private boolean validateFields() {
        boolean outcome = true;
        if (TextUtils.isEmpty(clientIdentificationEditText.getText())) {
            clientIdentificationEditText.setError("El campo identificación es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(clientNameEditText.getText())) {
            clientNameEditText.setError("El campo nombre es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(clientLastnameEditText.getText())) {
            clientLastnameEditText.setError("El campo apellido es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(clientEmailEditText.getText())) {
            clientEmailEditText.setError("El campo email es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(clientPhoneEditText.getText())) {
            clientPhoneEditText.setError("El campo teléfono es obligatorio");
            outcome = false;
        }
        if (TextUtils.isEmpty(clientMobileEditText.getText())) {
            clientMobileEditText.setError("El campo celular es obligatorio");
            outcome = false;
        }
        if (clientBirthDateTextView.getText().toString().equals("Ninguna")) {
            clientBirthDateTextView.setError("La fecha de nacimiento es obligatoria");
            outcome = false;
        }
        if (TextUtils.isEmpty(clientLocationLatitudeTextView.getText()) || TextUtils.isEmpty(getClientLocationLongitudeTextView.getText())) {
            clientLocationLatitudeTextView.setError("La ubicación es obligatoria");
            outcome = false;
        }
        return outcome;
    }

    private void submitRegister() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando");
        String url = UrlUtil.getInstance(getActivity()).getUrl(RequestType.CLIENT_REGISTER, null, null, null);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                getActivity().startActivity(getActivity().getIntent());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
                progressDialog.dismiss();
                if (error instanceof TimeoutError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("Error contactando al servidor");
                    alertDialog.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("Error");
                    alertDialog.show();
                }
            }
        }
        ) {
            @Override
            public byte[] getBody() {
                Cliente outcome = new Cliente();
                outcome.setIdentificacionCliente(clientIdentificationEditText.getText().toString());
                outcome.setTipoIdentificacionCliente(clientIdTypeSpinner.getSelectedItem().toString());
                outcome.setNombresCliente(clientNameEditText.getText().toString());
                outcome.setApellidosCliente(clientLastnameEditText.getText().toString());
                outcome.setEmailCliente(clientEmailEditText.getText().toString());
                outcome.setTelefonoCliente(clientPhoneEditText.getText().toString());
                outcome.setCelularCliente(clientMobileEditText.getText().toString());
                outcome.setGeneroCliente(clientGenreSpinner.getSelectedItem().toString());
                outcome.setFechaNacimientoCliente(clientBirthDateTextView.getText().toString());
                outcome.setLatitudCliente(BigDecimal.valueOf(Double.parseDouble(clientLocationLatitudeTextView.getText().toString())));
                outcome.setLongitudCliente(BigDecimal.valueOf(Double.parseDouble(getClientLocationLongitudeTextView.getText().toString())));
                outcome.setCodConsultora(((MainActivity) getActivity()).getConsultantIdentification());
                return new Gson().toJson(outcome).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        progressDialog.show();
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, null);
    }
}
