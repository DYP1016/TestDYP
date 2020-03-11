package com.example.dyp.testdyp.recycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dyp.util.LogUtil;
import com.example.dyp.testdyp.R;
import com.example.dyp.testdyp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecyclerView2Activity extends BaseActivity {

    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.bt_test)
    Button btTest;
    @BindView(R.id.bt_test_2)
    Button btTest2;
    @BindView(R.id.bt_test_3)
    Button btTest3;


    private Adapter adapter;

    private List<Integer> list = new ArrayList<>();

    private boolean flag = false;

    private PagingScrollHelper scrollHelper;//初始化横向管理器


    @Override
    public int getLayoutId() {
        return R.layout.activity_recycler_view2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        for (int i = 1; i <= 128; i++) {
            list.add(i);
        }
    }

    @Override
    public void setListener() {
        btTest3.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                LogUtil.e("onLayoutChange: " + v);
            }
        });
    }

    @Override
    public void processLogic() {
        //使用通用RecyclerView组件
        scrollHelper = new PagingScrollHelper();
        HorizontalPageLayoutManager horizontalPageLayoutManager = new HorizontalPageLayoutManager(2, 2);//这里两个参数是行列，这里实现的是一行三列

        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(2, 2, PagerGridLayoutManager.HORIZONTAL);

        rvMain.post(new Runnable() {
            @Override
            public void run() {
                adapter = new Adapter(list);//设置适配器
                adapter.setItemWidth(rvMain.getWidth());
                rvMain.setAdapter(adapter);
                scrollHelper.setUpRecycleView(rvMain);//将横向布局管理器和recycler view绑定到一起
                scrollHelper.setOnPageChangeListener(new PagingScrollHelper.onPageChangeListener() {
                    @Override
                    public void onPageChange(int index) {
                        LogUtil.e("onPageChange: " + index);
                    }
                });
            }
        });

        //设置滑动监听

        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false);
//        manager
        rvMain.setLayoutManager(manager);

//        PagerGridSnapHelper pagerGridSnapHelper = new PagerGridSnapHelper();
//        pagerGridSnapHelper.attachToRecyclerView(rvMain);

        layoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {
                LogUtil.i("onPageSizeChanged = " + pageSize);
            }

            @Override
            public void onPageSelect(int pageIndex) {
                LogUtil.i("onPageSelect = " + pageIndex);
            }
        });

        rvMain.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
//                LogUtil.i("onChildViewAttachedToWindow: " + view);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
//                LogUtil.i("onChildViewDetachedFromWindow: " + view);
            }
        });

//        rvMain.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false));//设置为横向
//        scrollHelper.updateLayoutManger();
//        scrollHelper.scrollToPosition(0);//默认滑动到第一页
//        rvMain.setHorizontalScrollBarEnabled(true);
    }

    @OnClick({R.id.bt_test, R.id.bt_test_2, R.id.bt_test_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_test:
                flag = !flag;
                if (flag) {
//                    rvMain.setLayoutManager(new PagerGridLayoutManager(2, 2, PagerGridLayoutManager.HORIZONTAL));
                    setWindowCount(2);
                } else {
//                    rvMain.setLayoutManager(new PagerGridLayoutManager(4, 4, PagerGridLayoutManager.HORIZONTAL));
                    setWindowCount(4);
                }

//                adapter.notifyDataSetChanged();
                break;
            case R.id.bt_test_2:
                adapter.notifyItemChanged(0, "2333");
                break;
            case R.id.bt_test_3:
                scrollHelper.scrollToPosition(3);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Integer> list;
        private int count = 0;
        private int mWidth;
        private int row = 1;
        private int itemWidth;
        private int[] positionArray;

        public Adapter(List<Integer> list) {
            this.list = list;

            updatePositionArray();
        }

        public void setWidth(int width) {
            this.mWidth = width;
        }

        public void setRow(int row) {
            this.row = row;

            updatePositionArray();
        }

        public void setItemWidth(int itemWidth) {
            this.itemWidth = itemWidth;
        }

        public void updatePositionArray() {
            positionArray = new int[list.size()];

            int pageSize = row * row;
            int page = list.size() / pageSize;

            int index = 0;
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < row; j++) {
                    for (int k = 0; k < row; k++) {
                        positionArray[index] = i * pageSize + k * row + j;
                        index++;
                    }
                }
            }

//            LogUtil.i("position array = " + Arrays.toString(positionArray));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            count++;
            LogUtil.i("on create view: " + count);
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_item_play_test, parent, false);
            frameLayout.setTag(count);
            frameLayout.getLayoutParams().width = itemWidth;

            return new ViewHolder(frameLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//            LogUtil.e("onBindViewHolder: " + viewHolder.index + " " + viewHolder);
//            viewHolder.tvTest.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            LogUtil.e("getItemCount");
            return list.size();
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
//            LogUtil.e("onViewDetachedFromWindow: " + holder.index);

        }

        @Override
        public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
//            LogUtil.e("onViewAttachedToWindow: " + holder.index);

        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            LogUtil.e("onViewRecycled: " + holder.index);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
            LogUtil.e("onBindViewHolder: " + position + " " + holder + " " + payloads);
            if (holder.itemView.getWidth() != itemWidth) {
                holder.itemView.getLayoutParams().width = itemWidth;
                LogUtil.e("reset width: " + holder.index + " old = " + holder.itemView.getWidth() + " new = " + itemWidth);
            }

            holder.tvTest.setText(String.valueOf(list.get(positionArray[position])));
            if (payloads.size() > 0) {
                holder.tvTest.requestLayout();
            }
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTest;
        private int index = -1;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            tvTest = itemView.findViewById(R.id.tv_count);
            tvTest.setText(String.valueOf(index));

            tvTest.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    LogUtil.e("onLayoutChange: " + v);
                }
            });
        }
    }

    private void setWindowCount(int row) {
        adapter.setItemWidth(rvMain.getWidth() / row);
        adapter.setRow(row);
        adapter.updatePositionArray();
        rvMain.setLayoutManager(new GridLayoutManager(this, row, RecyclerView.HORIZONTAL, false));
        adapter.notifyDataSetChanged();
        scrollHelper.updateLayoutManger();
    }
}
