package com.rehman.clicksonic.MenuFrag;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rehman.clicksonic.Lists.FacebookListActivity;
import com.rehman.clicksonic.Lists.InstagramListActivity;
import com.rehman.clicksonic.Lists.PaymentHistoryListActivity;
import com.rehman.clicksonic.Lists.TikTokActivity;
import com.rehman.clicksonic.Lists.YouTubeListActivity;
import com.rehman.clicksonic.R;

public class OrderHistoryFragment extends Fragment {

    CardView money_card,card_youTube,card_instagram,card_facebook,card_tiktok;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order_history, container, false);

        initView(view);

        card_youTube.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), YouTubeListActivity.class);
            intent.putExtra("type","user");
            startActivity(intent);
        });
        card_instagram.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InstagramListActivity.class);
            intent.putExtra("type","user");
            startActivity(intent);
        });
        card_facebook.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FacebookListActivity.class);
            intent.putExtra("type","user");
            startActivity(intent);
        });
        card_tiktok.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TikTokActivity.class);
            intent.putExtra("type","user");
            startActivity(intent);
        });
        money_card.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PaymentHistoryListActivity.class);
            startActivity(intent);
        });
        return view;

    }

    private void initView(View view) {

        money_card = view.findViewById(R.id.money_card);
        card_youTube = view.findViewById(R.id.card_youTube);
        card_instagram = view.findViewById(R.id.card_instagram);
        card_facebook = view.findViewById(R.id.card_facebook);
        card_tiktok = view.findViewById(R.id.card_tiktok);

    }
}