// Copyright (C) 2018 INTUZ.

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.phonenumberui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.phonenumberui.countrycode.Country;
import com.phonenumberui.countrycode.CountryCodeAdapter;
import com.phonenumberui.countrycode.CountryUtils;
import com.phonenumberui.recycler.FastScrollRecyclerViewItemDecoration;
import com.phonenumberui.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
public class CountryCodeActivity extends AppCompatActivity {
    public  String title = "";

    private AppCompatEditText etSearchCountry;
    private AppCompatTextView tvNoResult;
    private RecyclerView rvCountryCode;
    private RelativeLayout rlCountry;
    private AppCompatTextView tvToolbarTitle;
    private Activity mActivity = CountryCodeActivity.this;
    private List<Country> masterCountries = new ArrayList<>();
    private List<Country> mFilteredCountries;
    private List<Country> mTempCountries;
    private CountryCodeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code);
        Utility.hideSoftKeyboard(mActivity);
        setupUI();
        setUpToolBar();
        setUpDtata();
    }

    private void setupUI() {
        rlCountry = (RelativeLayout) findViewById(R.id.rlCountry);
        rvCountryCode = (RecyclerView) findViewById(R.id.rvCountryCode);
        etSearchCountry = (AppCompatEditText) findViewById(R.id.etSearchCountry);
        tvNoResult = (AppCompatTextView) findViewById(R.id.tvNoResult);
        rvCountryCode.setHasFixedSize(true);
        tvToolbarTitle = (AppCompatTextView) findViewById(R.id.tvToolbarTitle);

    }

    private void setUpToolBar() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(ContextCompat.getColor(mActivity, R.color.white));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(getIntent().hasExtra("TITLE")&&getIntent().getStringExtra("TITLE")!=null&& !getIntent().getStringExtra("TITLE").equalsIgnoreCase(""))
        {
            title = getIntent().getStringExtra("TITLE");
            getSupportActionBar().setTitle(title);

        }
        else

        {
            getSupportActionBar().setTitle("Select Country");

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpDtata() {

        etSearchCountry.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyQuery(s.toString());
            }
        });

        CountryCodeAdapter.Callback callback = new CountryCodeAdapter.Callback() {
            @Override
            public void onItemCountrySelected(Country country) {
                Log.e("Phone Code ", "" + country.getPhoneCode());
                Utility.hideKeyBoardFromView(mActivity);
                Intent intent = new Intent();
                intent.putExtra(AppConstant.COUNTRY, country);
                setResult(RESULT_OK, intent);
                finish();
            }
        };
        masterCountries.addAll(CountryUtils.getAllCountries(mActivity));
        this.mFilteredCountries = getFilteredCountries();
        HashMap<String, Integer> mapIndex = calculateIndexesForName(mFilteredCountries);
        mAdapter = new CountryCodeAdapter(mFilteredCountries, callback, mapIndex);


        rvCountryCode.setLayoutManager(new LinearLayoutManager(mActivity));
        rvCountryCode.setAdapter(mAdapter);
        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
        rvCountryCode.addItemDecoration(decoration);
        rvCountryCode.setItemAnimator(new DefaultItemAnimator());
    }

    private HashMap<String, Integer> calculateIndexesForName(List<Country> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getName();
            String index = name.substring(0, 1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }

    private List<Country> getFilteredCountries() {
        return getFilteredCountries("");
    }

    private void applyQuery(String query) {
        tvNoResult.setVisibility(View.GONE);
        query = query.toLowerCase();

        //if query started from "+" ignore it
        if (query.length() > 0 && query.charAt(0) == '+') {
            query = query.substring(1);
        }

        mFilteredCountries = getFilteredCountries(query);

        if (mFilteredCountries.size() == 0) {
            tvNoResult.setVisibility(View.VISIBLE);
        }

        mAdapter.notifyDataSetChanged();
    }

    private List<Country> getFilteredCountries(String query) {
        if (mTempCountries == null) {
            mTempCountries = new ArrayList<>();
        } else {
            mTempCountries.clear();
        }


        for (Country country : masterCountries) {
            if (country.isEligibleForQuery(query)) {
                mTempCountries.add(country);
            }
        }
        return mTempCountries;
    }

    private boolean isAlreadyInList(Country country, List<Country> countryList) {
        if (country != null && countryList != null) {
            for (Country iterationCountry : countryList) {
                if (iterationCountry.getIso().equalsIgnoreCase(country.getIso())) {
                    return true;
                }
            }
        }
        return false;
    }
}
