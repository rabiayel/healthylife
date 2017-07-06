package com.example.rabiayel.healthylife_3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SetMudahaleEdilen extends AppCompatActivity {

    private TextView textView_htc;
    private TextView textView_dtc;
    private TextView textView_tarih;
    private EditText editText_aciklama;
    private String htc, dtc;
    private int htcid, dtcid;
    private Button button_kaydet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mudahale_edilen);

        textView_htc= (TextView)findViewById(R.id.textView_htc);
        textView_dtc= (TextView)findViewById(R.id.textView_dtc);
        textView_tarih= (TextView)findViewById(R.id.textView_tarih);
        editText_aciklama = (EditText) findViewById(R.id.editText_aciklama);
        button_kaydet = (Button) findViewById(R.id.button_kaydet);

        Intent intent = this.getIntent();
        htc = intent.getStringExtra("htc");
        dtc = intent.getStringExtra("dtc");
        htcid= intent.getIntExtra("htcid", 0);
        dtcid= intent.getIntExtra("dtcid", 0);

        textView_htc.setText(htc);
        textView_dtc.setText(dtc);


        Date simdikiZaman = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        textView_tarih.setText(df.format(simdikiZaman));


        button_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConnectService().execute();

            }
        });



    }

    private void setAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetMudahaleEdilen.this);
        alertDialogBuilder
                //.setTitle("Uyarı")
                .setMessage("Kişiye yaptığınız mudahale kaydedilmiştir.")
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
    private class ConnectService extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog = new ProgressDialog(SetMudahaleEdilen.this);


        String text_aciklama=   editText_aciklama.getText().toString();

        String RESPONSE;

        boolean valid=false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Kontrol Ediliyor...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            String NAMESPACE ="http://tempuri.org/" ;
            String URL ="http://192.168.43.54/WebServis/WebServis.asmx";
            String METHOD_NAME ="setAcilMudahale";
            String SOAP_ACTION = "http://tempuri.org/setAcilMudahale";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("inputpersonelid", dtcid);
            request.addProperty("inputkisid", htcid);
            request.addProperty("inputnot", text_aciklama);


            HttpGetJSON httpGetJSON=new HttpGetJSON(URL,SOAP_ACTION);
            RESPONSE = httpGetJSON.getJsonString(request);
            valid=true;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if(valid){

                setAlertDialog();
                Intent intent = new Intent(SetMudahaleEdilen.this,MainActivity.class);
                startActivity(intent);
                SetMudahaleEdilen.this.finish();
            }
            else{
                setAlertDialogYanlis();
            }

        }
    }



    private void setAlertDialogYanlis(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetMudahaleEdilen.this);
        alertDialogBuilder
                //.setTitle("Uyarı")
                .setMessage("Açıklama kısmını lütfen doldurunuz..!")
                .setPositiveButton("Tamam",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
}
