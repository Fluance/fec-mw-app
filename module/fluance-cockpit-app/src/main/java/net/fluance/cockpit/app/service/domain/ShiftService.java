package net.fluance.cockpit.app.service.domain;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.Shift;

@Service
public class ShiftService {

	@Value("${shift.duration}")
	private int shiftDuration;
	
	@Value("${shift.tolerenceTimeInMinutes}")
	private int toleranceTime;
	
	public Shift getShift(int shiftNumber){
		if(shiftNumber < 0 ){
			throw new IllegalArgumentException("Shift Number must be a Natural number");
		}
		Calendar calendarStartDate = Calendar.getInstance();
		Calendar calendarEndDate = Calendar.getInstance();
		calendarStartDate.add(Calendar.HOUR_OF_DAY, (-8 * (shiftNumber+1)));
		calendarEndDate.add(Calendar.HOUR_OF_DAY, (-8 * shiftNumber));
		calendarEndDate.add(Calendar.MINUTE, toleranceTime);
		Date startDate = calendarStartDate.getTime();
		Date endDate = calendarEndDate.getTime();
		return new Shift(startDate, endDate);
	}
}
