package com.id05.asteriskcallmedisa;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AmiPhonePageAdapter extends FragmentPagerAdapter {

    private final int numOfTabs;

    AmiPhonePageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                CallsFragment frag = new CallsFragment();
                frag.setArguments(bundle);
                return frag;
            case 1:
                return new PhonebookFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}