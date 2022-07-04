package com.williambastos.openevents.ui.my_messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.williambastos.openevents.databinding.FragmentMyMessagesBinding;

public class MyMessagesFragment extends Fragment {

private FragmentMyMessagesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentMyMessagesBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        binding.textMytimeline.setText("My Messages");
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}