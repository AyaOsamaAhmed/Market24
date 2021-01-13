package com.ka8eem.market24.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.ReportModel;
import com.ka8eem.market24.viewmodel.ProductViewModel;

public class ReportDialog extends AppCompatDialogFragment {


    AlertDialog alertDialog;
    RadioGroup radioGroup;
    EditText editText;
    String reason, additionalReason;
    int adsId;

    public ReportDialog(int adsId) {
        this.adsId = adsId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_report, null);

        radioGroup = view.findViewById(R.id.report_radio_btn);
        radioGroup.check(R.id.report_reason1);
        reason = getString(R.string.reason1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.report_reason1:
                        reason = getString(R.string.reason1);
                        break;
                    case R.id.report_reason2:
                        reason = getString(R.string.reason2);
                        break;
                    case R.id.report_reason3:
                        reason = getString(R.string.reason3);
                        break;
                    case R.id.report_reason4:
                        reason = getString(R.string.reason4);
                        break;
                    default:
                        reason = getString(R.string.reason1);
                        break;
                }
            }
        });

        editText = view.findViewById(R.id.edit_text_additional_reason);
        final ProductViewModel productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(getString(R.string.choose_reason_for_report))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO post the report here
                        Log.e("ads_id", adsId + "");
                        additionalReason = editText.getText().toString();
                        if (additionalReason != null && !additionalReason.equals(""))
                            reason += ", " + additionalReason;
                        Log.e("reason", reason);
                        ReportModel model = new ReportModel(adsId, reason);
                        productViewModel.reportAds(model);
                        productViewModel.mutableReportAds.observe(getActivity(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (getActivity() != null) {
                                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

        alertDialog = builder.create();
        return alertDialog;
    }
}
