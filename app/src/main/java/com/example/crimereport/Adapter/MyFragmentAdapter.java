package com.example.crimereport.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.crimereport.closeCasesFragment;
import com.example.crimereport.openCasesFragment;

public class MyFragmentAdapter extends FragmentStateAdapter {


    public MyFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager,lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 2)
        {
            return new closeCasesFragment();
        }
        return new openCasesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
