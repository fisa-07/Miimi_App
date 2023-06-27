package com.example.miimi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        load_meme();
    }
    private void load_meme() {
        ProgressBar pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.VISIBLE);
        String url = " https://meme-api.com/gimme";
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //textView.setText("Response: " + response.toString());
                        try {
                            String meme_url = response.getString("url");
                            show_meme(meme_url);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast toast = Toast.makeText(MainActivity2.this, error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void show_meme(String url){
        ImageView meme_img = findViewById(R.id.imageView);
        ProgressBar pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);
        Picasso.get().load(url).into(meme_img);
    }

    public void share_in_app(){
        ImageView meme_img = findViewById(R.id.imageView);
        if (meme_img != null) {
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) meme_img.getDrawable());
            Bitmap bitmap = bitmapDrawable.getBitmap();
            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Miimi App", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I found a cool meme on the Miimi App");
            startActivity(Intent.createChooser(shareIntent, "Share Meme"));
        }
        else{
            Toast.makeText(MainActivity2.this,"Image is not Loaded",Toast.LENGTH_SHORT);
        }
    }
    public void share_meme(View view) {
        share_in_app();
    }

    public void next_meme(View view) {
        load_meme();
    }
}