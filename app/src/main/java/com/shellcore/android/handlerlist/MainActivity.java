package com.shellcore.android.handlerlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shellcore.android.handlerlist.adapters.PokemonListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // Services
    PokemonListAdapter adapter;

    // Variables
    private List<String> names;
    private HandlerThread handlerThread;
    private Handler handler;

    // Components
    @BindView(R.id.rec_list)
    RecyclerView recList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        names = new ArrayList<>();
        adapter = new PokemonListAdapter(this, names);
        recList.setLayoutManager(new LinearLayoutManager(this));
        recList.setAdapter(adapter);

        setupHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handlerThread.quit();
    }

    @OnClick(R.id.btn_update_list)
    public void updateList() {
        sendMessage();
    }

    private void setupHandler() {
        handlerThread = new HandlerThread("MyHandler");
        handlerThread.start();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String message = bundle.getString("message");
                names.add(message);
                adapter.notifyDataSetChanged();
                Log.d("Message", "" + message);
            }
        };
    }

    /**
     * This method simulate that a sender send a new pokemon name to the app,
     * then, we send a message to the HandlerThread
     */
    private void sendMessage() {
        Message message = handler.obtainMessage();

        List<String> pokemonList = new ArrayList<>();

        pokemonList.add("Bulbasaur");
        pokemonList.add("Charmander");
        pokemonList.add("Squirtle");
        pokemonList.add("Pikachu");

        Random random = new Random();

        String pokemonName = pokemonList.get(random.nextInt(pokemonList.size()));

        Bundle bundle = new Bundle();
        bundle.putString("message", pokemonName);

        message.setData(bundle);
        handler.sendMessage(message);
    }
}
