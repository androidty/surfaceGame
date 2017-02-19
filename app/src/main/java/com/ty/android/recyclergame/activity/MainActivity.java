package com.ty.android.recyclergame.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Toast;

import com.ty.android.recyclergame.R;
import com.ty.android.recyclergame.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private List<String> stringList;
    private List<String> idiomList;
    private CardView screenCard;

    private boolean startGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_view);
        screenCard = (CardView) findViewById(R.id.screen_card);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        stringList = new ArrayList<>();
//        String s = "";
//        for (int i = 100; i > 0; i--) {
//            s = i + "";
//            if (i == 99) {
//                s = "天";
//            }
//            stringList.add(s);
//        }


        initData();


        initEvent();
    }

    //随机取出的成语
    String s0 = "";

    private void initData() {
        //先得到结果集
        idiomList = new ArrayList<>();
        String allWord = "";

        String[] results = getResources().getStringArray(R.array.idioms);
        for (String s : results
                ) {
            allWord += s;
            idiomList.add(s);
        }
        //得到adapter里的数据集
        int flags = 0;
        Random random = new Random();
        for (int i = 1; i <= 96; i++) {
            int c = random.nextInt(allWord.length());
            String ch = allWord.charAt(c) + "";
            if (i % 4 == 0) {
                int ran = random.nextInt(4);
                if (flags == 4) {
                    flags = 0;
                }

                if (flags == 0) {
                    int x = random.nextInt(idiomList.size());
                    s0 = idiomList.get(x);
                }

                stringList.add(i - ran - 1, s0.charAt(3 - flags) + "");
                flags++;
            } else {
                stringList.add(ch);
            }
        }
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        myAdapter = new MyAdapter(this, stringList, 1);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (myAdapter.isFooterItem(position) || myAdapter.isHeaderItem(position)) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.scrollToPosition(myAdapter.getItemCount()-1);
    }

    String result = "";
    int flag = 0;

    private void initEvent() {
        myAdapter.setmCallback(new MyAdapter.Callback() {
            @Override
            public void click(int position) {
//                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "size:"+stringList.size(), Toast.LENGTH_SHORT).show();
                if(!startGame){
                    return ;
                }
                if (stringList.size() - position > 3 ) {
                    return;
                }
//                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();

                int m = stringList.size() - position;
                result += stringList.get(position-1);
                switch (m) {
                    case 0:
                        for (int i = 0; i < 4; i++) {
                            stringList.remove(position - i-1);
                            myAdapter.notifyItemRemoved(position - i-1);
                        }
                        break;
                    case 1:
                        for (int i = 0; i < 4; i++) {
                            stringList.remove(position - i );
                            myAdapter.notifyItemRemoved(position - i + 1);
                        }
                        break;
                    case 2:
                        for (int i = 0; i < 4; i++) {
                            stringList.remove(position - i + 1);
                            myAdapter.notifyItemRemoved(position - i + 2);
                        }
                        break;
                    case 3:
                        for (int i = 0; i < 4; i++) {
                            stringList.remove(position - i + 2);
                            myAdapter.notifyItemRemoved(position - i + 3);
                        }
                        break;
                }


                flag++;
                if (flag == 4) {
                Toast.makeText(MainActivity.this, ""+result
                        , Toast.LENGTH_SHORT).show();
                    if (checkStr(result)) {
                        Toast.makeText(MainActivity.this, "victory", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                    flag = 0;
                    result = "";
                }

                myAdapter.setInfos(stringList);
            }

            @Override
            public void footClick(int position) {
                startGame = true;
//                screenCard.setVisibility(View.VISIBLE);
                myAdapter.setFooterCount(0);
                myAdapter.notifyItemRemoved(position);

            }
        });
    }

    private boolean checkStr(String str) {
        if (idiomList.contains(str)) {
            return true;

        }
        return false;
    }
}
