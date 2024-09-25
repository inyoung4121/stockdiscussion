package in.stockcrawlingservice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateRangeFormatter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String[] getDateRange(int daysBack) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(daysBack);

        String formattedEndDate = endDate.format(formatter);
        String formattedStartDate = startDate.format(formatter);

        return new String[]{formattedStartDate, formattedEndDate};
    }

    public static String[] getMinusOneMinuteDateRange() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMinutes(1);

        String formattedEndDate = endDate.format(formatter);
        String formattedStartDate = startDate.format(formatter);

        return new String[]{formattedStartDate, formattedEndDate};
    }
}
