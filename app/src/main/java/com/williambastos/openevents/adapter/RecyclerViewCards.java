package com.williambastos.openevents.adapter;

import static com.williambastos.openevents.R.id.card_view;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.williambastos.openevents.EventActivity;
import com.williambastos.openevents.R;
import com.williambastos.openevents.model.Card;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class RecyclerViewCards extends RecyclerView.Adapter<RecyclerViewCards.CardViewHolder>{

    List<Card> cards;
    public RecyclerViewCards(List<Card> cards){
        this.cards = cards;
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public static TextView eventTitle;
        public static TextView eventDate;
        public static TextView eventGeo;
        public static ImageView eventPhoto;
        public static TextView eventId;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(card_view);
            eventTitle = (TextView)itemView.findViewById(R.id.title_event);
            eventDate = (TextView)itemView.findViewById(R.id.date_event);
            eventGeo = (TextView)itemView.findViewById(R.id.geo_event);
            eventId = (TextView) itemView.findViewById(R.id.idEvent);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), EventActivity.class);
                    intent.putExtra("id", Integer.parseInt(eventId.getText().toString()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card_view, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        CardViewHolder.eventTitle.setText(cards.get(i).title);
        CardViewHolder.eventDate.setText(cards.get(i).date);
        CardViewHolder.eventGeo.setText(cards.get(i).geo);
        CardViewHolder.eventPhoto.setImageResource(R.drawable.test);
        //CardViewHolder.eventId.setText(cards.get(i).id);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}