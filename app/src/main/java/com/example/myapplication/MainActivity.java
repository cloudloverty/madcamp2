package com.example.myapplication;
//added something
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    public JSONObject userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(loginIntent, 500);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            String result = data.getStringExtra("userJsonObject");
            try{
                userData = new JSONObject(result);

                Toast.makeText(this, "Welcome "+userData.get("name")+"!", Toast.LENGTH_SHORT).show();
                Log.d("디버그", ""+userData.get("_id"));
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText("연락처"));
                tabLayout.addTab(tabLayout.newTab().setText("갤러리"));
                tabLayout.addTab(tabLayout.newTab().setText("tab 3"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                final PagerAdapter adapter = new PagerAdapter
                        (getSupportFragmentManager(), tabLayout.getTabCount());

                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) { }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) { }
                });


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static  boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}