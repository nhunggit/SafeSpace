package bkav.android.safespace;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class fileAndMessage {
    private ImageButton file;
    private ImageButton message;
    public void initView(View view, AppCompatActivity appCompatActivity){
       // LinearLayout linearLayout= (LinearLayout) LinearLayout.inflate(R.layout.file_message);
        file= (ImageButton)view.findViewById(R.id.file);
        message= (ImageButton)view.findViewById(R.id.message);
        file.setOnClickListener((View.OnClickListener) appCompatActivity);
        message.setOnClickListener((View.OnClickListener) appCompatActivity);
    }
}
