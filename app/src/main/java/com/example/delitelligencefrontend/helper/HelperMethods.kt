package com.example.delitelligencefrontend.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class HelperMethods {
    companion object {
        fun Double.toCurrency(currencyCode: String, locale: Locale = Locale.getDefault()): String {
            val format: NumberFormat = NumberFormat.getCurrencyInstance(locale)
            format.currency = Currency.getInstance(currencyCode)
            return format.format(this)
        }

        fun String.toByteArrayOrNull(): ByteArray? = try {
            Base64.decode(this, Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            Log.e("Base64", "Decoding error", e)
            null
        }

        fun ByteArray.toBitmapOrNull(): Bitmap? = try {
            BitmapFactory.decodeByteArray(this, 0, this.size)
        } catch (e: Exception) {
            Log.e("Bitmap", "Decoding error", e)
            null
        }
    }


    fun Bitmap.toBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}