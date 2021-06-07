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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.ka8eem.market24.databinding.ActivityProductDetailsBinding;
import com.ka8eem.market24.databinding.FragmentSettingsBinding;
import com.ka8eem.market24.models.TermsModel;
import com.ka8eem.market24.ui.activities.HomeActivity;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.viewmodel.ProductViewModel;
import com.ka8eem.market24.viewmodel.UserViewModel;

import java.util.Locale;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FragmentSettingsBinding binding ;
    LinearLayout toolbar ;
    String[] arraySpinner ;
    UserViewModel userViewModel;
    String curLang ;
    NavController navController ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentSettingsBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.GONE);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.GetTerms();
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();

        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    navController.navigate(R.id.HomeFragment);
                    return true;
                }
                return false;
            }
        } );
        curLang = Constants.getLocal(getContext());
        if (curLang.equals("AR"))
        arraySpinner = new String[] {getString(R.string.arabic_lang), getString(R.string.english)};
        else
            arraySpinner = new String[] {getString(R.string.english), getString(R.string.arabic_lang)};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.languageSpinner.setAdapter(adapter);


        binding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1 ){
                    if (curLang.equals("AR"))
                        setLocale("EN");
                    else
                        setLocale("AR");

                    getContext().startActivity(new Intent(getContext(), HomeActivity.class));
                    getActivity().finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        userViewModel.term.observe(getActivity(), new Observer<TermsModel>() {
            @Override
            public void onChanged(TermsModel termsModel) {

                binding.termsCondition.setText(termsModel.getAr_terms());
            }
        });
                return binding.getRoot();
    }

    private void showChangeLanguageDialog(int pos) {
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