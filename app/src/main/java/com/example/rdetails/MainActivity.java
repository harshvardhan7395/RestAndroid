package com.example.rdetails;

import com.example.rdetails.MenuItems;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.MenuItem;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private int[] arr=new int[50];

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> restroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restroList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,createrest.class
                ));

            }
        });


        new GetDetails().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = arr[position];
                Intent intent = new Intent(MainActivity.this,MenuItems.class);
                Bundle b = new Bundle();
                b.putInt("position",pos);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    private class GetDetails extends AsyncTask<Void, Void, Void>{

        protected Void doInBackground(Void... arg0){
            Httphandler sh = new Httphandler();
            String url = "http://192.168.0.107:8000/api/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG,"Response from url: "+jsonStr);
            if(jsonStr != null){
                try{
                    JSONArray r = new JSONArray(jsonStr);

                    for(int i=0;i<r.length();i++) {

                        JSONObject resto = r.getJSONObject(i);
                        String name = resto.getString("name");
                        String address = resto.getString("address");
                        int Id = resto.getInt("id");
                        arr[i]=Id;

                        HashMap<String, String> details = new HashMap<>();
                        details.put("name",name);
                        details.put("address",address);
                        details.put("Id",String.valueOf(Id));

                        restroList.add(details);
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
            ListAdapter adapter = new SimpleAdapter(MainActivity.this,restroList,R.layout.list_items,
                    new String[]{"name","Id"},new int[]{R.id.name,R.id.Id});
            lv.setAdapter(adapter);


        }
    }

}

