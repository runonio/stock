package example.search.dir;


import io.runon.commons.utils.FileUtils;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.CountryCode;

import java.io.File;

/**
 * @author macle
 */
public class KorSpotCandleDirSearch1m {
    public static void main(String[] args) {
        File[] files = FileUtils.getDirs(new File(StockPaths.getSpotCandlePath(CountryCode.KOR, "KRX")) ,"1m");
        File[] files1d = FileUtils.getDirs(new File(StockPaths.getSpotCandlePath(CountryCode.KOR, "KRX")), "1d");

        System.out.println("1m: " + files.length +", 1d: " + files1d.length);


    }
}
