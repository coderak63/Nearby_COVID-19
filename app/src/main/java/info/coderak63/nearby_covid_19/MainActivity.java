package info.coderak63.nearby_covid_19;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import info.coderak63.nearby_covid_19.BuildConfig;
import info.coderak63.nearby_covid_19.R;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //InterstitialAd mInterstitialAd,mInterstitialAd2;
    //Random rand = new Random();
    //int random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2154187598034473/7857491602");
        mInterstitialAd.loadAd(new AdRequest.Builder().
                build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                // perform your task after the ad is closed
                Fragment fragment = null;
                fragment = new Menu2();

                //replacing the fragment
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                // load the ad again if you need to show it again
                mInterstitialAd.loadAd(new AdRequest.Builder().
                        build());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });



        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId("ca-app-pub-2154187598034473/8591988778");
        mInterstitialAd2.loadAd(new AdRequest.Builder().
                build());

        mInterstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                // perform your task after the ad is closed
                Fragment fragment = null;
                fragment = new Menu3();

                //replacing the fragment
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                // load the ad again if you need to show it again
                mInterstitialAd2.loadAd(new AdRequest.Builder().
                        build());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });



         */



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_menu1);
    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
*/


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent myIntent = new Intent(MainActivity.this,About.class);
            MainActivity.this.startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
                fragment = new Menu1();
                break;
            case R.id.nav_menu2:
                /*
                random=rand.nextInt(20);
                if (mInterstitialAd.isLoaded()&&random>14) {
                    mInterstitialAd.show();
                } else {
                    fragment = new Menu2();
                }

                 */
                fragment = new Menu2();
                break;
            case R.id.nav_menu3:
                /*
                random=rand.nextInt(20);
                if (mInterstitialAd2.isLoaded()&&random<6) {
                    mInterstitialAd2.show();
                } else {
                    fragment = new Menu3();
                }

                 */
                fragment = new Menu3();
                break;
            case R.id.nav_share:
                try{
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"NearbyCOVID-19");
                    String shareMessage = "\nTry out this amazing application\n\n";
                    shareMessage+="https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID+"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                    startActivity(Intent.createChooser(shareIntent,"Choose one"));

                }catch (Exception e)
                {

                }
                break;
            case R.id.nav_rate:
                Uri uri =Uri.parse("market://details?id="+ getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW,uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_DOCUMENT|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try{
                    startActivity(goToMarket);
                }catch(ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
                }
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }


}