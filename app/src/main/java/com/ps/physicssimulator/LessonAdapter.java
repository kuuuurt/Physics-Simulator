package com.ps.physicssimulator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;

public class LessonAdapter extends CursorAdapter {

    private static Context mContext;

    public static class ViewHolder {
        public final ImageView logoView;
        public final TextView titleView;
        public final TextView descView;


        public ViewHolder(View view) {
            logoView = (ImageView) view.findViewById(R.id.image_logo);
            titleView = (TextView) view.findViewById(R.id.text_title);
            descView = (TextView) view.findViewById(R.id.text_description);
        }
    }

    public LessonAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_chapter_lesson,
                viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.logoView.setImageResource(mContext.getResources().getIdentifier(
                cursor.getString(cursor
                        .getColumnIndex(DataContract.LessonEntry.COLUMN_LOGO)),
                "drawable", mContext.getPackageName()));
        viewHolder.titleView.setText(cursor.getString(cursor.getColumnIndex(
                DataContract.LessonEntry.COLUMN_NAME)));
        viewHolder.descView.setText(cursor.getString(cursor.getColumnIndex(
                DataContract.LessonEntry.COLUMN_DESCRIPTION)));

    }
}
