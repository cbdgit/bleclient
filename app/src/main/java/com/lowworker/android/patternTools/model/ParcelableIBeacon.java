package com.lowworker.android.patternTools.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by surecase on 15/07/14.
 */
public class ParcelableIBeacon implements Parcelable {

    protected String proximityUuid;
    protected String beaconId;
    protected int major;
    protected int minor;
    protected Double distance;
    private String longitude;
    private String latitude;
    protected int rssi;
    protected int txPower;
    protected String bluetoothAddress;
    protected Double runningAverageRssi;
    private String imageUrl;
    private String name;
    private String description;
    private String subscriptions_count;

    public ParcelableIBeacon() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubscriptions_count() {
        return subscriptions_count;
    }

    public void setSubscriptions_count(String subscriptions_count) {
        this.subscriptions_count = subscriptions_count;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public void setProximityUuid(String proximityUuid) {
        this.proximityUuid = proximityUuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }



    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public Double getRunningAverageRssi() {
        return runningAverageRssi;
    }

    public void setRunningAverageRssi(Double runningAverageRssi) {
        this.runningAverageRssi = runningAverageRssi;
    }

    protected ParcelableIBeacon(Parcel in) {
        proximityUuid = in.readString();
        beaconId = in.readString();
        major = in.readInt();
        minor = in.readInt();
        distance = in.readByte() == 0x00 ? null : in.readDouble();
        rssi = in.readInt();
        txPower = in.readInt();
        bluetoothAddress = in.readString();
        runningAverageRssi = in.readByte() == 0x00 ? null : in.readDouble();
        imageUrl= in.readString();
        name= in.readString();
        description= in.readString();
        subscriptions_count= in.readString();
        longitude = in.readString();
        latitude = in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proximityUuid);
        dest.writeString(beaconId);
        dest.writeInt(major);
        dest.writeInt(minor);

        if (distance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(distance);
        }
        dest.writeInt(rssi);
        dest.writeInt(txPower);
        dest.writeString(bluetoothAddress);
        if (runningAverageRssi == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(runningAverageRssi);
        }
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(subscriptions_count);
        dest.writeString(longitude);
        dest.writeString(latitude);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ParcelableIBeacon> CREATOR = new Parcelable.Creator<ParcelableIBeacon>() {
        @Override
        public ParcelableIBeacon createFromParcel(Parcel in) {
            return new ParcelableIBeacon(in);
        }

        @Override
        public ParcelableIBeacon[] newArray(int size) {
            return new ParcelableIBeacon[size];
        }
    };

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    @Override
    public String toString() {
        return "ParcelableIBeacon{" +
                "proximityUuid='" + proximityUuid + '\'' +
                ", beaconId='" + beaconId + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                ", distance=" + distance +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", rssi=" + rssi +
                ", txPower=" + txPower +
                ", bluetoothAddress='" + bluetoothAddress + '\'' +
                ", runningAverageRssi=" + runningAverageRssi +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subscriptions_count='" + subscriptions_count + '\'' +
                '}';
    }
}