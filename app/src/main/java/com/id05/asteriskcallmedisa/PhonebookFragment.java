package com.id05.asteriskcallmedisa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;
import java.util.Collections;

import static com.id05.asteriskcallmedisa.CallsFragment.calls;
import static com.id05.asteriskcallmedisa.MainActivity.SERVERPORT;
import static com.id05.asteriskcallmedisa.MainActivity.SERVER_IP;
import static com.id05.asteriskcallmedisa.MainActivity.amisecret;
import static com.id05.asteriskcallmedisa.MainActivity.amiuser;
import static com.id05.asteriskcallmedisa.MainActivity.animationRotateLeft;
import static com.id05.asteriskcallmedisa.MainActivity.animationWait;
import static com.id05.asteriskcallmedisa.MainActivity.astercontext;
import static com.id05.asteriskcallmedisa.MainActivity.butDel;
import static com.id05.asteriskcallmedisa.MainActivity.callAddBase;
import static com.id05.asteriskcallmedisa.MainActivity.dial;
import static com.id05.asteriskcallmedisa.MainActivity.getFullCurrentDate;
import static com.id05.asteriskcallmedisa.MainActivity.inputNumber;
import static com.id05.asteriskcallmedisa.MainActivity.myphonenumber;
import static com.id05.asteriskcallmedisa.MainActivity.slPanel;
import static com.id05.asteriskcallmedisa.MainActivity.wait;

public class PhonebookFragment extends Fragment implements ConnectionCallback, RecordAdapter.OnContactClickListener {

    EditText mySeachText;
    public RecordAdapter adapter;
    public ArrayList<Contact> contacts = new ArrayList<>();
    public ArrayList<Contact> bufcontacts = new ArrayList<>();
    static RecyclerView recyclerView;
    AmiState amistate = new AmiState();
    Boolean callingState = false;
    private static MyTelnetClient mtc;


    public PhonebookFragment() {

    }


    public static PhonebookFragment newInstance(String param1, String param2) {
        return new PhonebookFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_phonebook, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(true);
        contacts = MainActivity.contacts;
        onSetContacnts(contacts);
        mySeachText = fragmentView.findViewById(R.id.editText);

        mySeachText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

        return fragmentView;
    }

    public void onSetContacnts(ArrayList<Contact> contactslist){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecordAdapter(contactslist,getContext());
        adapter.setOnContactClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onContactClick(int position) {
        if(mySeachText.getText().length()>=1) {
            calling(bufcontacts.get(position));
        }else{
            calling(contacts.get(position));
        }
    }

    @SuppressLint("SetTextI18n")
    public void calling(Contact contact){
        callAddBase(new Call(contact.getName(),contact.getPhone(),getFullCurrentDate()));
        Collections.reverse(calls);
        calls.add(new Call(contact.getName(),contact.getPhone(),getFullCurrentDate()));
        Collections.reverse(calls);
        CallsFragment.adapter.notifyDataSetChanged();
        String number = contact.getPhone();
        String buf = number.replace(" ","");
        number = buf.replace("-","");
        callingState = true;
        slPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        butDel.setImageDrawable(wait);
        butDel.startAnimation(animationWait);
        inputNumber.setText("WAIT");
        amistate.action = "open";
        amistate.instruction = number;
        doSomethingAsyncOperaion(amistate);
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
                    amistate.setResultOperation(buf.contains("Response: SuccessMessage: Authentication accepted"));
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
                    Log.d("aster","comenter = "+comenter);
                    System.out.println("aster"+"comenter = "+comenter);
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
    public void onFailure(AmiState amiState) {
        inputNumber.setText(amistate.getAction()+" error");
        butDel.setImageDrawable(dial);
        callingState = true;
    }

    @Override
    public void onEnd() {

    }
}