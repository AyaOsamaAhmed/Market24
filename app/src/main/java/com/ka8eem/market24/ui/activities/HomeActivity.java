package com.ka8eem.market24.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ka8eem.market24.R;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.ui.fragments.AddProductFragment;
import com.ka8eem.market24.ui.fragments.AllChatsFragment;
import com.ka8eem.market24.ui.fragments.FavouriteFragment;
import com.ka8eem.market24.ui.fragments.HomeFragment;
import com.ka8eem.market24.ui.fragments.MyAdsFragment;
import com.ka8eem.market24.ui.fragments.ProfileFragment;
//import com.ka8eem.market24.ui.fragments.SearchFragment;
import com.ka8eem.market24.ui.fragments.SettingsFragment;
import com.ka8eem.market24.util.Constants;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Menu myMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_home);
        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {
        preferences = getSharedPreferences(Constants.SHARED, MODE_PRIVATE);
        editor = preferences.edit();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
//        toggle = new ActionBarDrawerToggle(this, drawerLayout,
//                R.string.open_action_bar_toggle, R.string.close_action_bar_toggle);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            setTitle("");
        } else {
            String title = savedInstanceState.getString("title");
            setTitle(title);
        }
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user_name);
        CircleImageView user_image = (CircleImageView) headerView.findViewById(R.id.user_image);

        MenuItem logout_item =  navigationView.getMenu().findItem(R.id.nav_logout);

        boolean logged = preferences.getBoolean("LOGGED_IN", false);
        if (!logged){
            navUsername.setVisibility(View.GONE);
            logout_item.setVisible(false);
        }else{

            UserModel userModel = Constants.getUser(getApplicationContext());
            navUsername.setText(userModel.getUserName());
            navUsername.setVisibility(View.VISIBLE);
            logout_item.setVisible(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem logout_item = menu.findItem(R.id.nav_logout);
        boolean logged = preferences.getBoolean("LOGGED_IN", false);
        if (!logged)
        {
            logout_item.setVisible(false);
        }
        else
        {
            logout_item.setVisible(true);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem logout_item = menu.findItem(R.id.nav_logout);
        boolean logged = preferences.getBoolean("LOGGED_IN", false);
        if (!logged)
        {
            logout_item.setVisible(false);
        }
        else
        {
            logout_item.setVisible(true);
        }
        return true;
    }
    private void loadLocale() {
        SharedPreferences pref = getSharedPreferences(Constants.SHARED, MODE_PRIVATE);
        String lang = pref.getString("MY_LANG", "NOT");
        if (!lang.equals("NOT"))
            setLocale(lang);
        else
            setLocale("AR");
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE).edit();
        editor.putString("MY_LANG", lang);
        editor.commit();
        editor.apply();
    }

    public void openDrawer(View view) {
        String lang = Constants.getLocal(this);
        if (lang.equals("AR"))
            drawerLayout.openDrawer(Gravity.RIGHT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
    }

    private void selectItemDrawer(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
//            case R.id.nav_search:
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new SearchFragment()).addToBackStack(null).commit();
//                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_logout:
                    logout();
                break;
            case R.id.nav_add: {

                if (!checkRegister())
                    startRegisterActivity();
                else if (!checkLoggedIn())
                    startLoginActivity();
                else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new AddProductFragment()).addToBackStack(null).commit();
                }
                break;
            }
            case R.id.nav_favourite:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FavouriteFragment()).addToBackStack(null).commit();
                break;
           /* case R.id.nav_notification:
                if (!checkRegister())
                    startRegisterActivity();
                else if (!checkLoggedIn())
                    startLoginActivity();
                else
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new AllChatsFragment()).addToBackStack(null).commit();
                break;*/
            case R.id.nav_profile:
                if (!checkRegister())
                    startRegisterActivity();
                else if (!checkLoggedIn())
                    startLoginActivity();
                else
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_ads:
                if (!checkRegister())
                    startRegisterActivity();
                else if (!checkLoggedIn())
                    startLoginActivity();
                else
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new MyAdsFragment()).addToBackStack(null).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment()).commit();

                break;

        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    private void logout() {
        //
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        //
        editor.putBoolean("LOGGED_IN", false);
        editor.commit();
        editor.apply();
         finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean checkRegister() {
        boolean ret = preferences.getBoolean("REGISTERED", false);
        return ret;
    }

    private boolean checkLoggedIn() {
        boolean ret = preferences.getBoolean("LOGGED_IN", false);
        return ret;
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemDrawer(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            if (toggle.onOptionsItemSelected(item))
                return true;
            return false;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", getTitle().toString());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}