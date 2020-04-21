
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private LayoutInflater inflater;
    private List<News> news;

    SearchAdapter(Context ctx, List<News> news) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.news = news;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_layout, parent, false);
        viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsViewHolder holders = (NewsViewHolder) holder;

        News news = this.news.get(position);
        ZonedDateTime timeNow = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        ZonedDateTime timeGenerated = ZonedDateTime.parse(news.getTime()).withZoneSameInstant(ZoneId.of("America/Los_Angeles"));

        Duration d = Duration.between(timeNow, timeGenerated);
        long seconds = d.getSeconds();
        long absSeconds = Math.abs(seconds);
        String timeReturn = "";
        if ((absSeconds / 3600) > 24) {
            timeReturn = (absSeconds / 3600) / 24 + "d ago";
        } else if (absSeconds / 3600 > 0) {
            timeReturn = (absSeconds / 3600) + "h ago";
        } else if ((absSeconds % 3600) / 60 > 0) {
            timeReturn = ((absSeconds % 3600) / 60) + "m ago";
        } else {
            timeReturn = (absSeconds % 60) + "s ago";
        }

        holders.newsTitle.setText(news.getTitle());
        holders.newsSource.setText(news.getSection());
        holders.newsDate.setText(timeReturn);
        holders.newsTime.setText(news.getTime());
        holders.newsURL.setText(news.getWebURL());
        holders.newsID.setText(news.getId());
        holders.newsImgURL.setText(news.getImg());
        if (news.getImg().equals("Guardians_hq.png")) {
            Picasso.get().load(R.drawable.default_img).fit().into(holders.newsImage);
        } else {
            Picasso.get().load(news.getImg()).fit().into(holders.newsImage);
        }
        //BookMark
        if (SharedPreference.getSavedObjectFromPreference(context, "storage", holders.newsID.getText().toString(), News.class) == null) {
            holders.newsBookmark.setTag("noBookmark");
        } else {
            holders.newsBookmark.setTag("Bookmark");
            holders.newsBookmark.setImageResource(R.drawable.ic_bookmark_24px);
        }

    }


    @Override
    public int getItemCount() {
        return this.news.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsSource, newsDate, newsID, newsURL, newsImgURL, newsTime;

        ImageView newsImage, newsBookmark, newsTwitter, dialog_bookmark;

        Intent intent;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            newsTime = itemView.findViewById(R.id.newsTime);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsSource = itemView.findViewById(R.id.newsSource);
            newsDate = itemView.findViewById(R.id.newsDate);
            newsBookmark = itemView.findViewById(R.id.bookmark);
            newsTwitter = itemView.findViewById(R.id.twitter);
            newsURL = itemView.findViewById(R.id.newsURL);
            newsID = itemView.findViewById(R.id.newsID);
            dialog_bookmark = itemView.findViewById(R.id.dialog_bookmark);
            newsImgURL = itemView.findViewById(R.id.newsImgURL);

            newsBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("CONTEXT: ", view.getContext().toString());
                    if (newsBookmark.getTag().equals("noBookmark")) {
                        newsBookmark.setImageResource(R.drawable.ic_bookmark_24px);
                        newsBookmark.setTag("Bookmark");
                        Log.i("NEWS ID: ", newsID.getText().toString());
                        Toast.makeText(view.getContext(), "\"" + newsTitle.getText().toString() + "\" was added to bookmarks", Toast.LENGTH_LONG).show();
                        News news = new News();

                        news.setId(newsID.getText().toString());
                        news.setImg(newsImgURL.getText().toString());
                        news.setSection(newsSource.getText().toString());
                        news.setTime(newsTime.getText().toString());
                        news.setTitle(newsTitle.getText().toString());
                        news.setWebURL(newsURL.getText().toString());
                        SharedPreference.saveObjectToSharedPreference(view.getContext(), "storage", news.getId(), news);

                    } else {
                        newsBookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                        newsBookmark.setTag("noBookmark");
                        Toast.makeText(view.getContext(), "\"" + newsTitle.getText().toString() + "\" was removed from bookmarks", Toast.LENGTH_LONG).show();
                        SharedPreference.removeSavedObjectFromPreference(view.getContext(), "storage", newsID.getText().toString());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(v.getContext(), ArticleActivity.class);
                    intent.putExtra("url", newsID.getText());
                    intent.putExtra("newsImage", newsImgURL.getText().toString());
                    intent.putExtra("newsSource", newsSource.getText());
                    intent.putExtra("newsDate", newsDate.getText());
                    intent.putExtra("newsTime", newsTime.getText());
                    intent.putExtra("newsTitle", newsTitle.getText());
                    intent.putExtra("newsURL", newsURL.getText());
                    v.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final Dialog dialog = new Dialog(view.getContext());
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.dialog);
                    // Set dialog title
                    dialog.setTitle("Custom Dialog");

                    // set values for custom dialog components - text, image and button
                    TextView text = dialog.findViewById(R.id.textDialog);
                    text.setText(newsTitle.getText());
                    ImageView image = dialog.findViewById(R.id.imageDialog);

                    Bitmap bitmap = ((BitmapDrawable) newsImage.getDrawable()).getBitmap();

                    image.setImageBitmap(bitmap);

                    ImageView twitter = dialog.findViewById(R.id.twitter);
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check out this Link: &url="
                                    + newsURL.getText() + "&hashtags=CSCI571NewsSearch"));
                            view.getContext().startActivity(intent);
                        }
                    });

                    final ImageView dialog_Bookmark = dialog.findViewById(R.id.dialog_bookmark);

                    if (SharedPreference.getSavedObjectFromPreference(view.getContext(), "storage", newsID.getText().toString(), News.class) == null) {
                        dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                    } else {
                        dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_24px);
                    }

                    dialog_Bookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (newsBookmark.getTag().equals("noBookmark")) {
                                News news = new News();
                                Toast.makeText(view.getContext(), "\"" + newsTitle.getText().toString() + "\" was added to bookmarks", Toast.LENGTH_LONG).show();

                                news.setId(newsID.getText().toString());
                                news.setImg(newsImgURL.getText().toString());
                                news.setSection(newsSource.getText().toString());
                                news.setTime(newsTime.getText().toString());
                                news.setTitle(newsTitle.getText().toString());
                                news.setWebURL(newsURL.getText().toString());

                                dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_24px);
                                newsBookmark.setImageResource(R.drawable.ic_bookmark_24px);
                                SharedPreference.saveObjectToSharedPreference(view.getContext(), "storage", newsID.getText().toString(), news);
                                newsBookmark.setTag("Bookmark");

                            } else {
                                Toast.makeText(view.getContext(), "\"" + newsTitle.getText().toString() + "\" was removed from bookmarks", Toast.LENGTH_LONG).show();
                                dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                                newsBookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                                SharedPreference.removeSavedObjectFromPreference(view.getContext(), "storage", newsID.getText().toString());
                                newsBookmark.setTag("noBookmark");
                            }
                        }
                    });
                    dialog.show();

                    return false;
                }
            });
        }
    }
}
