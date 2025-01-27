package com.example.delitelligencefrontend.helper

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
    }
}