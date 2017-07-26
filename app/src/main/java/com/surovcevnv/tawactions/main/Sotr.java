package com.surovcevnv.tawactions.main;

/**
 * Created by surovcevnv on 26.07.17.
 */
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Этот класс описывает сотрудника
 */
public class Sotr implements Parcelable {

    /** Фио **/
    public String name_sotr;

    /** Табельный номер */
    public String id_sotr;

    /** Тип интерфейса */
    public String id_role;

    /** Должность */
    public String name_dolgn;


    public Sotr(String name_sotr, String id_sotr, String id_role, String name_dolgn) {
        this.name_sotr = name_sotr;
        this.id_sotr = id_sotr;
        this.id_role = id_role;
        this.name_dolgn = name_dolgn;
    }

    protected Sotr(Parcel in) {
        name_sotr = in.readString();
        id_sotr = in.readString();
        id_role = in.readString();
        name_dolgn = in.readString();
    }

    public static final Creator<Sotr> CREATOR = new Creator<Sotr>() {
        @Override
        public Sotr createFromParcel(Parcel in) {
            return new Sotr(in);
        }

        @Override
        public Sotr[] newArray(int size) {
            return new Sotr[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name_sotr);
        parcel.writeString(id_sotr);
        parcel.writeString(id_role);
        parcel.writeString(name_dolgn);
    }
}
