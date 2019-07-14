package pt.ubi.trackingwebapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {

    private EditText eventName, description, country, city, street;
    private Session session;
    private TextView mChooseDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String owner, eventChoosenDate;
    private Button btn_create, btn_cancel;
    private  String URL_CREATE = "/trackingapi/api/Events/createEvent.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        URL_CREATE =getString(R.string.ip) +URL_CREATE;
        eventName = findViewById(R.id.eventName);
        description = findViewById(R.id.description);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        street = findViewById(R.id.street);
        btn_create = findViewById(R.id.btn_create);
        btn_cancel = findViewById(R.id.btn_cancel);
        session = new Session(CreateEvent.this);
        mChooseDate = (TextView) findViewById(R.id.eventDatePicker);
        mChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateEvent.this,
                       android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                        mDateSetListener,
                        year, month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String date =year + "/" + month + "/" +dayOfMonth;
                eventChoosenDate = date;

                mChooseDate.setText(date);
            }
        };

             // get Owner from Session
            owner = session.getUsername();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateEvent.this, DashboardActivity.class));
            }
        });

    }

    protected  void createEvent() {
        btn_create.setVisibility(View.GONE);
        final String eventName=this.eventName.getText().toString().trim();
        final String description=this.description.getText().toString().trim();
        final String country=this.country.getText().toString().trim();
        final String city=this.city.getText().toString().trim();
        final String street=this.street.getText().toString().trim();

         StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CREATE,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         try {
                             JSONObject jsonObject = new JSONObject(response);
                             String success = jsonObject.getString("success");

                             if (success.equals("1")) {
                                 Toast.makeText(CreateEvent.this, "Create Event Success", Toast.LENGTH_SHORT);
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                             Toast.makeText(CreateEvent.this, "Create Event Failed" + e.toString(), Toast.LENGTH_SHORT);
                             btn_create.setVisibility(View.VISIBLE);
                         }
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         Toast.makeText(CreateEvent.this, "Create Event Failed " + error.toString(), Toast.LENGTH_SHORT);
                         btn_create.setVisibility(View.VISIBLE);

                     }
                 })    {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> params = new HashMap<>();
                 params.put("eventName", eventName);
                 params.put("description", description);
                 params.put("eventDate", eventChoosenDate);
                 params.put("country", country);
                 params.put("city", city);
                 params.put("street", street);
                 if(owner!= null)
                     params.put("owner", owner);
                 return params;
             }
         };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
