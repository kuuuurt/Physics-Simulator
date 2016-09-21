package com.ps.physicssimulator;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ps.physicssimulator.data.DataContract;

public class ConstantActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CONSTANT_LOADER = 0;
    private static ConstantAdapter mConstantAdap;
    private static String formulaName;
    private static Intent backIntent;

    @Override
    public Intent getSupportParentActivityIntent() {
        return backIntent;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void finish() {
        Intent intent = getSupportParentActivityIntent();
        setResult(RESULT_OK, intent);

        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(CONSTANT_LOADER, null, this);


        Intent intent = getIntent();
        if(intent != null){
            formulaName = intent.getStringExtra("formulaName");
            Bundle b = intent.getExtras();
            backIntent = intent;
//            backIntent = super.getSupportParentActivityIntent();
//            backIntent.putExtra("currentChapter", b.getInt("currentChapter"));
//            backIntent.putExtra("currentLesson", b.getInt("currentLesson"));
//            backIntent.putExtra("currentFormula", b.getInt("currentFormula"));
//            backIntent.putExtra("currentVariable", b.getInt("currentVariable"));
//            backIntent.putExtra("formulaName", b.getString("formulaName"));
//            backIntent.putExtra("size", b.getInt("size"));
//            int size = intent.getIntExtra("size", 0);
//            for (int i = 0; i < size; i++) {
//                backIntent.putExtra("value" + i, b.getStringArray("value" + i));
//            }
        }

        setTitle("Constants in " + formulaName);

        mConstantAdap = new ConstantAdapter(this, null, true);

        ListView constantList = (ListView) findViewById(R.id.list_view_constants);
        constantList.setAdapter(mConstantAdap);
        constantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor data = mConstantAdap.getCursor();
                data.move(i);

                final Cursor constant = ConstantActivity.this.getContentResolver().query(
                        DataContract.ConstantEntry.buildConstantUri(data.getLong(data.getColumnIndex(
                                DataContract.FormulaConstantEntry.COLUMN_CONSTANT_KEY))),
                        null,
                        null,
                        null,
                        null
                );

                constant.moveToFirst();
                AlertDialog.Builder builder = new AlertDialog.Builder(ConstantActivity.this);
                LayoutInflater inflater = ConstantActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_change_constant, null);
                TextView txtDefault = (TextView)dialogView.findViewById(R.id.text_default_value);
                txtDefault.setText(txtDefault.getText() + ": " + constant.getString(constant.getColumnIndex(DataContract.ConstantEntry.COLUMN_DEFAULT)));

                TextView txtCurrent = (TextView)dialogView.findViewById(R.id.text_current_value);
                txtCurrent.setText(txtCurrent.getText() + ": " + constant.getString(constant.getColumnIndex(DataContract.ConstantEntry.COLUMN_CURRENT)));

                builder.setTitle(constant.getString(constant.getColumnIndex(DataContract.ConstantEntry.COLUMN_NAME)));
                builder.setView(dialogView)
                         .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 TextInputEditText txtNewValue = (TextInputEditText)dialogView.findViewById(R.id.input_new_value);

                                 ContentValues values = new ContentValues();
                                 values.put(DataContract.ConstantEntry.COLUMN_CURRENT, Double.parseDouble(String.valueOf(txtNewValue.getText())));

                                 ConstantActivity.this.getContentResolver().update(
                                         DataContract.ConstantEntry.CONTENT_URI,
                                         values,
                                         DataContract.ConstantEntry._ID + " = ?",
                                         new String[] { Long.toString(constant.getLong(constant.getColumnIndex(DataContract.ConstantEntry._ID)))}
                                 );
                                 Snackbar.make(findViewById(android.R.id.content), "Constant Updated", Snackbar.LENGTH_SHORT).show();
                             }
                         })
                         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                             }
                         })
                         .setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 ContentValues values = new ContentValues();
                                 values.put(DataContract.ConstantEntry.COLUMN_CURRENT, constant.getDouble(constant.getColumnIndex(DataContract.ConstantEntry.COLUMN_DEFAULT)));

                                 ConstantActivity.this.getContentResolver().update(
                                         DataContract.ConstantEntry.CONTENT_URI,
                                         values,
                                         DataContract.ConstantEntry._ID + " = ?",
                                         new String[] { Long.toString(constant.getLong(constant.getColumnIndex(DataContract.ConstantEntry._ID)))}
                                 );
                                 Snackbar.make(findViewById(android.R.id.content), "Constant Reverted to Default Value", Snackbar.LENGTH_SHORT).show();
                             }
                         })
                .create().show();

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataContract.FormulaConstantEntry.buildFormulaConstant("Free-fall Velocity"),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mConstantAdap.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mConstantAdap.swapCursor(null);
    }
}
