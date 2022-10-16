package fr.cours.ppe4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;


public class PageMedicament extends AppCompatActivity {

    private Intent PageCrationMedicament;
    private Intent PageAccueilSetting;
    private Intent PageInscriptionDeconnexion;
    private Intent PageMenuUtilisateur;

    private CookieManager CookieManagercookieManager;
    private CookieHandler cookieManager;
    private StringRequest StringRequestsr;
    private RequestQueue requestQueue;

    private String _matricule, _libelle, _prix, _quantite;
    private String url = "http://192.168.56.1/zonestockage/demandeMedicament.php";


    private Button button_action_ajouter_medicament;
    private BottomNavigationView bottomNavigationView;


    private TextView matricule, demande_medicament_libelle, demande_medicament_prix;

    SessionManager sessionManager;

    String[] items = {"1", "2", "3", "4", "5"};
    AutoCompleteTextView demande_medicament_quantite;
    ArrayAdapter<String> adapterItems;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_medicament);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        this.matricule = findViewById(R.id.matricule);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();


        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(SessionManager.NAME);
        matricule.setText(mName);

        demande_medicament_quantite = findViewById(R.id.demande_medicament_quantite);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        demande_medicament_quantite.setAdapter(adapterItems);

        demande_medicament_quantite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageMenuUtilisateur = new Intent(PageMedicament.this, MenuUtilisateur.class);
                startActivity(PageMenuUtilisateur);
            }
        });

        findViewById(R.id.ic_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageAccueilSetting = new Intent(PageMedicament.this, PageParametre.class);
                startActivity(PageAccueilSetting);
            }
        });


        findViewById(R.id.ajouterMedicament).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageCrationMedicament = new Intent(PageMedicament.this, PageMedicament.class);
                startActivity(PageCrationMedicament);
            }
        });


        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageInscriptionDeconnexion = new Intent(PageMedicament.this, PageAccueilConnexion.class);
                startActivity(PageInscriptionDeconnexion);
                sessionManager.logout();
            }
        });

        CookieManagercookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        requestQueue = Volley.newRequestQueue(PageMedicament.this);


        this.button_action_ajouter_medicament = (Button) findViewById(R.id.button_action_ajouter_mediacement);

        button_action_ajouter_medicament.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                demandeMedicament(view);
            }
        });

    }

    protected void demandeMedicament(android.view.View view) {


        this.demande_medicament_libelle = (TextView) findViewById(R.id.demande_medicament_libelle);
        this.demande_medicament_prix = (TextView) findViewById(R.id.demande_medicament_prix);

        _matricule = matricule.getText().toString();
        _libelle = demande_medicament_libelle.getText().toString();
        _prix = demande_medicament_prix.getText().toString();
        _quantite = demande_medicament_quantite.getText().toString();


        StringRequestsr = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        String reponse = response;

                        if (reponse.equals("MedicamentOk")) {
                            Intent myIntent = new Intent(PageMedicament.this, PageMedicament.class);
                            startActivity(myIntent);
                        } else if (reponse.equals("Erreur Insertion Medicament !")) {
                            Intent myIntent = new Intent(PageMedicament.this, PageMedicament.class);
                            startActivity(myIntent);
                        }
                        /*JSONObject connected = null;
                        try {
                            connected = new JSONObject(""+response+"");
                            Boolean state = connected.getBoolean("demandeMedicament");
                            if (state) {
                                Intent myIntent = new Intent(PageMedicament.this, PageMedicament.class);
                                startActivity(myIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String err = e.getMessage();
                            Log.e("medicamentTest", "JsonException" + err);
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        Log.e("medicament", err);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("matricule", _matricule);
                params.put("libelle", _libelle);
                params.put("prix", _prix);
                params.put("quantite", _quantite);
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