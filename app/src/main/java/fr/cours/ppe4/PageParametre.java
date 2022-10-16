package fr.cours.ppe4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PageParametre extends AppCompatActivity {

    private Intent PageInscriptionDeconnexion;
    private Intent PageMenuUtilisateur;
    private Intent PageCrationMedicament;

    private TextView matricule;
    SessionManager sessionManager;

    private StringRequest StringRequestsr;
    private RequestQueue requestQueue;

    private BottomNavigationView bottomNavigationView;
    private String url = "http://192.168.56.1/zonestockage/affichageParametre.php";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);


        this.matricule = findViewById(R.id.matricule);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        requestQueue = Volley.newRequestQueue(PageParametre.this);

        HashMap<String , String > user = sessionManager.getUserDetail();
        String mName = user.get(SessionManager.NAME);
        matricule.setText(mName);


        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageMenuUtilisateur = new Intent(PageParametre.this, MenuUtilisateur.class);
                startActivity(PageMenuUtilisateur);
            }
        });

        findViewById(R.id.ajouterMedicament).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageCrationMedicament = new Intent(PageParametre.this, PageMedicament.class);
                startActivity(PageCrationMedicament);
            }
        });

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageInscriptionDeconnexion = new Intent(PageParametre.this, PageAccueilConnexion.class);
                startActivity(PageInscriptionDeconnexion);
                sessionManager.logout();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        consultationParametre();

    }

    protected void consultationParametre() {



        StringRequestsr = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    TextView service,password,type;
                    String _service,_password,_type;


                    @Override
                    public void onResponse(String response) {
                        JSONArray collectionParametre = null;

                        try {

                            collectionParametre = new JSONArray(response);

                            for(int y =0 ; y <= collectionParametre.length()-1; y++) {


                                JSONObject jsonObject = collectionParametre.getJSONObject(y);

                                _service=jsonObject.opt("service").toString();
                                _password=jsonObject.opt("password").toString();
                                _type=jsonObject.opt("type").toString();

                                this.service=(TextView)findViewById(R.id.service);
                                this.password=(TextView)findViewById(R.id.password);
                                this.type=(TextView)findViewById(R.id.type);

                                service.setText(_service);
                                password.setText(_password);
                                type.setText(_type);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            String err = e.getMessage();
                            Log.e("MainActivity", "JsonException" + err);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("matricule", matricule.toString());
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
