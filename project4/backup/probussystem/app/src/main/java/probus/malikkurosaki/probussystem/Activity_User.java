package probus.malikkurosaki.probussystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.particle.ParticleView;

public class Activity_User extends AppCompatActivity {

    @BindView(R.id.particleView)
    ParticleView particleView;
    @BindView(R.id.loginName)
    EditText loginName;
    @BindView(R.id.loginPass)
    EditText loginPass;
    @BindView(R.id.loginHotelCode)
    EditText loginHotelCode;
    @BindView(R.id.tombolLogin)
    Button tombolLogin;
    @BindView(R.id.tombolExploreDemo)
    Button tombolExploreDemo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__user);
        ButterKnife.bind(this);


        tombolExploreDemo.setOnClickListener(v->{
            startActivity(new Intent(Activity_User.this,Activity_Probus.class));
            finish();
        });

        tombolLogin.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(),"Belum pak , pilih yang explore dulu",Toast.LENGTH_LONG).show();
        });



    }


}
