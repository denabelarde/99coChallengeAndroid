package com.ninetyninecochallenge.places.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ninetyninecochallenge.places.R;
import com.ninetyninecochallenge.places.dto.StoreDto;

public class StoresAdapter extends ArrayAdapter<StoreDto> {
	private final Context context;
	TextView tv1, tv2, tv3, tv4, tv5, tv6;
	ArrayList<StoreDto> storeList;

	public StoresAdapter(Context context, ArrayList<StoreDto> storeList) {
		super(context, R.layout.favoritestores_item, storeList);
		this.context = context;
		this.storeList = storeList;
		// TODO Auto-generated constructor stub
		// userdto = UserModel.getCurrentUser(context);
	}

	public class ViewHolder {
		TextView storename;
		TextView storedetails;
		// LoadImage loadImg;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = inflater
					.inflate(R.layout.favoritestores_item, parent, false);
			holder = new ViewHolder();
			holder.storename = (TextView) view.findViewById(R.id.store_name);
			holder.storedetails = (TextView) view
					.findViewById(R.id.store_subdetails);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		StoreDto storedto = storeList.get(position);

		holder.storename.setText(storedto.getName());
		holder.storedetails.setText(storedto.getAddress());
		return view;

	}

}
