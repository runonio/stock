
package example;

import io.runon.commons.utils.FileUtils;
import io.runon.stock.trading.path.StockPaths;

/**
 * @author macle
 */
public class StockDirLength {
    public static void main(String[] args) {
        System.out.println("candle length: " + FileUtils.getDirLength(StockPaths.getSpotCandlePath("KOR")));
        System.out.println("credit loan length: " + FileUtils.getDirLength(StockPaths.getSpotCreditLoanPath("KOR")));
        System.out.println("investor length: " + FileUtils.getDirLength(StockPaths.getInvestorPath("KOR")));
        System.out.println("program length: " + FileUtils.getDirLength(StockPaths.getProgramPath("KOR")));
        System.out.println("short selling length: " + FileUtils.getDirLength(StockPaths.getShortSellingPath("KOR")));
        System.out.println("stock loan length: " + FileUtils.getDirLength(StockPaths.getStockLoanPath("KOR")));
        System.out.println("volume length: " + FileUtils.getDirLength(StockPaths.getVolumePowerPath("KOR")));
    }
}
