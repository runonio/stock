package io.runon.stock.trading.data.management;

import io.runon.trading.CountryCode;

/**
 * @author macle
 */
public interface Spot1mCandleOutParam {
    String [] getExchanges();

    CountryCode getCountryCode();
}
