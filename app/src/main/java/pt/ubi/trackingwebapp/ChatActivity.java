package pt.ubi.trackingwebapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
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

public class ChatActivity extends AppCompatActivity {
    private EditText editChatForm;
    private String URL_Messages = "/trackingapi/api/Messages/getMessages.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        URL_Messages =getString(R.string.ip) + URL_Messages;
        editChatForm = (EditText) findViewById(R.id.chatForm);
        getMessagesFromServer();

    }

    protected void getMessagesFromServer(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Messages,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("messages");

                            if(success.equals("1")){
                                ListView myListView = (ListView) findViewById(R.id.messages_view);
                                ArrayList<Message> eventsList = new ArrayList<>();
                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String owner = object.getString("owner").trim();
                                    String message = object.getString("Message").trim();

                                    Boolean sendByUs = owner.equals("tester") ? true : false;

                                    Message msg = new Message(owner, message, sendByUs);
                                    eventsList.add(msg);
                                }
                                MessageAdapter adapter = new MessageAdapter(ChatActivity.this,R.layout.adapter_view_layout,eventsList);
                                myListView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(ChatActivity.this, "Getting Messages from server Failed! Error "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ChatActivity.this, "Getting Messages from server Failed! Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
