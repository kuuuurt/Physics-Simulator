package com.ps.physicssimulator;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;

/**
 * Created by qwerasdf on 7/22/16.
 */
public class ConstantAdapter extends CursorAdapter {


    private static Context mContext;
    public static class ViewHolder {
        public final TextView symbolView;
        public final TextView titleView;
        public final TextView descView;


        public ViewHolder(View view) {
            symbolView = (TextView) view.findViewById(R.id.text_symbol);
            titleView = (TextView) view.findViewById(R.id.text_title);
            descView = (TextView) view.findViewById(R.id.text_description);
        }
    }

    public ConstantAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_constant,
                viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Cursor constant = mContext.getContentResolver().query(
                DataContract.ConstantEntry.buildConstantUri(cursor.getLong(cursor.getColumnIndex(
                        DataContract.FormulaConstantEntry.COLUMN_CONSTANT_KEY))),
                null,
                null,
                null,
                null
        );
        constant.moveToFirst();
        viewHolder.symbolView.setText(constant.getString(constant.getColumnIndex(
                DataContract.ConstantEntry.COLUMN_SYMBOL)));
        viewHolder.titleView.setText(constant.getString(constant.getColumnIndex(
                DataContract.ConstantEntry.COLUMN_NAME)));
        viewHolder.descView.setText(constant.getString(constant.getColumnIndex(
                DataContract.ConstantEntry.COLUMN_DESC)));

    }
}
