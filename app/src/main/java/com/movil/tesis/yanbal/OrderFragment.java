package com.movil.tesis.yanbal;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.movil.tesis.yanbal.model.Cliente;
import com.movil.tesis.yanbal.model.ProductosYanbal;
import com.movil.tesis.yanbal.util.RequestType;
import com.movil.tesis.yanbal.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class OrderFragment extends Fragment {

    private Spinner clientsSpinner;
    private EditText codeEditText;
    private EditText quantityEditText;
    private Button addItemButton;
    private ProductosYanbal itemToBeAdded;
    private TableLayout tableLayout;

    private static final String TAG = "OrderFragment";

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        inflateViews(rootView);
        initClientsSpinner();
        return rootView;
    }

    private void inflateViews(View rootView) {
        clientsSpinner = (Spinner) rootView.findViewById(R.id.clientsSpinner);
        codeEditText = (EditText) rootView.findViewById(R.id.codeEditText);
        quantityEditText = (EditText) rootView.findViewById(R.id.quantityEditText);
        addItemButton = (Button) rootView.findViewById(R.id.addItemButton);
        if (addItemButton != null) {
            setAddItemButton();
        }
        tableLayout = (TableLayout) rootView.findViewById(R.id.tableLayout);
    }

    private void setAddItemButton() {
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldsValid()) {
                    checkItemAvailability();
                }
            }
        });
    }

    private boolean areFieldsValid() {
        if (TextUtils.isEmpty(codeEditText.getText())) {
            codeEditText.setError("El campo código es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(quantityEditText.getText())) {
            quantityEditText.setError("El campo cantidad es obligatorio");
            return false;
        }
        return true;
    }

    private void addItem() {
        TableRow row = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp);
        TextView codeTextView = new TextView(getActivity());
        codeTextView.setText(String.valueOf(itemToBeAdded.getCodigoRapido()));
        row.addView(codeTextView);
        TextView unitPriceTextView = new TextView(getActivity());
        unitPriceTextView.setText(String.valueOf(itemToBeAdded.getValor()));
        row.addView(unitPriceTextView);
        TextView descriptionTextView = new TextView(getActivity());
        descriptionTextView.setText(itemToBeAdded.getNombreProducto());
        row.addView(descriptionTextView);
        TextView quantityTextView = new TextView(getActivity());
        quantityTextView.setText(quantityEditText.getText());
        row.addView(quantityTextView);
        TextView totalTextView = new TextView(getActivity());
        totalTextView.setText(String.valueOf(Double.parseDouble(itemToBeAdded.getValor().toString()) * Integer.parseInt(quantityEditText.getText().toString())));
        row.addView(totalTextView);
        tableLayout.addView(row);
    }

    private void checkItemAvailability() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Verificando disponibilidad");
        final String code = codeEditText.getText().toString();
        String url = UrlUtil.getInstance(getActivity()).getUrl(RequestType.PRODUCT_EXISTENCE_CHECK, null, null, code);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response != null) {
                    itemToBeAdded = new Gson().fromJson(response.toString(), ProductosYanbal.class);
                    //Toast.makeText(getActivity(), itemToBeAdded.toString(), Toast.LENGTH_SHORT).show();
                    addItem();
                }
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
                    return;
                }
                if (error instanceof ParseError) {
                    AlertDialog incorrectCredentialsAlertDialog = new AlertDialog.Builder(getActivity()).create();
                    incorrectCredentialsAlertDialog.setMessage("No se encontró el prododucto con el código " + codeEditText.getText());
                    incorrectCredentialsAlertDialog.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("Error");
                    alertDialog.show();
                }
            }
        });
        progressDialog.show();
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, null);
    }

    private void initClientsSpinner() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Inicializando");
        String url = UrlUtil.getInstance(getActivity()).getUrl(RequestType.CLIENTS_LIST, null, null, null);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                if (response != null) {
                    Type listType = new TypeToken<List<Cliente>>() {
                    }.getType();
                    List<Cliente> clients = new Gson().fromJson(response.toString(), listType);
                    clientsSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, clients));
                }
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
        });
        progressDialog.show();
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, null);
    }

}
