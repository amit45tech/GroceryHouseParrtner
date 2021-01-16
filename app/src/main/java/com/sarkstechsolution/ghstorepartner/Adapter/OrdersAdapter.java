package com.sarkstechsolution.ghstorepartner.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sarkstechsolution.ghstorepartner.CanceledOrderFragment;
import com.sarkstechsolution.ghstorepartner.CompletedOrdersFragment;
import com.sarkstechsolution.ghstorepartner.PendingOrdersFragment;
import com.sarkstechsolution.ghstorepartner.RejectedOrderFragment;

public class OrdersAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public OrdersAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                PendingOrdersFragment pendingOrdersFragment = new PendingOrdersFragment();
                return pendingOrdersFragment;
            case 1:
                CompletedOrdersFragment completedOrdersFragment =  new CompletedOrdersFragment();
                return completedOrdersFragment;
            case 2:
                CanceledOrderFragment canceledOrderFragment =  new CanceledOrderFragment();
                return canceledOrderFragment;
            case 3:
                RejectedOrderFragment rejectedOrderFragment =  new RejectedOrderFragment();
                return rejectedOrderFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
