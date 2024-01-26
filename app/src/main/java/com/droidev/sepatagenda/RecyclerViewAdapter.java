package com.droidev.sepatagenda;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<String> banco;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public RecyclerViewAdapter(Context context, ArrayList<String> banco, RecyclerViewClickInterface recyclerViewClickInterface) {

        this.layoutInflater = LayoutInflater.from(context);
        this.banco = banco;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.row_visualizador, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String[] textId = banco.get(position).split("#@#");

        if (textId[6].contains("NORMAL")) {

            holder.visualizador.setBackgroundColor(Color.parseColor("#FFFF00"));
        } else if (textId[6].contains("URGENTE")) {

            holder.visualizador.setBackgroundColor(Color.parseColor("#FF5349"));
        } else if (textId[6].contains("RESOLVIDO")) {

            holder.visualizador.setBackgroundColor(Color.parseColor("#00FF00"));
        }

        holder.visualizador.setText(textId[0] +
                "\n" + textId[1] +
                "\n" + textId[2] +
                "\n" + textId[3] +
                "\n" + textId[4] +
                "\n" + textId[5] +
                "\n" + textId[6] +
                "\n" + textId[7] +
                "\n" + textId[8]);
    }

    @Override
    public int getItemCount() {
        return banco.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView visualizador;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    recyclerViewClickInterface.onLongItemClick(getAdapterPosition());

                    return true;
                }
            });

            visualizador = itemView.findViewById(R.id.visualizador);
        }
    }
}
