package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<DetailArticle> detailArticles;

    public ArticleAdapter(Context ctx, List<DetailArticle> detailArticle) {
        this.inflater = LayoutInflater.from(ctx);
        this.detailArticles = detailArticle;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false);
        viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
        DetailArticle detailArticle = detailArticles.get(position);

        if (detailArticle.getArticleImg().compareTo("") != 0)
            Picasso.get().load(detailArticle.getArticleImg()).fit().into(((ArticleViewHolder) holder).articleImg);
        else {
            Picasso.get().load(R.drawable.default_img).fit().into(((ArticleViewHolder) holder).articleImg);
        }

        articleViewHolder.articleTitle.setText(detailArticle.getArticleTitle());
        articleViewHolder.articleSource.setText(detailArticle.getArticleSource());

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(detailArticle.getArticleDate());
        zonedDateTime = zonedDateTime.withZoneSameLocal(ZoneId.of("America/Los_Angeles"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String formattedString = zonedDateTime.format(formatter);
        articleViewHolder.articleDate.setText(formattedString);
        articleViewHolder.articleContent.setText(detailArticle.getArticleContent());
        articleViewHolder.articleURL.setText(detailArticle.getArticleURL());
    }


    @Override
    public int getItemCount() {
        return this.detailArticles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle, articleSource, articleDate, articleContent, articleURL;
        ImageView articleImg;
        CardView cardview;


        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImg = itemView.findViewById(R.id.articleImg);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleSource = itemView.findViewById(R.id.articleSource);
            articleDate = itemView.findViewById(R.id.articleDate);
            articleContent = itemView.findViewById(R.id.articleContent);
            articleURL = itemView.findViewById(R.id.articleURL);
            cardview = itemView.findViewById(R.id.cardView);
        }
    }
}
