package com.apps.ayurvedcareproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    DatabaseHelper db;
    String username;
    List<HealthModel> list;
    RecyclerView recyclerView;
    Button backBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username");
        list = new ArrayList<>();

        Cursor cursor = db.getHistory(username);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new HealthModel(
                        cursor.getString(0), // problem
                        cursor.getString(1), // solution
                        cursor.getString(2)  // timestamp
                ));
            }
            cursor.close();
        }

        recyclerView.setAdapter(new HealthAdapter(list));

        // Back Button: Go back to HomeActivity
        backBtn.setOnClickListener(v -> finish());

        // Logout Button: Go back to LoginActivity and clear task stack
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
