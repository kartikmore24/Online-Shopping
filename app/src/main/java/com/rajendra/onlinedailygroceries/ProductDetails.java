package com.rajendra.onlinedailygroceries;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetails extends AppCompatActivity implements PaymentResultListener {
    Button buynow;
    ImageView img, back;
    TextView proName, proPrice, proDesc, proQty, proUnit;

    String name, price, desc, qty, unit;
    int image, price1, final_price;
    //first
    //second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Checkout.preload(getApplicationContext());
        buynow = (Button)findViewById(R.id.button);
        Intent i = getIntent();

         name = i.getStringExtra("name");
         image = i.getIntExtra("image", R.drawable.b1);
         price = i.getStringExtra("price");
         desc = i.getStringExtra("desc");
         qty = i.getStringExtra("qty");
         unit = i.getStringExtra("unit");

         proName = findViewById(R.id.productName);
         proDesc = findViewById(R.id.prodDesc);
         proPrice = findViewById(R.id.prodPrice);
         img = findViewById(R.id.big_image);
         back = findViewById(R.id.back2);
         proQty = findViewById(R.id.qty);
         proUnit = findViewById(R.id.unit);

         proName.setText(name);
         proPrice.setText(price);
         proDesc.setText(desc);
         proQty.setText(qty);
         proUnit.setText(unit);

        price1 = Integer.parseInt(price.replaceAll("[\\D]",""));
        final_price = price1 * 100;


        img.setImageResource(image);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProductDetails.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makepayment();

            }
        });

    }

    private void makepayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_XL0Uu6jLEdZWwI");
        /**
         * Instantiate Checkout
         */


        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", name);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", final_price);//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Intent intent = new Intent(ProductDetails.this, payment_failed.class);
        startActivity(intent);

    }

    @Override
    public void onPaymentError(int i, String s) {
        Intent intent1 = new Intent(ProductDetails.this, payment_successful.class);
        startActivity(intent1);


    }
}



