package ieetu.common.utils;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;

class DateUtilsTest {
    @Test
    public void 요일구하기() throws Exception {

        String dayOfWeek = DateUtils.dateToDayOfWeek("2022-04-07");
        System.out.println("dayOfWeek = " + dayOfWeek);

        dayOfWeek = DateUtils.dateToDayOfWeek("20220407", TextStyle.SHORT);
        System.out.println("dayOfWeek = " + dayOfWeek);

        dayOfWeek = DateUtils.dateToDayOfWeek("2022.04.07");
        System.out.println("dayOfWeek = " + dayOfWeek);

        dayOfWeek = DateUtils.dateToDayOfWeek("2022/04/07", TextStyle.SHORT);
        System.out.println("dayOfWeek = " + dayOfWeek);
    }

    @Test
    public void 타임스탬프를_날짜로_변환() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        LocalDateTime localDateTime = DateUtils.timestampToDate(currentTimeMillis);
        System.out.println("localDateTime = " + localDateTime);

    }

    @Test
    public void 특정요일의_날짜_구하기() throws Exception {
        LocalDate localDate = DateUtils.dayOfWeekToDate(DayOfWeek.THURSDAY, false);
        System.out.println("localDate = " + localDate);

        localDate = DateUtils.dayOfWeekToDate(DayOfWeek.MONDAY, false);
        System.out.println("localDate = " + localDate);

        localDate = DateUtils.dayOfWeekToDate(DayOfWeek.SUNDAY, true);
        System.out.println("localDate = " + localDate);

    }

    @Test
    public void 특정기간에서_특정요일에_해당하는_날짜_구하기() throws Exception {
        List<LocalDate> dayOfWeekToDate = DateUtils.getDayOfWeekToDate("20220401", "20220430", DayOfWeek.MONDAY);
        System.out.println("dayOfWeekToDate = " + dayOfWeekToDate);
    }

    @Test
    public void 년도_혹은_월_정보_구하기() throws Exception {
        String s = DateUtils.dateToYearMonth("20220301");
        System.out.println("s = " + s);

        s = DateUtils.dateToYearMonth("2022-03-01");
        System.out.println("s = " + s);

        s = DateUtils.dateToYearMonth("2022/03/01");
        System.out.println("s = " + s);

        s = DateUtils.dateToYearMonth("2022.03.01");
        System.out.println("s = " + s);
    }

    @Test
    public void 특정_달의_마지막날짜_구하기() throws Exception {
        LocalDate localDate = DateUtils.dateToLastDate("20240201");
        System.out.println("localDate = " + localDate);

        localDate = DateUtils.dateToLastDate(LocalDate.now().toString());
        System.out.println("localDate = " + localDate);
    }

    @Test
    public void 두_날짜_사이의_차이_구하기() throws Exception {
        String start = "2023-04-04";
        String end = "2023-03-04";
        long difference = DateUtils.getDifferenceDayOfTwoDates(start, end);
        System.out.println("difference = " + difference);

        difference = DateUtils.getDifferenceMonthOfTwoDates(start, end);
        System.out.println("difference = " + difference);

        difference = DateUtils.getDifferenceYearOfTwoDates(start, end);
        System.out.println("difference = " + difference);
    }

    @Test
    public void 특정_기간_사이에_해당_날짜의_포함여부() throws Exception {
        boolean includedByDates = DateUtils.isIncludedByDates("2021-04-04", "2021-03-07", "2021-04-02");
        System.out.println("includedByDates = " + includedByDates);

        includedByDates = DateUtils.isIncludedByDates("2021-04-04", "2021-03-07", "2021-04-02", "2021-04-04");
        System.out.println("includedByDates = " + includedByDates);
    }

    @Test
    public void aaa() throws Exception {
        boolean includedByDates = DateUtils.isIncludedByDates("2021-04-04", "2021-03-07", "2021-04-02", "2021-04-04");
        System.out.println("includedByDates = " + includedByDates);
    }

}