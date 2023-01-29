package io.runon.stock.ds;

import java.math.BigDecimal;

/**
 * 주식 발행 내역
 * @author macle
 */
public class ValidNullCheck {

    public static boolean equals(String source, String target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }

        return source.equals(target);

    }

    public static boolean equals(Long source, Long target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }


        return source.longValue() == target.longValue();

    }

    public static boolean equals(Integer source, Integer target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }


        return source.intValue() == target.intValue();

    }

    public static boolean equals(BigDecimal source, BigDecimal target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }


        return source.compareTo(target) == 0;

    }


}
