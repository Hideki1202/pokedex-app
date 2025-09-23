package com.example.pokedex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pokedex.MainActivity;
import com.example.pokedex.R;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreen);

        ImageView imageView = findViewById(R.id.imageSplah);

        RotateAnimation rotate = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotate.setDuration(2000); // 2 segundos
        rotate.setRepeatCount(Animation.INFINITE);


        TextView textView = findViewById(R.id.textView2);
        String text = "POKEDEX APP";
        Handler handler = new Handler(Looper.getMainLooper());

        Runnable runnable = new Runnable() {
            int index = 0;
            @Override
            public void run() {
                SpannableString spannable = new SpannableString(text);

                // para cada letra, resetamos estilo normal
                for (int i = 0; i < text.length(); i++) {
                    if (i == index) {
                        // aplica span na letra atual (ex: aumenta tamanho)
                        spannable.setSpan(
                                new RelativeSizeSpan(1.5f), // 1.5x maior
                                i, i + 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        );
                    }
                }

                textView.setText(spannable);

                // vai para a próxima letra
                index = (index + 1) % text.length();

                handler.postDelayed(this, 200); // troca a cada 200ms
            }
        };

        handler.post(runnable);// gira sem parar

        imageView.startAnimation(rotate);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }, 3500);

    }
}