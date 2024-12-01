package com.example.discountcalcbasic;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvOutput, tvDiscount;
    EditText etValue, etDiscount;
    Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvOutput = findViewById(R.id.tvOutput);
        tvDiscount = findViewById(R.id.tvDiscount);
        etDiscount = findViewById(R.id.etDiscount);
        etValue = findViewById(R.id.etValue);
        btnCalculate = findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize variables for calculation
                double units, rebate, totalCharges = 0, finalCost;

                try {
                    // Get the values for units and rebate from EditText fields
                    units = Double.parseDouble(etValue.getText().toString());
                    rebate = Double.parseDouble(etDiscount.getText().toString());

                    // Validate rebate percentage (0% to 5%)
                    if (rebate < 0 || rebate > 5) {
                        Toast.makeText(getApplicationContext(), "Rebate must be between 0% and 5%", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Calculate the total charges based on the units
                    if (units > 0) {
                        // First 200 kWh
                        double block1 = Math.min(units, 200);
                        totalCharges += block1 * 0.218;
                        units -= block1;

                        // Next 100 kWh
                        if (units > 0) {
                            double block2 = Math.min(units, 100);
                            totalCharges += block2 * 0.334;
                            units -= block2;
                        }

                        // Next 300 kWh
                        if (units > 0) {
                            double block3 = Math.min(units, 300);
                            totalCharges += block3 * 0.516;
                            units -= block3;
                        }

                        // Above 600 kWh
                        if (units > 0) {
                            totalCharges += units * 0.546;
                        }
                    }

                    // Round the total charges to 2 decimal places before applying the rebate
                    totalCharges = Math.round(totalCharges * 100.0) / 100.0;

                    // Apply rebate (subtract the rebate percentage)
                    finalCost = totalCharges - (totalCharges * rebate / 100.0);

                    // Round the final cost to 2 decimal places
                    finalCost = Math.round(finalCost * 100.0) / 100.0;

                    // Display the result
                    tvOutput.setText(String.format("RM %.2f", finalCost));
                    tvDiscount.setText(String.format("%.2f%%", rebate));

                } catch (NumberFormatException nfe) {
                    // Handle input errors
                    Toast.makeText(getApplicationContext(), "Please enter both value and discount", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            // Start About Activity
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (selected == R.id.menuSettings) {
            // Handle Settings action
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
