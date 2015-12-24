package com.asdzheng.sweetshow.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.asdzheng.sweetshow.http.GsonRequest;
import com.example.asdzheng.bean.NewChannelInfoDetailDto;
import com.example.asdzheng.bean.NewChannelInfoDto;
import com.example.asdzheng.bean.UserInfo;
import com.example.asdzheng.testsame.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    GridView gridView;

    UserInfo info;

    RequestQueue queue;

    ImageAdapter adapter;

    List<NewChannelInfoDetailDto> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.gv_test);

        queue = Volley.newRequestQueue(this);

        list = new ArrayList<>();

        adapter = new ImageAdapter(list);

        gridView.setAdapter(adapter);

        GsonRequest<NewChannelInfoDto> request = new GsonRequest<>(Request.Method.GET, "http://v2.same.com/channel/1033563/senses", NewChannelInfoDto.class, new Response.Listener<NewChannelInfoDto>() {

            @Override
            public void onResponse(NewChannelInfoDto response) {
                if(response.getData().getResults() != null) {
//                    list.addAll(response.getData().getResults());
                    adapter.addAll(response.getData().getResults());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("main", error.toString());
            }
        } );

        queue.add(request);


    }


    public class ImageAdapter extends BaseAdapter {

        List<NewChannelInfoDetailDto> results;

        public ImageAdapter(List<NewChannelInfoDetailDto> results) {
            this.results = results;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addAll(List<NewChannelInfoDetailDto> list) {
            results.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder ;
            if(convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.adapter, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Log.i(" imageUrl ", results.get(position).photo + "");

            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(results.get(position).photo, holder.iv);
            return convertView;
        }

         class ViewHolder {
            ImageView iv;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
