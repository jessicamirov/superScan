package com.example.barcode;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<ItemSample> itemSample = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.searchBar);
        TextView itemTxt = findViewById(R.id.itemTxt);
        readItemsData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    // Validate and parse barcode
                    int barcode = Integer.parseInt(query.trim());
                    ItemSample myItem = getItemByBarcode(barcode);

                    if (myItem != null) {
                        String itemDetails = String.format(
                                "Barcode: %d\nName: %s\nPrice: %.2f",
                                myItem.getBarcode(), myItem.getProductName(), myItem.getPrice());
                        itemTxt.setText(itemDetails);
                        return true;
                    } else {
                        itemTxt.setText("Item not found.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    itemTxt.setText("Invalid barcode. Please enter numbers only.");
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Toast.makeText(this, "Welcome to the Barcode Scanner!", Toast.LENGTH_LONG).show();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Reads the CSV file containing item data and populates the item list.
     */
    private void readItemsData() {
        try (InputStream is = getResources().openRawResource(R.raw.items);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            reader.readLine(); // Skip the header line
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                // Ensure the line has enough data
                if (tokens.length < 3) {
                    Log.w("CSVParser", "Skipping invalid row: " + line);
                    continue;
                }

                try {
                    ItemSample sample = new ItemSample(
                            Integer.parseInt(tokens[0].trim()),  // Barcode
                            tokens[1].trim(),                   // Product Name
                            Float.parseFloat(tokens[2].trim())  // Price
                    );
                    itemSample.add(sample);
                } catch (NumberFormatException e) {
                    Log.w("CSVParser", "Error parsing row: " + line, e);
                }
            }
        } catch (IOException e) {
            Log.e("CSVParser", "Error reading CSV file", e);
        }
    }

    /**
     * Finds an item by its barcode.
     * @param barcode The barcode to search for.
     * @return The corresponding ItemSample object or null if not found.
     */
    private ItemSample getItemByBarcode(int barcode) {
        for (ItemSample item : itemSample) {
            if (item.getBarcode() == barcode) {
                return item;
            }
        }
        return null;
    }
}
