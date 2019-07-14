package pt.ubi.trackingwebapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    private EditText name, email, password, c_password, age, phoneNumber, username, nationality;
    private Button btn_regist;
    private  String URL_REGIST = "/trackingapi/api/userInfo/createUserInfo.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        URL_REGIST =getString(R.string.ip) +URL_REGIST;
        name = findViewById(R.id.name);
        username = findViewById(R.id.Username);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        nationality = findViewById(R.id.nationality);
        password =  findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_regist = findViewById(R.id.btn_regist);

        btn_regist.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regist();
            }
        });
    }

    private void Regist() {
        btn_regist.setVisibility(View.GONE);
        final String name=this.name.getText().toString().trim();
        final String email=this.email.getText().toString().trim();
        final String password=this.password.getText().toString().trim();
        final String age=this.age.getText().toString().trim();
        final String username=this.username.getText().toString().trim();
        final String phoneNumber=this.phoneNumber.getText().toString().trim();
        final String nationality=this.nationality.getText().toString().trim();

      StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT);
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Failed " + e.toString(), Toast.LENGTH_SHORT);
                            btn_regist.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Register Failed " + error.toString(), Toast.LENGTH_SHORT);
                        btn_regist.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("name", name);
                params.put("age", age);
                params.put("email", email);
                params.put("phoneNumber", phoneNumber);
                params.put("nationality", nationality);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
