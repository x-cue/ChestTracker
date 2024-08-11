package com.xcue.lib.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {
    public static String getTimeSince(ZonedDateTime startTime, ZonedDateTime endTime) {
        return formatSeconds(endTime.toEpochSecond() - startTime.toEpochSecond());
    }

    /**
     * @param seconds Amount of seconds
     * @return Formatted string that goes up to days
     */
    public static String formatSeconds(long seconds) {
        long days = seconds / 60 / 60 / 24;
        long hours = seconds / 60 / 60 % 24;
        long minutes = seconds / 60 % 60;
        seconds = seconds % 60;

        String time = "";
        if (days > 0) {
            time += days + "d ";
            time += String.format("%02dh ", hours);
            time += String.format("%02dm ", minutes);
        } else if (hours > 0) {
            time += hours + "h ";
            time += String.format("%02dm ", minutes);
        } else if (minutes > 0) {
            time += minutes + "m ";
        }
        time += String.format("%02ds", seconds);

        return time;
    }

    public static ZonedDateTime now(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    /**
     *
     * @return Current ZonedDateTime using system default zone
     */
    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.systemDefault());
    }
}
