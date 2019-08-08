package dev.malikkurosaki.probussystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapzen.speakerbox.Speakerbox;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class LayoutChatBot extends Fragment{
    private View view;
    private RecyclerView wadahChat;
    private EditText inputChat;
    private ImageButton chatSend;
    private ImageView chatIcon;
    private Context context;
    private Activity activity;
    private boolean diam = true;

    private DatabaseReference db;
    private SimpleTooltip tooltip2;

    // perkenalan
    private EditText chatDaftarUser,chatDaftarPerusahaan,chatDaftarPass;
    private Button chatDaftarSimpan;
    private Button chatLogin;

    private DroidSpeech droidSpeech;
    private Speakerbox berbicara;
    private SimpleTooltip tooltip;

    //setingan
   private String sNama;
   private BottomSheetDialog dialog;

   private String TAG = "-->";
   private String userName;
   private String perusahaan;
   private boolean sudahDaftar = false;
   private boolean namaSudahAda = false;

   private String hasilBicara;
   private String kun;

   private String aUser;
   private String aPerusahaan;
   private String aPassword;
   private String cUser;
   private String cPerusahaan;
   private String cPassword;
   private ImageView mulaiBicara;
   private Spinner hotKey;
   private FrameLayout botContainer;
   private ImageView saluranHotkey;
   private LinearLayout tulisContainer;

    private String namaUrlNya = "";

    private List<Map<String, Objects>> listData;

    // dialog cus
    private AlertDialog.Builder builderCus;
    private Dialog dialogCus;

    private static LayoutChatBot newInstance(){
        return new LayoutChatBot();
    }

    // keatas

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_chat_bot,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;

        Paper.init(context);

        // difinisi view
        inputChat = view.findViewById(R.id.chatInput);
        chatSend = view.findViewById(R.id.chatSend);
        chatIcon = view.findViewById(R.id.chatIcon);
        wadahChat = view.findViewById(R.id.chatWadah);
        mulaiBicara = view.findViewById(R.id.mulaiBicara);
        hotKey = view.findViewById(R.id.hotKey);
        botContainer = view.findViewById(R.id.botContainer);
        saluranHotkey = view.findViewById(R.id.saluranHotkey);
        tulisContainer = view.findViewById(R.id.tulisContainer);

        mulaiBicara.setVisibility(View.GONE);


        namaUrlNya = Paper.book().read("url");


        droidSpeech = new DroidSpeech(context,null);
        berbicara = new Speakerbox(activity.getApplication());
        berbicara.getTextToSpeech().setLanguage(new Locale("in_ID"));

        // firebase
        db = FirebaseDatabase.getInstance().getReference();

        LayoutPenjelasanChatBot penjelasanChatBot = new LayoutPenjelasanChatBot(context);
        //penjelasanChatBot.show();

        userName = Paper.book().read("nama")+"@"+Paper.book().read("perusahaan");
        perusahaan = Paper.book().read("perusahaan");

        if (perusahaan != null){
            kegiatanChat();
            sudahDaftar = true;
        }

        // perkenlan dialog
        dialog  = new BottomSheetDialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewPerkenalan = inflater.inflate(R.layout.layout_perkenalan_chatbot,null,false);
        dialog.setContentView(viewPerkenalan);

        chatDaftarUser = viewPerkenalan.findViewById(R.id.chatDaftarUser);
        chatDaftarPerusahaan = viewPerkenalan.findViewById(R.id.chatDaftarPerusahaan);
        chatDaftarPass = viewPerkenalan.findViewById(R.id.chatDaftarPass);
        chatDaftarSimpan = viewPerkenalan.findViewById(R.id.chatDaftarSimpan);
        chatLogin = viewPerkenalan.findViewById(R.id.chatLogin);

        // ketika icon pinguin di click

        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check sudah daftar apa belum
                if (!vKenalan()){
                    dialog.show();
                    berbicara.play("it seems like we haven't met yet, don't know it then don't love, let's know you first");
                    return;
                }
                // untuk toogle
                if (diam){
                    bolehMendengarkan();
                    diam = false;
                }else {
                    tidakBolehMendengarkan();
                    diam = true;
                }
            }
        });



        inputChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tidakBolehMendengarkan();
            }
        });

        chatLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               login();
            }
        });

        chatDaftarSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               daftar();
            }
        });

        // mendengarkan hasil bicara
        droidSpeech.setOnDroidSpeechListener(new OnDSListener() {
            @Override
            public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
                droidSpeech.setPreferredLanguage("en_US");
            }

            @Override
            public void onDroidSpeechRmsChanged(float rmsChangedValue) {
                int nilai = Math.round(rmsChangedValue);
                if (!diam){
                    if (nilai < 5 ){
                        YoYo.with(Techniques.Wobble)
                                .duration(500)
                                .playOn(chatIcon);
                        YoYo.with(Techniques.Wobble).duration(500).playOn(mulaiBicara);
                    }
                }
                if (diam){
                  tidakBolehMendengarkan();
                }


            }

            @Override
            public void onDroidSpeechLiveResult(String liveSpeechResult) {

            }

            @Override
            public void onDroidSpeechFinalResult(String finalSpeechResult) {
                inputChat.setText(finalSpeechResult.toLowerCase());
                tukangProses(finalSpeechResult);
                diam = true;
                tidakBolehMendengarkan();

            }

            @Override
            public void onDroidSpeechClosedByUser() {
                Toast.makeText(getContext(),"close",Toast.LENGTH_LONG).show();
                tidakBolehMendengarkan();
            }

            @Override
            public void onDroidSpeechError(String errorMsg) {
                tidakBolehMendengarkan();
            }
        });

        // mengirimkan data ke firebase
        chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sudahDaftar){
                    Toast.makeText(getContext(),"it's good to register first, click the mic",Toast.LENGTH_LONG).show();
                    return;
                }
                String kNama = inputChat.getText().toString().trim();
                if (TextUtils.isEmpty(kNama)){
                    String jawab = "Did you forget to type something";
                    botKirimPesan(jawab);
                    berbicara.play(jawab);
                    YoYo.with(Techniques.Wobble)
                            .duration(500)
                            .playOn(inputChat);

                    return;
                }
                tukangProses(kNama);
                hilangkanaKeyboard();


            }
        });


       // mengupdate jawabana dari server
       mengupdateJawabah();

        String sudahKenalan = Paper.book().read("sudahkenalan");
        if (sudahKenalan==null){
            perkenalanAwal();
            penjelasanChatBot.show();
            //Paper.book().write("sudahkenalan",null);
        }else {
            penjelasanChatBot.dismiss();
        }

       //tambahClient();


        final List<String> namaHot = new ArrayList<>();
        namaHot.add("pilih hot key");
        namaHot.add("How are you");
        namaHot.add("search by name ");
        namaHot.add("search by name eko");
        namaHot.add("search by name david");
        namaHot.add("search by name dav");
        namaHot.add("search by name all");

        ArrayAdapter htAdapter = new ArrayAdapter<>(context, R.layout.layout_hotkey_adapter, namaHot);
        htAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hotKey.setAdapter(htAdapter);

        hotKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!= 0){
                    inputChat.setText(namaHot.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saluranHotkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotKey.performClick();
            }
        });
    }


    void hilangkanaKeyboard(){
        InputMethodManager imm=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    void login(){
        cUser= chatDaftarUser.getText().toString().trim();
        cPerusahaan = chatDaftarPerusahaan.getText().toString().trim();
        cPassword = chatDaftarPass.getText().toString().trim();

        if (TextUtils.isEmpty(cUser) || TextUtils.isEmpty(cPerusahaan) || TextUtils.isEmpty(cPassword)){
            String peringatan1 = "fill in everything, nothing is empty";
            berbicara.play(peringatan1);
            Toast.makeText(getContext(),peringatan1,Toast.LENGTH_LONG).show();
            return;
        }
        if (cUser.contains(" ") || cPerusahaan.contains(" ") || cPassword.contains(" ")){
            String peringatan2 = "no space there is no space";
            berbicara.play(peringatan2);
            Toast.makeText(getContext(),peringatan2,Toast.LENGTH_LONG).show();
            return;
        }

        db.child("chatbot").child("user").child(cPerusahaan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(cUser+"@"+cPerusahaan)){
                    String usr = dataSnapshot.child(cUser+"@"+cPerusahaan).child("user").getValue(String.class);
                    String psd = dataSnapshot.child(cUser+"@"+cPerusahaan).child("password").getValue(String.class);
                    String per = dataSnapshot.child(cUser+"@"+cPerusahaan).child("perusahaan").getValue(String.class);
                    String url = dataSnapshot.child(cUser+"@"+cPerusahaan).child("url").getValue(String.class);

                    //Toast.makeText(getContext(),usr+"-"+psd+"-"+per,Toast.LENGTH_LONG).show();
                    if (cUser.equals(usr) && cPerusahaan.equals(per) && cPassword.equals(psd)){
                        Paper.book().write("nama",cUser);
                        Paper.book().write("perusahaan",cPerusahaan);
                        Paper.book().write("url",url);
                        sudahDaftar = true;
                        dialog.dismiss();
                        startActivity(((Activity) context).getIntent());
                    }else {
                        Toast.makeText(getContext(),"well that's wrong, try repeating the correct three times permanently blocked",Toast.LENGTH_LONG).show();
                        berbicara.play("well that's wrong, try repeating the correct three times permanently blocked");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    void daftar(){
        aUser  = chatDaftarUser.getText().toString().trim();
        aPerusahaan  = chatDaftarPerusahaan.getText().toString().trim();
        aPassword  = chatDaftarPass.getText().toString().trim();

        if (TextUtils.isEmpty(aUser) || TextUtils.isEmpty(aPerusahaan) || TextUtils.isEmpty(aPassword)){
            String peringatan1 = "fill in everything, nothing is empty";
            berbicara.play(peringatan1);
            Toast.makeText(getContext(),peringatan1,Toast.LENGTH_LONG).show();
            return;
        }
        if (aUser.contains(" ") || aPerusahaan.contains(" ") || aPassword.contains(" ")){
            String peringatan2 = "no space there is no space";
            berbicara.play(peringatan2);
            Toast.makeText(getContext(),peringatan2,Toast.LENGTH_LONG).show();
            return;
        }

        db.child("chatbot").child("user").child(aPerusahaan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(aUser+"@"+aPerusahaan)){
                    Toast.makeText(getContext(),"name has been used, or just login",Toast.LENGTH_LONG).show();
                    berbicara.play("name has been used, or just login");
                    return;
                }
                db.child("client").child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(aPassword)){
                            berbicara.play("I can't recognize your company ID");
                            YoYo.with(Techniques.Tada).duration(1000).playOn(chatDaftarPass);
                            return;
                        }

                        final String aUrl = dataSnapshot.child(aPassword).child("url").getValue(String.class);
                        Log.i(TAG, "onDataChange: "+aUrl);

                        if (aUrl == null){
                            berbicara.play("I can't connect to the internet, try checking the internet");
                            return;
                        }
                            Map<String,Object> paketUser = new HashMap<>();
                            paketUser.put("user",aUser);
                            paketUser.put("perusahaan",aPerusahaan);
                            paketUser.put("password",aPassword);
                            paketUser.put("url",aUrl);
                            db.child("chatbot").child("user").child(aPerusahaan).child(aUser+"@"+aPerusahaan).setValue(paketUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        sNama = aUser;
                                        Paper.book().write("nama",aUser);
                                        Paper.book().write("perusahaan",aPerusahaan);
                                        Paper.book().write("url",aUrl);
                                        dialog.dismiss();
                                        String peringatan3 = "Your data has been saved successfully, please continue again";
                                        berbicara.play(peringatan3);
                                        Toast.makeText(getContext(),peringatan3,Toast.LENGTH_LONG).show();
                                        sudahDaftar = true;
                                        startActivity(((Activity) context).getIntent());
                                    }
                                }
                            });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // disini om layout chatbotnya
    void kegiatanChat(){
        db.child("chatbot").child("chat").child(perusahaan).child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Objects> ambil = (HashMap<String, Objects>)ds.getValue();
                    Map<String,Objects> terima = new HashMap<>();
                    for (Map.Entry<String,Objects> entry : ambil.entrySet()){
                        terima.put(entry.getKey(),entry.getValue());
                    }
                    listData.add(terima);
                }

                // adapter chat botnya
                final ChatbotRecyclerAdapter adapter = new ChatbotRecyclerAdapter(context,listData);
                wadahChat.setLayoutManager(new LinearLayoutManager(context));
                wadahChat.setAdapter(adapter);
                wadahChat.scrollToPosition(listData.size()-1);

                builderCus = new AlertDialog.Builder(context);
                final View viewCus = getLayoutInflater().inflate(R.layout.layout_chatbot_vholder4_v2,null,false);
                builderCus.setView(viewCus);
                dialogCus = builderCus.create();
                final TextView tamuBook = viewCus.findViewById(R.id.tamu_book);
                final TextView tamuNama = viewCus.findViewById(R.id.tamu_nama);
                final TextView tamuStt = viewCus.findViewById(R.id.tamu_stt);
                final TextView tamuJenisKamar = viewCus.findViewById(R.id.tamu_jenis_kamar);
                final TextView tamuPax = viewCus.findViewById(R.id.tamu_pax);
                final TextView tamuPax2 = viewCus.findViewById(R.id.tamu_pax2);
                final TextView tamuBreakfast = viewCus.findViewById(R.id.tamu_breakfast);
                final TextView tanggalDatang = viewCus.findViewById(R.id.tanggal_datang);
                final TextView bulanDatang = viewCus.findViewById(R.id.bulan_datang);
                final TextView tahunDatang = viewCus.findViewById(R.id.tahun_datang);
                final TextView tanggalBerangkat = viewCus.findViewById(R.id.tanggal_berangkat);
                final TextView bulanBerangkat = viewCus.findViewById(R.id.bulan_berangkat);
                final TextView tahunBerangkat = viewCus.findViewById(R.id.tahun_berangkat);
                      final TextView agennya = viewCus.findViewById(R.id.agennya);
                adapter.setKetikaHolder4Diklik(new ChatbotRecyclerAdapter.KetikaHolder4Diklik() {
                    @Override
                    public void maka(View view, int position) {
                        dialogCus.show();

                        Map<String,Objects> ambilSemua = adapter.getPosisi(position);

                        String tang = String.valueOf(ambilSemua.get("tanggal")).replace(" 00:00:00","");
                        String[] datang = String.valueOf(ambilSemua.get("tgl_datang")).replace(" 00:00:00","").split("-");
                        String[] berang = String.valueOf(ambilSemua.get("tgl_berang")).replace(" 00:00:00","").split("-");

                        String jenisKamar = !String.valueOf(ambilSemua.get("jenis_kamar")).equals("null")?String.valueOf(ambilSemua.get("jenis_kamar")):"";

                        tamuBook.setText(tang);
                        tamuNama.setText(String.valueOf(ambilSemua.get("nama_tamu")));
                        tamuStt.setText(String.valueOf(ambilSemua.get("stt")));
                        tamuJenisKamar.setText(jenisKamar);
                        tamuPax.setText("ADULT : "+String.valueOf(ambilSemua.get("pax")));
                        tamuPax2.setText("CHILD : "+String.valueOf(ambilSemua.get("pax2")));
                        tamuBreakfast.setText(String.valueOf(ambilSemua.get("nm_makan")).trim());
                        tanggalDatang.setText(datang[2]);
                        bulanDatang.setText(datang[1]);
                        tahunDatang.setText(datang[0]);
                        tanggalBerangkat.setText(berang[2]);
                        bulanBerangkat.setText(berang[1]);
                        tahunBerangkat.setText(berang[0]);
                        agennya.setText(String.valueOf(ambilSemua.get("agen")));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        wadahChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && tulisContainer.getVisibility() == View.VISIBLE){
                    YoYo.with(Techniques.FadeOutDown).duration(200).withListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            tulisContainer.setVisibility(View.GONE);
                        }
                    }).playOn(tulisContainer);


                }else if (dy > 0 && tulisContainer.getVisibility() == View.GONE){
                    tulisContainer.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(200).playOn(tulisContainer);
                }
            }
        });
    }

    void perkenalanAwal(){
        tooltip = new SimpleTooltip.Builder(context)
                .anchorView(chatIcon)
                .animated(true)
                .text("click here")
                .gravity(Gravity.TOP)
                .build();

        tooltip.show();

        tooltip2 = new SimpleTooltip.Builder(context)
                .backgroundColor(getResources().getColor(R.color.colorBg1))
                .arrowColor(getResources().getColor(R.color.colorBg1))
                .textColor(getResources().getColor(R.color.colorPutih))
                .anchorView(chatIcon)
                .animated(true)
                .text("please say something")
                .gravity(Gravity.TOP)
                .build();

        YoYo.with(Techniques.Wobble)
                .duration(1000)
                .repeat(2)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        tooltip.dismiss();
                        new SimpleTooltip.Builder(context)
                                .animationDuration(1000)
                                .anchorView(inputChat)
                                .animated(true)
                                .text("or type something here")
                                .gravity(Gravity.TOP)
                                .build()
                                .show();
                        Paper.book().write("sMenyapa","benar");
                    }
                })
                .playOn(chatIcon);
        tooltip.show();
    }

    void bolehMendengarkan(){
        chatIcon.setImageResource(R.drawable.mic_ok_24);
        droidSpeech.startDroidSpeechRecognition();
        Paper.book().write("sMenyapa",true);
        mulaiBicara.setVisibility(View.VISIBLE);
        berbicara.stop();
    }

    void tidakBolehMendengarkan(){
        chatIcon.setImageResource(R.drawable.mic_none_24);
        droidSpeech.closeDroidSpeechOperations();
        mulaiBicara.setVisibility(View.GONE);
    }

    private boolean vKenalan(){
        String nam  = Paper.book().read("nama");
        return nam != null;
    }


    void tukangProses(String jNama){


        this.hasilBicara = jNama.toLowerCase();
        String[] kPecahan = hasilBicara.split(" ");
        if (hasilBicara.contains("please open")){
            meKirimPesan(hasilBicara);
            if (kPecahan.length > 3){
                List<PackageInfo> apps = context.getPackageManager().getInstalledPackages(0);
                List<String> namaApp = new ArrayList<>();
                for (PackageInfo packageInfo : apps){
                    namaApp.add(packageInfo.packageName);
                }

                List<String> hasilAplikasi = new ArrayList<>();
                for (int i = 0;i<namaApp.size();i++){
                    if (namaApp.get(i).contains(kPecahan[3])){
                        hasilAplikasi.add(namaApp.get(i));
                    }
                }
                if (!hasilAplikasi.isEmpty()){
                    botKirimPesan("open "+kPecahan[3]);
                    berbicara.play("open "+kPecahan[3]);

                    inputChat.setText("");
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(hasilAplikasi.get(0));
                    hasilAplikasi.clear();
                    startActivity(intent);
                }else {
                    botKirimPesan("it seems like I can't find the application whose name is "+kPecahan[3] +" or it seems like I am getting tired");
                    berbicara.play("it seems like I can't find the application whose name is "+kPecahan[3] +" or it seems like I am getting tired");
                    inputChat.setText("");
                }

            }else {
                inputChat.setText("");
                botKirimPesan("less specific commands");
                berbicara.play("less specific commands");
            }

        }

        if (hasilBicara.contains("by name")){
            meKirimPesan(hasilBicara);
            //todo : cek bagian sini
            String pencari = " ";
            String[] prosesKunci = hasilBicara.split(pencari);

            String theKey = String.valueOf(prosesKunci[3]);
           /* Log.i(TAG, "tukangProses: "+theKey);
            NgetesRetrofit ngetesRetrofit = new NgetesRetrofit(context);
            ngetesRetrofit.dapatkanCustomer(theKey,namaUrlNya);*/

            if (prosesKunci[0] == null || prosesKunci[1] == null){
                berbicara.play("keyword requirements are not right");
                botKirimPesan("use the [request] scheme [by name] [customer]");
                return;
            }
            berbicara.play("for a while, I find it first by name ,"+prosesKunci[3]);
            botKirimPesan("for a while, I find it first by name,"+prosesKunci[3]);

            pencarianDataCustomer(theKey);

        }else {
            db.child("chatbot").child("percakapan").child("sudah").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String[] pisahKata = hasilBicara.split(" ");
                    if (pisahKata.length == 1){
                        meKirimPesan(pisahKata[0]);
                        berbicara.play(pisahKata[0]+". that means");
                        botKirimPesan(pisahKata[0]+ ". that means ?");
                        inputChat.setText("");
                        return;
                    }

                    meKirimPesan(hasilBicara);

                    if (pisahKata.length == 2){
                        Map<String,Objects> ambil = (HashMap<String, Objects>)dataSnapshot.getValue();
                        String terima = "";
                        for (Map.Entry<String,Objects> ent : ambil.entrySet()){
                            if (String.valueOf(ent.getKey()).contains(pisahKata[0]) && String.valueOf(ent.getKey()).contains(pisahKata[1])){
                                terima = String.valueOf(ent.getValue());
                            }
                        }
                        pikirJawaban(terima,dataSnapshot);
                    }
                    if (pisahKata.length == 3){
                        Map<String,Objects> ambil = (HashMap<String, Objects>)dataSnapshot.getValue();
                        String terima = "";
                        for (Map.Entry<String,Objects> ent : ambil.entrySet()){
                            if (String.valueOf(ent.getKey()).contains(pisahKata[0]) && String.valueOf(ent.getKey()).contains(pisahKata[pisahKata.length-1])){
                                terima = String.valueOf(ent.getValue());
                            }
                        }
                        pikirJawaban(terima,dataSnapshot);
                    }
                    if (pisahKata.length > 3){
                        Map<String,Objects> ambil = (HashMap<String, Objects>)dataSnapshot.getValue();
                        String terima = "";
                        for (Map.Entry<String,Objects> ent : ambil.entrySet()){
                            if (String.valueOf(ent.getKey()).contains(pisahKata[0]) && String.valueOf(ent.getKey()).contains(pisahKata[1]) && String.valueOf(ent.getKey()).contains(pisahKata[2]) && String.valueOf(ent.getKey()).contains(pisahKata[pisahKata.length-1])){
                                terima = String.valueOf(ent.getValue());
                            }
                        }
                        pikirJawaban(terima,dataSnapshot);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    void pikirJawaban(String terima,DataSnapshot dataSnapshot){
        if (!terima.equals("")){
            Log.i(TAG, "onDataChange: terima = "+terima);
            botKirimPesan(terima);
            berbicara.play(terima);
            terima = "";
            inputChat.setText(terima);
        }else {
            Log.i(TAG, "onDataChange: lainnya");
            if (dataSnapshot.hasChild(hasilBicara)){
                String jawaban = dataSnapshot.child(hasilBicara).getValue(String.class);
                botKirimPesan(jawaban);
                berbicara.play(jawaban);
            }else {
                db.child("chatbot").child("percakapan").child("belum").child(hasilBicara).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            String ngeJawab = "Sorry, your question is not registered, then I will ask Mr. Very to update it";
                            botKirimPesan(ngeJawab);
                            berbicara.play(ngeJawab);
                            inputChat.setText("");
                        }
                    }
                });
            }
        }
    }

    void pencarianDataCustomer(String kunciNamanya){


//        namaUrlNya = Paper.book().read("url");
//        if (namaUrlNya.equals("")){
//            berbicara.play("I can't recognize the url of your company, contact probus");
//            return;
//        }
//        Log.i(TAG, "pencarianDataCustomer: "+namaUrlNya);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(namaUrlNya)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi  jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<JsonObject>> panggilan = jsonPlaceHolderApi.getCustomer(kunciNamanya);
        panggilan.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> diterima = response.body();
                List<Map<String,Objects>> diterima2 = new ArrayList<>();
                Map<String,String> dataBaru = new HashMap<>();
                for (JsonObject semua : diterima){
                    Map<String,Objects> maka = new Gson().fromJson(semua.toString(),HashMap.class);
                    Map<String,Objects> maka2 = new HashMap<>();
                    for (Map.Entry<String,Objects> entry : maka.entrySet()){
                        maka2.put(entry.getKey(),entry.getValue());
                        dataBaru.put(entry.getKey(),String.valueOf(entry.getValue()));

                    }
                    Log.i(TAG, "onResponse: "+maka2.get("nama_tamu"));
                    diterima2.add(maka2);
                    dataBaru.put("type","4");
                    botKirimPesanHasilCustomer(dataBaru);

                }

                if (dataBaru.isEmpty()){
                    inputChat.setText("");
                    botKirimPesan("Sorry, the data regarding this name is not registered with our database");
                    berbicara.play("Sorry, the data regarding this name is not registered with our database");
                    return;
                }

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });





        /*List<Map<String,Objects>> diterima2 = new ArrayList<>();
                Map<String,String> dataBaru = new HashMap<>();
                for (JsonObject semua : diterima){
                    Map<String,Objects> maka = new Gson().fromJson(semua.toString(),HashMap.class);
                    Map<String,Objects> maka2 = new HashMap<>();
                    for (Map.Entry<String,Objects> entry : maka.entrySet()){
                        maka2.put(entry.getKey(),entry.getValue());
                        dataBaru.put(entry.getKey(),String.valueOf(entry.getValue()));

                    }
                    Log.i(TAG, "onResponse: "+maka2.get("nama_tamu"));
                    diterima2.add(maka2);
                    dataBaru.put("type","4");
                    botKirimPesanHasilCustomer(dataBaru);

                }

                if (dataBaru.isEmpty()){
                    inputChat.setText("");
                    botKirimPesan("Sorry, the data regarding this name is not registered with our database");
                    berbicara.play("Sorry, the data regarding this name is not registered with our database");
                    return;
                }
*/


        /*Call<List<PojoGetCustomer>> panggil = jsonPlaceHolderApi.getCustomer(kunciKata);


        try {
            panggil.enqueue(new Callback<List<PojoGetCustomer>>() {
                @Override
                public void onResponse(Call<List<PojoGetCustomer>> call, Response<List<PojoGetCustomer>> response) {
                    if (!response.isSuccessful()){
                        Log.i(TAG, "onResponse: "+response.code());
                    }

                    List<PojoGetCustomer> pelanggan = response.body();
                    if (pelanggan.isEmpty()){
                        inputChat.setText("");
                        botKirimPesan("Sorry, the data regarding this name is not registered with our database");
                        berbicara.play("Sorry, the data regarding this name is not registered with our database");
                        return;
                    }

                    if (kunciKata.contains("all")){
                        Map<String,String> dataSemua = new HashMap<>();
                        List<String> namaPesan = new ArrayList<>();
                        for (PojoGetCustomer pj : pelanggan){
                            namaPesan.add(pj.nm_cus);
                        }
                        Set<String> set = new HashSet<>(namaPesan);
                        namaPesan.clear();
                        namaPesan.addAll(set);
                        dataSemua.put("pesan",TextUtils.join("\n",namaPesan));
                        dataSemua.put("type","2");
                        botKirimPesanHasilCustomer(dataSemua);

                        inputChat.setText("");
                        berbicara.play("The following are the results of the data you requested");
                        dataSemua.clear();
                    }else {
                        Map<String,String> dataBaru = new HashMap<>();
                        for (PojoGetCustomer pj : pelanggan){
                            String[] datangs = String.valueOf(pj.tgl_datang).split("-");
                            String[] pergis = String.valueOf(pj.tgl_berang).split("-");
                            Map<String,String> namaBUlan = new HashMap<>();
                            namaBUlan.put("01","JAN");
                            namaBUlan.put("02","FEB");
                            namaBUlan.put("03","MAR");
                            namaBUlan.put("04","APR");
                            namaBUlan.put("05","MEI");
                            namaBUlan.put("06","JUN");
                            namaBUlan.put("07","JUL");
                            namaBUlan.put("08","AGS");
                            namaBUlan.put("09","SEP");
                            namaBUlan.put("10","OKT");
                            namaBUlan.put("11","NOV");
                            namaBUlan.put("12","DES");

                            dataBaru.put("nama",String.valueOf(pj.nm_cus));
                            dataBaru.put("room",String.valueOf(pj.no_room));
                            dataBaru.put("in",String.valueOf(datangs[2]));
                            dataBaru.put("out",String.valueOf(pergis[2]));
                            dataBaru.put("blnin",String.valueOf(namaBUlan.get(datangs[1])));
                            dataBaru.put("blnout",String.valueOf(namaBUlan.get(pergis[1])));
                            dataBaru.put("type","4");

                            botKirimPesanHasilCustomer(dataBaru);
                        }
                        dataBaru.clear();
                        inputChat.setText("");
                        berbicara.play("The following are the results of the data you requested");
                    }
                }

                @Override
                public void onFailure(Call<List<PojoGetCustomer>> call, Throwable t) {
                    Log.i(TAG, "onFailure: "+t.getMessage());
                    Toast.makeText(getContext(),"check your internet connecttion, or call maintener developer",Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(),"data malfunction ,"+e,Toast.LENGTH_LONG).show();
            Log.i(TAG, "pencarianDataCustomer: "+e);
        }
*/


    }

    void botKirimPesan(String pesannya){
        Map<String, String> pesanBot = new HashMap<>();
        pesanBot.put("pesan",pesannya);
        pesanBot.put("type","2");
        db.child("chatbot").child("chat").child(perusahaan).child(userName).push().setValue(pesanBot).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    inputChat.setText("");

                }
            }
        });
    }
    void botKirimPesanHasilCustomer(Map<String,String> dataCus){
        db.child("chatbot").child("chat").child(perusahaan).child(userName).push().setValue(dataCus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    inputChat.setText("");

                }
            }
        });
    }

    void meKirimPesan(String pesanku){
        Map<String, String> pesanMe = new HashMap<>();
        pesanMe.put("pesan",pesanku);
        pesanMe.put("type","0");
        db.child("chatbot").child("chat").child(perusahaan).child(userName).push().setValue(pesanMe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    inputChat.setText("");

                }
            }
        });
    }

    void mengupdateJawabah(){
        db.child("chatbot").child("percakapan").child("belum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ddss : dataSnapshot.getChildren()){
                    String dataku = ddss.getValue(String.class);
                    if (!dataku.equals("")){
                        kun = ddss.getKey();
                        db.child("chatbot").child("percakapan").child("sudah").child(kun).setValue(dataku).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    db.child("chatbot").child("percakapan").child("belum").child(kun).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getContext(),"new data has been updated",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        droidSpeech.closeDroidSpeechOperations();
        berbicara.stop();
        Paper.book().write("sudahkenalan","sudah");
    }

    // kebawah
}
