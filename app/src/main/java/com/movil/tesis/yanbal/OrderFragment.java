package com.movil.tesis.yanbal;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.movil.tesis.yanbal.adapter.OrderItemAdapter;
import com.movil.tesis.yanbal.model.Cliente;
import com.movil.tesis.yanbal.model.Consultora;
import com.movil.tesis.yanbal.model.PedidosCabecera;
import com.movil.tesis.yanbal.model.PedidosDetalle;
import com.movil.tesis.yanbal.model.ProductosYanbal;
import com.movil.tesis.yanbal.util.Constants;
import com.movil.tesis.yanbal.util.Preferences;
import com.movil.tesis.yanbal.util.RequestType;
import com.movil.tesis.yanbal.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.movil.tesis.yanbal.R.id.recyclerView;

public class OrderFragment extends Fragment {

    private Spinner clientsSpinner;
    private EditText codeEditText;
    private EditText quantityEditText;
    private Button addItemButton;
    private ProductosYanbal itemToBeAdded;
    private String consultantIdentification;
    private List<PedidosDetalle> orderItems;
    private PedidosCabecera orderHeader;
    private OrderItemAdapter orderItemAdapter;
    private PedidosDetalle itemToUpdate;
    private PedidosDetalle itemToDelete;

    private final SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());

    private static final String TAG = "OrderFragment";

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consultantIdentification = Preferences.getInstance(getActivity()).readStringPreference(Constants.CONSULTANT_ID, null);
        orderItems = new ArrayList<>();
        orderHeader = new PedidosCabecera();
        orderItemAdapter = new OrderItemAdapter(orderItems, new OnItemClickListener());
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
        Button registerOrderButton = (Button) rootView.findViewById(R.id.registerOrderButton);
        if (registerOrderButton != null) {
            registerOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateAndSubmit();
                }
            });
        }
        RecyclerView orderItemsRecyclerView = (RecyclerView) rootView.findViewById(recyclerView);
        if (orderItemsRecyclerView != null) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            orderItemsRecyclerView.setLayoutManager(mLayoutManager);
            orderItemsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            orderItemsRecyclerView.setAdapter(orderItemAdapter);
        }

    }

    private void validateAndSubmit() {
        boolean error = false;
        String alertText = "";
        if (clientsSpinner.getSelectedItem() == null) {
            error = true;
            alertText += getString(R.string.select_client);
            alertText += "\n";
        }
        if (orderItems.isEmpty()) {
            error = true;
            alertText += getString(R.string.add_item);
        }
        if (error) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(getActivity());
            errorDialog.setMessage(alertText);
            errorDialog.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            errorDialog.create().show();
        } else {
            registerOrder();
        }
    }

    public class OnItemClickListener implements AdapterView.OnClickListener, AdapterView.OnLongClickListener {
        @Override
        public void onClick(View view) {
            updateFields();
        }

        @Override
        public boolean onLongClick(final View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("¿Está seguro que desea eliminar el elemento?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    OrderItemAdapter.ViewHolder viewHolder = (OrderItemAdapter.ViewHolder) view.getTag();
                    itemToDelete = findItemInList(Integer.parseInt(viewHolder.code.getText().toString()));
                    if (itemToDelete != null) {
                        orderItems.remove(itemToDelete);
                        orderItemAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Item eliminado", Toast.LENGTH_SHORT).show();
                        clearFields();
                        codeEditText.requestFocus();
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
            return true;
        }

    }

    private void updateFields() {
        Toast.makeText(getActivity(), "TODO update fields", Toast.LENGTH_SHORT).show();
    }

    private void registerOrder() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando orden");
        String url = UrlUtil.getInstance(getActivity()).getUrl(RequestType.ORDER_REGISTER, null, null, null);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: " + response.toString());
                //PedidosCabecera createdOrder = new Gson().fromJson(response.toString(), PedidosCabecera.class);
                Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
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
                Cliente client = (Cliente) clientsSpinner.getSelectedItem();
                Consultora consultant = new Consultora();
                consultant.setIdentificacionConsultora(consultantIdentification);
                orderHeader = new PedidosCabecera();
                orderHeader.setCliente(client);
                orderHeader.setConsultora(consultant);
                orderHeader.setFechaCompra(dt.format(new Date()));
                orderHeader.setPedidosDetalles(orderItems);
                Log.d(TAG, "getBody: " + new Gson().toJson(orderHeader));
                return new Gson().toJson(orderHeader).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        progressDialog.show();
        AppSingleton.getInstance(getActivity()).addToRequestQueue(request, null);
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
        PedidosDetalle itemToAdd = new PedidosDetalle();
        itemToAdd.setNombreProducto(String.valueOf(itemToBeAdded.getCodigoRapido()));
        itemToAdd.setDescripcionProducto(itemToBeAdded.getNombreProducto());
        itemToAdd.setPrecio(itemToBeAdded.getValor().doubleValue());
        itemToAdd.setCantidad(Integer.parseInt(quantityEditText.getText().toString()));
        itemToUpdate = findItemInList(itemToBeAdded.getCodigoRapido());
        if (itemToUpdate != null) {
            showUpdateAlert();
        } else {
            orderItems.add(itemToAdd);
            orderItemAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), R.string.item_added, Toast.LENGTH_SHORT).show();
            clearFields();
            codeEditText.requestFocus();
        }
    }


    private void showUpdateAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("El item que desea agregar ya se encuentra en la lista ¿Desea actualizar la cantidad?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemToUpdate.setCantidad(Integer.parseInt(quantityEditText.getText().toString()));
                orderItemAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Item actualizado", Toast.LENGTH_SHORT).show();
                clearFields();
                codeEditText.requestFocus();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private PedidosDetalle findItemInList(Integer code) {
        for (PedidosDetalle item : orderItems) {
            if (item.getNombreProducto().equals(String.valueOf(code))) {
                return item;
            }
        }
        return null;
    }

    private void clearFields() {
        codeEditText.getText().clear();
        quantityEditText.getText().clear();
    }

    private void checkItemAvailability() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.checking_availability));
        final String code = codeEditText.getText().toString();
        String url = UrlUtil.getInstance(getActivity()).getUrl(RequestType.PRODUCT_EXISTENCE_CHECK, null, null, code);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response != null) {
                    itemToBeAdded = new Gson().fromJson(response.toString(), ProductosYanbal.class);
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
                    incorrectCredentialsAlertDialog.setMessage(getString(R.string.product_not_found, codeEditText.getText()));
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
