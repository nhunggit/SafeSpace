package bkav.android.safespace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.zip.Inflater;

public class OpenFileOrMessage extends AppCompatActivity implements View.OnClickListener {
    private ImageButton file;
    private ImageButton message;
    private String ENCRYPT= "encrypt";
    private String ACTION_SAFE= "com.android.actionSafe";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_message);
        file= findViewById(R.id.file);
        message= findViewById(R.id.message);
        file.setOnClickListener(this);
        message.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.file){
            //start activity duyet file
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra(ENCRYPT, 1);
            intent.setClassName("com.bkav.android.bexplorer", "com.bkav.android.bexplorer.main.BkavExplorerHomeActivity");
        }else if(v.getId()==R.id.message){
            //start activity btalk
            Intent intent = new Intent();
            intent.putExtra(ENCRYPT,1);
            intent.setClassName("bkav.android.btalk", "bkav.android.btalk.activities.BtalkMessageActivity");
            startActivity(intent);
        }
    }
}
