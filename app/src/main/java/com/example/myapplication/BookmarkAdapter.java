package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
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

public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private LayoutInflater inflater;
    private List<News> news;

    BookmarkAdapter(Context ctx, List<News> news) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.news = news;
    }
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_card, parent, false);
        viewHolder = new BookmarkViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            BookmarkViewHolder holders = (BookmarkViewHolder) holder;
            News news = null;
            try {
                news = (News) this.news.get(position);
            } catch (Exception e) {
                return ;
            }
            // Change ZoneDateTime !!!!!

            holders.bookmarkTitle.setText(news.getTitle());
            holders.bookmarkSource.setText(news.getSection());
            holders.bookmarkDate.setText(news.getTime());
            holders.bookmarkURL.setText(news.getWebURL());
            holders.bookmarkID.setText(news.getId());
            holders.bookmarkImgURL.setText(news.getNewsImgURL());
            Picasso.get().load(news.getImg()).fit().into(holders.bookmarkImage);



            //BookMark
            if (SharedPreference.getSavedObjectFromPreference(context, "storage", holders.bookmarkID.getText().toString(), News.class) == null) {
                holders.bookmark_Change.setTag("noBookmark");
            } else {
                holders.bookmark_Change.setTag("Bookmark");
                holders.bookmark_Change.setImageResource(R.drawable.ic_bookmark_24px);
            }
    }

    @Override
    public int getItemCount() {
        if(this.news == null)
            return 0;
        Log.i("NEWS COUNT", String.valueOf(this.news.size()));
        return this.news.size();

    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView bookmarkTitle, bookmarkDate, bookmarkSource, bookmarkURL, bookmarkID, bookmarkImgURL;

        ImageView bookmarkImage, bookmark_Change;


        BookmarkViewHolder(@NonNull final View itemView) {
            super(itemView);

            bookmarkImage = itemView.findViewById(R.id.bookmarkImage);
            bookmarkTitle = itemView.findViewById(R.id.bookmarkTitle);
            bookmarkSource = itemView.findViewById(R.id.bookmarkSource);
            bookmarkDate = itemView.findViewById(R.id.bookmarkDate);
            bookmark_Change = itemView.findViewById(R.id.bookmark_Change);
            bookmarkURL = itemView.findViewById(R.id.bookmarkURL);
            bookmarkID = itemView.findViewById(R.id.bookmarkID);
            bookmarkImgURL = itemView.findViewById(R.id.bookmarkImgURL);

            bookmark_Change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("CONTEXT: ", view.getContext().toString());
                        bookmark_Change.setImageResource(R.drawable.ic_bookmark_border_24px);
                        bookmark_Change.setTag("noBookmark");
                        SharedPreference.removeSavedObjectFromPreference(view.getContext(), "storage", bookmarkID.getText().toString());
                        news.remove(news.get(getAdapterPosition()));
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), news.size());

                        Log.i("LOG ID: ", "ID" + SharedPreference.getSavedObjectFromPreference(view.getContext(), "storage", bookmarkID.getText().toString(), News.class));

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ArticleActivity.class);
                    intent.putExtra("url", bookmarkID.getText());

                    intent.putExtra("newsImage", bookmarkURL.getText().toString());
                    intent.putExtra("newsSource", bookmarkSource.getText());
                    intent.putExtra("newsDate", bookmarkDate.getText());
                    intent.putExtra("newsTitle", bookmarkTitle.getText());
                    intent.putExtra("newsURL", bookmarkURL.getText());
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
                    text.setText(bookmarkTitle.getText());
                    ImageView image = dialog.findViewById(R.id.imageDialog);

//                    StringToBitMap(bookmarkImage);
                    Bitmap bitmap = ((BitmapDrawable) bookmarkImage.getDrawable()).getBitmap();

                    image.setImageBitmap(bitmap);

                    ImageView twitter = dialog.findViewById(R.id.twitter);
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check out this Link: &url="
                                    + bookmarkURL.getText() + "&hashtags=CSCI571NewsSearch"));
                            view.getContext().startActivity(intent);
                        }
                    });

                    final ImageView dialog_Bookmark = dialog.findViewById(R.id.dialog_bookmark);

                    if (SharedPreference.getSavedObjectFromPreference(view.getContext(), "storage", bookmarkID.getText().toString(), News.class) == null) {
                        dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                    } else {
                        dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_24px);
                    }

                    dialog_Bookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (bookmark_Change.getTag().equals("noBookmark")) {
                                News news = new News();

                                news.setId(bookmarkID.getText().toString());
                                news.setImg(bookmarkImgURL.getText().toString());
                                news.setSection(bookmarkSource.getText().toString());
                                news.setTime(bookmarkDate.getText().toString());
                                news.setTitle(bookmarkTitle.getText().toString());
                                news.setWebURL(bookmarkURL.getText().toString());


                                dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_24px);
                                bookmark_Change.setImageResource(R.drawable.ic_bookmark_24px);
                                SharedPreference.saveObjectToSharedPreference(view.getContext(), "storage", bookmarkID.getText().toString(), news);
                                bookmark_Change.setTag("Bookmark");

                            } else {
                                dialog_Bookmark.setImageResource(R.drawable.ic_bookmark_border_24px);
                                bookmark_Change.setImageResource(R.drawable.ic_bookmark_border_24px);
                                SharedPreference.removeSavedObjectFromPreference(view.getContext(), "storage", bookmarkID.getText().toString());
                                news.remove(news.get(getAdapterPosition()));
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), news.size());

                                bookmark_Change.setTag("noBookmark");

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
