package com.example.rdetails;

import com.example.rdetails.createitem;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MenuItems extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private int pos;
    private TextView txt1,txt2;

    private ListView lvl;

    ArrayList<HashMap<String, String>> itmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
         pos = b.getInt("position");
       // Log.e(TAG,"position: "+pos);
        itmList = new ArrayList<>();

        lvl = (ListView) findViewById(R.id.list);

        txt1=(TextView) findViewById(R.id.name1);
        txt2=(TextView) findViewById(R.id.address1);


        new Get().execute();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(MenuItems.this,createitem.class);
               Bundle b = new Bundle();
               b.putInt("posi",pos);
               intent.putExtras(b);
               startActivity(intent);

            }
        });


    }


    private class Get extends AsyncTask<Void, Void, Void>{

        protected Void doInBackground(Void... arg0){
            Httphandler sh = new Httphandler();
            String url = "http://192.168.0.107:8000/api/"+pos+"/items/";
            Log.e(TAG,"Response from url: "+url);
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG,"Response from url: "+jsonStr);
            if(jsonStr != null){
                try{

                    JSONObject jsobj = new JSONObject(jsonStr);
                    String name = jsobj.getString("name");
                    String address = jsobj.getString("address");
                    JSONArray r = jsobj.getJSONArray("items");
                    txt1.setText("Rest:"+name);
                    txt2.setText("Add:"+address);

                    Log.e(TAG,"Response from url: "+r);

                    for(int i=0;i<r.length();i++) {

                        JSONObject resto = r.getJSONObject(i);
                        String dish = resto.getString("dish name");
                        String price = resto.getString("price");


                        HashMap<String, String> details = new HashMap<>();
                        details.put("dish name",dish);
                        details.put("price",price);

                        itmList.add(details);
                    }


                }catch(final JSONException e){
                    Log.e(TAG,"json parsing error: "+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "json parsing error: "+e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else{
                Log.e(TAG,"didn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "didn't get json from server. Check LogCat for possible error",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(Void result){
            super .onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MenuItems.this,itmList,R.layout._list,
                    new String[]{"dish name","price"},new int[]{R.id.dish,R.id.price});
            lvl.setAdapter(adapter);


        }

    }


}
