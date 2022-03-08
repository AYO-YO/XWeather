package cn.fanbaby.xweather;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout srl_pull_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        init();
    }

    void init() {
//        srl_pull_refresh = findViewById(R.id.main_swiper_pull_refresh);
//        srl_pull_refresh.setColorSchemeColors(Color.parseColor("#00aaff"), Color.parseColor("#00ff00"));
//        srl_pull_refresh.setProgressBackgroundColorSchemeColor(Color.parseColor("#aaaaaa"));
        listen();
    }

    private void listen() {
        srl_pull_refresh.setOnRefreshListener(() -> {
            if (srl_pull_refresh.isRefreshing())
                Toast.makeText(this, "正在刷新", Toast.LENGTH_SHORT).show();
            srl_pull_refresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    srl_pull_refresh.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        });
    }
}