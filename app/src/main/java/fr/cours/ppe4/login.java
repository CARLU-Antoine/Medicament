package fr.cours.ppe4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private TextView connexion_matricule, connexion_password;
    private Button button_action_connexion,button_retour;
    private Intent retour;

    private CookieManager CookieManagercookieManager;
    private CookieHandler cookieManager;
    private StringRequest StringRequestsr;
    private RequestQueue requestQueue;

    private String _password, _matricule;
    SessionManager sessionManager;
    private String url = "http://192.168.56.1/zonestockage/login.php";


        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.connexion);


            sessionManager = new SessionManager(this);

            this.button_retour=(Button)findViewById(R.id.button_retour);

            CookieManagercookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            requestQueue = Volley.newRequestQueue(login.this);

            this.button_action_connexion=(Button)findViewById(R.id.button_action_connexion);

            button_action_connexion.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    authentification(view);
                }
            });

            button_retour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retour = new Intent(login.this, PageAccueilConnexion.class);
                    startActivity(retour);
                }
            });




        }


    protected void authentification(android.view.View view) {

        this.connexion_matricule=(TextView)findViewById(R.id.connexion_matricule);
        this.connexion_password=(TextView)findViewById(R.id.connexion_password);
        this.button_action_connexion=(Button) findViewById(R.id.button_action_connexion);


         _matricule = connexion_matricule.getText().toString();
        _password = connexion_password.getText().toString();

        sessionManager.createSession(_matricule);


        StringRequestsr = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        String reponse=response;

                        if(reponse.equals("Authentification")){
                            Intent myIntent = new Intent(login.this, MenuUtilisateur.class);
                            startActivity(myIntent);
                        }else if(reponse.equals("Erreur Insertion !")){
                            Intent myIntent = new Intent(login.this, login.class);
                            startActivity(myIntent);
                        }
                        /*JSONObject connected = null;
                        try {
                            connected = new JSONObject(""+response+"");
                            Boolean state = connected.getBoolean("authentification");
                            if (state) {
                                Intent myIntent = new Intent(login.this, MenuUtilisateur.class);
                                startActivity(myIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String err = e.getMessage();
                            Log.e("loginTest", "JsonException" + err);
                        }*/
                    }
                },
                            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String err = error.getMessage();
                Log.e("matricule", err);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("matricule", _matricule);
                params.put("password", _password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        requestQueue.add(StringRequestsr);
    }
}