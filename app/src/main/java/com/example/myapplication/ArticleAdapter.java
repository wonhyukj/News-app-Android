package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static java.security.AccessController.getContext;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater inflater;
    List<DetailArticle> detailArticles;

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
        DetailArticle detailArticle = (DetailArticle) detailArticles.get(position);

        Picasso.get().load(detailArticle.getArticleImg()).fit().into(((ArticleViewHolder) holder).articleImg);

        articleViewHolder.articleTitle.setText(detailArticle.getArticleTitle());
        articleViewHolder.articleSource.setText(detailArticle.getArticleSource());

//            ZonedDateTime timeGenerated = ZonedDateTime.parse(detailArticle.getArticleDate());
//
//            ZonedDateTime timeNow = ZonedDateTime.now( ZoneId.of( "America/Montreal" ) );

        articleViewHolder.articleDate.setText(detailArticle.getArticleDate());
        articleViewHolder.articleContent.setText(detailArticle.getArticleContent());
        articleViewHolder.articleURL.setText(detailArticle.getArticleURL());

//            articleViewHolder.cardview.setPreventCornerOverlap(false);
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
            cardview = (CardView) itemView.findViewById(R.id.cardView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }
}
