package com.example.sensore_android_app.ui.home.ui.dashboard;

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
import com.example.sensore_android_app.databinding.FragmentDashboardBinding;
import com.example.sensore_android_app.ui.home.HumedadActivity;
import com.example.sensore_android_app.ui.home.LuminosidadActivity;
import com.example.sensore_android_app.ui.home.TemperaturaActivity;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    AppCompatImageView imageButtonHum;
    AppCompatImageView imageButtonTem;
    AppCompatImageView imageButtonLum;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageButtonHum = root.findViewById(R.id.btnHumedad);
        imageButtonTem = root.findViewById(R.id.btnTemperatura);
        imageButtonLum = root.findViewById(R.id.btnLuminosidad);

        imageButtonHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HumedadActivity.class));
            }
        });

        imageButtonTem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TemperaturaActivity.class));
            }
        });

        imageButtonLum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LuminosidadActivity.class));
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