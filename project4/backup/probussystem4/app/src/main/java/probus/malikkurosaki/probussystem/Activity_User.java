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


    private SQLiteDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__user);
        ButterKnife.bind(this);

        db = openOrCreateDatabase("PROBUS",MODE_PRIVATE,null);

        /*db.execSQL("create table if not exists user(" +
                "id integer primary key not null," +
                "nama text not null)");*/

        try(Cursor cursor = db.rawQuery("select * from user",null)) {
            while (cursor.moveToNext()){
                Log.i("cursor", "onCreate: "+cursor.getString(1));
            }
        }

        tombolExploreDemo.setOnClickListener(v->{
            startActivity(new Intent(Activity_User.this,Activity_Probus.class));
            finish();
        });

        tombolLogin.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(),"Belum pak , pilih yang explore dulu",Toast.LENGTH_LONG).show();
        });



    }


}
