package com.example.historymap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        }

        // all of the methods below are identical, except for the string that goes with the intent
        public void goToRestaurantsMap(View view){
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("choice", "restaurants");
            startActivity(i);
        }

        public void goToHotelsMap(View view){
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("choice", "hotels");
            startActivity(i);
        }

        public void goToHistoricalMap(View view){
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("choice", "history");
            startActivity(i);
        }

        public void goToPubsMap(View view){
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("choice", "pubs");
            startActivity(i);
        }

        public void goToCoffeeMap(View view){
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("choice", "coffee");
            startActivity(i);
        }

        public void goToShoppingMap(View view){
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("choice", "shopping");
            startActivity(i);
        }

}
