package com.dev.muslim.heartberry;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {
    public static String IP="http://192.168.43.50:8080";
    private EditText txtName;
    private EditText txtBorn;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtName = (EditText) findViewById(R.id.txtName);
        txtBorn = (EditText) findViewById(R.id.txtBorn);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView txtLogin= (TextView) findViewById(R.id.txtLogin);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Typo_Round_Regular_Demo.otf");
        txtLogin.setTypeface(typeface);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProses();
            }
        });
    }

    public void loginProses() {
        final ProgressDialog dialog = new ProgressDialog(Login.this);
        dialog.setMessage("Get Data...");
        dialog.setCancelable(false);
        dialog.show();

        String URL = IP+"/heartberry/api/login.php";
        final String name = txtName.getText().toString();
        String born = txtBorn.getText().toString();

        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("born", born);

        final AsyncHttpClient client = new AsyncHttpClient();

        client.post(URL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Log.d("HASIL",response.toString());

                    switch (String.valueOf(response.getString("status"))) {
                        case "success":
                            Intent intent = new Intent(Login.this,ConditionNow.class);
                            intent.putExtra("name",name);
                            intent.putExtra("id_elderly",response.getString("id_elderly"));
                            Log.d("id_elderly",response.getString("id_elderly"));

                            startActivity(intent);

                            break;
                        case "failure":
                            Toast.makeText(getApplicationContext(),"Login Failure",Toast.LENGTH_LONG).show();

                            break;

                    }


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("HASIL",responseString.toString());

            }
        });

    }
}
