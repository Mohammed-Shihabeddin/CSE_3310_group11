package com.e.vast;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private int[] mImageIds = new int[] {R.drawable.style01, R.drawable.style02, R.drawable.style_03, R.drawable.style04, R.drawable.style05};
    private LayoutInflater layoutInflater;
    ImageView imageView;
    Button bRender;


    ImageAdapter(Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout, container, false);
        imageView = (ImageView) item_view.findViewById(R.id.styleImage);
        TextView image_position = (TextView) item_view.findViewById(R.id.image_count);
        image_position.setText("Image " + (position+1));
        Glide.with(mContext).load(mImageIds[position]).thumbnail(0.1f).into(imageView);
        container.addView(item_view);

        bRender = item_view.findViewById(R.id.bRender);

        bRender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DisplayFinalImage.class);
                intent.putExtra("styleImage", item_view.getResources().getResourceEntryName(mImageIds[position])+".jpg");
                mContext.startActivity(intent);
            }
        });

        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
