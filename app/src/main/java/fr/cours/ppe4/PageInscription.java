package fr.cours.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;



public class PageInscription extends AppCompatActivity {


    private TextView demande_inscription_service, demande_inscription_Matricule, demande_inscription_password;
    private Button button_action_inscription;

    private CookieManager CookieManagercookieManager;
    private CookieHandler cookieManager;
    private StringRequest StringRequestsr;
    private RequestQueue requestQueue;

    private String _service, _matricule, _password, _type;
    private String url = "http://192.168.56.1/zonestockage/demandeInscription.php";



    String[] items =  {"sociéte prive","sociéte public","freelance","particulier"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    private Button button_retour;
    private Intent retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);


        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        this.button_retour=(Button)findViewById(R.id.button_retour);

        button_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retour = new Intent(PageInscription.this, PageAccueilConnexion.class);
                startActivity(retour);
            }
        });

        CookieManagercookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        requestQueue = Volley.newRequestQueue(PageInscription.this);



        this.button_action_inscription=(Button)findViewById(R.id.button_action_inscription);

        button_action_inscription.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                demandeInscription(view);
            }
        });

    }

    protected void demandeInscription(android.view.View view) {

            this.demande_inscription_service = (TextView) findViewById(R.id.demande_inscription_service);
            this.demande_inscription_Matricule = (TextView) findViewById(R.id.demande_inscription_matricule);
            this.demande_inscription_password=(TextView)findViewById(R.id.demande_inscription_password);

            _service=demande_inscription_service.getText().toString();
            _matricule = demande_inscription_Matricule.getText().toString();
            _password = demande_inscription_password.getText().toString();
            _type= autoCompleteTxt.getText().toString();


            StringRequestsr = new StringRequest(Request.Method.POST, url,

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            String reponse = response;

                            if (reponse.equals("InscriptionOk")) {
                                Intent myIntent = new Intent(PageInscription.this, login.class);
                                startActivity(myIntent);
                            } else if (reponse.equals("Erreur Insertion !")) {
                                Intent myIntent = new Intent(PageInscription.this, PageAccueilConnexion.class);
                                startActivity(myIntent);
                            }
                        /*JSONObject connected = null;
                        try {
                            connected = new JSONObject(""+response+"");
                            Boolean state = connected.getBoolean("demandeInscription");
                            if (state) {
                                Intent myIntent = new Intent(PageInsertion.this, MenuUtilisateur.class);
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
                            Log.e("inscription", err);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("service", _service);
                    params.put("matricule", _matricule);
                    params.put("password", _password);
                    params.put("type", _type);
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