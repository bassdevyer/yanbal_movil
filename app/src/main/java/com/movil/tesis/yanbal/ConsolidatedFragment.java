package com.movil.tesis.yanbal;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.movil.tesis.yanbal.adapter.OrderItemAdapter;
import com.movil.tesis.yanbal.model.PedidosCabecera;
import com.movil.tesis.yanbal.model.PedidosDetalle;
import com.movil.tesis.yanbal.util.RequestType;
import com.movil.tesis.yanbal.util.UrlUtil;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ConsolidatedFragment extends Fragment {

    private static final String TAG = "ConsolidatedFragment";


    private Spinner campaignSpinner;
    private Spinner weekSpinner;
    private RecyclerView recyclerView;
    private List<PedidosDetalle> orderItems;
    private OrderItemAdapter orderItemAdapter;
    private Button submitButton;


    public ConsolidatedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderItems = new ArrayList<>();
        orderItemAdapter = new OrderItemAdapter(orderItems, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_consolidated, container, false);
        inflateViews(rootView);
        return rootView;
    }

    private void inflateViews(View rootView) {
        campaignSpinner = (Spinner) rootView.findViewById(R.id.campaignSpinner);
        weekSpinner = (Spinner) rootView.findViewById(R.id.weekSpinner);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(orderItemAdapter);
        }
        submitButton = (Button) rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveOrderDetails();
            }
        });
    }

    private void retrieveOrderDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Consultando...");
        String url = UrlUtil.getInstance(getActivity()).getUrl(RequestType.CONSOLIDATED, null, null, null, ((MainActivity) getActivity()).getConsultantIdentification(), campaignSpinner.getSelectedItem().toString(), weekSpinner.getSelectedItem().toString());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                if (response != null) {
                    Type listType = new TypeToken<List<PedidosCabecera>>() {
                    }.getType();
                    List<PedidosCabecera> orders = new Gson().fromJson(response.toString(), listType);
                    populateRecyclerView(orders);
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

    private void populateRecyclerView(List<PedidosCabecera> orders) {
        List<PedidosDetalle> details = new ArrayList<>();
        for (PedidosCabecera order : orders) {
            details.addAll(order.getPedidosDetalles());
        }
        orderItems.clear();
        orderItems.addAll(details);
        orderItemAdapter.notifyDataSetChanged();
    }

}
