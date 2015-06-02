package com.lowworker.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.lowworker.android.R;
import com.squareup.picasso.Picasso;

public class PicassoImageGetter implements Html.ImageGetter {

    final Resources resources;
    private Drawable mDefaultDrawable;
    private int mMaxWidth;
    private  Context context;
    final TextView textView;

    public PicassoImageGetter(final TextView textView, Context context) {
        this.textView  = textView;
        this.context     = context;
        this.resources = context.getResources();
        mMaxWidth = ScreenUtils.getDisplayWidth(context) - ScreenUtils.dp(context, 100);
        mDefaultDrawable = context.getResources().getDrawable(R.drawable.ic_launcher);
    }

    @Override public Drawable getDrawable(final String source) {
        final BitmapDrawablePlaceHolder result = new BitmapDrawablePlaceHolder();

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(final Void... meh) {
                try {
                    return Picasso.with(context).load(source).get();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final Bitmap bitmap) {
                try {
                    final BitmapDrawable drawable = new BitmapDrawable(resources, bitmap);
                    int width;
                    int height;
                    if (bitmap.getWidth() > mMaxWidth) {
                        width = mMaxWidth;
                        height = mMaxWidth * bitmap.getHeight() / bitmap.getWidth();
                    } else {
                        width = bitmap.getWidth();
                        height = bitmap.getHeight();
                    }
                    drawable.setBounds(0, 0, width, height);

                    result.setDrawable(drawable);
                    result.setBounds(0, 0, width, height);

                    textView.setText(textView.getText()); // invalidate() doesn't work correctly...
                } catch (Exception e) {
                /* nom nom nom*/
                }
            }

        }.execute((Void) null);

        return result;
    }

    public class BitmapDrawablePlaceHolder extends BitmapDrawable {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if(drawable != null){
                drawable.draw(canvas);
            }else{
                mDefaultDrawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

    }
}