package fr.cours.ppe4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class MenuUtilisateur extends AppCompatActivity {


    private Intent PageAccueilSetting;
    private Intent PageInscriptionDeconnexion;
    private Intent PageCrationMedicament;

    private BottomNavigationView bottomNavigationView;

    private CookieManager CookieManagercookieManager;
    private CookieHandler cookieManager;
    private StringRequest StringRequestsr;
    private RequestQueue requestQueue;
    private LinearLayout zone_inflate_consultation;
    private LayoutInflater layoutInflater;

    SessionManager sessionManager;

    private int _lenghtReq;
    private String url = "http://192.168.56.1/zonestockage/affichageMedicament.php";
    //private String url = "http://172.16.252.5/zonestockage/affichageReservationUtilisateur.php";

    private TextView textView8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utilisateur);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();



        CookieManagercookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        requestQueue = Volley.newRequestQueue(MenuUtilisateur.this);

        this.zone_inflate_consultation=(LinearLayout) findViewById(R.id.zone_inflate_consultation);
        layoutInflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        findViewById(R.id.ic_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageAccueilSetting = new Intent(MenuUtilisateur.this, PageParametre.class);
                startActivity(PageAccueilSetting);
            }
        });

        findViewById(R.id.ajouterMedicament).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageCrationMedicament = new Intent(MenuUtilisateur.this, PageMedicament.class);
                startActivity(PageCrationMedicament);
            }
        });

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageInscriptionDeconnexion = new Intent(MenuUtilisateur.this, PageAccueilConnexion.class);
                startActivity(PageInscriptionDeconnexion);
                sessionManager.logout();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        consultationMedicament();

    }

    protected void consultationMedicament() {

        zone_inflate_consultation.removeAllViews();


        StringRequestsr = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    TextView id_medicament, libelle, prix, quantite, matricule;
                    String _id_medicament, _libelle, _prix, _quantite, _matricule;


                    @Override
                    public void onResponse(String response) {
                        JSONArray collectionMedicament = null;

                        try {

                            collectionMedicament = new JSONArray(response);

                            for(int y =0 ; y <= collectionMedicament.length()-1; y++) {

                                View viewInflater = layoutInflater.inflate(R.layout.consultation_inflater, null, false);
                                zone_inflate_consultation.addView(viewInflater);

                                JSONObject jsonObject = collectionMedicament.getJSONObject(y);

                                _id_medicament=jsonObject.opt("id_medicament").toString();
                                _libelle=jsonObject.opt("libelle").toString();
                                _prix=jsonObject.opt("prix").toString();
                                _quantite=jsonObject.opt("quantite").toString();
                                _matricule=jsonObject.opt("matricule").toString();

                                this.id_medicament=(TextView)viewInflater.findViewById(R.id.id);
                                this.libelle=(TextView)viewInflater.findViewById(R.id.libelleConsult);
                                this.prix=(TextView)viewInflater.findViewById(R.id.prixConsult);
                                this.quantite=(TextView)viewInflater.findViewById(R.id.quantiteConsult);
                                this.matricule=(TextView)viewInflater.findViewById(R.id.matriculeConsult);

                                id_medicament.setText(_id_medicament);
                                libelle.setText(_libelle);
                                prix.setText(_prix);
                                quantite.setText(_quantite);
                                matricule.setText(_matricule);
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
                //params.put("login", _login);
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


