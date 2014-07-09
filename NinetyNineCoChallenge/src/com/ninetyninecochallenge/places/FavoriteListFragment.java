package com.ninetyninecochallenge.places;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.ninetyninecochallenge.places.adapters.StoresAdapter;
import com.ninetyninecochallenge.places.dto.StoreDto;
import com.ninetyninecochallenge.places.models.FavoriteStoreModel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FavoriteListFragment extends ListFragment {

	StoresAdapter storeAdapter;
	ArrayList<StoreDto> storeList = new ArrayList<StoreDto>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		storeList = FavoriteStoreModel.getAllFavorites(getActivity());
		storeAdapter = new StoresAdapter(getActivity(), storeList);
		setListAdapter(storeAdapter);
//		getListView().setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> myAdapter, View myView,
//					int myItemInt, long mylng) {
//				StoreDto storeDto = storeList.get(myItemInt);
//				LatLng storeLocation = new LatLng(storeDto.getLat(), storeDto
//						.getLonghi());
//				((MainActivity) getActivity())
//						.plotUserLocationAndFavoriteStore(storeLocation,storeDto);
//			}
//		});
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> myAdapter, View myView,
				int myItemInt, long mylng) {
			StoreDto storeDto = storeList.get(myItemInt);
			LatLng storeLocation = new LatLng(storeDto.getLat(), storeDto
					.getLonghi());
			((MainActivity) getActivity())
					.plotUserLocationAndFavoriteStore(storeLocation,storeDto);
		}
	});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("Favoritelist onresume");

		super.onResume();
	}
}
