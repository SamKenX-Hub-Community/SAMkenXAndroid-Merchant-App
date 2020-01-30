package com.bitcoin.merchant.app.util

import android.content.Context
import android.util.Log
import com.bitcoin.merchant.app.model.CountryCurrency
import java.text.NumberFormat
import java.util.*

class AmountUtil(private val context: Context) {
    fun formatFiat(amountFiat: Double): String {
        val currency = AppUtil.getCurrency(context)
        val country = AppUtil.getCountryIso(context)
        val locale = AppUtil.getLocale(context)
        val cc: CountryCurrency = CountryCurrency.get(context, currency, country, locale)
        var fiat: String? = null
        try {
            val formatter = NumberFormat.getCurrencyInstance(cc.locale)
            val instance = Currency.getInstance(currency)
            formatter.currency = instance
            formatter.maximumFractionDigits = instance.defaultFractionDigits
            fiat = formatter.format(amountFiat)
        } catch (e: Exception) {
            Log.d(TAG, "Locale not supported for $currency failed to format to fiat: $amountFiat")
        }
        fiat = if (fiat != null) {
            val currencySign = "\u00a4"
            fiat.replace(currencySign, currency)
        } else {
            currency + " " + MonetaryUtil.instance.fiatDecimalFormat.format(amountFiat)
        }
        return fiat
    }

    fun formatBch(amountBch: Double): String {
        return MonetaryUtil.instance.bchDecimalFormat.format(amountBch) + " " + DEFAULT_CURRENCY_BCH
    }

    companion object {
        const val TAG = "AmountUtil"
        const val DEFAULT_CURRENCY_BCH = "BCH"
    }
}