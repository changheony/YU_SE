package com.example.nevi_menu.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nevi_menu.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExerciseVideo extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    Button exercise_video_youtubeBtn;

    String name;
    String Uri;
    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;
    TextView exercise_video_exercisename;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);


        Intent intent = getIntent();
        name = intent.getStringExtra("name");


        exercise_video_exercisename = findViewById(R.id.exercise_video_exercisename);
        exercise_video_exercisename.setText(name);
        exercise_video_youtubeBtn = findViewById(R.id.exercise_video_youtubeBtn);
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.exercise_video_youtubeView);
        YouTubePlayer.OnInitializedListener listener;


        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        mDatabaseRef = database.getReference("Video"); // DB 테이블 연결
        mDatabaseRef.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Video video = dataSnapshot.getValue(Video.class);

                Uri= video.getUri();//"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(name, String.valueOf(databaseError.toException()));
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


        exercise_video_youtubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyC3GKJQWdngBcl9Gh6n9n2f3RB9dY2Cynw", listener);

            }
        });

    }

}


