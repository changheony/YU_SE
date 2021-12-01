package com.example.nevi_menu.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nevi_menu.MainActivity;
import com.example.nevi_menu.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExerciseScene extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    Button exercise_secne_youtubeBtn;

    String name;
    String exerciseAreaName;
    String dayName;
    String Uri;

    String profile;
    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;
    TextView exercise_scene_exercisename;
    TextView exercise_scene_description;
    TextView exercise_secne_val;
    RatingBar star;
    Button exercise_scene_complete_btn;
    Button exercise_scene_cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_scene);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        exerciseAreaName = intent.getStringExtra("exerciseAreaName");
        dayName = intent.getStringExtra("dayName");

        exercise_scene_exercisename = findViewById(R.id.exercise_secne_exercisename);
        exercise_scene_description = findViewById(R.id.exercise_secne_description);
        exercise_secne_val = findViewById(R.id.exercise_secne_val);
        star = findViewById(R.id.exercise_secne_star);
        exercise_scene_complete_btn = findViewById(R.id.exercise_secne_complete_btn);
        exercise_scene_cancel_btn = findViewById(R.id.exercise_secne_cancel_btn);
        exercise_secne_youtubeBtn = findViewById(R.id.exercise_secne_youtubeBtn);
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.exercise_secne_youtubeView);
        YouTubePlayer.OnInitializedListener listener;


        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        mDatabaseRef = database.getReference(exerciseAreaName); // DB 테이블 연결
        mDatabaseRef.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ExerciseSelectItem exerciseItem = dataSnapshot.getValue(ExerciseSelectItem.class);

                profile = exerciseItem.getIv_profile();
                exercise_scene_exercisename.setText(exerciseItem.getTv_name());
                Uri= exerciseItem.getUri();//"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                exercise_scene_description.setText(exerciseItem.getDescription());
                star.setRating(Float.parseFloat(exerciseItem.getStar()));
                exercise_secne_val.setText(exerciseItem.getTv_time());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(dayName, String.valueOf(databaseError.toException()));
            }
        });


        exercise_scene_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 얻어오기
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // 현재 유저 정보 가져오기

                Exercise exercise = new Exercise();
                exercise.setIv_profile(profile);
                exercise.setTv_name(exercise_scene_exercisename.getText().toString());
                exercise.setTv_time(exercise_secne_val.getText().toString());

                DatabaseReference mDBRefUser = FirebaseDatabase.getInstance().getReference("UserAccount"); // DB 테이블 연결
                DatabaseReference mdatabaseRef = mDBRefUser.child(firebaseUser.getUid()).child("Exercise"+dayName);
                mdatabaseRef.child(exercise.getTv_name()).setValue(exercise);

                Video video = new Video();
                video.setUri(Uri);

                mDBRefUser = FirebaseDatabase.getInstance().getReference("Video");
                mdatabaseRef = mDBRefUser.child(exercise_scene_exercisename.getText().toString());
                mdatabaseRef.setValue(video);


                Intent intent = new Intent(ExerciseScene.this , MainActivity.class);
                startActivity(intent);
            }
        });

        exercise_scene_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseScene.this , MainActivity.class);
                startActivity(intent);
            }
        });






        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(Uri); //
                //https://www.youtube.com/watch?v=NmkYHmiNArc 유투브에서 v="" 이부분이 키에 해당
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(),"오류 발생", Toast.LENGTH_SHORT).show();
            }
        };


        exercise_secne_youtubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyC3GKJQWdngBcl9Gh6n9n2f3RB9dY2Cynw", listener);

            }
        });

    }

}
