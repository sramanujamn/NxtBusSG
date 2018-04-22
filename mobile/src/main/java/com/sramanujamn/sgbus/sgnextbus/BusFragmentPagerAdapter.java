package com.sramanujamn.sgbus.sgnextbus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BusFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = BusFragmentPagerAdapter.class.getSimpleName();

    public static final int INDEX_NEARBY_BUSSTOPS = 0;
    public static final int INDEX_BUS_LIST = 1;
    public static final int INDEX_BUS_DETAILS = 2;

    private static final String[] tabTitles = new String[] {"Nearby", "Buses", "Details"};

    private Bundle bundle;

    public BusFragmentPagerAdapter(FragmentManager fragmentManager, Bundle bundle) {
        super(fragmentManager);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case INDEX_NEARBY_BUSSTOPS:
                return new NearbyBusStopsFragment();
            case INDEX_BUS_LIST:
                Fragment fragment = new BusArrivalListFragment();
                return fragment;
            case INDEX_BUS_DETAILS:
                return new BusDetailsFragment();
            default:
                return new NearbyBusStopsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
