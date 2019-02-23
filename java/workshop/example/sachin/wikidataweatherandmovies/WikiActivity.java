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

public class WikiActivity extends AppCompatActivity {
    private TextView mTextViewResult,text_Description;
    private RequestQueue mQueue;
    private Button buttonParse;
    private String query;
    private EditText editText;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        mAdView=findViewById(R.id.adView);

        MobileAds.initialize(WikiActivity.this,"ca-app-pub-3513016482059275~5685759534");
        AdRequest adRequest=new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
        mTextViewResult=findViewById(R.id.text_viewresult);
        text_Description=findViewById(R.id.text_description);
        buttonParse=findViewById(R.id.btn_parse);
        editText=findViewById(R.id.etquery);
        mQueue= Volley.newRequestQueue(this);
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse() {


        query=editText.getText().toString();
        String URL="https://www.wikidata.org/w/api.php?action=wbsearchentities&search="+query+"&language=en&format=json";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object=response.getJSONObject("searchinfo");
                    JSONArray jsonArray=response.getJSONArray("search");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String description=object.getString("description");
                    String city =object.getString("label");

                    mTextViewResult.setText("Query:"+city);
                    text_Description.setText("Description:"+description);
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

