package com.dev.muslim.heartberry;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ConditionNow extends AppCompatActivity {

    private TextView heartRate ;
    private Button btnDetail;
    private ImageView icHeart;
    public String name,id;
    private  Handler handler;
    private MediaPlayer mPlayer;
    AlertDialog.Builder builder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_now);
        heartRate = (TextView)findViewById(R.id.txtHeartRate);
        btnDetail = (Button)findViewById(R.id.btnDetail);
        icHeart =(ImageView)findViewById(R.id.icHeart);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Typo_Round_Regular_Demo.otf");
        heartRate.setTypeface(typeface);
        builder = new AlertDialog.Builder(this);
        Intent intent= getIntent();
        setTitle(String.valueOf(intent.getStringExtra("name")));

        handler = new Handler();
        m_Runnable.run();

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(),MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id_elderly",id);
                startActivity(intent);
            }
        });
    }


    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            loadDatabase();
            Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_SHORT).show();
            ConditionNow.this.handler.postDelayed(m_Runnable,1000);
        }

    };


    public void loadDatabase(){

        final AsyncHttpClient client= new AsyncHttpClient();
//        RequestParams params = new RequestParams();
        Intent id_elderly= getIntent();
//        params.put("id_elderly",id_elderly.getStringExtra("id_elderly"));
//      String URL=Login.IP+"/heartberry/api/SendData.php?id_elderly="+id_elderly.getStringExtra("id_elderly");
        String URL= "http://192.168.43.50:8080/heartberry/api/SendData.php?id_elderly="+id_elderly.getStringExtra("id_elderly");
        client.get(getApplicationContext(), URL,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    JSONArray jsonArray= response.getJSONArray("data");
                    JSONObject object= jsonArray.getJSONObject(jsonArray.length()-1);
                    Log.d("hasil",String.valueOf(jsonArray.length()));
                    heartRate.setText(String.valueOf(object.getString("heartbeat")));
                    name =String.valueOf(object.getString("name"));
                    id = String.valueOf(object.getString("id_elderly"));
                    setTitle(name);

                    switch (String.valueOf(object.getString("condition"))) {
                        case "very good":
                            icHeart.setImageResource(R.mipmap.ic_good);
                            break;
                        case "good":
                            icHeart.setImageResource(R.mipmap.ic_good);
                            break;
                        case "not bad":
                            icHeart.setImageResource(R.mipmap.ic_warning);
                            break;
                        case "bad":
                            icHeart.setImageResource(R.mipmap.ic_danger);
                            break;
                    }
                    if(object.getString("condition").equals("bad")){
                        mPlayer = MediaPlayer.create(ConditionNow.this, R.raw.alarm);
                        handler.removeCallbacks(m_Runnable);
                        if(!mPlayer.isPlaying())
                        {
                            dialogAlert();
                            mPlayer.start();
                        }
                    }

                    } catch (JSONException e)
                {
                          e.printStackTrace();
                }
            }
        });

}
    public void dialogAlert(){


        builder.setTitle("Danger !");
        builder.setMessage("Need immediate help.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if(mPlayer.isPlaying()){
                    mPlayer.stop();
                }
                dialog.dismiss();
            }

        });

//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // I do not need any action here you might
//                dialog.dismiss();
//            }
//        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        if(mPlayer.isPlaying()){
            mPlayer.stop();
        }
        super.onDestroy();
    }
}
