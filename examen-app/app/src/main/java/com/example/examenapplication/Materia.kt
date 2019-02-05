package com.example.examenapplication

import android.os.Parcel
import android.os.Parcelable


class Materia(var id:Int, var codigoBarra: Int, var nombre: String,
              var codigo: Int,
              var descripcion: String,
              var activo: Boolean,
              var fechaCreacion: String,
              var numeroHorasPorSemana: Int):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(codigoBarra)
        parcel.writeString(nombre)
        parcel.writeInt(codigo)
        parcel.writeString(descripcion)
        parcel.writeByte(if (activo) 1 else 0)
        parcel.writeString(fechaCreacion)
        parcel.writeInt(numeroHorasPorSemana)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Materia> {
        override fun createFromParcel(parcel: Parcel): Materia {
            return Materia(parcel)
        }

        override fun newArray(size: Int): Array<Materia?> {
            return arrayOfNulls(size)
        }
    }
}