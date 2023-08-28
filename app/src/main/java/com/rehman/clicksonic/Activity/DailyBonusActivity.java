package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
import com.google.rpc.context.AttributeContext;
import com.rehman.clicksonic.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyBonusActivity extends AppCompatActivity {

    ImageView spin_Image;

    LuckyWheel lucky_wheel;
    List<WheelItem> wheelItemList = new ArrayList<>();
    String points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_bonus);

        spin_Image = findViewById(R.id.spin_Image);

        luckyWheel();

        // creating an object of Random class
        // to generate random numbers for the spin
        Random random = new Random();

        // on click listener for btnSpin
        spin_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disabling the button so that user
                // should not click on the button
                // while the wheel is spinning
//                        btnSpin.setEnabled(false);

                // reading random value between 10 to 30
//                        int spin = random.nextInt(20)+10;

                // since the wheel has 10 divisions, the
                // rotation should be a multiple of
                // 360/10 = 36 degrees
//                        spin = spin * 36;

                // timer for each degree movement
//                timer = new CountDownTimer(spin*20,1) {
//                    @Override
//                    public void onTick(long l) {
//                        // rotate the wheel
//                        float rotation = ivWheel.getRotation() + 2;
//                        ivWheel.setRotation(rotation);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        // enabling the button again
//                        btnSpin.setEnabled(true);
//                    }
//                }.start();

                points = String.valueOf(random.nextInt(10));
                if (points.equals("0"))
                {
                    points = String.valueOf(1);
                }
                lucky_wheel.rotateWheelTo(Integer.parseInt(points));

            }
        });
    }

    private void luckyWheel()
    {
        lucky_wheel = findViewById(R.id.lucky_wheel);

        WheelItem wheelItem1 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.light_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"0");

        wheelItemList.add(wheelItem1);

        WheelItem wheelItem2 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.normal_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"15");

        wheelItemList.add(wheelItem2);

        WheelItem wheelItem3 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.light_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"25");

        wheelItemList.add(wheelItem3);

        WheelItem wheelItem4 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.normal_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"50");

        wheelItemList.add(wheelItem4);

        WheelItem wheelItem5 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.light_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"75");

        wheelItemList.add(wheelItem5);

        WheelItem wheelItem6 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.normal_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"100");

        wheelItemList.add(wheelItem6);

        WheelItem wheelItem7 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.light_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"150");

        wheelItemList.add(wheelItem7);

        WheelItem wheelItem8 = new WheelItem(ResourcesCompat.getColor(getResources(),R.color.normal_golden,null),
                BitmapFactory.decodeResource(getResources(),R.drawable.dollar),"200");

        wheelItemList.add(wheelItem8);


        lucky_wheel.addWheelItems(wheelItemList);

        lucky_wheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                WheelItem selectedItems =  wheelItemList.get(Integer.parseInt(points)-1);
                String point_amount = selectedItems.text;

                Toast.makeText(DailyBonusActivity.this, ""+point_amount, Toast.LENGTH_SHORT).show();
            }
        });

    }
}