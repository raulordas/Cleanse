package com.cleanseproject.cleanse.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleanseproject.cleanse.R;
import com.cleanseproject.cleanse.activities.EventDetailsActivity;
import com.cleanseproject.cleanse.dataClasses.Event;
import com.cleanseproject.cleanse.services.EventManagerService;
import com.cleanseproject.cleanse.services.ImageManagerService;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    private ArrayList<Event> listaEventos;

    public EventListAdapter(ArrayList<Event> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public EventListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vista_evento, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.asignarDatos(listaEventos.get(i));

    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private EventManagerService eventManagerService;
        private ImageManagerService imageManagerService;
        private TextView txtTitulo, txtDistancia;
        private ImageView ivFoto;
        private ImageButton btnLike, btnShare;
        private Context context;

        public MyViewHolder(View v) {
            super(v);
            txtTitulo = v.findViewById(R.id.tvTitulo);
            txtDistancia = v.findViewById(R.id.txtDistancia);
            btnLike = v.findViewById(R.id.btnLike);
            btnShare = v.findViewById(R.id.btnShare);
            ivFoto = v.findViewById(R.id.ivEvento);
            context = v.getContext();
            imageManagerService = new ImageManagerService();
            eventManagerService = new EventManagerService();
        }

        public void asignarDatos(Event event) {
            txtTitulo.setText(event.getName());
            String distancia;
            if (event.getDistance() >= 1000)
                distancia = Math.round(event.getDistance() / 1000) + " km";
            else
                distancia = Math.round(event.getDistance()) + " m";
            txtDistancia.setText(distancia);
            if (event.isFavourite())
                btnLike.setImageResource(R.drawable.corazon_pressed);
            else
                btnLike.setImageResource(R.drawable.corazon_transparente);
            imageManagerService.eventImageDownloadUrl(
                    event.getId(),
                    imageUrl -> {
                        Glide.with(context)
                                .load(imageUrl)
                                .into(ivFoto);
                    });
            ivFoto.setOnClickListener(v -> {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("Evento", event.getId());
                context.startActivity(intent);
            });
            btnLike.setOnClickListener(v -> {
                if (!event.isFavourite()) {
                    btnLike.setImageResource(R.drawable.corazon_pressed);
                    eventManagerService.setEventAsFavourite(event.getId());
                    event.setFavourite(true);
                } else {
                    btnLike.setImageResource(R.drawable.corazon_transparente);
                    eventManagerService.deleteFavouriteEvent(event.getId());
                    event.setFavourite(false);
                }
            });
        }


    }

}
