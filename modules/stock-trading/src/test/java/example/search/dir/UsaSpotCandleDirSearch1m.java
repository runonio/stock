package example.search.dir;


import io.runon.commons.utils.FileUtil;
import io.runon.stock.trading.path.StockPaths;
import io.runon.trading.CountryCode;

import java.io.File;

/**
 * @author macle
 */
public class UsaSpotCandleDirSearch1m {
    public static void main(String[] args) {
        File[] files = FileUtil.getDirs(new File(StockPaths.getSpotCandlePath(CountryCode.USA, null)), "1m");
        File[] files1d = FileUtil.getDirs(new File(StockPaths.getSpotCandlePath(CountryCode.USA, null)), "1d");

        System.out.println("1m: " + files.length +", 1d: " + files1d.length);
//        for(File file : files){
//            System.out.println(file.getAbsolutePath());
//        }

    }
}
