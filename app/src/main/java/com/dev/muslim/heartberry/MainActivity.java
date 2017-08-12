package com.dev.muslim.heartberry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public RecyclerView myRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public RecyclerView.Adapter mAdapter;
    List<DataElderly> arrayList= new ArrayList<>();
    private String URL="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRecyclerView = (RecyclerView)findViewById(R.id.recycleLayout);
        mLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent= getIntent();
        setTitle(intent.getStringExtra("name"));

        loadDatabase();

    }
    public void loadDatabase(){
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Get Data...");
        dialog.setCancelable(false);
        dialog.show();
        final AsyncHttpClient client= new AsyncHttpClient();

        URL="http://192.168.1.9:8080/heartberry/api/SendData.php";
        client.setConnectTimeout(10000);
        client.get(getApplicationContext(), URL,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    JSONArray jsonArray= response.getJSONArray("data");
                    Log.d("hasil",String.valueOf(jsonArray.length()));

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject object= jsonArray.getJSONObject(i);
//                        int rate= Integer.valueOf(object.getString("heartbeat"));
//                        String kondisi;
//                        if(rate >80){
//                            kondisi="Tidak Normal";
//                        }else{
//                            kondisi="Normal";
//                        }
                        DataElderly data= new DataElderly(Integer.valueOf(object.getString("id_elderly")),object.getString("name"),object.getString("born"),object.getString("room"),object.getString("waktu"),object.getString("heartbeat"),object.getString("kondisi"));
                        arrayList.add(data);


                    }
                        mAdapter= new PelangganAdapter (arrayList,getApplicationContext());
                        myRecyclerView.setAdapter(mAdapter);
                    if(dialog.isShowing()){
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Data Berhasil di Perbarui",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }

    public class PelangganAdapter extends RecyclerView.Adapter<PelangganAdapter.ViewHolder>{
        Context context;
        List<DataElderly> data;
        public PelangganAdapter(List<DataElderly> data, Context context) {
            this.data= data;
            this.context= context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = myRecyclerView.getChildAdapterPosition(view);
                    if (pos >= 0 && pos < getItemCount()) {
//                        Intent intent= new Intent(context,DetailPelanggan.class);
//                        intent.putExtra("id1",arrayList.get(pos).getId1());
//                        intent.putExtra("no_pel",arrayList.get(pos).getNo_Pel());
//                        intent.putExtra("no_tiang",arrayList.get(pos).getNo_tiang());
//                        intent.putExtra("nama",arrayList.get(pos).getNama());
//                        intent.putExtra("alamat",arrayList.get(pos).getAlamat());
//                        intent.putExtra("lat",arrayList.get(pos).getLat());
//                        intent.putExtra("long",arrayList.get(pos).getLng());
//                        intent.putExtra("kode_baca",arrayList.get(pos).getKode_baca());
//                        intent.putExtra("status",arrayList.get(pos).getStatus());
//                        startActivity(intent);
                    }
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DataElderly pl= data.get(position);
//            holder.id_elderly.setText(String.valueOf(pl.getId_elderly()));
//            holder.name.setText(String.valueOf(pl.getName()));
//            holder.room.setText("Room : " +String.valueOf(pl.getRoom()));
            holder.waktu.setText(String.valueOf(pl.getWaktu()));
            holder.heartbeat.setText(String.valueOf(pl.getHeartbeat()));
            holder.kondisi.setText(String.valueOf(pl.getKondisi()));

            switch (String.valueOf(pl.getKondisi())) {
                case "good" :
                    holder.icon.setImageResource(R.mipmap.ic_good);
                    break;
                case "warning":
                    holder.icon.setImageResource(R.mipmap.ic_warning);
                    break;
                case "danger":
                    holder.icon.setImageResource(R.mipmap.ic_danger);
                    break;


            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView id_elderly,name,born,room,waktu,heartbeat,kondisi;
            public ImageView icon;

            public ViewHolder(View itemView) {
                super(itemView);

//                id_elderly =(TextView)itemView.findViewById(R.id.id);
//                name=(TextView)itemView.findViewById(R.id.name);
//                room=(TextView)itemView.findViewById(R.id.room);
                waktu=(TextView)itemView.findViewById(R.id.waktu);
                heartbeat=(TextView)itemView.findViewById(R.id.heartbeat);
                kondisi=(TextView)itemView.findViewById(R.id.kondisi);
                icon=(ImageView) itemView.findViewById(R.id.ic_icon);
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Typo_Round_Regular_Demo.otf");
                heartbeat.setTypeface(typeface);
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
    }




}
