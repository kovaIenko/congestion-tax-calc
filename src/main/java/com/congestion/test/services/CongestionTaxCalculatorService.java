package com.congestion.test.services;

import com.congestion.test.dto.TollPeriodDto;
import com.congestion.test.vehicles.Vehicle;
import com.congestion.test.vehicles.VehicleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CongestionTaxCalculatorService {

  int getTax(final Vehicle vehicle,
             final List<LocalDateTime> passages,
             final List<String> publicHolidays,
             final List<TollPeriodDto> cityHoursTollRules,
             final Set<VehicleType> freeVehicles);
}
