package com.ace.member.utils;


import com.ace.member.bean.CurrencyMarket;

public class CurrencyUtil {

	public static double transfer(CurrencyMarket currencyMarket, String sourceCurrency, String targetCurrency, double amount) {
		if (currencyMarket == null) return 0;
		if (currencyMarket.getCurrencySource().toLowerCase().equals(sourceCurrency.toLowerCase()) && currencyMarket.getCurrencyTarget().toLowerCase().equals(targetCurrency.toLowerCase())) {
			return amount / Math.min(Math.max(currencyMarket.getExchangeBuy(), currencyMarket.getExchangeMin()), currencyMarket.getExchangeMax());
		} else if (currencyMarket.getCurrencySource().toLowerCase().equals(targetCurrency.toLowerCase()) && currencyMarket.getCurrencyTarget().toLowerCase().equals(sourceCurrency.toLowerCase())) {
			return amount / Math.min(Math.max(currencyMarket.getExchangeSell(), currencyMarket.getExchangeMin()), currencyMarket.getExchangeMax());
		}
		return 0;
	}
}
