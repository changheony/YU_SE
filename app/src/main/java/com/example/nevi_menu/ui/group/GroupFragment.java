package com.example.nevi_menu.ui.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nevi_menu.R;
import com.example.nevi_menu.databinding.FragmentGroupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GroupFragment extends Fragment implements View.OnClickListener {

    private GroupViewModel groupViewModel;
    private FragmentGroupBinding binding;
    private DatabaseReference mDBReference;
    private DatabaseReference mDBEnN;
    private DatabaseReference mDBRefUser;
    private final ArrayList<String> emailList = new ArrayList<>();
    private final ArrayList<String> nicknameList = new ArrayList<>();
    private final ArrayList<String> currentWeight = new ArrayList<>();
    private final ArrayList<String> targettWeight = new ArrayList<>();
    private ListView list; //멤버 리스트뷰
    private List<String> data_list; //멤버들을 저장하는 리스트
    private List<String> nick_list;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth mFirebaseAuth; //디비인증
    private FirebaseUser firebaseUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Button btn_addmem; //멤버추가 버튼변수
        mDBRefUser = FirebaseDatabase.getInstance().getReference("member_list");
        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
        firebaseUser = mFirebaseAuth.getCurrentUser(); //현재 유저정보 가져오기
        groupViewModel =
                new ViewModelProvider(this).get(GroupViewModel.class);

        binding = FragmentGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot(); //루트가 결정되고 그다음 버튼 받아내자

        //버튼설정
        //ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_group,container,false);
        btn_addmem = (Button)root.findViewById(R.id.btn_addmem);
        btn_addmem.setOnClickListener(this);

        //리스트 설정
        list = (ListView)root.findViewById(R.id.list_member);
        data_list = new ArrayList<>(); //데이터를 저장하기위한 문자열 리스트
        nick_list = new ArrayList<>(); //닉네임만 저장하기위한 문자열 리스트
        //멤버리스트 불러오기
        List_load();


        //파이어베이스에서 데이타를 읽어올 경로
        mDBEnN= FirebaseDatabase.getInstance().getReference().child("UserAccount");
        mDBEnN.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String firemessage = snapshot.getValue().toString();    //문자열로 받기
                    String segments[] = firemessage.split(","); //idx2=nickname, idx3=email
                    int idx1 = segments[2].indexOf("=");
                    String nn = segments[2].substring(idx1+1);
                    int idx2 = segments[4].indexOf("=");
                    String e = segments[4].substring(idx2+1);
                    int idx3 = segments[1].indexOf("=");
                    String crw = segments[1].substring(idx3+1);
                    int idx4 = segments[5].indexOf("=");
                    String tgw = segments[5].substring(idx4+1);
                    emailList.add(e);  //리스트에 이메일 추가
                    nicknameList.add(nn);
                    currentWeight.add(crw);
                    targettWeight.add(tgw);
                    System.out.println(firemessage);
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

        //리스트 뷰 클릭 이벤트
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                String dnick = item.split(" ")[0]; //delete될 닉네임
                AlertDialog.Builder ad2 = new AlertDialog.Builder(getActivity());
                ad2.setIcon(R.mipmap.ic_launcher);
                ad2.setTitle("삭제");
                ad2.setMessage(dnick+"님을 정말로 삭제하시겠습니까?");
                ad2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.print("선택된것 : ");
                        System.out.println(item);
                        System.out.print("nick_list1");
                        System.out.println(nick_list);
                        data_list.remove(item);
                        while(nick_list.remove(dnick)); //버그방지
                        System.out.print("nick_list2");
                        System.out.println(nick_list);
                        list_store();
                        List_load();
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
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setIcon(R.mipmap.ic_launcher);
        ad.setTitle("멤버 추가");
        ad.setMessage("추가하고자 하는 멤버의 \n닉네임을 입력해 주세요");
        String meminfo;
        final EditText et = new EditText(getActivity());
        ad.setView(et);

        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String result = et.getText().toString();
                //List_load();
                //닉네임 있는지 확인인
                if(nicknameList.contains(result)) {
                    System.out.println(result);
                    dialogInterface.dismiss();
                    if(nick_list.contains(result)){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("멤버 에러")
                                .setMessage("이미 존재하는 멤버입니다.")
                                .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int sumthin) {
                                    }
                                })
                                .show(); // 팝업창 보여줌
                    }
                    else { //문제없을 때 group list에 멤버 추가
                        for(int j = 0; j<nicknameList.size();j++){
                            if(nicknameList.get(j).compareTo(result) == 0){ //닉네임이 포함된 순번
                                String CRW = currentWeight.get(j);
                                String TGW = targettWeight.get(j);

                                String meminfo = String.format("%-24s%-5s%-25s%-5s%-25s",result,"|",CRW,"|",TGW);
                                data_list.add(meminfo);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        nick_list.add(result);
                        System.out.print("data_list : ");
                        System.out.println(data_list); //test용 프린터

                        list_store();
                    }
                }

                else{
                    dialogInterface.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("멤버 에러")
                            .setMessage("존재하지 않는 멤버 닉네임입니다.")
                            .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dlg, int sumthin) {
                                }
                            })
                            .show(); // 팝업창 보여줌
                }

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


    private void List_load() {
        //파이어베이스에서 데이타를 읽어올 경로
        mDBEnN= FirebaseDatabase.getInstance().getReference().child("member_list").child(firebaseUser.getUid());
        mDBEnN.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //DataSnapshot snapshot : dataSnapshot.getChildren()
                    String test = snapshot.getValue().toString();    //문자열로 받기
                    test = test.substring(1,test.length()-1);


                    String segments[] = test.split(",");
                    //data_list = Arrays.asList(test.toString());
                    data_list = new ArrayList<>();//초기화
                    for(int i = 0; i<segments.length;i++){
                        data_list.add(segments[i].trim());
                    }
                    //System.out.print("test :::::::::::: ");
                    //System.out.println(test);
                    //System.out.print("멤버 수 :::::::::::: ");
                    //System.out.println(segments.length);
                    //System.out.print("최종 멤버 리스트 :::::::::::: ");
                    //System.out.println(data_list);
                    for(int i = 0; i<data_list.size();i++){
                        nick_list.add(data_list.get(i).split(" ")[0]);
                    }

                }
                adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, data_list);//리스트간 연결하기위한 것
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.out.println("fail....................");
            }
        });

    }



    private void list_store() {
        mDBReference = mDBRefUser.child(firebaseUser.getUid());
        mDBReference.child("memberlist").setValue(data_list);
    }
}

