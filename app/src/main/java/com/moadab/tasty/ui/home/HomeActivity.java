package com.moadab.tasty.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moadab.tasty.Fragments.UserFragment;
import com.moadab.tasty.LoginOrSignUpActivity;
import com.moadab.tasty.R;
import com.moadab.tasty.UserActivity;
import com.moadab.tasty.adapter.RecyclerViewHomeAdapter;
import com.moadab.tasty.adapter.ViewPagerHeaderAdapter;
import com.moadab.tasty.Common.Utils;
import com.moadab.tasty.remote.model.Categories;
import com.moadab.tasty.remote.model.Meals;
import com.moadab.tasty.ui.category.CategoryActivity;
import com.moadab.tasty.ui.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeView  {

    private FirebaseAuth mAuth;
    private DatabaseReference nRootRef;

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";

    @BindView(R.id.viewPagerHeader)
    ViewPager viewPagerMeal;
    @BindView(R.id.recyclerCategory)
    RecyclerView recyclerViewCategory;
    @BindView(R.id.name)
    TextView userName;
    @BindView(R.id.image_profile)
    ImageView imageProfile;


    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        nRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        imageProfile.setOnClickListener(v -> viewProfile());
        userName.setOnClickListener(v -> viewProfile());
        presenter = new HomePresenter(this);
        presenter.getMeals();
        presenter.getCategories();
    }

    private void viewProfile() {
        Intent intent = new Intent(HomeActivity.this, UserActivity.class);
        startActivity(intent);
    }


    @Override
    public void showLoading() {
        findViewById(R.id.shimmerMeal).setVisibility(View.VISIBLE);
        findViewById(R.id.shimmerCategory).setVisibility(View.VISIBLE);
        readInfoUser();
    }

    @Override
    public void hideLoading() {
        findViewById(R.id.shimmerMeal).setVisibility(View.GONE);
        findViewById(R.id.shimmerCategory).setVisibility(View.GONE);
    }


    @Override
    public void setMeal(List<Meals.Meal> meal) {
        ViewPagerHeaderAdapter headerAdapter = new ViewPagerHeaderAdapter(meal, this);
        viewPagerMeal.setAdapter(headerAdapter);
        viewPagerMeal.setPadding(20, 0, 150, 0);
        headerAdapter.notifyDataSetChanged();

        headerAdapter.setOnItemClickListener((v, position) -> {

            TextView mealName = v.findViewById(R.id.mealName);
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(EXTRA_DETAIL,mealName.getText().toString());
            startActivity(intent);
        });
    }

    @Override
    public void setCategory(List<Categories.Category> category) {
        RecyclerViewHomeAdapter homeAdapter = new RecyclerViewHomeAdapter(category, this);
        recyclerViewCategory.setAdapter(homeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setNestedScrollingEnabled(true);
        homeAdapter.notifyDataSetChanged();

        homeAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra(EXTRA_CATEGORY, (Serializable) category);
            intent.putExtra(EXTRA_POSITION, position);
            startActivity(intent);
        });
    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Title", message);
    }


    /* get user information in firebase */
    public void readInfoUser() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String txtName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String imgProfile = Objects.requireNonNull(snapshot.child("imageurlProfile").getValue().toString());

                userName.setText(txtName);

                if (!imgProfile.equals("default")) {
                    Picasso.get()
                            .load(imgProfile)
                            .placeholder(R.drawable.ic_persona)
                            .error(R.drawable.ic_persona)
                            .into(imageProfile);

                }else {
                    imageProfile.setImageResource(R.drawable.ic_persona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
