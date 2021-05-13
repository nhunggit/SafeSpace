package bkav.android.safespace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText pass;
    private EditText passCreate;
    private EditText confirm_pass;
    private Button login;
    private Button createPass;
    private String mPass="";
    private String enterPass="";
    private String confirmPass="";
//    boolean mBound = false;
//    SafeSpaceService mService;


    public String getEnterPass() {
        return enterPass;
    }

    public void setEnterPass(String enterPass) {
        this.enterPass = enterPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public String getmPass() {
        return mPass;
    }

    public void setmPass(String mPass) {
        this.mPass = mPass;
    }

    Messenger mService= null;
    boolean mBound;


    public class ActivityHandle extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d("nhungltk", "handleMessage activity: "+msg.what);
            if(msg.what==1){
                Log.d("nhungltk", "handleMessage: "+"  ok");
            }
        }
    }

    Messenger activityMeassenger = new Messenger(new ActivityHandle());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, SafeSpaceService.class),new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("nhungltk", "onServiceConnected: ");
                mService = new Messenger(service);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("nhungltk", "onServiceDisconnected: ");
                mService= null;
                mBound=false;
            }
        }, Context.BIND_AUTO_CREATE);
        pass= (EditText)findViewById(R.id.pass_word);
        passCreate= (EditText)findViewById(R.id.enter_pass);
        confirm_pass= (EditText)findViewById(R.id.confirm_pass);
        login= (Button)findViewById(R.id.login);
        createPass= (Button)findViewById(R.id.create_password);
        createPass.setOnClickListener(this);
        if(mPass==""){
            createPass.setVisibility(View.VISIBLE);
        }
        //else{
//            login.setVisibility(View.VISIBLE);
//            pass.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.create_password) {
            Message msg = Message.obtain(null, SafeSpaceService.CHECK_HAS_PASSWORD, 0, 0,0);
            Log.d("nhungltk", "  Bound: "+mBound);
            if(!mBound){
                Log.d("nhugntlk", "onCreate: "+ "Bound null"); return;}
            try {
                //msg.replyTo=activityMeassenger;
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            passCreate.setVisibility(View.VISIBLE);
            enterPass= String.valueOf(passCreate.getText());
            if(!enterPass.equals("")){
                setEnterPass(enterPass);
                passCreate.setVisibility(View.GONE);
                confirm_pass.setVisibility(View.VISIBLE);
            }
            confirmPass= String.valueOf(confirm_pass.getText());
            if(!confirmPass.equals("")){
                setConfirmPass(confirmPass);
            }

        }
    }


}