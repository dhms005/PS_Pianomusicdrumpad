package com.pianomusicdrumpad.pianokeyboard.Piano.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.TreeMap;

import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.ChordsGameActivity;
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.ProgressListActivity;
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.ScalesGameActivity;
import com.pianomusicdrumpad.pianokeyboard.Piano.Activity.UserSolosActivity;
import com.pianomusicdrumpad.pianokeyboard.Piano.managers.ProgressHelper;
import com.pianomusicdrumpad.pianokeyboard.R;

public class ProgressListAdapter extends BaseAdapter implements Filterable {
    private static float dipBigTextSize = 20.0f;
    private static float dipSmallTextSize = 15.0f;
    private static String lastExerciseText;
    public static TreeMap<String, String> progressMap;
    private static int progressMapIdx;
    private static SharedPreferences sharedPrefs;
    private HashMap<String, Integer> alphaIndexer;
    public Activity context;
    public ListView listview;
    private LayoutInflater mInflater;
    private String[] sections;

    public Filter getFilter() {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {


        TextView exerciseHeaderLine;
        TextView levelTextView;
        TextView percentTextView;
        RelativeLayout lineItem;
        TextView scoreTextView;
        ImageView starImageView;
        ImageView trendImage;

        ViewHolder() {
        }
    }

    public ProgressListAdapter(Activity context2) {
        this.mInflater = LayoutInflater.from(context2);
        this.context = context2;
        readProgressMap(context2);
    }

    public static void readProgressMap(Context context2) {
        progressMap = (TreeMap) ProgressHelper.openProgressMapFromPrefs(context2);
        //Log.v("saveProgress", "progressMap Read from onResume in ListActivity");
    }

    public int getCount() {
        return progressMap.size();
    }

    public Object getItem(int i) {
        String str = (String) progressMap.keySet().toArray()[i];
        String str2 = progressMap.get(str);
        //Log.v("ProgressListAdapter", "getItem at position:" + i + " key:" + str + " value:" + str2);
        return str2;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        //Log.v("ProgressListAdapter", "getView position:" + i + " idx:" + progressMapIdx);
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = this.mInflater.inflate(R.layout.progress_adapter_item, viewGroup, false);
            viewHolder.lineItem = (RelativeLayout) view2.findViewById(R.id.lineItem);
            viewHolder.exerciseHeaderLine = (TextView) view2.findViewById(R.id.exerciseHeaderLine);
            viewHolder.starImageView = (ImageView) view2.findViewById(R.id.starImageView);
            viewHolder.levelTextView = (TextView) view2.findViewById(R.id.levelTextView);
            viewHolder.scoreTextView = (TextView) view2.findViewById(R.id.scoreTextView);
            viewHolder.percentTextView = (TextView) view2.findViewById(R.id.percentTextView);
            viewHolder.trendImage = (ImageView) view2.findViewById(R.id.trendImage);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        String str = (String) progressMap.keySet().toArray()[i];
        String str2 = progressMap.get(str);
        String exerciseFromKey = ProgressHelper.getExerciseFromKey(str);
        viewHolder.exerciseHeaderLine.setText(exerciseFromKey.toUpperCase());
        viewHolder.exerciseHeaderLine.setTextColor(Color.parseColor("#FFFFFF"));
        //Log.v("ProgressListAdapter", "exerciseText:" + exerciseFromKey + " lastExerciseText:" + lastExerciseText + " pos:" + i);
        String levelFromKey = ProgressHelper.getLevelFromKey(str);
        viewHolder.levelTextView.setText(levelFromKey);
        viewHolder.levelTextView.setTextColor(Color.parseColor("#2f76b4"));
        if (levelFromKey.length() >= 12) {
            viewHolder.levelTextView.setTextSize(dipSmallTextSize);
        } else {
            viewHolder.levelTextView.setTextSize(dipBigTextSize);
        }
        String scoreFromValue = ProgressHelper.getScoreFromValue(str2);
        viewHolder.scoreTextView.setText(scoreFromValue);
        viewHolder.scoreTextView.setTextColor(Color.parseColor("#2f76b4"));
        if (scoreFromValue.length() >= 5) {
            viewHolder.scoreTextView.setTextSize(dipSmallTextSize);
        } else {
            viewHolder.scoreTextView.setTextSize(dipBigTextSize);
        }
        viewHolder.percentTextView.setText(ProgressHelper.getPercentageFromValue(str2));
        int percentageIntFromValue = ProgressHelper.getPercentageIntFromValue(str2);
        viewHolder.percentTextView.setTextColor(Color.parseColor(ProgressHelper.getColorFromPercentage(percentageIntFromValue)));
        int numberCorrectFromValue = ProgressHelper.getNumberCorrectFromValue(str2);
        if (numberCorrectFromValue >= 20 && percentageIntFromValue >= 50) {
            viewHolder.starImageView.setImageResource(R.drawable.three_star_98x30);
        } else if (numberCorrectFromValue >= 10) {
            viewHolder.starImageView.setImageResource(R.drawable.two_star_98x30);
        } else if (numberCorrectFromValue >= 3) {
            viewHolder.starImageView.setImageResource(R.drawable.one_star_98x30);
        } else {
            viewHolder.starImageView.setImageResource(R.drawable.no_star_98x30);
        }
        int trendFromValue = ProgressHelper.getTrendFromValue(str2);
        if (trendFromValue == 1) {
            viewHolder.trendImage.setImageResource(R.drawable.up_green_arrow_60x60);
        } else if (trendFromValue == 3) {
            viewHolder.trendImage.setImageResource(R.drawable.red_arrow_down_60x60);
        } else {
            viewHolder.trendImage.setImageResource(R.drawable.orange_arrow_60x60);
        }
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                        String str = (String) ProgressListAdapter.progressMap.keySet().toArray()[i];
                        //Log.v("ProgressListAdapter", "Item Clicked: at position:" + i + " key:" + str + " value:" + ProgressListAdapter.progressMap.get(str));
                        String[] split = str.split("[|]");
                        int parseInt = Integer.parseInt(split[0]);
                        String str2 = split[1];
                        if (parseInt == 1) {
                            Intent intent = new Intent(ProgressListAdapter.this.context, ScalesGameActivity.class);
                            intent.putExtra("LEVEL_FROM_PROGRESS", str2);
                            ProgressListAdapter.this.context.startActivity(intent);
                        } else if (parseInt == 2) {
                            Intent intent2 = new Intent(ProgressListAdapter.this.context, ChordsGameActivity.class);
                            intent2.putExtra("LEVEL_FROM_PROGRESS", str2);
                            ProgressListAdapter.this.context.startActivity(intent2);
                        } else {
                            ((ProgressListActivity) ProgressListAdapter.this.context).goBack();
                        }

            }
        });
        return view2;
    }
}
