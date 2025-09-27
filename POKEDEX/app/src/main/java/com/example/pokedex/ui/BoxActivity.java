package com.example.pokedex.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.api.RetrofitClient;
import com.example.pokedex.api.dto.PokemonDetailsResponse;
import com.example.pokedex.api.service.PokeApiService;
import com.example.pokedex.utils.FavoriteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoxActivity extends AppCompatActivity {

    private FrameLayout container;
    private List<ImageView> imageViews = new ArrayList<>();
    private List<float[]> velocities = new ArrayList<>(); // dx, dy por imagem
    private Handler handler = new Handler();
    private Random random = new Random();
    private int containerWidth, containerHeight;
    private int escolhaNumero = 0;
    private boolean isBatalha= false;
    PokemonDetailsResponse firstPokemon;
    PokemonDetailsResponse secondPokemon;

    int statsPokemonFirst;
    int statsPokemonSecond;

    ConstraintLayout layoutBattle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        container = findViewById(R.id.container);
        Button batalhaBtn = findViewById(R.id.batalha_btn);
        TextView boxSpan = findViewById(R.id.spanBox);
        TextView fechar = findViewById(R.id.fechar);
        TextView resultado = findViewById(R.id.resultado);


        fechar.setOnClickListener(v -> {
            layoutBattle.setVisibility(View.GONE);
        });

        layoutBattle  = findViewById(R.id.battle_layout);

        List<String> urls = FavoriteUtils.getFavoriteImageUrls(this);


        batalhaBtn.setOnClickListener(v -> {
            boxSpan.setText("Escolha o primeiro pokemon clicando nele");
            boxSpan.setWidth(650);
            layoutBattle.setVisibility(View.GONE);
            escolhaNumero = 1;
            isBatalha = true;
        });



        container.post(() -> {
            containerWidth = container.getWidth();
            containerHeight = container.getHeight();

            for (String url : urls) {
                ImageView imageView = new ImageView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200, 200);
                imageView.setLayoutParams(params);

                Glide.with(this).load(url).centerCrop().into(imageView);

                // guarda a URL na tag da view
                imageView.setTag(R.id.image_url_tag, url);

                // posição inicial e velocidade…
                float x = random.nextInt(containerWidth - params.width);
                float y = random.nextInt(containerHeight - params.height);
                imageView.setX(x);
                imageView.setY(y);

                float dx = random.nextFloat() * 4 - 2;
                float dy = random.nextFloat() * 4 - 2;
                velocities.add(new float[]{dx, dy});

                // aqui troca: usa 'v' do clique

                imageView.setOnClickListener(v -> {
                    String clickedUrl = (String) v.getTag(R.id.image_url_tag);
                    String fileName = clickedUrl.substring(url.lastIndexOf('/') + 1); // "553.png"
                    String number = fileName.replace(".png", ""); // "553"
                    int pokeNumber = Integer.parseInt(number);
                    if (isBatalha){
                        if(escolhaNumero==1){
                            escolhaNumero = 2;
                            boxSpan.setText("Escolha o segundo pokemon clicando nele");
                            String urlApi = "https://pokeapi.co/api/v2/pokemon/" + number + "/";

                            PokeApiService service = RetrofitClient.getClient().create(PokeApiService.class);
                            Call<PokemonDetailsResponse> call = service.getPokemonByUrl(urlApi);

                            call.enqueue(new Callback<PokemonDetailsResponse>() {
                                @Override
                                public void onResponse(Call<PokemonDetailsResponse> call, Response<PokemonDetailsResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        PokemonDetailsResponse pokemon = response.body();
                                        String name = pokemon.getName();
                                        String id = pokemon.getId();

                                        firstPokemon = pokemon;
                                        statsPokemonFirst = pokemon.getTotalStats();

                                        Log.i("POKEMON", "Name: " + name + " | Id: " + id);
                                        Toast.makeText(getApplicationContext(),
                                                "Pokemon: " + name + " (" + id + ")",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("POKEMON", "Erro na resposta: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<PokemonDetailsResponse> call, Throwable t) {
                                    Log.e("POKEMON", "Erro: " + t.getMessage(), t);
                                }
                            });




                        }else if (escolhaNumero==2){
                            escolhaNumero = 0;
                            isBatalha = false;
                            boxSpan.setText("BOX pokémon favoritos");
                            String urlApi = "https://pokeapi.co/api/v2/pokemon/" + number + "/";

                            PokeApiService service = RetrofitClient.getClient().create(PokeApiService.class);
                            Call<PokemonDetailsResponse> call = service.getPokemonByUrl(urlApi);

                            call.enqueue(new Callback<PokemonDetailsResponse>() {
                                @Override
                                public void onResponse(Call<PokemonDetailsResponse> call, Response<PokemonDetailsResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        PokemonDetailsResponse pokemon = response.body();

                                        String name = pokemon.getName();
                                        String id = pokemon.getId();
                                        secondPokemon = pokemon;
                                        statsPokemonSecond = pokemon.getTotalStats();

                                        Log.i("POKEMON", "Name: " + name + " | Id: " + id);
                                        Toast.makeText(getApplicationContext(),
                                                "Pokemon: " + name + " (" + id + ")",
                                                Toast.LENGTH_SHORT).show();

                                        layoutBattle.setVisibility(View.VISIBLE);
                                        TextView nomePokemon1 = findViewById(R.id.pokemonName1);
                                        TextView nomePokemon2 = findViewById(R.id.pokemonName2);
                                        TextView statsPokemon1 = findViewById(R.id.pokemonStats1);
                                        TextView statsPokemon2 = findViewById(R.id.pokemonStats2);
                                        ImageView imagePokemon1 = findViewById(R.id.pokemonImage1);
                                        ImageView imagePokemon2 = findViewById(R.id.pokemonImage2);

                                        nomePokemon1.setText(firstPokemon.getName());
                                        nomePokemon2.setText(secondPokemon.getName());

                                        statsPokemon1.setText(String.valueOf("Status totais:"+statsPokemonFirst));
                                        statsPokemon2.setText(String.valueOf("Status totais:"+statsPokemonSecond));

                                        Glide.with(BoxActivity.this)
                                                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" +  firstPokemon.getId()+ ".png") // exemplo
                                                .into(imagePokemon1);
                                        Glide.with(BoxActivity.this)
                                                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" +  secondPokemon.getId()+ ".png") // exemplo
                                                .into(imagePokemon2);





                                        if(statsPokemonSecond > statsPokemonFirst){
                                            resultado.setText("POKEMON GANHADOR: " + secondPokemon.getName());
                                        } else if (statsPokemonFirst > statsPokemonSecond) {
                                            resultado.setText("POKEMON GANHADOR: " + firstPokemon.getName());

                                        }else {
                                            resultado.setText("EMPATE");

                                        }
                                    } else {
                                        Log.e("POKEMON", "Erro na resposta: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<PokemonDetailsResponse> call, Throwable t) {
                                    Log.e("POKEMON", "Erro: " + t.getMessage(), t);
                                }
                            });


                        }
                    }
                    Log.i("POKEMON_NUM", "Número: " + pokeNumber);
                    Log.i("DEBUG_CLICK", "URL clicada: " + clickedUrl);
                    Toast.makeText(this, "URL: " + clickedUrl, Toast.LENGTH_SHORT).show();

                    // mostrar coração
                    if(!isBatalha){
                        showHeart((ImageView) v);
                    }

                });
                enableDrag(imageView);
                container.addView(imageView);
                imageViews.add(imageView);
            }

            startSmoothAnimation();
        });
    }

    private void showHeart(ImageView clickedImage) {
        ImageView heart = new ImageView(this);
        heart.setImageResource(R.drawable.coracao);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200, 200);
        heart.setLayoutParams(params);

        // pegar o centro do imageView clicado
        float centerX = clickedImage.getX() + clickedImage.getWidth() / 2f - params.width / 2f;
        float centerY = (clickedImage.getY() + clickedImage.getHeight() / 2f - params.height / 2f) + 1f;

        // posicionar exatamente sobre a imagem clicada
        heart.setX(centerX);
        heart.setY(centerY);

        container.addView(heart);
        heart.bringToFront();

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
            long downTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downTime = System.currentTimeMillis();
                        view.setTag("dragging");
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        return false; // <<< devolve falso para que o onClick seja chamado depois
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
                        return false; // <<< importante para disparar o click se foi um toque rápido
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
