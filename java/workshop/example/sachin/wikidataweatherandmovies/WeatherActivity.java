package workshop.example.sachin.wikidataweatherandmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeatherActivity extends AppCompatActivity {
    private TextView etDate,etTemp,etDescription,etNameCity;
    private RequestQueue mQueue;
    private Button buttonParse;
    private EditText editText;
    private String city;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mAdView=findViewById(R.id.adView);

        MobileAds.initialize(WeatherActivity.this,"ca-app-pub-3513016482059275~5685759534");
        AdRequest adRequest=new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
        etDate=findViewById(R.id.etDate);
        etTemp=findViewById(R.id.etTemp);
        etDescription=findViewById(R.id.etDescription);
        etNameCity=findViewById(R.id.etNameCity);
        buttonParse=findViewById(R.id.btn_parse);
        editText=findViewById(R.id.etCity);
        mQueue= Volley.newRequestQueue(this);
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse() {
        city=editText.getText().toString();

        String URL="http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=b2965a74d19d88b7aca1b6b865c76083";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object=response.getJSONObject("main");
                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String temp=String.valueOf(main_object.getDouble("temp"));
                    String description=object.getString("description");
                    String city =response.getString("name");
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
                    String date=sdf.format(calendar.getTime());

                    Double temp_int=Double.parseDouble(temp);
                    double centi=(temp_int-273.15);
                    centi=Math.round(centi);
                    int i=(int)centi;
                   etDate.setText(date);
                   etDescription.setText(description);
                   etNameCity.setText(city);
                   etTemp.setText(String.valueOf(i)+"°");

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
