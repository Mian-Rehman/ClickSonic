package com.rehman.clicksonic.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rehman.clicksonic.PurchaseFrag.CoinsPurhaseFragment;
import com.rehman.clicksonic.PurchaseFrag.PointsPurhaseFragment;


public class PurchaseTabsAdapter extends FragmentStateAdapter {



    public PurchaseTabsAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new CoinsPurhaseFragment();
                break;

//            case 1:
//                fragment = new PointsPurhaseFragment();
//                break;


        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
