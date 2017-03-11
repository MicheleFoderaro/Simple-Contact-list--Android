package com.example.michele.rubrica.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michele.rubrica.R;
import com.example.michele.rubrica.activities.ContattoDetailsActivity;
import com.example.michele.rubrica.activities.MainActivity;
import com.example.michele.rubrica.models.Contatto;

import java.util.ArrayList;

/**
 * Created by Michele on 10/03/2017.
 */

public class ContattoAdapter extends RecyclerView.Adapter<ContattoAdapter.ContattoVH>{

    private Context context;
    ArrayList<Contatto> dataSet = new ArrayList<>();
    private int position;

    public ContattoAdapter(Context c) {
        this.context = c;
    }

    public void addDataSet (Contatto e) {
        dataSet.add(0, e);
        notifyItemInserted(0);
    }

    public void setDataSet (ArrayList<Contatto> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public ArrayList<Contatto> getDataSet() {
        return dataSet;
    }

    public void editDataSet (Contatto e, int position) {
        dataSet.set(position, e);
        notifyItemChanged(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public void removeDataSet(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    public Contatto getDataSet(int position) {
        return dataSet.get(position);
    }

    @Override
    public ContattoAdapter.ContattoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contatto, parent, false);
        return new ContattoVH(view);
    }

    @Override
    public void onBindViewHolder(ContattoAdapter.ContattoVH holder, int position) {
        Contatto contatto = dataSet.get(position);
        holder.nomeTv.setText(contatto.getNome());
        holder.numeroTv.setText(contatto.getNumero());
        if (contatto.getSpeciale()==0) {
            holder.imageView.setImageResource(0);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_bookmark_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ContattoVH extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        TextView nomeTv, numeroTv;
        ImageView imageView;
        Button messaggia, call;

        public ContattoVH(View itemView) {
            super(itemView);
            nomeTv = (TextView) itemView.findViewById(R.id.contatto_nome);
            numeroTv = (TextView) itemView.findViewById(R.id.contatto_cellulare);
            imageView = (ImageView) itemView.findViewById(R.id.contatto_image);
            messaggia = (Button) itemView.findViewById(R.id.contatto_messaggia_btn);
            call = (Button) itemView.findViewById(R.id.contaggo_call_btn);
            messaggia.setOnClickListener(this);
            call.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setPosition(getAdapterPosition());
                    return false;
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuInflater inflater = ((MainActivity)context).getMenuInflater();
            inflater.inflate(R.menu.menu_context_contatto, contextMenu);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.contaggo_call_btn:
                    Intent intent = new Intent();
                    intent.setAction(intent.ACTION_VIEW);
                    Uri uri = Uri.parse("tel:" + numeroTv.getText().toString());
                    intent.setData(uri);
                    view.getContext().startActivity(intent);
                    break;
                case R.id.contatto_messaggia_btn:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, numeroTv.getText().toString());
                    sendIntent.setType("text/plain");
                    view.getContext().startActivity(sendIntent);
                    break;
            }
        }
    }
}