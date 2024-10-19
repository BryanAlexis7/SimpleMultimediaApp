package com.example.simplemultimediaapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;  // Variable para controlar el estado de reproducción de audio
    private boolean isVideoPlaying = false;  // Variable para controlar el estado de reproducción de video
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicitar permisos en tiempo de ejecución
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        // Animación de imagen
        ImageView imageView = findViewById(R.id.imageView);
        Button animateButton = findViewById(R.id.animateButton);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animateButton.setOnClickListener(v -> imageView.startAnimation(fadeInAnimation));

        // Reproducción de audio
        Button playAudioButton = findViewById(R.id.playAudioButton);
        mediaPlayer = MediaPlayer.create(this, R.raw.opening);  // Asegúrate de tener el archivo en res/raw

        playAudioButton.setOnClickListener(v -> {
            if (isPlaying) {
                // Si el audio está en reproducción, detenerlo
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);  // Reiniciar al inicio
                playAudioButton.setText("Reproducir Audio");
                isPlaying = false;
            } else {
                // Si el audio está detenido, reproducirlo
                mediaPlayer.start();
                playAudioButton.setText("Parar Audio");
                isPlaying = true;
            }
        });

        // Reproducción de video
        VideoView videoView = findViewById(R.id.myVideoView);
        Button playVideoButton = findViewById(R.id.playVideoButton);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tomyjerryvideo);  // Asegúrate de tener el archivo en res/raw
        videoView.setVideoURI(videoUri);

        // Botón para alternar entre reproducir y detener el video
        playVideoButton.setOnClickListener(v -> {
            if (isVideoPlaying) {
                // Si el video está en reproducción, pausarlo
                videoView.pause();
                playVideoButton.setText("Reproducir Video");
                isVideoPlaying = false;
            } else {
                // Si el video está detenido, reproducirlo
                videoView.start();
                playVideoButton.setText("Parar Video");
                isVideoPlaying = true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Manejo de la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
