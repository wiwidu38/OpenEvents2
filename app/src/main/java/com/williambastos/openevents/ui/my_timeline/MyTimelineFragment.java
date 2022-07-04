package com.williambastos.openevents.ui.my_timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.williambastos.openevents.databinding.FragmentMyTimelineBinding;

public class MyTimelineFragment extends Fragment {

private FragmentMyTimelineBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentMyTimelineBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        binding.textMytimeline.setText("My TimeLine");
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}