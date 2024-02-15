package com.congestion.test.services;

import com.congestion.test.dto.TollPeriodDto;
import com.congestion.test.vehicles.Vehicle;
import com.congestion.test.vehicles.VehicleType;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CongestionTaxCalculatorServiceIml implements CongestionTaxCalculatorService {

  @Override
  public int getTax(final Vehicle vehicle, final List<LocalDateTime> passages, final List<String> publicHolidays,
                    final List<TollPeriodDto> cityHoursTollRules, final Set<VehicleType> freeVehicles) {
    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Map<String, List<LocalDateTime>> groupedByDay = passages.stream()
      .sorted()
      .collect(Collectors.groupingBy(sdf::format));

    int totalFee = 0;

    for (List<LocalDateTime> dailyDates : groupedByDay.values()) {
      LocalDateTime intervalStart = dailyDates.get(0);
      int dailyTotalFee = 0;
      int highestFeeIn60MinWindow = 0;

      for (LocalDateTime dateTime : dailyDates) {
        int nextFee = getTollFee(dateTime, vehicle, publicHolidays, cityHoursTollRules, freeVehicles);

        long minutes = ChronoUnit.MINUTES.between(intervalStart, dateTime);

        if (minutes <= 60) {
          highestFeeIn60MinWindow = Math.max(highestFeeIn60MinWindow, nextFee);
        } else {
          dailyTotalFee += highestFeeIn60MinWindow;
          intervalStart = dateTime;
          highestFeeIn60MinWindow = nextFee;
        }
      }

      dailyTotalFee += highestFeeIn60MinWindow;
      dailyTotalFee = Math.min(dailyTotalFee, 60);
      totalFee += dailyTotalFee;
    }

    return totalFee;
  }

  private int getTollFee(final LocalDateTime dateTime,
                         final Vehicle vehicle,
                         final List<String> publicHolidays,
                         final List<TollPeriodDto> cityHoursTollRules,
                         final Set<VehicleType> freeVehicles) {
    if (isTollFreeDate(dateTime, publicHolidays) || isTollFreeVehicle(vehicle, freeVehicles)) {
      return 0;
    }
    return cityHoursTollRules.stream()
      .filter(rule -> rule.isInPeriod(dateTime))
      .map(TollPeriodDto::getFee)
      .findFirst()
      .orElse(0);
  }

  private boolean isTollFreeDate(final LocalDateTime date, final List<String> publicHolidays) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    Month month = date.getMonth();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = date.format(formatter);

    //todo exception days could be extracted as we did for public holidays
    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
      return true;
    }
    //todo exception months could be extracted as we did for public holidays
    if (month == Month.JULY) {
      return true;
    }
    if (publicHolidays.contains(formattedDate)) {
      return true;
    }
    // Check for days before public holidays
    String nextDayFormatted = date.plusDays(1).format(formatter);
    return publicHolidays.contains(nextDayFormatted);
  }

  private boolean isTollFreeVehicle(final Vehicle vehicle, final Set<VehicleType> freeVehicles) {
    if (vehicle == null) {
      return false;
    }
    VehicleType vehicleType = vehicle.getType();
    return freeVehicles.contains(vehicleType);
  }

}
