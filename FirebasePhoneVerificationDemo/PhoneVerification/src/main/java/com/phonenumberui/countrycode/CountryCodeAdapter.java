// Copyright (C) 2018 INTUZ.

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.phonenumberui.countrycode;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phonenumberui.R;
import com.phonenumberui.recycler.FastScrollRecyclerViewInterface;

import java.util.HashMap;
import java.util.List;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.CountryCodeViewHolder> implements FastScrollRecyclerViewInterface {

    private List<Country> mCountries;
    private Callback mCallback;
    private HashMap<String, Integer> mMapIndex;

    public interface Callback {
        void onItemCountrySelected(Country country);
    }

    public CountryCodeAdapter(List<Country> countries, Callback callback, HashMap<String, Integer> mapIndex) {
        this.mCountries = countries;
        this.mCallback = callback;
        mMapIndex = mapIndex;
    }

    @Override
    public CountryCodeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View rootView = inflater.inflate(R.layout.item_country, viewGroup, false);
        return new CountryCodeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CountryCodeViewHolder viewHolder, final int i) {
        final int position = viewHolder.getAdapterPosition();
        viewHolder.setCountry(mCountries.get(position));
        viewHolder.rlyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onItemCountrySelected(mCountries.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    class CountryCodeViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlyMain;
        TextView tvName, tvCode;
        ImageView imvFlag;
        LinearLayout llyFlagHolder;
        View viewDivider;

        CountryCodeViewHolder(View itemView) {
            super(itemView);
            rlyMain = (RelativeLayout) itemView;
            tvName = (TextView) rlyMain.findViewById(R.id.country_name_tv);
            tvCode = (TextView) rlyMain.findViewById(R.id.code_tv);
            imvFlag = (ImageView) rlyMain.findViewById(R.id.flag_imv);
            llyFlagHolder = (LinearLayout) rlyMain.findViewById(R.id.flag_holder_lly);
            viewDivider = rlyMain.findViewById(R.id.preference_divider_view);
        }

        private void setCountry(Country country) {
            if (country != null) {
                viewDivider.setVisibility(View.GONE);
                tvName.setVisibility(View.VISIBLE);
                tvCode.setVisibility(View.VISIBLE);
                llyFlagHolder.setVisibility(View.VISIBLE);
                String countryNameAndCode = tvName.getContext()
                        .getString(R.string.country_name_and_code, country.getName(),
                                country.getIso().toUpperCase());
                tvName.setText(countryNameAndCode);
                tvCode.setText(
                        tvCode.getContext().getString(R.string.phone_code, country.getPhoneCode()));
                imvFlag.setImageResource(CountryUtils.getFlagDrawableResId(country.getIso()));

            } else {
                viewDivider.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.GONE);
                tvCode.setVisibility(View.GONE);
                llyFlagHolder.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }
}

