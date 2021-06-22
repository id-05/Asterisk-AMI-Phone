package com.id05.asteriskcallmedisa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.id05.asteriskcallmedisa.data.AmiState;
import com.id05.asteriskcallmedisa.data.Call;
import com.id05.asteriskcallmedisa.util.AbstractAsyncWorker;
import com.id05.asteriskcallmedisa.util.ConnectionCallback;
import com.id05.asteriskcallmedisa.util.MyTelnetClient;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;
import java.util.Collections;
import static com.id05.asteriskcallmedisa.MainActivity.*;

public class CallsFragment extends Fragment implements ConnectionCallback, CallAdapter.OnContactClickListener {

    @SuppressLint("StaticFieldLeak")
    static CallAdapter adapter;
    static ArrayList<Call> calls = new ArrayList<>();
    RecyclerView recyclerView;
    AmiState amistate = new AmiState();
    Boolean callingState = false;
    MyTelnetClient mtc;

    public CallsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_calls, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerViewCalls);
        recyclerView.setNestedScrollingEnabled(true);
        calls = getCallList();
        Collections.reverse(calls);
        if(calls.size()>0) {
            onSetCalls(calls);
        }
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        calls = getCallList();
        Collections.reverse(calls);
        if(calls.size()>0) {
            onSetCalls(calls);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        super.onResume();
        calls = getCallList();
        Collections.reverse(calls);
        if(calls.size()>0) {
            onSetCalls(calls);
        }
    }

    public  void onSetCalls(ArrayList<Call> calllist){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CallAdapter(calllist,getContext());
        adapter.setOnContactClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    public void calling(Call call){
        callAddBase(new Call(call.getName(),call.getNumber(),getFullCurrentDate()));
        Collections.reverse(calls);
        calls.add(call);
        Collections.reverse(calls);
        CallsFragment.adapter.notifyDataSetChanged();
        String number = call.getNumber();
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
                    mtc = new MyTelnetClient(SERVER_IP,SERVER_PORT);
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
    public void onContactClick(int position) {
        calling(calls.get(position));
    }

    @Override
    public void onDeleteAll(){
        deleteAllCalls();
        calls.clear();
        adapter.notifyDataSetChanged();
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

    @SuppressLint("SetTextI18n")
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