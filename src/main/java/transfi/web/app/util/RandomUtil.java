package transfi.web.app.util;

import java.util.Random;

public class RandomUtil {

    public Long generateRandom(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return Long.parseLong(sb.toString());

    }
}
