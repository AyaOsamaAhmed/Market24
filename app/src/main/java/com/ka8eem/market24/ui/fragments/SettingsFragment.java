package com.ka8eem.market24.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ka8eem.market24.R;
import com.ka8eem.market24.ui.activities.HomeActivity;
import com.ka8eem.market24.util.Constants;

import java.util.Locale;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }
    FirebaseAuth mAuth;
    TextView textLang;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CardView cardViewContainer, cardViewLogout;

    SearchView searchView;
    ImageView filterImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        searchView = (SearchView) getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        filterImage = (ImageView) getActivity().findViewById(R.id.filter_icon);
        filterImage.setVisibility(View.GONE);
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();
        cardViewLogout = view.findViewById(R.id.card_view_logout_container);
        boolean logged = preferences.getBoolean("LOGGED_IN", false);
        if (!logged)
            cardViewLogout.setVisibility(View.GONE);
        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        cardViewContainer = view.findViewById(R.id.card_view_language_container);
        cardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
        textLang = view.findViewById(R.id.lang_);
        return view;
    }

    private void showChangeLanguageDialog() {
        final String[] languages = {"EN", "AR"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.choose_language);
        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            String curLang = null;

            @Override
            public void onClick(DialogInterface dialog, int pos) {

                switch (pos) {
                    case 0:
                        setLocale("EN");
                        curLang = getString(R.string.english);
                        break;
                    case 1:
                        setLocale("AR");
                        curLang = getString(R.string.arabic_lang);
                        break;
                    default:
                        setLocale("EN");
                        break;
                }
                String lang = Constants.getLocal(getContext());
                if (lang.equals("AR"))
                    lang = getString(R.string.arabic_lang);
                else
                    lang = getString(R.string.english);
                textLang.setText(lang);
                dialog.dismiss();
                getContext().startActivity(new Intent(getContext(), HomeActivity.class));
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(configuration,
                getActivity().getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE).edit();
        editor.putString("MY_LANG", lang);
        editor.commit();
        editor.apply();
    }

    private void logout() {
        //
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        //
        editor.putBoolean("LOGGED_IN", false);
        editor.commit();
        editor.apply();
        getActivity().finish();
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}