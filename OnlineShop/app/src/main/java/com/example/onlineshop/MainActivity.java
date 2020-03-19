package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SELECTED_CATEGORY = "";
    public static final String FILE_NAME = "products.txt";

    private PreferenceManager preferenceFragment;

    ListView myListView;
    ArrayList<Product> productsArray;
    TextView textViewDescription;
    TextView textViewDescriptionTitle;
    Spinner categoriesSpinner;
    int count = 0;

    private void GetItems()
    {
        //TVs
        productsArray.add(new Product("SMART TV SAMSUNG 32inch", "Specificatii TV", 1350, 5040));
        productsArray.add(new Product("LG TV 101cm", "Specificatii TV", 1000, 4500));
        productsArray.add(new Product("Panasonic TV 81cm", "Specificatii TV", 899, 6105));

        //Phones
        productsArray.add(new Product("Iphone 6", "Specificatii Iphone 6", 400, 122));
        productsArray.add(new Product("Iphone 6s", "Specificatii Iphone 6s", 600, 125));
        productsArray.add(new Product("Iphone 7", "Specificatii Iphone 7", 900, 131));
        productsArray.add(new Product("Iphone 8", "Specificatii Iphone 8", 1300, 145));
        productsArray.add(new Product("Iphone XR", "Specificatii Iphone XR", 2800, 160));
        productsArray.add(new Product("Iphone X", "Specificatii Iphone X", 3200, 160));
        productsArray.add(new Product("Iphone XS", "Specificatii Iphone XS", 3500, 160));
        productsArray.add(new Product("Iphone 11", "Specificatii Iphone 11", 4100, 190));
        productsArray.add(new Product("Iphone 11 PRO", "Specificatii Iphone 11 PRO", 4750,200));
        productsArray.add(new Product("Iphone 11 PRO MAX", "Specificatii Iphone 11 PRO MAX", 5400, 210));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceFragment = new PreferenceManager(this);

        // spinner settings
        categoriesSpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
        categoriesSpinner.setOnItemSelectedListener(this);

        loadSelectedCategory();

        // listView settings
        myListView = findViewById(R.id.listview);

        productsArray = new ArrayList<>();

        GetItems();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productsArray);
        myListView.setAdapter(arrayAdapter);

        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDescriptionTitle = findViewById(R.id.textViewDescriptionTitle);
        textViewDescriptionTitle.setTypeface(null, Typeface.BOLD);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "index " + position + " with title " + productsArray.get(position).getName(), Toast.LENGTH_SHORT).show();

                textViewDescriptionTitle.setText(productsArray.get(position).getName());
                textViewDescription.setText(productsArray.get(position).getDescription());

                textViewDescriptionTitle.setVisibility(View.VISIBLE);
                textViewDescription.setVisibility(View.VISIBLE);
            }
        });
        registerForContextMenu(myListView);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listview_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setingsImgId:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.Share:
                return true;

            case R.id.About:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() method ");
    }

    String TAG = "Life cycle method";
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume() method");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() method ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() method ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() method ");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //save selected category
        saveSelectedCategory();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveSelectedCategory()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SELECTED_CATEGORY, categoriesSpinner.getSelectedItemPosition());

        editor.apply();

        saveSelectedItemToInternalStorage();
    }

    public void loadSelectedCategory()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int indexCategory = sharedPreferences.getInt(SELECTED_CATEGORY, 0);

        categoriesSpinner.setSelection(indexCategory);

        loadSelectedItemFromInternalStorage();
    }

    public void saveSelectedItemToInternalStorage()
    {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(categoriesSpinner.getSelectedItem().toString().getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadSelectedItemFromInternalStorage()
    {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine()) != null)
            {
                sb.append(text).append("\n");
            }

            Toast.makeText(this, "Loaded category: " +  sb.toString(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fis != null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

/*
    public void switchOffEvent(View v)
    {
        androidx.preference.SwitchPreference preference = preferenceFragment.findPreference("theme");
        preference.setSummaryOff("Switch off");
        preference.setSummaryOn("Switch on");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isChecked = sharedPreferences.getBoolean("theme", false);
        Toast.makeText(this, "isChecked: " + isChecked, Toast.LENGTH_SHORT).show();
    }

    public void switchOnEvent(View v)
    {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }*/
}
