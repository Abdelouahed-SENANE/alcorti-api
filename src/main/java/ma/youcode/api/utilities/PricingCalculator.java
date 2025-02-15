package ma.youcode.api.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeMap;

public class PricingCalculator {
    private static final TreeMap<Double , Double> MULTIPLIERS = new TreeMap<>();
    private static final Logger log = LogManager.getLogger(PricingCalculator.class);

    static {
        MULTIPLIERS.put(1.0, 2.0);
        MULTIPLIERS.put(2.0, 1.25);
        MULTIPLIERS.put(3.0, 1.0);
        MULTIPLIERS.put(4.0, 0.8);
        MULTIPLIERS.put(5.0, 0.7);
        MULTIPLIERS.put(6.0, 0.6);
        MULTIPLIERS.put(7.0, 0.5);
        MULTIPLIERS.put(8.0, 0.45);
        MULTIPLIERS.put(9.0, 0.42);
        MULTIPLIERS.put(10.0, 0.4);
        MULTIPLIERS.put(Double.MAX_VALUE, 0.39);
    }

    public static double calculatePrice(double volume , double distance) {

        if (volume <= 0 || distance <= 0) {
            throw new IllegalArgumentException("Volume and distance must be greater than 0");
        }



        double multiplier = MULTIPLIERS.ceilingEntry(volume).getValue();
        log.info("Multiplier for volume {} is {}", volume, multiplier);
        return volume * multiplier * distance;
    }
}
