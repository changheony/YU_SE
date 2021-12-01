package com.example.nevi_menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nevi_menu.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

//this is main
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //앱의 시작지점 틀었을때 이걸 실행해라
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar); //binding의 appbar의 toolbar를 서포트엑터로 설정 그니까 이걸해야 .noActionbar해도 오류안남

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView; //네비게이션 메뉴바 연결
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_food, R.id.nav_exercise, R.id.nav_group, R.id.nav_shopping, R.id.nav_my_page, R.id.nav_logout) //각 메뉴마다 메뉴바를 추가해주는 역할
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main); //위에서 toolbar설정한거에 abb_bar에 네비게이션 버튼 추가하는거
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
//mai