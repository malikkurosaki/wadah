package probus.malikkurosaki.mygalery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private ImageView myImage;
    private Button myButton;
    private boolean show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImage = findViewById(R.id.myImage);
        myButton = findViewById(R.id.myButton);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show){
                    myImage.setVisibility(View.GONE);
                    show =false;
                }else{
                    myImage.setVisibility(View.VISIBLE);
                    show = true;
                }
            }
        });
    }
}
