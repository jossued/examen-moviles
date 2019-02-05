package com.example.examenapplication

import android.os.Parcel
import android.os.Parcelable

class Estudiante(var id:Int, var nombres: String, var apellidos: String,
              var semestreActual: Int,
              var fechaNacimiento: String,
              var graduado: Boolean): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombres)
        parcel.writeString(apellidos)
        parcel.writeInt(semestreActual)
        parcel.writeString(fechaNacimiento)
        parcel.writeByte(if (graduado) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Estudiante> {
        override fun createFromParcel(parcel: Parcel): Estudiante {
            return Estudiante(parcel)
        }

        override fun newArray(size: Int): Array<Estudiante?> {
            return arrayOfNulls(size)
        }
    }
}

