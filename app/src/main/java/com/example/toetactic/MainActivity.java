package com.example.toetactic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.toetactic.R;
import com.example.toetactic.VsSmartai;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView search,summary,view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search=findViewById(R.id.Search_card);
        summary=findViewById(R.id.Summary);
        view=findViewById(R.id.Viewall_card);

        search.setOnClickListener(this);
        summary.setOnClickListener(this);
        view.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch(view.getId()){
            case R.id.Search_card: i=new Intent(this, VsPlayer.class);
                startActivity(i);
                break;
            case R.id.Summary: i=new Intent(this, VsSmartai.class);
                startActivity(i);
                break;
            case R.id.Viewall_card: i=new Intent(this, VsAi.class);
                startActivity(i);
                break;
            default:break;
        }

    }
}