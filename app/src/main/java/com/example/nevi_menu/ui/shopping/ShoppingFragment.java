package com.example.nevi_menu.ui.shopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nevi_menu.R;
import com.example.nevi_menu.databinding.FragmentShoppingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {

    private ShoppingViewModel shoppingViewModel;
    private FragmentShoppingBinding binding;
    private ListView list;
    private String item;
    private String site_exp;
    private String site_addr;
    private String total_info;
    private List<String> data = new ArrayList<>();
    private DatabaseReference mDBReference;
    private DatabaseReference mDBspsite;
    private String item_and_info[];
    private String site[];
    private List<String> store_site = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingViewModel =
                new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentShoppingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = root.findViewById(R.id.shop_list);
        mDBReference = FirebaseDatabase.getInstance().getReference();
        mDBspsite = FirebaseDatabase.getInstance().getReference().child("shopping_list");
        listload();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String site = store_site.get(i);
                System.out.println("사이트 주소 : "+site);
                Intent intenturi = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+site));
                startActivity(intenturi);

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, data);//리스트간 연결하기위한 것
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();//리스트 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}