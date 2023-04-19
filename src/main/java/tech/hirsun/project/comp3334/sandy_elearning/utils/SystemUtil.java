package tech.hirsun.project.comp3334.sandy_elearning.utils;

import java.math.BigInteger;
import java.security.MessageDigest;


public class SystemUtil {

    private SystemUtil(){}

    public static String genToken(String src){
        if (null == src || "".equals(src)){
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

}
