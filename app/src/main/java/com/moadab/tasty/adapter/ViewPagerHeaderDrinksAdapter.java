package com.moadab.tasty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.moadab.tasty.R;
import com.moadab.tasty.remote.model.Drinks;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerHeaderDrinksAdapter extends PagerAdapter {

    private List<Drinks> meals;
    private Context context;
    private static ClickListener clickListener;

    public ViewPagerHeaderDrinksAdapter(List<Drinks> meals, Context context) {
        this.meals = meals;
        this.context = context;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ViewPagerHeaderDrinksAdapter.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return meals.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_view_pager_header,
                container,
                false
        );

        ImageView mealThumb = view.findViewById(R.id.mealThumb);
        TextView mealName = view.findViewById(R.id.mealName);

        String strMealThumb = meals.get(position).getStrdrinkthumb();
        Picasso.get().load(strMealThumb).into(mealThumb);

        String strMealName = meals.get(position).getStrdrink();
        mealName.setText(strMealName);

        view.setOnClickListener(v -> clickListener.onClick(v, position));

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public interface ClickListener {
        void onClick(View v, int position);
    }
}
