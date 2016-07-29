package com.ps.physicssimulator;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        return intent;
        //return intent.putExtra(Intent.EXTRA_TEXT, mChapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(CONSTANT_LOADER, null, this);


        Intent intent = getIntent();
        if(intent != null){
            formulaName = intent.getStringExtra("formulaName");
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

                AlertDialog.Builder builder = new AlertDialog.Builder(ConstantActivity.this);
                LayoutInflater inflater = ConstantActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_change_constant, null);
                TextView txtDefault = (TextView)dialogView.findViewById(R.id.text_default_value);
                txtDefault.setText(txtDefault.getText() + ": " + data.getString(data.getColumnIndex(DataContract.ConstantEntry.COLUMN_DEFAULT)));

                TextView txtCurrent = (TextView)dialogView.findViewById(R.id.text_current_value);
                txtCurrent.setText(txtCurrent.getText() + ": " + data.getString(data.getColumnIndex(DataContract.ConstantEntry.COLUMN_CURRENT)));

                builder.setTitle(data.getString(data.getColumnIndex(DataContract.ConstantEntry.COLUMN_NAME)));
                builder.setView(dialogView)
                         .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

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

                             }
                         });


                AlertDialog dialog = builder.create();




                dialog.show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataContract.ConstantEntry.buildConstantFormula("Free-fall Velocity"),
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
