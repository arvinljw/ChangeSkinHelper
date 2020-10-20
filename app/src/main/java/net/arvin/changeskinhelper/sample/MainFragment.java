package net.arvin.changeskinhelper.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.arvin.changeskinhelper.ChangeSkinHelper;
import net.arvin.changeskinhelper.core.ChangeSkinListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2019-08-09 20:15
 * Function：
 * Desc：
 */
public class MainFragment extends Fragment implements ChangeSkinListener {
    private List<String> items;
    private SkinAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChangeSkinHelper.addListener(this);
        items = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            items.add("item" + i);
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SkinAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void changeSkin() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ChangeSkinHelper.removeListener(this);
    }

    public static class SkinAdapter extends RecyclerView.Adapter<SkinViewHolder> {
        private Context context;
        private List<String> items;

        public SkinAdapter(Context context, List<String> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public SkinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            return new SkinViewHolder(layoutInflater.inflate(R.layout.item_test, null));
        }

        @Override
        public void onBindViewHolder(@NonNull SkinViewHolder skinViewHolder, int i) {
            skinViewHolder.setData(items.get(i));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    public static class SkinViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public SkinViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item);
            ChangeSkinHelper.setSkin(textView);
        }

        public void setData(String item) {
            ChangeSkinHelper.applyViews(itemView);
            textView.setText(item);
        }
    }
}
