package bkav.android.safespace;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class SafeSpaceService extends Service {

    static final int CREATE_PASSWORD=1;
    static final int LOGIN= 2;
    static final int CHECK_HAS_PASSWORD= 3;
    private String mPass;
    private String mConfimPass;
    private String mPassLogin;
   // Activity mActivity;
    private String salt= "salt_safe_space";

//    SafeSpaceService(Activity activity){
//        mActivity= activity;
//    }

    public String getmPassLogin() {
        return mPassLogin;
    }

    public void setmPassLogin(String mPassLogin) {
        this.mPassLogin = mPassLogin;
    }

    public String getmPass() {
        return mPass;
    }

    public void setmPass(String mPass) {
        this.mPass = mPass;
    }

    public String getmConfimPass() {
        return mConfimPass;
    }

    public void setmConfimPass(String mConfimPass) {
        this.mConfimPass = mConfimPass;
    }

    public  boolean CheckPassLogin(){
        boolean check= false;
        try {
            String pass= readInfoSafe();
            String passLogin= getmPassLogin();
            if(pass!=null && passLogin != null && pass.equals(passLogin)){
                check=true;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean checkPassCreate(){
        boolean check = false;
        if(mPass != null && mConfimPass != null && mPass.equals(mConfimPass)){
            try {
                saveInfoSafe(mPass, salt);
                check=true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    public boolean checkHaspass() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(readInfoSafe()!=null){
            return true;
        }else
            return false;
    }

    public static final String BOS_SERVICE = "bos";
    Class<?> mClass;
    {
        try {
            mClass = Class.forName("android.os.bos.BosManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void saveInfoSafe(String pass, String salt) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Object mBosManager =getApplicationContext().getSystemService(BOS_SERVICE);
        if(mBosManager != null && mClass != null){
            Method method=null;
            method=mClass.getMethod("saveInfoSafe",String.class, String.class);
            method.setAccessible(true);
            String[] arg= {pass,salt};
            try{
                method.invoke(mBosManager,arg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String readInfoSafe() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String pass="";
        Object mBosManager =getApplicationContext().getSystemService(BOS_SERVICE);
        if(mBosManager!= null && mClass != null){
            Method method= null;
            method= mClass.getMethod("readInfoSafe");
            method.setAccessible(true);
            pass=(String)method.invoke(mBosManager);
            Log.d("nhungltk", "readInfoSafe: "+pass);
        }
        return pass;
    }

    class IncomingHandler extends Handler{
        private Context applicationContext;

        IncomingHandler(Context context){
            Log.d("nhungltk", "IncomingHandler: "+context);
            applicationContext= context.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d("nhungltk", "handlerMessage service: "+msg.what);
            switch (msg.what){
                case CREATE_PASSWORD:
                    if(checkPassCreate()==true) {
                        Message message = Message.obtain(null, 1, 0, 0);
                        try {
                            msg.replyTo.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case LOGIN:
                    CheckPassLogin();
                    break;
                case CHECK_HAS_PASSWORD:
                    try {
                        if(checkHaspass()==true){
                            Message message = Message.obtain(null, 2, 0, 0);
                            try {
                                msg.replyTo.send(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                default:
                    super.handleMessage(msg);
            }
        }
    }

    Messenger mMessage;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("nhungltk", "onBind: ");
        mMessage = new Messenger(new IncomingHandler(this));
        return mMessage.getBinder();
    }
}
