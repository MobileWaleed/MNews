package loovo.com.mnews.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import loovo.com.mnews.MNewsApp;
import loovo.com.mnews.R;
import loovo.com.mnews.repository.model.MySharedPreferences;
import loovo.com.mnews.repository.model.SearchFilter;
import loovo.com.mnews.utils.Const;
import loovo.com.mnews.view.adapters.SpinnerAdapter;

public class FilterSearchResultDialogFragment extends DialogFragment {

    @BindView(R.id.language_spinner)
    Spinner langSpinner;
    @BindView(R.id.btn_apply)
    Button apply;
    @BindView(R.id.btn_cancel)
    Button cancel;
    @BindView(R.id.filter_group)
    RadioGroup radioGroup;
    @BindView(R.id.btn_published_date)
    RadioButton btnPublishedDate;
    @BindView(R.id.btn_relevancy)
    RadioButton btnRelevancy;
    @BindView(R.id.btn_popularity)
    RadioButton btnPopularity;
    @BindView(R.id.keyword_input)
    EditText keyWordInput;

    @Inject
    SearchFilter searchFilter;
    @Inject
    MySharedPreferences mySharedPreferences;
    String[] langCodes;

    public FilterSearchResultDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FilterSearchResultDialogFragment newInstance(String title) {
        FilterSearchResultDialogFragment frag = new FilterSearchResultDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MNewsApp.getApp().getAppComponent().inject(this);
        View view = inflater.inflate(R.layout.filter_result_dialog, container);
        ButterKnife.bind(this, view);
        prepareSpinner();
        prepareApplyBtn();
        prepareRadioGroup();
        prepareCancelBtn();
        return view;
    }

    private void prepareCancelBtn() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void prepareRadioGroup() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_published_date:
                        searchFilter.setSortBy(Const.PUBLISHEDATKEY);
                        break;
                    case R.id.btn_relevancy:
                        searchFilter.setSortBy(Const.RELEVANCYKEY);
                        break;
                    case R.id.btn_popularity:
                        searchFilter.setSortBy(Const.POPULARITYKEY);
                        break;
                }
            }
        });
    }

    private void setRadioGroup() {
        if (searchFilter.getSortBy().equals(Const.PUBLISHEDATKEY)) {
            btnPublishedDate.setChecked(true);
        } else if (searchFilter.getSortBy().equals(Const.RELEVANCYKEY)) {
            btnRelevancy.setChecked(true);
        } else if (searchFilter.getSortBy().equals(Const.POPULARITYKEY)) {
            btnPopularity.setChecked(true);
        }

    }

    private void prepareApplyBtn() {
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = keyWordInput.getText().toString();
                searchFilter.setKeyWord(keyword);
                mySharedPreferences.putSearchFilter(searchFilter);
                dismiss();
            }
        });
    }

    private void prepareSpinner() {

        String[] langs = getResources().getStringArray(R.array.language_name_array);
        langCodes = getResources().getStringArray(R.array.language_code_array);
        langSpinner.setAdapter(new SpinnerAdapter(getActivity(), R.layout.language_spinner, langs, Const.mFlagsIds));
        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchFilter.setLang(langCodes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setSpinner() {
        langSpinner.setSelection(getPositionOfStringItemInArray(searchFilter.getLang()));
    }

    private void setKeyword() {
        keyWordInput.setText(searchFilter.getKeyWord());
    }

    private int getPositionOfStringItemInArray(String item) {
        return Arrays.asList(langCodes).indexOf(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mySharedPreferences.getSearchFilter() != null) {
            searchFilter = mySharedPreferences.getSearchFilter();
        }
        setRadioGroup();
        setSpinner();
        setKeyword();
    }

}