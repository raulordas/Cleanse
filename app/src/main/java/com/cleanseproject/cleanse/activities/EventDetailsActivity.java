package com.cleanseproject.cleanse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleanseproject.cleanse.R;
import com.cleanseproject.cleanse.adapters.UsersInEventAdapter;
import com.cleanseproject.cleanse.dataClasses.Event;
import com.cleanseproject.cleanse.dataClasses.User;
import com.cleanseproject.cleanse.services.ChatManagerService;
import com.cleanseproject.cleanse.services.EventManagerService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EventManagerService eventManagerService;
    private ChatManagerService chatManagerService;

    private Event event;

    private ImageView imagenEvento;
    private TextView txtDescripcion, txtTituloImagen, txtDistancia, txtJoinChat;
    private RecyclerView rvUsuarios;
    private UsersInEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = findViewById(R.id.event_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imagenEvento = findViewById(R.id.imagenEventoSeleccionado);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtTituloImagen = findViewById(R.id.txtTituloImagen);
        txtDistancia = findViewById(R.id.txtDistancia);
        txtJoinChat = findViewById(R.id.txt_join_chat);
        rvUsuarios = findViewById(R.id.rvUsuarios);
        eventManagerService = new EventManagerService();
        chatManagerService = new ChatManagerService();
        firebaseAuth = FirebaseAuth.getInstance();
        txtJoinChat.setOnClickListener(v -> startChat());
        txtDescripcion.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        String idEvento = intent.getStringExtra("Evento");
        eventManagerService.getEvent(idEvento, event -> {
            this.event = event;
            toolbar.setTitle(event.getName());
            txtTituloImagen.setText(event.getName());
            txtDescripcion.setText(event.getDescription());
            String posicionEvento = "Lat:" + event.getLatitude() + " Long:" + event.getLongitude();
            txtDistancia.setText(posicionEvento);
        });
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvUsuarios.setLayoutManager(layoutManager);
        User u1 = new User();
        User u2 = new User();
        ArrayList<User> lista = new ArrayList<>();
        lista.add(u1);
        lista.add(u2);
        adapter = new UsersInEventAdapter(lista);
        rvUsuarios.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startChat() {
        chatManagerService.joinChat(firebaseAuth.getCurrentUser().getUid(), event.getId());
    }

}
