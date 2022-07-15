package com.app.apicalling.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.apicalling.Adapter.ArtworkAdapter;
import com.app.apicalling.Database.TinyDB;
import com.app.apicalling.Model.Artworks;
import com.app.apicalling.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Artworks> allArtworks;
    ArtworkAdapter artworkAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Boolean isLoading = false;
    String CurrentURL = "https://api.artic.edu/api/v1/artworks?page=1&limit=5";
    String OriginalURL = "https://api.artic.edu/api/v1/artworks?page=1&limit=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (isNetworkAvailable()) {
                    if (!isLoading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadData(CurrentURL);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        allArtworks = new ArrayList<>();

        if (isNetworkAvailable())
            loadData(CurrentURL);
        else
            fetchDataLocally();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allArtworks = new ArrayList<>();

                if (isNetworkAvailable())
                    loadData(OriginalURL);
                else
                    fetchDataLocally();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }


    public void loadData(String url) {
        isLoading = true;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject pagination = response.getJSONObject("pagination");
                    CurrentURL = pagination.getString("next_url");
                    JSONArray array = response.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        int artist_id = obj.has("id") ? obj.getInt("id") : 0;
                        int fiscal_year;
                        try {
                            fiscal_year = obj.has("fiscal_year") ? obj.getInt("fiscal_year") : 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                            fiscal_year = 0;
                        }
                        String artist_display = obj.has("artist_display") ? obj.getString("artist_display") : "Empty";
                        String title = obj.has("title") ? obj.getString("title") : "Empty";
                        Artworks artwork = new Artworks(title, artist_display, fiscal_year, artist_id);
                        allArtworks.add(artwork);
                    }

                    artworkAdapter = new ArtworkAdapter(MainActivity.this, allArtworks);
                    recyclerView.setAdapter(artworkAdapter);

                    saveDataLocally();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLoading = false;
                Log.d("APIDATA", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APIDATA", error.toString());
                isLoading = false;
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void saveDataLocally() {
//        ArrayList<Artworks> artworks = allArtworks;
        ArrayList<Object> playerObjects = new ArrayList<Object>();

        for (Artworks a : allArtworks) {
            playerObjects.add((Object) a);
        }

        TinyDB tinydb = new TinyDB(this);
        tinydb.putListObject("data", playerObjects);
    }

    public void fetchDataLocally() {
        TinyDB tinydb = new TinyDB(this);
        allArtworks = tinydb.getArtworks("data");
        artworkAdapter = new ArtworkAdapter(MainActivity.this, allArtworks);
        recyclerView.setAdapter(artworkAdapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}