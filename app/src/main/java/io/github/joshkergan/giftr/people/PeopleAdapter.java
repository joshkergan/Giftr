package io.github.joshkergan.giftr.people;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView.Adapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.joshkergan.giftr.R;

/**
 * Created by Josh on 28/09/2016.
 */

public final class PeopleAdapter extends CursorAdapter{


	public PeopleAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

	}
}
