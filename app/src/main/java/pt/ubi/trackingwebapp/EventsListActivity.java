package pt.ubi.trackingwebapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventsListActivity extends AppCompatActivity {
    private static final String TAG = "EventsListActivity";
    private String URL_Events = "/trackingapi/api/Events/readEvents.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        URL_Events =getString(R.string.ip) +URL_Events;
        Log.d(TAG, "onCreate: Started.");
        getEvents();
    }

    protected void getEvents(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Events,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("events");

                            if(success.equals("1")){
                                ListView myListView = (ListView) findViewById(R.id.events_list_View);
                                ArrayList<Event> eventsList = new ArrayList<>();
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String owner = object.getString("owner").trim();
                                    String description = object.getString("description").trim();
                                    Event event = new Event(owner, name,description);
                                    eventsList.add(event);
                                }
                                EventsListAdapter adapter = new EventsListAdapter(EventsListActivity.this,R.layout.adapter_view_layout,eventsList);
                                myListView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(EventsListActivity.this, "Login Failed! Error "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(EventsListActivity.this, "Login Failed! Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}

