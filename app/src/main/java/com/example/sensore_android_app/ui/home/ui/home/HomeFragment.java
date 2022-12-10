package com.example.sensore_android_app.ui.home.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.databinding.FragmentHomeBinding;
import com.example.sensore_android_app.ui.home.HumedadActivity;
import com.example.sensore_android_app.ui.home.LuminosidadActivity;
import com.example.sensore_android_app.ui.home.TemperaturaActivity;


public class HomeFragment extends Fragment {

    AppCompatImageView imageButtonHum;
    AppCompatImageView imageButtonTem;
    AppCompatImageView imageButtonLum;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageButtonHum = root.findViewById(R.id.btnHumedad);
        imageButtonTem = root.findViewById(R.id.btnTemperatura);
        imageButtonLum = root.findViewById(R.id.btnLuminosidad);

        imageButtonHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HumedadActivity.class);
                intent.putExtra("id", "BOX1");
                startActivity(intent);
            }
        });

        imageButtonTem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TemperaturaActivity.class);
                intent.putExtra("id", "BOX1");
                startActivity(intent);
            }
        });

        imageButtonLum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LuminosidadActivity.class);
                intent.putExtra("id", "BOX1");
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}