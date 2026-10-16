package com.example.horamarcada;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.horamarcada.databinding.ActivityCasoBinding;

public class CasoActivity extends AppCompatActivity {

    private static final String SOLUCAO = "Rafael";

    private ActivityCasoBinding binding;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCasoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        // O MediaPlayer é criado uma única vez, aqui no onCreate
        mediaPlayer = MediaPlayer.create(this, R.raw.transmissao_relojoaria);

        // Chamado quando o áudio termina sozinho
        mediaPlayer.setOnCompletionListener(player -> {
            player.seekTo(0);
            binding.textEstadoAudio.setText(R.string.estado_concluido);
        });

        binding.buttonReproduzir.setOnClickListener(view -> {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                binding.textEstadoAudio.setText(R.string.estado_reproduzindo);
            }
        });

        binding.buttonPausar.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                binding.textEstadoAudio.setText(R.string.estado_pausado);
            }
        });

        // Voltar ao início com pause() e seekTo(0), sem usar stop()
        binding.buttonInicio.setOnClickListener(view -> {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            binding.textEstadoAudio.setText(R.string.estado_parado);
        });

        binding.buttonRecuar.setOnClickListener(view -> {
            int novaPosicao = mediaPlayer.getCurrentPosition() - 10000;
            if (novaPosicao < 0) {
                novaPosicao = 0;
            }
            mediaPlayer.seekTo(novaPosicao);
        });

        binding.buttonAvancar.setOnClickListener(view -> {
            int novaPosicao = mediaPlayer.getCurrentPosition() + 10000;
            if (novaPosicao > mediaPlayer.getDuration()) {
                novaPosicao = mediaPlayer.getDuration();
            }
            mediaPlayer.seekTo(novaPosicao);
        });

        binding.buttonVerificar.setOnClickListener(view -> {
            String resposta = binding.editResposta.getText().toString().trim();
            String justificativa = binding.editJustificativa.getText().toString().trim();

            if (resposta.isEmpty()) {
                binding.editResposta.setError(getString(R.string.erro_resposta_vazia));
                binding.editResposta.requestFocus();
                return;
            }

            if (justificativa.length() < 20) {
                binding.editJustificativa.setError(getString(R.string.erro_justificativa));
                binding.editJustificativa.requestFocus();
                return;
            }

            // trim() tirou os espaços; equalsIgnoreCase ignora maiúsculas e minúsculas
            if (resposta.equalsIgnoreCase(SOLUCAO)) {
                binding.textResultado.setText(R.string.resultado_correto);
                binding.textResultado.setTextColor(ContextCompat.getColor(this, R.color.verde_ok));
            } else {
                binding.textResultado.setText(R.string.resultado_incorreto);
                binding.textResultado.setTextColor(ContextCompat.getColor(this, R.color.vinho));
            }
        });

        binding.buttonVoltar.setOnClickListener(view -> finish());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Se o usuário sair da tela, o áudio é pausado
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.textEstadoAudio.setText(R.string.estado_pausado);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
