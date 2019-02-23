package workshop.example.sachin.wikidataweatherandmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieActivity extends AppCompatActivity {
    private EditText etName,etYear;
    private Button btnSearch;
    private RequestQueue queue;
    private ImageView img;
    private String name,year;
    private TextView tvtitle,tvrating,tvdescription;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        mAdView=findViewById(R.id.adView);

        MobileAds.initialize(MovieActivity.this,"ca-app-pub-3513016482059275~5685759534");
        AdRequest adRequest=new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
        etName=findViewById(R.id.etName);
        etYear=findViewById(R.id.etYear);
        btnSearch=findViewById(R.id.btnSearch);
        queue= Volley.newRequestQueue(this);
        img=findViewById(R.id.img);
        tvtitle=findViewById(R.id.tvtitle);
        tvrating=findViewById(R.id.tvrating);
        tvdescription=findViewById(R.id.tvdescription);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMovie();
            }
        });

    }

    private void searchMovie() {
        name=etName.getText().toString();
        year=etYear.getText().toString();
        String URL="http://www.omdbapi.com/?apikey=aecb4a72&t="+name+"&y="+year;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("Ratings");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String title=response.getString("Title");
                    String imgurl=response.getString("Poster");
                    String rate=object.getString("Value");
                    String description=response.getString("Plot");
                    String imgs[]={imgurl};
                    Glide.with(MovieActivity.this).load(imgs[0]).into(img);
                    tvtitle.setText("Title:"+title);
                    tvrating.setText("Ratings:"+rate);
                    tvdescription.setText("Plot:"+description);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);

    }
}
