
package example;

import io.runon.commons.utils.FileUtil;
import io.runon.stock.trading.path.StockPaths;

/**
 * @author macle
 */
public class StockDirLength {
    public static void main(String[] args) {
        System.out.println("candle length: " + FileUtil.getDirLength(StockPaths.getSpotCandlePath("KOR")));
        System.out.println("credit loan length: " + FileUtil.getDirLength(StockPaths.getSpotCreditLoanPath("KOR")));
        System.out.println("investor length: " + FileUtil.getDirLength(StockPaths.getInvestorPath("KOR")));
        System.out.println("program length: " + FileUtil.getDirLength(StockPaths.getProgramPath("KOR")));
        System.out.println("short selling length: " + FileUtil.getDirLength(StockPaths.getShortSellingPath("KOR")));
        System.out.println("stock loan length: " + FileUtil.getDirLength(StockPaths.getStockLoanPath("KOR")));
        System.out.println("volume length: " + FileUtil.getDirLength(StockPaths.getVolumePowerPath("KOR")));
    }
}
