package me.yoursun.browser;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import me.yoursun.browser.databinding.TabsActivityBinding;

public class TabsActivity extends AppCompatActivity {

    private static final String TAG = TabsActivity.class.getSimpleName();

    public static final String TAB_TO_SHOW = "tab_to_show";

    private TabsActivityBinding binding;
    private TabsViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.tabs_activity);

        viewModel = new TabsViewModel();

        binding.tabsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.tabsRecyclerView.setAdapter(new TabsAdapter(viewModel, position -> {
            setResult(RESULT_OK, new Intent().putExtra(TAB_TO_SHOW, position));
            finish();
        }));
    }


    private static class TabsAdapter extends RecyclerView.Adapter<TabsAdapter.TabViewHolder> {

        private WeakReference<TabsViewModel> viewModelWeakRef;

        private TabViewHolder.OnTabClickListener tabClickListener;

        TabsAdapter(TabsViewModel viewModel, TabViewHolder.OnTabClickListener listener) {
            viewModelWeakRef = new WeakReference<>(viewModel);
            tabClickListener = listener;
        }

        @Override
        public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tab_view_holder, parent, false);
            return new TabViewHolder(v, tabClickListener);
        }

        @Override
        public void onBindViewHolder(TabViewHolder holder, int position) {
            holder.title.setText(viewModelWeakRef.get().getTitle(position));
            holder.tabImage.setImageBitmap(viewModelWeakRef.get().getCaptured(position));
        }

        @Override
        public int getItemCount() {
            return viewModelWeakRef.get().getTabCount();
        }

        static class TabViewHolder extends RecyclerView.ViewHolder {

            interface OnTabClickListener {
                void onTabClicked(int position);
            }

            private TextView title;
            private ImageView tabImage;

            TabViewHolder(View itemView, OnTabClickListener listener) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tabTitle);
                tabImage = (ImageView) itemView.findViewById(R.id.tabImage);
                itemView.setOnClickListener(view -> listener.onTabClicked(getAdapterPosition()));
            }
        }
    }
}
