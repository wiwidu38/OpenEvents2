package com.williambastos.openevents.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.williambastos.openevents.R;
import com.williambastos.openevents.model.User;
import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder> {

    ArrayList<User> users;
    private Context context;

    public UsersAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_view, parent, false);
        return new UsersAdapter.UsersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersAdapterViewHolder holder, int position) {
        holder.bind(users.get(position), context);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class UsersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private User user;
        private Context context;
        private ImageView user_image;
        private TextView user_name;
        private TextView user_lastName;
        private CardView userCard;

        public UsersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_image = (ImageView) itemView.findViewById(R.id.user_picture);
            this.user_name = (TextView) itemView.findViewById(R.id.user_name);
            this.user_lastName = (TextView) itemView.findViewById(R.id.user_last_name);
            this.userCard = (CardView) itemView.findViewById(R.id.user_card_view);

        }

        public void bind(User _user, Context _context) {
            this.user = _user;
            this.context = _context;

            String url = "";
            if (this.user.getImage() != null) {
                if (this.user.getImage().startsWith("http")) {
                    url = this.user.getImage();
                } else {
                    url = "http://172.16.205.68/img/" + this.user.getImage();
                }
            }

            Glide.with(context)
                    .load(url)
                    .into(user_image);

            this.userCard.setOnClickListener(this);
            this.user_name.setText(this.user.getName());
            this.user_lastName.setText(this.user.getLast_name());
        }

        @Override
        public void onClick(View view) {
            // Intent intent = new Intent(context, InfoUserActivity.class);
            // intent.putExtra("id", this.user.getId());
            // this.context.startActivity(intent);
        }
    }
}
