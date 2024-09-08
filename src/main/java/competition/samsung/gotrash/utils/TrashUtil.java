package competition.samsung.gotrash.utils;

import competition.samsung.gotrash.constant.TrashConstant;
import competition.samsung.gotrash.entity.Trash;

import java.math.BigInteger;
import java.util.Objects;

public class TrashUtil {

    public static BigInteger GetTrashCoin(Trash trash){
        BigInteger coin = BigInteger.valueOf(0);
        if(Objects.equals(trash.getCategory(), TrashConstant.KERTAS)){
            coin = BigInteger.valueOf(10);
        }else if(Objects.equals(trash.getCategory(), TrashConstant.PLASTIK)){
            coin = BigInteger.valueOf(20);
        }else if(Objects.equals(trash.getCategory(), TrashConstant.LOGAM)){
            coin = BigInteger.valueOf(15);
        }else{
            coin = BigInteger.valueOf(5);
        }

        return coin;
    }

    public static BigInteger GetTrashRating(Trash trash){
        BigInteger rating = BigInteger.valueOf(0);
        if(Objects.equals(trash.getCategory(), TrashConstant.KERTAS)){
            rating = BigInteger.valueOf(16);
        }else if(Objects.equals(trash.getCategory(), TrashConstant.PLASTIK)){
            rating = BigInteger.valueOf(32);
        }else if(Objects.equals(trash.getCategory(), TrashConstant.LOGAM)){
            rating = BigInteger.valueOf(25);
        }else{
            rating = BigInteger.valueOf(10);
        }

        return rating;
    }
}
