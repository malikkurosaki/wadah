package dev.malikkurosaki.probussystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.paperdb.Paper;

public class LayoutAdminPanel extends Fragment {

    private Context context;
    private Activity activity;
    // firebase
    private DatabaseReference db;

    // bottom dialog
    private EditText userAdmin;
    private EditText passAdmin;
    private Button masukAdmin;
    private BottomSheetDialog loginDialog;
    private ViewPager adminContentContainer;

    private String usr,pass;
    // falisasi
    String sudahLogin;
    LayoutAdminPanel newInstance(){
        return new LayoutAdminPanel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_admin_panel,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.context = view.getContext();
        this.activity = (Activity)context;

        Paper.init(context);
        sudahLogin = Paper.book().read("sudahLogin");

        // bottom login
        loginDialog = new BottomSheetDialog(context);
        LayoutInflater inf = LayoutInflater.from(context);
        View vd = inf.inflate(R.layout.layout_login_admin,null,false);
        loginDialog.setContentView(vd);
        userAdmin = vd.findViewById(R.id.userAdmin);
        passAdmin = vd.findViewById(R.id.passAdmin);
        masukAdmin = vd.findViewById(R.id.masukAdmin);
        adminContentContainer = vd.findViewById(R.id.adminContentContainer);


        if (sudahLogin == null || sudahLogin.equals("belum")){
            loginDialog.show();
        }else {
            loginDialog.dismiss();
            MyFragmentViewPager fragmentViewPager = new MyFragmentViewPager(getChildFragmentManager());
            fragmentViewPager.addFragment(new UpdateNewsFeed());
            adminContentContainer.setAdapter(fragmentViewPager);

        }

        // difinisi firebase
        db = FirebaseDatabase.getInstance().getReference();
        db.child("validasi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Objects> minta = (HashMap<String,Objects>)dataSnapshot.getValue();
                Map<String,Objects> terima = new HashMap<>();
                for (Map.Entry<String,Objects> ent : minta.entrySet()){
                    terima.put(ent.getKey(),ent.getValue());
                }

                pass = String.valueOf(terima.get("pass"));
                usr = String.valueOf(terima.get("user"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        masukAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passNya = passAdmin.getText().toString().trim();
                String userNya = userAdmin.getText().toString().trim();

                if (TextUtils.isEmpty(userNya) || TextUtils.isEmpty(passNya)){
                    startActivity(new Intent(context,Main2Activity.class));
                    return;
                }

                if (!userNya.equals(usr) || !passNya.equals(pass)){
                    startActivity(new Intent(context,Main2Activity.class));
                    return;
                }

                Paper.book().write("sudahLogin","sudah");
                activity.recreate();

            }
        });

        /*String[] menuNya = {
                "pilih menunya",
                "edit halaman awal",
                "update content menu samping",
                "update newsfeed"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.layout_admin_pilih_menu, menuNya);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilihMenu.setAdapter(adapter);

        pilihMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position!=0){

                    switch (position){
                        case 1:
                            itemMenu = new LayoutUpdate();
                            break;
                        case 2:
                            itemMenu = new UpdateHalamanMenu();
                            break;
                        case 3:
                            itemMenu = new UpdateNewsFeed();
                    }
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.adminPanelConatainer,itemMenu);
                    transaction.commitAllowingStateLoss();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

}
