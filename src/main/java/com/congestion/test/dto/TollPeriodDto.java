package com.congestion.test.dto;

import java.time.LocalDateTime;

public class TollPeriodDto {

  private final int startHour;
  private final int startMinute;
  private final int endHour;
  private final int endMinute;
  private final int fee;

  public TollPeriodDto(int startHour, int startMinute, int endHour, int endMinute, int fee) {
    this.startHour = startHour;
    this.startMinute = startMinute;
    this.endHour = endHour;
    this.endMinute = endMinute;
    this.fee = fee;
  }

  public boolean isInPeriod(LocalDateTime dateTime) {
    int hour = dateTime.getHour();
    int minute = dateTime.getMinute();

    return (hour > startHour || (hour == startHour && minute >= startMinute)) &&
      (hour < endHour || (hour == endHour && minute <= endMinute));
  }

  public int getFee() {
    return fee;
  }

}
