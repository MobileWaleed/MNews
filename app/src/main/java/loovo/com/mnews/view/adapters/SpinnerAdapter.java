package loovo.com.mnews.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import loovo.com.mnews.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private String[] langs;
    private int[] flags;
    @BindView(R.id.tv_spinnervalue)
    TextView langLabel;
    @BindView(R.id.lang_icon)
    ImageView langIcon;

    public SpinnerAdapter(Context context, int textViewResourceId, String[] objects, int[] flags) {
        super(context, textViewResourceId, objects);
        this.langs = objects;
        this.flags = flags;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_spinner, parent, false);
        ButterKnife.bind(this, row);
        langLabel.setText(langs[position]);
        langIcon.setImageResource(flags[position]);
        return row;
    }
}