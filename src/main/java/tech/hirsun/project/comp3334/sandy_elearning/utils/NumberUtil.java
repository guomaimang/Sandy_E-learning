package tech.hirsun.project.comp3334.sandy_elearning.utils;


public class NumberUtil {

    private NumberUtil() {
    }

    // generate random number
    public static int genRandomNum(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }
}
