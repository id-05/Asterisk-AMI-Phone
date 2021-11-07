package com.id05.asteriskcallmedisa;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.id05.asteriskcallmedisa.data.AmiState;
import com.id05.asteriskcallmedisa.util.AbstractAsyncWorker;
import com.id05.asteriskcallmedisa.util.ConnectionCallback;
import com.id05.asteriskcallmedisa.util.MyTelnetClient;
import static com.id05.asteriskcallmedisa.MainActivity.*;

public class SettingsActivity extends AppCompatActivity implements ConnectionCallback {

    EditText ipaddrEdit;
    EditText portEdit;
    EditText amiuserEdit;
    EditText amisecretEdit;
    EditText asteriskcontextEdit;
    EditText myphonenumberEdit;
    Button testBut;
    Button saveBut;
    Button cancelBut;
    SharedPreferences sPref;
    LinearLayout settinglayout;
    MyTelnetClient mtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ipaddrEdit = findViewById(R.id.ipaddress);
        portEdit = findViewById(R.id.asterport);
        amiuserEdit = findViewById(R.id.amiusername);
        amisecretEdit = findViewById(R.id.amiusersecret);
        asteriskcontextEdit = findViewById(R.id.astercontext);
        myphonenumberEdit = findViewById(R.id.myphonemuber);
        testBut = findViewById(R.id.testcon);
        saveBut = findViewById(R.id.savebutton);
        cancelBut = findViewById(R.id.cancelbutton);
        saveBut.setOnClickListener(saveClick);
        cancelBut.setOnClickListener(cancelClick);
        testBut.setOnClickListener(testConnection);
        settinglayout = findViewById(R.id.settinglayout);
    }

    final View.OnClickListener testConnection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if((!ipaddrEdit.getText().toString().equals("")) & (!portEdit.getText().toString().equals(""))
                    & (!amiuserEdit.getText().toString().equals("")) & (!amisecretEdit.getText().toString().equals(""))) {
                AmiState amistate = new AmiState();
                amistate.action="open";
                doSomethingAsyncOperaion(amistate);
            }else{
                if(ipaddrEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty IP", Toast.LENGTH_SHORT); toast.show();
                }
                if(portEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty PORT", Toast.LENGTH_SHORT); toast.show();
                }
                if(amiuserEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty AMI username", Toast.LENGTH_SHORT); toast.show();
                }
                if(amisecretEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty AMI secret", Toast.LENGTH_SHORT); toast.show();
                }
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    public void doSomethingAsyncOperaion(final AmiState amistate) {
        new AbstractAsyncWorker(this, amistate) {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected AmiState doAction() throws Exception {
                if(amistate.action.equals("open")){
                    mtc = new MyTelnetClient(ipaddrEdit.getText().toString(),Integer.parseInt(portEdit.getText().toString()));
                    amistate.setResultOperation(mtc.isConnected());
                }
                if(amistate.action.equals("login")){
                    String com1 = "Action: Login\n"+
                            "Events: off\n"+
                            "Username: "+amiuserEdit.getText().toString()+"\n"+
                            "Secret: "+amisecretEdit.getText().toString()+"\n";
                    String buf = mtc.getResponse(com1);
                    amistate.setResultOperation(true);
                    amistate.setResultOperation(buf.contains("Response: SuccessMessage: Authentication accepted"));
                    amistate.setDescription(buf);
                }
                if(amistate.action.equals("exit")){
                    String com1 = "Action: Logoff\n";
                    mtc.sendCommand(com1);
                    amistate.setResultOperation(true);
                    amistate.setDescription("");
                }
                return amistate;
            }
        }.execute();
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onSuccess(AmiState amistate) {
        String buf = amistate.getAction();
        if(buf.equals("open")){
            amistate.setAction("login");
            doSomethingAsyncOperaion(amistate);
        }
        if(buf.equals("login")){
            amistate.setAction("exit");
            doSomethingAsyncOperaion(amistate);
        }
        if(buf.equals("exit")){
            Snackbar.make(settinglayout,
                    R.string.SUCCESS,
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(AmiState amistate) {
        Snackbar.make(settinglayout,
                R.string.FAILURE,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onEnd() {

    }

    View.OnClickListener saveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if((!ipaddrEdit.getText().toString().equals("")) & (!portEdit.getText().toString().equals(""))
                    & (!amiuserEdit.getText().toString().equals("")) & (!amisecretEdit.getText().toString().equals(""))
                        & (!asteriskcontextEdit.getText().toString().equals("")) & (!myphonenumberEdit.getText().toString().equals(""))) {
                saveCONFIG();
                finish();
            }else{
                if(ipaddrEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty IP", Toast.LENGTH_SHORT); toast.show();
                }
                if(portEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty PORT", Toast.LENGTH_SHORT); toast.show();
                }
                if(amiuserEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty AMI username", Toast.LENGTH_SHORT); toast.show();
                }
                if(amisecretEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty AMI secret", Toast.LENGTH_SHORT); toast.show();
                }
                if(asteriskcontextEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty Asterisk context", Toast.LENGTH_SHORT); toast.show();
                }
                if(myphonenumberEdit.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SettingsActivity.this, "Empty Phone Number", Toast.LENGTH_SHORT); toast.show();
                }
            }
        }
    };

    View.OnClickListener cancelClick = v -> finish();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        ipaddrEdit.setText(SERVER_IP);
        portEdit.setText(Integer.toString(SERVER_PORT));
        amiuserEdit.setText(amiuser);
        amisecretEdit.setText(amisecret);
        asteriskcontextEdit.setText(astercontext);
        myphonenumberEdit.setText(myphonenumber);
    }

    public void saveCONFIG() {
        sPref = getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("IP", ipaddrEdit.getText().toString());
        SERVER_IP = ipaddrEdit.getText().toString();
        editor.putInt("PORT", Integer.parseInt(portEdit.getText().toString()));
        SERVER_PORT = Integer.parseInt(portEdit.getText().toString());
        editor.putString("AMIUSER", amiuserEdit.getText().toString());
        amiuser = amiuserEdit.getText().toString();
        editor.putString("AMISECRET", amisecretEdit.getText().toString());
        amisecret = amisecretEdit.getText().toString();
        editor.putString("CONTEXT", asteriskcontextEdit.getText().toString());
        astercontext = asteriskcontextEdit.getText().toString();
        editor.putString("MYPHONE", myphonenumberEdit.getText().toString());
        myphonenumber = myphonenumberEdit.getText().toString();
        editor.apply();
    }
}