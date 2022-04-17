package com.example.shopapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.R;
import com.example.shopapp.models.MyCartModel;
import com.example.shopapp.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class DetailedActivity extends AppCompatActivity {

    TextView quantity;
    int totalQuantity =1;
    int totalPrice=0;

    ImageView detailedImg,productImg;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    TextView price,description,rating;
    Button addToCart;
    ImageView addItem,removeItem;
    Toolbar toolbar;
    MyCartModel myCartModel=null;
    ViewAllModel viewAllModel = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        toolbar=findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);}


        final Object object=getIntent().getSerializableExtra("detail");
        if(object instanceof ViewAllModel){
                viewAllModel=(ViewAllModel) object;
        }

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = auth.getCurrentUser();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        quantity=findViewById(R.id.quantity);
        detailedImg=findViewById(R.id.detailed_img);
        productImg=findViewById(R.id.product_image1);
        addItem=findViewById(R.id.add_item);
        removeItem=findViewById(R.id.remove_item);
        price=findViewById(R.id.detailed_price);
        description=findViewById(R.id.detailed_dec);
        rating=findViewById(R.id.detailed_rating);

        if(viewAllModel !=null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);
            rating.setText(viewAllModel.getRating());
            description.setText(viewAllModel.getDescription());
            price.setText("Price: $"+viewAllModel.getPrice()+ "/kg");
            totalPrice=viewAllModel.getPrice() * totalQuantity;

            if(viewAllModel.getType().equals("egg")){
                price.setText("Price: $"+viewAllModel.getPrice()+ "/dozen");
                totalPrice=viewAllModel.getPrice() * totalQuantity;

            }
            if(viewAllModel.getType().equals("milk")){
                price.setText("Price: $"+viewAllModel.getPrice()+ "/litre");
                totalPrice=viewAllModel.getPrice() * totalQuantity;

            }

        }
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity<10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice=viewAllModel.getPrice() * totalQuantity;

                }

            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>0){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice=viewAllModel.getPrice() * totalQuantity;

                }


            }
        });
        addToCart=findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });




    }

    private void addedToCart() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MM,dd,yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("productName",viewAllModel.getName());
        cartMap.put("productImage",viewAllModel.getImg_url());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);


        firestore.collection("AddToCart").document( Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Added to a cart", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


    }
}