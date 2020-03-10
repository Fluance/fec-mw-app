package net.fluance.cockpit.core.repository.jdbc.doctor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;

import net.fluance.cockpit.core.model.AppointmentReference;
import net.fluance.cockpit.core.model.DayAppointments;
import net.fluance.cockpit.core.model.DoctorReference;
import net.fluance.cockpit.core.model.PatientVisitReference;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;

@Repository
@Component
public class DoctorRepository{
	
	private final static Integer SIZE = 2;
	
	private DayAppointments mock(Integer days) {
		DayAppointments dayAppointment = new DayAppointments();
		
		List<AppointmentReference> appointments = new ArrayList<>();
				
		AppointmentReference e = new AppointmentReference(String.valueOf(days), patientMock(days), doctorMock(days), "type", new Date(), new Date(), "serviceName", "subject", "room");
		appointments.add(e);
		appointments.add(e);
		
		dayAppointment.setAppointments(appointments);
		
		LocalDate localDate = LocalDate.now();		
		dayAppointment.setDate(java.sql.Date.valueOf(localDate.plusDays(days)));
		return dayAppointment;
	};
	
	private DoctorReference doctorMock(Integer number) {
		return new DoctorReference("M", new Date(), String.valueOf(number), "speciality", "googleToken", "linkedinToken", "email", "externalPhoneNumberOne", "externalPhoneNumbertwo", "latitude", "longitude", "pictureUri");
	};
	
	private PatientVisitReference patientMock(Integer id){
		return new PatientVisitReference(Long.valueOf(id), "firstName", "lastName", new Date(), "m");
	}
	
	private List<DayAppointments> mocks() {
		List<DayAppointments> dayAppointments = new ArrayList<>();
		for(Integer i = 0; i < SIZE; i++)
		    dayAppointments.add(mock(i));
		return dayAppointments;
	};
	
	public List<DayAppointments> getVisitsForCurrentDoctor(Long companyId, LocalDate from, LocalDate to) {
		
		List<DayAppointments> dayAppointments = mocks();
		return dayAppointments;
	}



}