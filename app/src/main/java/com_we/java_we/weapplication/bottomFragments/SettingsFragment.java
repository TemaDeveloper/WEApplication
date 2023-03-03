package com_we.java_we.weapplication.bottomFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import com_we.java_we.weapplication.BuildConfig;
import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.managers.SharedPrefManager;


public class SettingsFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    //widgets
    private RelativeLayout feedbackLayout, shareLayout;
    private Spinner spinnerLanguages;
    private LinearLayout quitLayout;
    private LottieAnimationView lottieSwitcher;
    //check the switcher
    private Boolean mode;
    //the spinner support
    private String language;
    private String[] languages = {"Choose the language", "English", "Français"};
    private ArrayAdapter<String> languagesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        init(view);

        languagesAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, languages);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(languagesAdapter);
        spinnerLanguages.setSelection(0);

        shareLayout.setOnClickListener(this);
        quitLayout.setOnClickListener(this);
        lottieSwitcher.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                setLanguage(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(mode == false){
            lottieSwitcher.setMinAndMaxProgress(0f, .5f);
        }else{
            lottieSwitcher.setMinAndMaxProgress(.5f, 1.0f);
        }



        return view;
    }

    private void init(View view){
        mode = SharedPrefManager.getInstance(getContext()).getMode();
        spinnerLanguages = view.findViewById(R.id.spinner_languages);
        feedbackLayout = view.findViewById(R.id.lin_feed_back);
        shareLayout = view.findViewById(R.id.lin_share_app);
        quitLayout = view.findViewById(R.id.lin_quit);
        lottieSwitcher = view.findViewById(R.id.switcher);
    }

    public void setLanguage(AdapterView<?> parent, int position) {
        String selectedLanguage = parent.getItemAtPosition(position).toString();
        if (selectedLanguage.equals("English")) {
            setLocal(getActivity(), "en");
            SharedPrefManager.getInstance(getContext()).setLanguage("English");
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }else if (selectedLanguage.equals("Français")) {
            setLocal(getActivity(), "fr");
            SharedPrefManager.getInstance(getContext()).setLanguage("Français");
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
    }

    public void setLocal(Activity activity, String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.switcher:
                if (mode == true) {
                    lottieSwitcher.setMinAndMaxProgress(0f, .5f);//.5f, 1.0f
                    lottieSwitcher.playAnimation();
                    SharedPrefManager.getInstance(getContext()).setMode(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mode = false;
                } else {
                    lottieSwitcher.setMinAndMaxProgress(.5f, 1.0f);
                    lottieSwitcher.playAnimation();
                    SharedPrefManager.getInstance(getContext()).setMode(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mode = true;
                }
                break;
            case R.id.lin_quit:
                final Dialog dialog = new Dialog(getActivity(), R.style.ThemeDialog);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.alert_dialog_quit);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                MaterialButton btnNo = dialog.findViewById(R.id.btn_no), btnYes = dialog.findViewById(R.id.btn_yes);

                btnNo.setOnClickListener(v -> dialog.dismiss());


                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().moveTaskToBack(true);
                        getActivity().finish();
                        System.exit(0);
                        dialog.cancel();
                    }
                });

                dialog.show();
                break;
            case R.id.lin_share_app:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WE app");
                    String shareMessage = "\n " + "Let me recommend" + " \n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "APP"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.lin_feed_back:
                Intent feedbackEmail = new Intent(Intent.ACTION_SEND);
                feedbackEmail.setType("text/email");
                feedbackEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"freshart666@gmail.com"});
                feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                startActivity(Intent.createChooser(feedbackEmail, "Send feedback"));
                break;
        }
    }
}