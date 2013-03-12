//  16:43   10.03.2013 
package com.example.android_test_assignment.model;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class PointData extends Point {

	public long id;

	public PointData(){
	}

	public PointData(int x, int y) {
		super(x, y);
	}

	public PointData(long id, int x, int y) {
		super(x, y);
		this.id = id;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeInt(x);
		out.writeInt(y);
	}

	@Override
	public void readFromParcel(Parcel in) {
		id = in.readLong();
		x = in.readInt();
		y = in.readInt();
	}

	public static final Parcelable.Creator<PointData> CREATOR = new Parcelable.Creator<PointData>() {

		public PointData createFromParcel(Parcel in) {
			PointData r = new PointData();
			r.readFromParcel(in);
			return r;
		}

		public PointData[] newArray(int size) {
			return new PointData[size];
		}
	};


}
