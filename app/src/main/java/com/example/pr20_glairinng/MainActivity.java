package com.example.pr20_glairinng;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText courseNameEdt, courseDurationEdt, courseDescriptionEdt;

    private Button submitCourseBtn;

    private String courseName, courseDuration, courseDescription;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration);
        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);

        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseName = courseNameEdt.getText().toString();
                courseDescription = courseDescriptionEdt.getText().toString();
                courseDuration = courseDurationEdt.getText().toString();
                
                if (TextUtils.isEmpty(courseName)) {
                    courseNameEdt.setError("Пожалуйста, введите название курса");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    courseDescriptionEdt.setError("Пожалуйста, введите описание курса");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    courseDurationEdt.setError("Пожалуйста, укажите продолжительность курса");
                } else {
                    addDataToFirestore(courseName, courseDescription, courseDuration);
                }
            }
        });
    }

    private void addDataToFirestore(String courseName, String courseDescription, String courseDuration) {
        CollectionReference dbCourses = db.collection("Courses");
        Courses courses = new Courses(courseName, courseDescription, courseDuration);
        dbCourses.add(courses).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this, "Ваш курс был добавлен в Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Не удалось добавить курс \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
