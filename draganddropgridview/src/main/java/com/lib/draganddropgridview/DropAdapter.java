package com.lib.draganddropgridview;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 드래그 어댑터
 * @author jhson
 *
 */
public abstract class DropAdapter extends BaseAdapter{

	private List<DropModel> mList = null;
	protected Context mContext = null;

	public DropAdapter(Context context, List<DropModel> list){
		mList = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	public List<DropModel> getItems(){
		return mList;
	}

}
