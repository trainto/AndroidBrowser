package me.yoursun.browser;

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

    private TabsActivityBinding binding;
    private TabsViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.tabs_activity);

        viewModel = new TabsViewModel();

        binding.tabsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.tabsRecyclerView.setAdapter(new TabsAdapter(viewModel));
    }


    private static class TabsAdapter extends RecyclerView.Adapter<TabsAdapter.TabViewHolder> {

        private WeakReference<TabsViewModel> viewModelWeakRef;

        TabsAdapter(TabsViewModel viewModel) {
            viewModelWeakRef = new WeakReference<>(viewModel);
        }

        @Override
        public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tab_view_holder, parent, false);
            return new TabViewHolder(v);
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

            private TextView title;
            private ImageView tabImage;

            TabViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tabTitle);
                tabImage = (ImageView) itemView.findViewById(R.id.tabImage);
            }
        }

    }
}
