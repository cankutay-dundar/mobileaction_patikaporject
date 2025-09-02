package com.patika.airquality.util;

import java.time.LocalDate;
import java.util.*;

public class RangeUtils
{
    public static List<LocalDate> enumerate(LocalDate start, LocalDate end)
    {
    List<LocalDate> days = new ArrayList<>();
    LocalDate cur = start;
    while (!cur.isAfter(end)) {
    days.add(cur);
    cur = cur.plusDays(1);
    }
    return days;
    }
}
