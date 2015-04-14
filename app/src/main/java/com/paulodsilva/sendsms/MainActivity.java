package com.paulodsilva.sendsms;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ClasseMensagem[] sms;

    private TextView txtTotalSMS;
    private TextView txtSMSEnviados;
    private Button btEnviarSMS;
    private int enviados = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTotalSMS = (TextView) findViewById(R.id.txtTotalSMS);
        txtSMSEnviados = (TextView) findViewById(R.id.txtSMSEnviados);
        btEnviarSMS = (Button) findViewById(R.id.btEnviarSMS);

        sms = getMSGToSend();

        txtTotalSMS.setText(String.valueOf(sms.length));
        btEnviarSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ClasseMensagem mensagem : sms) {
                    SendSMS(mensagem);
                }

            }
        });


    }

    public void SendSMS(ClasseMensagem mensagem) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            smsManager.sendTextMessage(mensagem.getFone(), null, mensagem.getMsg(), null, null);
            txtSMSEnviados.setText(String.valueOf(enviados));
            enviados++;
        } catch (Exception e) {
            Log.e("Send_Error", e.getMessage(), e);
        }

    }

    public ClasseMensagem[] getMSGToSend() {
        String Msgs = "";
        ClasseMensagem[] mensagens = null;
        try {
            File myFile = new File(Environment.getExternalStoragePublicDirectory("SendMessages"), "mensagens.json");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            Msgs = aBuffer;
            myReader.close();

            Msgs = Msgs.replace("\t", "");
            Gson gson = new Gson();
            mensagens = gson.fromJson(Msgs, ClasseMensagem[].class);

        } catch (Exception e) {
            Log.e("Read_Error", e.getMessage(), e);
        }


        return mensagens;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
