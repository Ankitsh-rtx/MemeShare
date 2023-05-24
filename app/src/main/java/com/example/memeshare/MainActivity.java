package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ImageView imageview;
    private String currentImageUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button share = findViewById(R.id.share);
        Button next = findViewById(R.id.next);
        loadMeme();
        share.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"Hey! Check this meme i got from MemeShare "+currentImageUrl);
            intent.setType("text/plain");

            //Intent Catcher by any app...
            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);
        });

        next.setOnClickListener(view -> {
            loadMeme();
        });
    }
    private void loadMeme(){
//        RequestQueue queue = Volley.newRequestQueue(this);

        //progress bar to show whether the image is loading or not ...
        ProgressBar loadStatus = findViewById(R.id.loadingStatus);
        loadStatus.setVisibility(View.VISIBLE);

        String url ="https://meme-api.com/gimme";
        imageview = findViewById(R.id.imageView);

        // Request a json Object response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentImageUrl = response.getString("url");
                    Glide.with(MainActivity.this).load(currentImageUrl).listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loadStatus.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loadStatus.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageview);

                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                }
            }
            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.d("error","didn't work"); }
        });

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}