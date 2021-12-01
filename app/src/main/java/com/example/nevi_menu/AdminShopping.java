package com.example.nevi_menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminShopping extends AppCompatActivity {

    private ListView list;
    private Button addbt;
    private String item;
    private String site_exp;
    private String site_addr;
    private String total_info;
    private List<String> data = new ArrayList<>();
    private DatabaseReference mDBReference;
    private DatabaseReference mDBspsite;
    private String item_and_info[];
    private String site[];
    //private String store_item_list[];
    private List<String> store_site = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shopping);

        list = (ListView)findViewById(R.id.shopping_admin_lt);
        addbt = (Button)findViewById(R.id.spbutton_add);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        list.setAdapter(adapter);
        mDBReference = FirebaseDatabase.getInstance().getReference();
        mDBspsite = FirebaseDatabase.getInstance().getReference().child("shopping_list");

        listload();

        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(AdminShopping.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("입력 1");
                ad.setMessage("종류를 입력하여 주십시오...");
                final EditText et = new EditText(AdminShopping.this);
                ad.setView(et);

                ad.setPositiveButton("다음", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item = et.getText().toString();
                        dialogInterface.dismiss();
                        nextset1();
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                ad.show();


            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("------------------삭제결과출력라인----------------");
                String tmp = (String) adapterView.getItemAtPosition(i);

                System.out.println("삭제전site : "+store_site);

                store_site.remove(i); //사이트 주소 삭제


                System.out.println("sitelist : "+store_site);

                System.out.println("datalist : "+data);
                liststore(2,data.get(i));

                //while(data.remove(data.get(i)));//데이타 삭제
                listload();

                //초기화
                //adapter.notifyDataSetChanged();//리스트 새로고침
            }
        });


    }

    private void nextset1(){
        AlertDialog.Builder ad2 = new AlertDialog.Builder(AdminShopping.this);
        ad2.setIcon(R.mipmap.ic_launcher);
        ad2.setTitle("입력 2");
        ad2.setMessage("사이트설명 입력하여 주십시오...");
        final EditText et2 = new EditText(AdminShopping.this);
        ad2.setView(et2);

        ad2.setPositiveButton("다음", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                site_exp = et2.getText().toString();
                dialogInterface.dismiss();
                nextset2();
            }
        });

        ad2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        ad2.show();
    }

    private void nextset2(){
        AlertDialog.Builder ad3 = new AlertDialog.Builder(AdminShopping.this);
        ad3.setIcon(R.mipmap.ic_launcher);
        ad3.setTitle("입력 3");
        ad3.setMessage("사이트주소 입력하여 주십시오...");
        final EditText et3 = new EditText(AdminShopping.this);
        ad3.setView(et3);

        ad3.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                site_addr = et3.getText().toString();
                dialogInterface.dismiss();
                System.out.println(item);
                System.out.println(site_exp);
                System.out.println(site_addr);
                setstr();
            }
        });

        ad3.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        ad3.show();
    }

    private void setstr(){
        total_info = String.format("%s%s%s%s","품목  : ",item,"     설명 : ",site_exp);
        data.add(total_info);
        liststore(1,"store");
        listload();
        adapter.notifyDataSetChanged();//리스트 새로고침
    }

    private void liststore(int i,String s){
        if(i==1) {
            mDBReference.child("shopping_list").child(total_info).child("site").setValue(site_addr);
        }
        if(i==2){
            mDBReference.child("shopping_list").child(s).setValue(null);
            //mDBReference.child("shopping_list").child(total_info).child("site").setValue(site_addr);
        }
    }

    private void listload(){
        mDBspsite.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                store_site.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tmpstr = snapshot.getKey().toString(); //리스트에 저장할 값
                    item_and_info =  tmpstr.split("=");

                    System.out.println("----------------newline----------------------");
                    System.out.print("key : ");
                    System.out.println(item_and_info[0]);
                    data.add(item_and_info[0]);

                    String firemessage = snapshot.getValue().toString();    //실제 사이트
                    site = firemessage.split("=");

                    String tmp = site[1];
                    System.out.println(tmp.substring(0,tmp.length()-1)); //주소사이트만 남기기
                    store_site.add(tmp.substring(0,tmp.length()-1));

                }
                System.out.println("----------------로드 후 결과 출력 -----------------");

                System.out.println("datalist 테스트 : "+data);
                System.out.println("sitelist 테스트 : "+store_site);
                adapter.notifyDataSetChanged();//리스트 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}