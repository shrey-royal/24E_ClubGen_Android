package com.royal.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.royal.newsapp.R;
import com.royal.newsapp.WebViewActivity;
import com.royal.newsapp.model.Article;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<Article> articles;
    private Context context;

    public NewsAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.titleText.setText(article.title);
        holder.sourceText.setText(article.source.name);
        Glide.with(context).load(article.urlToImage).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", article.url);
            context.startActivity(intent);
        });
    }

    public int getItemCount() {
        return articles.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, sourceText;
        ImageView imageView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            sourceText = itemView.findViewById(R.id.sourceText);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
