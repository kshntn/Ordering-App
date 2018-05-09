

/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    static final String quantityreset = "A";
    static final String counterreset = "B";
    int quantity = 0;
    int flag = 0;
    int counter = 0;
    TextView quantityTextView;
    TextView orderSummaryTextView;
    TextView counterTextView;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            quantity = savedInstanceState.getInt(quantityreset);
            counter = savedInstanceState.getInt(counterreset);
        }
        setContentView(R.layout.activity_main);
        display(quantity);
        displayPrice(0);
        displayMessage(NumberFormat.getCurrencyInstance().format(0), counter);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(quantityreset, quantity);
        savedInstanceState.putInt(counterreset, counter);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * This method is called when the send order button is clicked.
     */
    public void sendOrder(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getResources().getString(R.string.address))); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject) + counter);
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sendOrder, NameText(), calculatePrice(), quantity, checkStateWhipped(), checkStateChocolate()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method is called for checking the state of whippedCream button.
     */
    private boolean checkStateWhipped() {
        boolean hasWhippedCream = ((CheckBox) findViewById(R.id.checkWhipped)).isChecked();
        return hasWhippedCream;
    }

    /**
     * This method is called for checking the state of Chocolate button.
     */
    private boolean checkStateChocolate() {
        boolean hasChocolate = ((CheckBox) findViewById(R.id.checkChocolate)).isChecked();
        return hasChocolate;
    }

    /**
     * This method is called for checking the name in Edittext.
     */
    private String NameText() {
        EditText Name = findViewById(R.id.plain_text_input);
        return Name.getText().toString();
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
//        if (flag == 1) {
//            flag = 0;
//            Resetorder(view);
//        }
        if (quantity > 99) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.moreThan100), Toast.LENGTH_SHORT).show();
        } else
            quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
//        if (flag == 1) {
//            flag = 0;
//            Resetorder(view);
//        }
        if (quantity == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.decrementlessThan0), Toast.LENGTH_SHORT).show();
            displayMessage(NumberFormat.getCurrencyInstance().format(0), counter);
        } else
            quantity = quantity - 1;

        display(quantity);
    }

    /**
     * This method is called when the new order  is clicked.
     */
    public void Resetorder(View view) {
        quantity = 0;
        CheckBox hasWhippedCream = findViewById(R.id.checkWhipped);
        hasWhippedCream.setChecked(false);
        CheckBox hasChocolate = findViewById(R.id.checkChocolate);
        hasChocolate.setChecked(false);
        display(quantity);
        displayPrice(0);
        EditText Name = findViewById(R.id.plain_text_input);
        Name.setText("");
        background = findViewById(R.id.image);
        background.setImageResource(R.drawable.coffee2);
    }

    /**
     * This method is called for calculating the price
     */
    private String calculatePrice() {
        int basePrice = 5;
        if (checkStateWhipped()) {
            basePrice += 1;
        }
        if (checkStateChocolate()) {
            basePrice += 2;
        }
        int price = quantity * basePrice;
        String cost = NumberFormat.getCurrencyInstance().format(price);
        return cost;
    }

    /**
     * This method is called for creating the order summary
     */
    private String orderSummary() {
        if (checkStateWhipped()) {
            background = findViewById(R.id.image);
            background.setImageResource(R.drawable.whippedcream);
        }
        if (checkStateChocolate()) {
            background = findViewById(R.id.image);
            background.setImageResource(R.drawable.coffeechocolate);
        }
        if (checkStateChocolate() && checkStateWhipped()) {
            background = findViewById(R.id.image);
            background.setImageResource(R.drawable.chocolatewhipped);
        }
        return getResources().getString(R.string.priceMessage, NameText(), calculatePrice(), quantity, checkStateWhipped(), checkStateChocolate());
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message, int count) {
        orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        counterTextView = (TextView) findViewById(R.id.counter);
        counterTextView.setText("" + count);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method is called when the bill button is clicked.
     */
    public void submitOrder(View view) {
        if (quantity == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.lessThan0), Toast.LENGTH_SHORT).show();
            Resetorder(view);
        } else {
            counter += 1;
            flag = 1;
            displayMessage(orderSummary(), counter);
        }
    }


}