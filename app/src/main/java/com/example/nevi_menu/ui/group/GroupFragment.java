package com.example.nevi_menu.ui.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.nevi_menu.MainActivity;
import com.example.nevi_menu.databinding.FragmentGroupBinding;
import com.example.nevi_menu.ui.group.GroupViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.example.nevi_menu.R;

public class GroupFragment extends Fragment implements View.OnClickListener {

    private GroupViewModel groupViewModel;
    private FragmentGroupBinding binding;
    private DatabaseReference mDBReference;
    private final ArrayList<String> emailList = new ArrayList<>();
    private final ArrayList<String> nicknameList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Button btn_addmem;



        groupViewModel =
                new ViewModelProvider(this).get(GroupViewModel.class);

        binding = FragmentGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_group,container,false);
        btn_addmem = (Button)root.findViewById(R.id.btn_addmem);
        System.out.println("1111"); //check
        btn_addmem.setOnClickListener(this);

        //파이어베이스에서 데이타를 읽어올 경로
        mDBReference= FirebaseDatabase.getInstance().getReference().child("UserAccount");
        mDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String firemessage = snapshot.getValue().toString();    //문자열로 받기
                    String segments[] = firemessage.split(","); //idx2=nickname, idx3=email
                    int idx1 = segments[2].indexOf("=");
                    String nn = segments[2].substring(idx1+1);
                    int idx2 = segments[3].indexOf("=");
                    String e = segments[3].substring(idx2+1);
                    emailList.add(e);  //리스트에 이메일 추가
                    nicknameList.add(nn);
                }
                System.out.println(emailList);
                System.out.println(nicknameList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.out.println("fail....................");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //fragment는 클릭 리스너가 안먹히기때문에 따로 클릭리스너 이벤트를 지정
    @Override
    public void onClick(View view) {
        System.out.println("2222"); //check


        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setTitle("멤버 추가");
        ad.setMessage("추가하고자 하는 멤버의 이름을 추가해 주세요");

        final EditText et = new EditText(getActivity());
        ad.setView(et);

        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String result = et.getText().toString();
                System.out.println(result);
                dialogInterface.dismiss();
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
}