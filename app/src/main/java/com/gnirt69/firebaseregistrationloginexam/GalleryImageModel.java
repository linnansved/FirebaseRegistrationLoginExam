package com.gnirt69.firebaseregistrationloginexam;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryImageModel implements Parcelable {

    String name, url;

    public GalleryImageModel() {

    }

    protected GalleryImageModel(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<GalleryImageModel> CREATOR = new Creator<GalleryImageModel>() {
        @Override
        public GalleryImageModel createFromParcel(Parcel in) {
            return new GalleryImageModel(in);
        }

        @Override
        public GalleryImageModel[] newArray(int size) {
            return new GalleryImageModel[size];
        }
    };

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }
}

