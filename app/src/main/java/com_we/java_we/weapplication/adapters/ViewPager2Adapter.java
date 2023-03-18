package com_we.java_we.weapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com_we.java_we.weapplication.R;
import com_we.java_we.weapplication.models.Person;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

    private List<Person> people;
    private Context context;

    public ViewPager2Adapter(List<Person> people, Context ctx) {
        this.context = ctx;
        this.people = people;
    }

    //return layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    //bind the screen with the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.images.setImageResource(people.get(position).getImage());
        holder.nameAgeText.setText(people.get(position).getName() + ", " + people.get(position).getAge());
        holder.descriptionText.setText(people.get(position).getDescription());
    }

    //return the size of the List
    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView images;
        private TextView nameAgeText, descriptionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.image_view_person);
            nameAgeText = itemView.findViewById(R.id.text_view_name_age);
            descriptionText = itemView.findViewById(R.id.text_view_description);
        }
    }
}
