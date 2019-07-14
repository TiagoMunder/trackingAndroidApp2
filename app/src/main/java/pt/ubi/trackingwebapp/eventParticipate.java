package pt.ubi.trackingwebapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class eventParticipate extends AppCompatActivity {
    private EditText edit_accessCode;
    private String URL_ParticipateEvent = "/trackingapi/api/Events/readEventWithAccessCode.php";
    private Button btn_participate, btn_cancelParticipate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_participate);
        URL_ParticipateEvent =getString(R.string.ip) +URL_ParticipateEvent;
        edit_accessCode = findViewById(R.id.accessCode);
        btn_participate = findViewById(R.id.btn_participate);
        btn_cancelParticipate = findViewById(R.id.btn_cancelParticipate);

        btn_cancelParticipate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(eventParticipate.this, DashboardActivity.class));
            }
        });

        btn_participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accessCode = edit_accessCode.getText().toString().trim();

                if(!accessCode.isEmpty()) {
                    Participate(accessCode);
                } else {
                    edit_accessCode.setError("You need to insert an Email");
                }
            }
        });

    }

    private void Participate (final String accessCode) {
        btn_participate.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ParticipateEvent,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if(success.equals("1")){
                                    String id = jsonObject.getString("event");
                                        Toast.makeText(eventParticipate.this, "Success Participating in Event. \n Your Name : "+id, Toast.LENGTH_SHORT).show();
                                        Intent eventInfoIntent = new Intent(eventParticipate.this, eventInfoActivity.class);
                                        eventInfoIntent.putExtra("eventID",id);
                                        startActivity(eventInfoIntent);
                                }
                                else {
                                    Toast.makeText(eventParticipate.this, "You entered an invalid Code!! ", Toast.LENGTH_SHORT).show();
                                    btn_participate.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                btn_participate.setVisibility(View.VISIBLE);
                                Toast.makeText(eventParticipate.this, "You entered an invalid Code!! "+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    btn_participate.setVisibility(View.VISIBLE);

                    Toast.makeText(eventParticipate.this, "Something Failed "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accessCode", accessCode);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

}
