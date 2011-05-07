package com.incrediblekids.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{
	public int iItemImgId;
	public int iItemWordId;
	public int iCardImgId;
	public int iCardWordId;
	public String strWordCharId;
	
	public Item(String _name, int _imgId, int _wordId, int _cardImgId, int _cardWordId){
		strWordCharId = _name;
		iItemImgId = _imgId;
		iItemWordId = _wordId;
		iCardImgId = _cardImgId;
		iCardWordId = _cardWordId;
	}
	
	public Item(Parcel src){
		strWordCharId = src.readString();
		iItemImgId = src.readInt();
		iItemWordId = src.readInt();
		iCardImgId = src.readInt();
		iCardWordId = src.readInt();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		dest.writeString(strWordCharId);
		dest.writeInt(iItemImgId);
		dest.writeInt(iItemWordId);
		dest.writeInt(iCardImgId);
		dest.writeInt(iCardWordId);
	}
	
	public static final Parcelable.Creator<Item> CREATOR =
		new Parcelable.Creator<Item>(){
		public Item createFromParcel(Parcel in){
			return new Item(in);
		}
		
		public Item[] newArray(int size){
			return new Item[size];
		}
	};
}