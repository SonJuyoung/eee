package ieetu.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    /**
     * 년-월-일 을 입력받아 요일을 반환한다.
     * dateString 의 형식은 yyyy-MM-dd / yyyy.MM.dd / yyyyMMdd / yyyy/MM/dd 형식을 지원한다.
     * default 로 TestStyle.FULL 스타일을 적용한다.(ex. 목요일)
     * TextStyle 을 변경하여 다양한 형태로 요일을 출력할 수 있다.(ex. TextStyle.SHORT -> 목)
     */
    public static String dateToDayOfWeek(String dateString) throws Exception {
        return getDayOfWeek(dateString, TextStyle.FULL);
    }

    public static String dateToDayOfWeek(String dateString, TextStyle textStyle) throws Exception {
        return getDayOfWeek(dateString, textStyle);
    }

    private static String getDayOfWeek(String dateString, TextStyle textStyle) throws Exception {
        LocalDate date = getLocalDate(dateString);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(textStyle, Locale.KOREA);
    }

    public static LocalDateTime timestampToDate(long timestampStr) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampStr), TimeZone.getDefault().toZoneId());
    }

    /**
     * 오늘기준 특정 요일의 날짜 구하기
     * isNext 로 이전, 이후를 결정
     * 오늘은 이전에 포함
     */
    public static LocalDate dayOfWeekToDate(DayOfWeek dayOfWeek, boolean isNext) {
        LocalDate now = LocalDate.now();

        if (isNext) {
            return now.with(TemporalAdjusters.next(dayOfWeek));
        } else {
            return now.with(TemporalAdjusters.previousOrSame(dayOfWeek));
        }
    }

    /**
     * 특정 기간에서 요일정보에 해당하는 날짜 정보 조회(YYYY-MM-DD)
     */
    public static List<LocalDate> getDayOfWeekToDate(String startDate, String endDate, DayOfWeek dayOfWeek) throws Exception {
        if (StringUtils.isEmpty(startDate)) {
            return null;
        }
        if (StringUtils.isEmpty(endDate)) {
            return null;
        }

        startDate = startDate.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
        endDate = endDate.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA));

        List<LocalDate> list = new ArrayList<>();
        while (start.isBefore(end)) {
            if (start.getDayOfWeek().getValue() == dayOfWeek.getValue()) {
                list.add(start);
            }
            start = start.plusDays(1L);
        }

        // 테스트용 로그. 주석처리 필요
//        Consumer<LocalDate> lambda= item -> System.out.println("item : " + item);
//        list.forEach(lambda);

        return list;
    }

    /**
     * Year-Month 정보
     */
    public static String dateToYearMonth(String dateString) throws Exception {
        LocalDate date = getLocalDate(dateString);
        if (date == null) return null;

        String monthStr = Integer.toString(date.getMonthValue());
        if (monthStr.length() < 2) {
            monthStr = "0" + monthStr;
        }

        return date.getYear() + "-" + monthStr;
    }

    /**
     * Month 정보
     */
    public static String dateToMonth(String dateString) throws Exception {
        LocalDate date = getLocalDate(dateString);
        if (date == null) return null;

        String monthStr = Integer.toString(date.getMonthValue());
        if (monthStr.length() < 2) {
            monthStr = "0" + monthStr;
        }

        return monthStr;
    }

    /**
     * 해당 월의 마지막 날짜 구하기
     */
    public static LocalDate dateToLastDate(String dateString) throws Exception {
        LocalDate localDate = getLocalDate(dateString);
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 두 날짜 사이의 차이 구하기
     */
    public static long getDifferenceYearOfTwoDates(String start, String end) throws Exception {
        return getDifferenceOfTwoDates(start, end, ChronoUnit.YEARS);
    }

    public static long getDifferenceMonthOfTwoDates(String start, String end) throws Exception {
        return getDifferenceOfTwoDates(start, end, ChronoUnit.MONTHS);
    }

    public static long getDifferenceDayOfTwoDates(String start, String end) throws Exception {
        return getDifferenceOfTwoDates(start, end, ChronoUnit.DAYS);
    }

    public static long getDifferenceOfTwoDates(String start, String end, ChronoUnit chronoUnit) throws Exception {
        LocalDate startDate = getLocalDate(start);
        LocalDate endDate = getLocalDate(end);

        return chronoUnit.between(startDate, endDate);
    }

    /**
     * 특정 기간 내에 해당 날짜 포함여부 구하기
     */
    public static boolean isIncludedByDates(String start, String end, String target) throws Exception {
        LocalDate startDate = getLocalDate(start);
        LocalDate endDate = getLocalDate(end);
        LocalDate targetDate = getLocalDate(target);

        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        // 시작일과 종료일을 포함하고자 하는 경우 isEqual 메소드 활용
        return startDate.isBefore(targetDate) && endDate.isAfter(targetDate);
    }

    public static boolean isIncludedByDates(String start, String end, String targetStart, String targetEnd) throws Exception {
        LocalDate startDate = getLocalDate(start);
        LocalDate endDate = getLocalDate(end);
        LocalDate targetStartDate = getLocalDate(targetStart);
        LocalDate targetEndDate = getLocalDate(targetEnd);

        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        if (targetStartDate.isAfter(targetEndDate)) {
            LocalDate temp = targetStartDate;
            targetStartDate = targetEndDate;
            targetEndDate = temp;
        }

        // 시작일과 종료일을 포함하고자 하는 경우 isEqual 메소드 활용
        return (startDate.isBefore(targetStartDate) || startDate.isEqual(targetStartDate)) && (endDate.isAfter(targetEndDate) || endDate.isEqual(targetEndDate));
    }

    private static LocalDate getLocalDate(String dateString) throws Exception {
        if (StringUtils.isEmpty(dateString)) {
            throw new Exception(dateString + " cannot be parsed to a LocalDate");
        }

        dateString = dateString.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA));
    }
}
