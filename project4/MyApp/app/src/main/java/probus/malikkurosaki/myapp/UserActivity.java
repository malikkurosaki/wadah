package probus.malikkurosaki.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {


    private EditText userName;
    private EditText password;
    private Button login;

    private String usr;
    private String pas;
    private Map<String,Object> validasi;

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        reference = FirebaseDatabase.getInstance().getReference();

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr = userName.getText().toString().trim();
                pas = password.getText().toString().trim();

                reference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        validasi = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Map<String,Object> terima = (Map<String, Object>) snapshot.getValue();
                            for (Map.Entry<String,Object> entry : terima.entrySet()){
                                validasi.put(entry.getKey(),entry.getValue());
                            }
                        }

                        if (usr.equals(String.valueOf(validasi.get("name"))) && pas.equals(String.valueOf(validasi.get("pass")))){
                            Toast.makeText(getApplicationContext(),"you has login",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(UserActivity.this,MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
