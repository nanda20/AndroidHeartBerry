package com.dev.muslim.heartberry;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ConditionNow extends AppCompatActivity {
    private TextView heartRate ;
    private Button btnDetail;
    private ImageView icHeart;
    public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_now);
        heartRate = (TextView)findViewById(R.id.txtHeartRate);
        btnDetail = (Button)findViewById(R.id.btnDetail);
        icHeart =(ImageView)findViewById(R.id.icHeart);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Typo_Round_Regular_Demo.otf");
        heartRate.setTypeface(typeface);

        loadDatabase();
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(),MainActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    public void loadDatabase(){
        String URL="http://192.168.1.9:8080/heartberry/api/SendData.php";
        final AsyncHttpClient client= new AsyncHttpClient();

        client.get(getApplicationContext(), URL,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    JSONArray jsonArray= response.getJSONArray("data");
                    JSONObject object= jsonArray.getJSONObject(jsonArray.length()-1);
                    Log.d("hasil",String.valueOf(jsonArray.length()));
                    heartRate.setText(String.valueOf(object.getString("heartbeat")));
                    name =String.valueOf(object.getString("name"));
                    setTitle(name);

                    switch (String.valueOf(object.getString("kondisi"))) {
                        case "good":
                            icHeart.setImageResource(R.mipmap.ic_good);
                            break;
                        case "warning":
                            icHeart.setImageResource(R.mipmap.ic_warning);
                            break;
                        case "danger":
                            icHeart.setImageResource(R.mipmap.ic_danger);
                            break;
                    }

                    } catch (JSONException e)
                {
                          e.printStackTrace();
                }
            }
        });

}

}
