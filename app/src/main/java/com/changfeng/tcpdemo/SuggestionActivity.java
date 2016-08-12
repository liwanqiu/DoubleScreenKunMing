package com.changfeng.tcpdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changfeng.tcpdemo.bean.Suggestion;
import com.changfeng.tcpdemo.listener.OnItemClickListener;
import com.changfeng.tcpdemo.listener.OnItemLongClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chang on 2016/8/10.
 */
public class SuggestionActivity extends BaseActivity {
    private static final String TAG = "SuggestionActivity";

    @BindView(R.id.recycler_view_suggestion)
    RecyclerView suggestionRecyclerView;

    @OnClick(R.id.button_add)
    void toAdd(View view) {
        Suggestion.SuggestionBean s = new Suggestion.SuggestionBean();
        s.setWeather("天气");
        s.setSuggestion("提示语");
        suggestions.add(s);
        adapter.updateDatas(suggestions);
    }
    @OnClick(R.id.button_finish)
    void toFinish(){
        finish();
    }

    Gson gson = new GsonBuilder().serializeNulls().create();
    String suggestionFileName;
    Suggestion  weatherSuggestion;
    private List<Suggestion.SuggestionBean> suggestions;

    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        ButterKnife.bind(this);


        try {
            suggestionFileName= Environment.getExternalStorageDirectory() + File.separator + Constants.SUGGESTION_FILE_NAME;
            weatherSuggestion = gson.fromJson(FileUtils.getStringFromFile(suggestionFileName), Suggestion.class);
            suggestions = weatherSuggestion.getSuggestion();
        } catch (Exception e) {
            weatherSuggestion = new Suggestion();
            suggestions = new ArrayList<>();
            Log.e(TAG, "onCreate: ", e);
        }

        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        // TODO: 2016/8/10
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        suggestionRecyclerView.setLayoutManager(layoutManager);



        adapter = new RecyclerViewAdapter(this, suggestions, new OnItemClickListener() {
            @Override
            public void onItemClickListener(final int position) {
                Log.i(TAG, "onItemClickListener: " + position);
                LinearLayout view = (LinearLayout) LayoutInflater.from(SuggestionActivity.this).inflate(R.layout.view_modify_suggestion, null);
                final EditText weatherEditText = (EditText) view.findViewById(R.id.edit_text_weather);
                final EditText suggestionEditText = (EditText) view.findViewById(R.id.edit_text_suggestion);
                weatherEditText.setText(suggestions.get(position).getWeather());
                suggestionEditText.setText(suggestions.get(position).getSuggestion());
                AlertDialog.Builder builder = new AlertDialog.Builder(SuggestionActivity.this)
                        .setTitle(getString(R.string.modify_suggestion))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        suggestions.get(position).setWeather(weatherEditText.getText().toString());
                        suggestions.get(position).setSuggestion(suggestionEditText.getText().toString());
                        adapter.updateDatas(suggestions);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setView(view);
                builder.create().show();
            }
        }, new OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(final int position) {
                Log.i(TAG, "onItemLongClickListener: " + position);
                LinearLayout view = (LinearLayout) LayoutInflater.from(SuggestionActivity.this).inflate(R.layout.view_delete_suggestion, null);
                final TextView weatherEditText = (TextView) view.findViewById(R.id.text_view_weather);
                final TextView suggestionEditText = (TextView) view.findViewById(R.id.text_view_suggestion);
                weatherEditText.setText(suggestions.get(position).getWeather());
                suggestionEditText.setText(suggestions.get(position).getSuggestion());
                AlertDialog.Builder builder = new AlertDialog.Builder(SuggestionActivity.this)
                        .setTitle(R.string.delete_suggestion)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        suggestions.remove(position);
                                        adapter.updateDatas(suggestions);
                                    }
                                }
                        ).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setView(view
                        );
                builder.create().show();
            }
        });
        suggestionRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        weatherSuggestion.setSuggestion(suggestions);
        try {
            FileUtils.writeFile(suggestionFileName, gson.toJson(weatherSuggestion));
            Toast.makeText(this, R.string.message_save_success, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "onDestroy: ", e);
            Toast.makeText(this, R.string.message_save_failed, Toast.LENGTH_LONG).show();
        }
        super.onDestroy();

    }
}
