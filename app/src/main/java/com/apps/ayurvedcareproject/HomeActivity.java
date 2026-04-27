package com.apps.ayurvedcareproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    DatabaseHelper db;
    String username;
    AutoCompleteTextView healthInput;
    Button getSolutionBtn, backToLoginBtn;
    TextView resultTxt;

    // List for Autocomplete
    String[] diseases = {
            "Fever", "Cough", "Cold", "Headache", "Stomach Ache",
            "Acidity", "Insomnia", "Joint Pain", "Skin Rash", "Hair Fall"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username");

        healthInput = findViewById(R.id.healthInput);
        getSolutionBtn = findViewById(R.id.getSolution);
        backToLoginBtn = findViewById(R.id.backToLoginBtn);
        resultTxt = findViewById(R.id.result);

        // Setting up Autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, diseases);
        healthInput.setAdapter(adapter);
        healthInput.setThreshold(1); // Start showing suggestions from 1st character

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSolutionBtn.setOnClickListener(v -> {
            String problem = healthInput.getText().toString().trim().toLowerCase();
            if (problem.isEmpty()) {
                Toast.makeText(this, "Please enter a health issue", Toast.LENGTH_SHORT).show();
                return;
            }

            String solution = getAyurvedicRemedy(problem);
            resultTxt.setText(solution);

            // Save to database
            db.saveHealthData(username, problem, solution);
        });

        Button historyBtn = findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        backToLoginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String getAyurvedicRemedy(String problem) {
        Map<String, String> remedies = new HashMap<>();

        remedies.put("fever", "• Drink Giloy juice twice a day to boost immunity.\n" +
                "• Consume a decoction made of Tulsi, Ginger, and Black Pepper.\n" +
                "• Take adequate rest and stay hydrated with warm water.\n" +
                "• Apply a cold compress if the temperature is high.");

        remedies.put("cough", "• Mix 1 tsp of honey with half tsp of ginger juice.\n" +
                "• Chew on a small piece of licorice (Mulethi) root.\n" +
                "• Gargle with warm salt water three times a day.\n" +
                "• Avoid dairy products and cold drinks until recovered.");

        remedies.put("cold", "• Inhale steam with Eucalyptus oil or Ajwain seeds.\n" +
                "• Drink warm turmeric milk (Golden Milk) before bed.\n" +
                "• Keep your chest warm and avoid exposure to cold wind.\n" +
                "• Eat light, easily digestible meals like Moong Dal Khichdi.");

        remedies.put("headache", "• Apply a paste of sandalwood or cinnamon on the forehead.\n" +
                "• Massage your scalp with warm Brahmi or Peppermint oil.\n" +
                "• Practice Anulom Vilom Pranayama for 10-15 minutes.\n" +
                "• Ensure 7-8 hours of sound sleep in a dark room.");

        remedies.put("stomach ache", "• Drink warm water with roasted Ajwain and a pinch of black salt.\n" +
                "• Apply a paste of Hing (Asafoetida) and water around the navel.\n" +
                "• Sip on Ginger tea or Peppermint tea slowly.\n" +
                "• Avoid spicy, oily, and heavy foods for 24 hours.");

        remedies.put("acidity", "• Chew on fennel seeds (Saunf) after every meal.\n" +
                "• Drink cold milk (without sugar) for instant relief.\n" +
                "• Consume Amla juice or powder daily in the morning.\n" +
                "• Avoid citrus fruits, caffeine, and spicy pickles.");

        remedies.put("insomnia", "• Massage the soles of your feet with warm Sesame oil.\n" +
                "• Drink a cup of warm milk with a pinch of nutmeg powder.\n" +
                "• Avoid using mobile or screens 1 hour before bedtime.\n" +
                "• Practice Shavasana (Corpse Pose) to relax the body.");

        remedies.put("joint pain", "• Massage the affected area with warm Mahanarayan oil.\n" +
                "• Add Turmeric and Ginger to your daily diet.\n" +
                "• Consume soaked Fenugreek seeds (Methi) in the morning.\n" +
                "• Apply a warm compress to improve blood circulation.");

        remedies.put("skin rash", "• Apply fresh Aloe Vera gel or Neem oil on the rash.\n" +
                "• Use a paste of Sandalwood and Turmeric for cooling.\n" +
                "• Bathe with lukewarm water infused with Neem leaves.\n" +
                "• Avoid harsh soaps and synthetic fabrics.");

        remedies.put("hair fall", "• Massage your scalp with Bhringraj or Amla oil.\n" +
                "• Wash hair with Reetha and Shikakai natural cleansers.\n" +
                "• Include protein-rich foods and curry leaves in your diet.\n" +
                "• Manage stress through meditation and regular exercise.");

        for (String key : remedies.keySet()) {
            if (problem.contains(key)) {
                return "Ayurvedic Treatment for " + key.toUpperCase() + ":\n\n" + remedies.get(key);
            }
        }
        return "No specific remedy found for '" + problem + "'.\n" +
                "Please consult a certified Ayurvedic practitioner for a personalized treatment plan.";
    }
}
