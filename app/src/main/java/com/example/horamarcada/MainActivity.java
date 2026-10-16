package com.example.horamarcada;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.horamarcada.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int margem = (int) (16 * getResources().getDisplayMetrics().density); // 16dp em pixels
            v.setPadding(barras.left + margem, barras.top + margem,
                    barras.right + margem, barras.bottom + margem);
            return insets;
        });

        // Mostra 0% assim que a tela abre
        binding.textValorProntidao.setText(getString(R.string.valor_prontidao, 0));

        // Atualiza o percentual em tempo real enquanto a SeekBar é arrastada
        binding.seekProntidao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textValorProntidao.setText(getString(R.string.valor_prontidao, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Limpar: volta o formulário ao estado inicial
        binding.buttonLimpar.setOnClickListener(view -> {
            binding.editCodinome.setText("");
            binding.editCodigoAcesso.setText("");
            binding.editCodinome.setError(null);
            binding.editCodigoAcesso.setError(null);
            binding.seekProntidao.setProgress(0);
            binding.editCodinome.requestFocus();
        });

        binding.buttonAcessar.setOnClickListener(view -> {
            String codinome = binding.editCodinome.getText().toString().trim();
            String codigo = binding.editCodigoAcesso.getText().toString().trim();
            int prontidao = binding.seekProntidao.getProgress();

            if (codinome.length() < 3) {
                binding.editCodinome.setError(getString(R.string.erro_codinome));
                binding.editCodinome.requestFocus();
                return;
            }

            if (!codigo.equals("042")) {
                binding.editCodigoAcesso.setError(getString(R.string.erro_codigo));
                binding.editCodigoAcesso.requestFocus();
                return;
            }

            if (prontidao < 50) {
                Toast.makeText(this, R.string.erro_prontidao, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, R.string.msg_acesso_liberado, Toast.LENGTH_SHORT).show();
            // Intent explícita: abre a tela do dossiê
            Intent intent = new Intent(MainActivity.this, CasoActivity.class);
            startActivity(intent);
        });
    }
}
