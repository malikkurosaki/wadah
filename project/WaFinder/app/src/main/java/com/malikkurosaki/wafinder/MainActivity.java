package com.malikkurosaki.wafinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button generate;
    private Button toInject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        generate.setOnClickListener(this);
        toInject.setOnClickListener(this);


    }

    public void init(){
        generate = findViewById(R.id.generate);
        toInject = findViewById(R.id.toInject);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.generate:
                startActivity(new Intent(this,Main2Activity.class));
                break;
            case R.id.toInject:
                startActivity(new Intent(this,Main3Activity.class));
                break;
        }
    }
}