package com.ninetyninecochallenge.places;

import java.util.ArrayList;

import com.ninetyninecochallenge.places.adapters.StoresAdapter;
import com.ninetyninecochallenge.places.dto.StoreDto;
import com.ninetyninecochallenge.places.models.FavoriteStoreModel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteListFragment extends ListFragment {

	StoresAdapter storeAdapter;
	ArrayList<StoreDto> storeList = new ArrayList<StoreDto>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		storeList = FavoriteStoreModel.getAllFavorites(getActivity());
		storeAdapter = new StoresAdapter(getActivity(), storeList);
		setListAdapter(storeAdapter);

		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("Favoritelist onresume");

		super.onResume();
	}
}
