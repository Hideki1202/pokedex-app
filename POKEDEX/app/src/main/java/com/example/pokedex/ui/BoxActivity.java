package com.example.pokedex.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.utils.FavoriteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoxActivity extends AppCompatActivity {

    private FrameLayout container;
    private List<ImageView> imageViews = new ArrayList<>();
    private List<float[]> velocities = new ArrayList<>(); // dx, dy por imagem
    private Handler handler = new Handler();
    private Random random = new Random();
    private int containerWidth, containerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        container = findViewById(R.id.container);

        List<String> urls = FavoriteUtils.getFavoriteImageUrls(this);

        container.post(() -> {
            containerWidth = container.getWidth();
            containerHeight = container.getHeight();

            for (String url : urls) {
                ImageView imageView = new ImageView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200, 200);
                imageView.setLayoutParams(params);

                Glide.with(this).load(url).centerCrop().into(imageView);

                // Posição inicial aleatória
                float x = random.nextInt(containerWidth - params.width);
                float y = random.nextInt(containerHeight - params.height);
                imageView.setX(x);
                imageView.setY(y);

                // Velocidade inicial aleatória (suave)
                float dx = random.nextFloat() * 4 - 2; // -2 a +2 px por frame
                float dy = random.nextFloat() * 4 - 2;
                velocities.add(new float[]{dx, dy});

                container.addView(imageView);
                imageViews.add(imageView);

                enableDrag(imageView);
                imageView.setOnClickListener(v -> showHeart((ImageView) v));

            }

            startSmoothAnimation();
        });
    }

    private void showHeart(ImageView clickedImage) {
        ImageView heart = new ImageView(this);
        heart.setImageResource(R.drawable.coracao);

        // aumenta um pouco para garantir que não ficou minúsculo
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(200, 200);
        heart.setLayoutParams(params);

        float centerX = clickedImage.getX() + clickedImage.getWidth() / 2f - params.width/2f;
        float centerY = clickedImage.getY() + clickedImage.getHeight() / 2f - params.height/2f;
        heart.setX(container.getWidth()/2f - params.width/2f);
        heart.setY(container.getHeight()/2f - params.height/2f);

        container.addView(heart);

        // força a ficar na frente
        heart.bringToFront();
        // força a ser clicável para ver se está lá
        heart.setClickable(true);
        heart.setBackgroundColor(0x55FF0000); // semi-transparente vermelho só para testar

        // animação
        heart.setScaleX(0f);
        heart.setScaleY(0f);
        heart.setAlpha(1f);

        heart.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .alpha(0f)
                .setDuration(800)
                .withEndAction(() -> container.removeView(heart))
                .start();
    }
    private void startSmoothAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < imageViews.size(); i++) {
                    ImageView iv = imageViews.get(i);

                    // pular se arrastando
                    if (iv.getTag() != null && iv.getTag().equals("dragging")) continue;

                    float[] vel = velocities.get(i);

                    // posição normal com velocidade
                    float baseX = iv.getX() + vel[0];
                    float baseY = iv.getY() + vel[1];

                    // quicar nas bordas
                    if (baseX <= 0 || baseX >= containerWidth - iv.getWidth()) vel[0] = -vel[0];
                    if (baseY <= 0 || baseY >= containerHeight - iv.getHeight()) vel[1] = -vel[1];

                    // tremor aleatório pequeno (±3 px)
                    float jitterX = (random.nextFloat() - 0.5f) * 6; // -3 a +3
                    float jitterY = (random.nextFloat() - 0.5f) * 6;

                    iv.setX(iv.getX() + vel[0] + jitterX);
                    iv.setY(iv.getY() + vel[1] + jitterY);
                }

                handler.postDelayed(this, 16); // ~60fps
            }
        }, 16);
    }

    private void enableDrag(final ImageView view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setTag("dragging");
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;
                        newX = Math.max(0, Math.min(newX, containerWidth - v.getWidth()));
                        newY = Math.max(0, Math.min(newY, containerHeight - v.getHeight()));
                        v.setX(newX);
                        v.setY(newY);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        view.setTag(null);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
