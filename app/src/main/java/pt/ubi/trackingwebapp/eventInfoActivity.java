package pt.ubi.trackingwebapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class eventInfoActivity extends AppCompatActivity {
    private TextView TV_Street,TV_City,TV_Country,TV_description,TV_owner,TV_date;
    private String URL_GetEventInfo = "/trackingapi/api/Events/readEventWithId.php";
    private String eventID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        URL_GetEventInfo = getString(R.string.ip) +URL_GetEventInfo;
        TV_Street = findViewById(R.id.street);
        TV_Country = findViewById(R.id.country);
        TV_City = findViewById(R.id.city);
        TV_description = findViewById(R.id.description);
        TV_date = findViewById(R.id.date);
        TV_owner = findViewById(R.id.owner);
        eventID = getIntent().getStringExtra("eventID");
        if (eventID.equals(null)) {
            Toast.makeText(eventInfoActivity.this, "Can't get Event Info!!! Redirecting to Dashboard!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(eventInfoActivity.this, DashboardActivity.class));
        }
        getEventInfo();
    }

    private void getEventInfo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GetEventInfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONObject jsonArray = jsonObject.getJSONObject ("event");
                            if(success.equals("1")){


                                    String name = jsonArray.getString("name").trim();
                                    String owner = jsonArray.getString("owner").trim();
                                    String street = jsonArray.getString("street").trim();
                                    String city = jsonArray.getString("city").trim();
                                    String country = jsonArray.getString("country").trim();
                                    String description = jsonArray.getString("description").trim();
                                    String date = jsonArray.getString("eventDate").trim();
                                    Event event = new Event(owner, name,description, street, city, country, date);
                                    TV_Street.setText(event.getStreet());
                                    TV_City.setText(event.getCity());
                                    TV_Country.setText(event.getCountry());
                                    TV_date.setText(event.getDate());
                                    TV_description.setText(event.getDescription());
                                    TV_owner.setText(event.getOwner());
                                   System.out.println(event);
                                    Toast.makeText(eventInfoActivity.this, "Success Fetching Event Info. \n Event Name : "+name, Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(eventInfoActivity.this, "Failed to Fetch Event Info! Error, Password or login ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(eventInfoActivity.this, "Failed to Fetch Event Info! Error "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(eventInfoActivity.this, "Failed to Fetch Event Info! Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", eventID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
