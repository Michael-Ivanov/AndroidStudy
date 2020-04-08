package com.android.coffee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button buttonMinus, buttonPlus, buttonOrder;
    TextView quantityField, totalField;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalField = findViewById(R.id.totalField);

        buttonMinus = findViewById(R.id.buttonMinus);
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt((String) quantityField.getText());
                quantity--;
                if(quantity < 0) quantity = 0;
                quantityField.setText(String.valueOf(quantity));
                String s = "Total: $" + String.valueOf(quantity * 2);
                totalField.setText(s);
            }
        });

        buttonPlus = findViewById(R.id.buttonPlus);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt((String) quantityField.getText());
                quantity++;
                if(quantity > 20) quantity = 20;
                quantityField.setText(String.valueOf(quantity));
                String s = "Total: $" + String.valueOf(quantity * 2);
                totalField.setText(s);
            }
        });
        quantityField = findViewById(R.id.quantityField);
        buttonOrder = findViewById(R.id.orderButton);
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "Total: $" + String.valueOf(quantity * 2) + "\nThank you!";
                totalField.setText(s);
            }
        });

    }
}
