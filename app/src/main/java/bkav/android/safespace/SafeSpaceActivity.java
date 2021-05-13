package bkav.android.safespace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

public class SafeSpaceActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton see;
    private ImageButton add;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_see_and_add);
        see= (ImageButton)findViewById(R.id.open_folder);
        add= (ImageButton)findViewById(R.id.add_folder);
        see.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.open_folder){
            //startActivity for open file or message
            Intent intent = new Intent(this,OpenFileOrMessage.class);
            startActivity(intent);

        }else if(v.getId()==R.id.add_folder){
            //startActivity for add file or message
            Intent intent= new Intent(this, AddFileOrMassage.class);
            startActivity(intent);
        }
    }
}
