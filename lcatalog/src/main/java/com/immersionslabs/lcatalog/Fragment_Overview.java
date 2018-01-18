package com.immersionslabs.lcatalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.adapters.MainListViewAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Fragment_Overview extends Fragment implements ApiCommunication {
    private static final String TAG = "Fragment_Overview";

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/vendorArticles";

    private ArrayList<String> item_ids;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_names;
    private ArrayList<String> item_images;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    RecyclerView main_recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_overview, container, false);

        item_ids = new ArrayList<>();
        item_names = new ArrayList<>();
        item_descriptions = new ArrayList<>();
        item_images = new ArrayList<>();
        item_prices = new ArrayList<>();
        item_discounts = new ArrayList<>();
        getData();

        return view;
    }

    private void getData() {
        ApiService.getInstance(getContext()).getData(this, false, "CATALOGUE", REGISTER_URL, "GETDATA");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {

        Log.e(TAG, "response--" + response);
        JSONArray resp = null;
        try {
            resp = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mainRecyclerView(resp);

    }

    private void mainRecyclerView(JSONArray m_jsonArray) {

        main_recycler = getView().findViewById(R.id.main_recycler);
        main_recycler.setHasFixedSize(true);

        for (int i = 0; i < m_jsonArray.length(); i++) {
            JSONObject obj;
            try {
                obj = m_jsonArray.getJSONObject(i);

                item_ids.add(obj.getString("_id"));
                item_descriptions.add(obj.getString("description"));
                item_images.add(obj.getString("img"));
                item_names.add(obj.getString("name"));
                item_prices.add(obj.getString("price"));
                item_discounts.add(obj.getString("discount"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "ids******" + item_ids);
            Log.e(TAG, "images******" + item_images);
            Log.e(TAG, "names******" + item_names);
            Log.e(TAG, "descriptions*******" + item_descriptions);
            Log.e(TAG, "prices******" + item_prices);
            Log.e(TAG, "discounts******" + item_discounts);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            main_recycler.setLayoutManager(linearLayoutManager);
            MainListViewAdapter gridAdapter = new MainListViewAdapter(this, item_ids, item_names, item_images, item_prices, item_discounts, item_descriptions);
            main_recycler.setAdapter(gridAdapter);
        }
    }
    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
