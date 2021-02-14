package com.id05.asteriskcallmedisa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements ConnectionCallback, RecordAdapter.OnContactClickListener {

    EditText mySeachText;
    public static int SERVERPORT;
    public static String SERVER_IP;
    public static String amiuser;
    public static String amisecret;
    public static String astercontext;
    public static String myphonenumber;
    private static MyTelnetClient mtc;
    public SharedPreferences sPref;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    public RecordAdapter adapter;
    public ArrayList<Contact> contacts = new ArrayList<>();
    public ArrayList<Contact> bufcontacts = new ArrayList<>();
    RecyclerView recyclerView;
    EditText inputNumber;
    Button but0;
    Button but1;
    Button but2;
    Button but3;
    Button but4;
    Button but5;
    Button but6;
    Button but7;
    Button but8;
    Button but9;
    ImageButton butDel;
    ImageButton butCall;
    private SlidingUpPanelLayout slPanel;
    Context context;
    Animation animationRotateRight = null;
    Animation animationRotateLeft = null;
    Animation animationWait = null;
    Drawable dial, backspace, wait;
    Boolean callingState = false;
    AudioManager audioManager;
    AmiState amistate = new AmiState();

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(true);
        inputNumber = findViewById(R.id.inputNumber);
        but0 = findViewById(R.id.but0);
        but0.setOnClickListener(digitClick);
        but1 = findViewById(R.id.but1);
        but1.setOnClickListener(digitClick);
        but2 = findViewById(R.id.but2);
        but2.setOnClickListener(digitClick);
        but3 = findViewById(R.id.but3);
        but3.setOnClickListener(digitClick);
        but4 = findViewById(R.id.but4);
        but4.setOnClickListener(digitClick);
        but5 = findViewById(R.id.but5);
        but5.setOnClickListener(digitClick);
        but6 = findViewById(R.id.but6);
        but6.setOnClickListener(digitClick);
        but7 = findViewById(R.id.but7);
        but7.setOnClickListener(digitClick);
        but8 = findViewById(R.id.but8);
        but8.setOnClickListener(digitClick);
        but9 = findViewById(R.id.but9);
        but9.setOnClickListener(digitClick);
        butDel = findViewById(R.id.butDel);
        butDel.setOnClickListener(digitClick);

        butCall = findViewById(R.id.butCall);
        butCall.setOnClickListener(digitClick);

        slPanel = findViewById(R.id.sliding_layout);
        slPanel.setShadowHeight(0);
        slPanel.addPanelSlideListener(panelSlideListener);
        dial = getResources().getDrawable(R.drawable.ic_baseline_dialpad_24);
        backspace = getResources().getDrawable(R.drawable.ic_baseline_backspace_24);
        wait = getResources().getDrawable(R.drawable.ic_baseline_wait_24);

        loadCONFIG();

        mySeachText = findViewById(R.id.editText);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED)
        {
            readContacts(this);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission
                                    .READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        mySeachText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mySeachText.getText().length()>=1) {
                    bufcontacts.clear();
                    for (Contact contact : contacts) {
                        if(contact.getName()!= null && !contact.getName().isEmpty()&& contact.getName().contains(mySeachText.getText())){
                            bufcontacts.add(contact);
                        }
                        onSetContacnts(bufcontacts);
                    }

                }else{
                    onSetContacnts(contacts);
                }
            }
        });

        mySeachText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        inputNumber.setKeyListener(null);

        animationRotateRight = AnimationUtils.loadAnimation(this, R.anim.rotateright);
        animationRotateRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                butDel.setImageDrawable(backspace);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationRotateLeft = AnimationUtils.loadAnimation(this, R.anim.rotateleft);
        animationRotateLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                butDel.setImageDrawable(dial);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationWait = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animationWait.setRepeatCount(Animation.INFINITE);
        animationWait.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                butDel.startAnimation(animationWait);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    View.OnClickListener digitClick = new View.OnClickListener() {
        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @Override
        public void onClick(View v) {
            String buf = inputNumber.getText().toString();
            audioManager.playSoundEffect(SoundEffectConstants.CLICK,1.0f);
            switch(v.getId()) {

                case R.id.but0: inputNumber.setText(buf+"0");
                    break;
                case R.id.but1: inputNumber.setText(buf+"1");
                    break;
                case R.id.but2: inputNumber.setText(buf+"2");
                    break;
                case R.id.but3: inputNumber.setText(buf+"3");
                    break;
                case R.id.but4: inputNumber.setText(buf+"4");
                    break;
                case R.id.but5: inputNumber.setText(buf+"5");
                    break;
                case R.id.but6: inputNumber.setText(buf+"6");
                    break;
                case R.id.but7: inputNumber.setText(buf+"7");
                    break;
                case R.id.but8: inputNumber.setText(buf+"8");
                    break;
                case R.id.but9: inputNumber.setText(buf+"9");
                    break;
                case R.id.butDel:
                    v.playSoundEffect(SoundEffectConstants.CLICK);
                    if(slPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                        slPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                    if(slPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        if (buf.length() != 0) {
                            inputNumber.setText(buf.substring(0, buf.length() - 1));
                        }else {
                            slPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                    }
                    break;
                case R.id.butCall:{
                    calling(inputNumber.getText().toString());
                }
                    break;
            }
        }
    };

    @SuppressLint("SetTextI18n")
    public void calling(String number){
        callingState = true;
        slPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        butDel.setImageDrawable(wait);
        butDel.startAnimation(animationWait);
        inputNumber.setText("WAIT");
        amistate.action = "open";
        amistate.instruction = number;
        doSomethingAsyncOperaion(amistate);
    }


    private final SlidingUpPanelLayout.PanelSlideListener panelSlideListener =
            new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View view, float v) {

                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    if(!callingState){
                        if(slPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            butDel.startAnimation(animationRotateRight);
                        }
                        if(slPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                            inputNumber.setText("");
                            butDel.startAnimation(animationRotateLeft);
                            slPanel.setPanelHeight((int) (40 * context.getResources().getDisplayMetrics().density));
                        }
                    }else{
                        if(slPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            slPanel.setEnabled(false);
                        }
                    }
                }
            };

    @Override
    public void onBackPressed() {
        if(slPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_dialpad_24);
            butDel.setImageDrawable(myDrawable);
        }else {
            if(callingState){
                inputNumber.setText("");
                butDel.setImageDrawable(dial);
                butDel.startAnimation(animationRotateLeft);
                callingState = false;
                slPanel.setEnabled(true);
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            ActionMenuItemView image = findViewById(R.id.settings);
            image.startAnimation(animationRotateLeft);
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            slPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            inputNumber.setText("");
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSetContacnts(ArrayList<Contact> contactslist){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecordAdapter(contactslist,this);
        adapter.setOnContactClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if ((grantResults.length > 0) &&
                    (grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED)) {
                readContacts(this);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void doSomethingAsyncOperaion(final AmiState amistate) {
        new AbstractAsyncWorker<Boolean>(this, amistate) {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected AmiState doAction() throws Exception {
                if(amistate.action.equals("open")){
                    mtc = new MyTelnetClient(SERVER_IP,SERVERPORT);
                    amistate.setResultOperation(mtc.isConnected());
                }
                if(amistate.action.equals("login")){
                    String com1 = "Action: Login\n"+
                            "Events: off\n"+
                            "Username: "+amiuser+"\n"+
                            "Secret: "+amisecret+"\n";
                    String buf = mtc.getResponse(com1);
                    if(buf.contains("Response: SuccessMessage: Authentication accepted")){
                        amistate.setResultOperation(true);
                    }else{
                        amistate.setResultOperation(false);
                    }
                    amistate.setDescription(buf);
                }
                if(amistate.action.equals("call")){
                    String comenter = "Action: Originate\n" +
                            "Channel: Local/"+myphonenumber+"@"+astercontext+"\n" +
                            "Exten: "+amistate.instruction+"\n" +
                            "Context: "+astercontext+"\n" +
                            "Priority: 1\n" +
                            "Async: true\n" +
                            "CallerID: "+myphonenumber+"\n" +
                            "ActionID: 123\n";
                    Boolean buf = mtc.sendCommand(comenter);
                    amistate.setResultOperation(buf);
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
            amistate.setAction("call");
            doSomethingAsyncOperaion(amistate);
        }
        if(buf.equals("call")){
            amistate.setAction("exit");
            doSomethingAsyncOperaion(amistate);
        }
        if(buf.equals("exit")){
            inputNumber.setText("");
            butDel.setImageDrawable(dial);
            butDel.startAnimation(animationRotateLeft);
            callingState = false;
            slPanel.setEnabled(true);
        }
    }

    @Override
    public void onFailure(AmiState amistate) {
        inputNumber.setText(amistate.getAction()+" error");
        butDel.setImageDrawable(dial);
        callingState = true;
    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onContactClick(int position) {
        calling(contacts.get(position).getPhone());
    }

    private void readContacts(Context context)
    {
        Contact contact;
        @SuppressLint("Recycle") Cursor cursor=context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                contact = new Contact();
                String id = cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts._ID));
                contact.setId(id);

                String name = cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts
                                        .DISPLAY_NAME));
                contact.setName(name);

                String has_phone = cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts
                                        .HAS_PHONE_NUMBER));
                if (Integer.parseInt(has_phone) > 0) {
                        Cursor cursor2;
                        cursor2 = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds
                                    .Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds
                                    .Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while(cursor2.moveToNext()) {
                        String phone = cursor2.getString(
                                cursor2.getColumnIndex(
                                        ContactsContract.
                                                CommonDataKinds.
                                                Phone.NUMBER));
                        contact.setPhone(phone);
                    }
                    cursor2.close();
                }
                contacts.add(contact);
            }
        }
        //if(contacts.size()>2) {
          // Collections.sort(contacts, new NameSorter());
        //}
        onSetContacnts(contacts);

    }

    public static class NameSorter implements Comparator<Contact> {
        @Override
        public int compare(Contact contact1, Contact contact2) {
            return contact1.getName().compareTo(contact2.getName());
        }
    }

    public void loadCONFIG() {
        sPref = getSharedPreferences("config",MODE_PRIVATE);
        SERVER_IP = sPref.getString("IP", "");
        SERVERPORT = sPref.getInt("PORT", 5038);
        amiuser = sPref.getString("AMIUSER", "");
        amisecret = sPref.getString("AMISECRET", "");
        astercontext = sPref.getString("CONTEXT", "from-internal");
        myphonenumber = sPref.getString("MYPHONE", "");
    }

}