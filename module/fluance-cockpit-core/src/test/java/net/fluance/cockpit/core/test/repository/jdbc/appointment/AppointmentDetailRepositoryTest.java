package net.fluance.cockpit.core.test.repository.jdbc.appointment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDetailRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class AppointmentDetailRepositoryTest extends AbstractTest{

	//EXPECTED DATA APPOINTMENTS BY SEARCH PARAMETERS
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.nb_records}")
	private Integer expected_nb_records;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.appoint_id}")
	private Integer expected_appoint_id;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.begindt}")
	private String expected_begindt;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.endt}")
	private String expected_enddt;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.duration}")
	private String expected_duration;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.description}")
	private String expected_description;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.appointment_type}")
	private String expected_appointment_type;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.company_id}")
	private Integer expected_company_id;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.patient.pid}")
	private Integer expected_pid;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.patient.firstname}")
	private String expected_firstname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.patient.lastname}")
	private String expected_lastname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.patient.maidenname}")
	private String expected_maidenname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.birthdate}")
	private String expected_birthdate;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.visit.visit_nb}")
	private String expected_visit_nb;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.visit.hospservice}")
	private String expected_hospservice;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.patientunit}")
	private String expected_patientunit;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.detail.patientroom}")
	private String expected_patientroom;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.personnels}")
	private String expected_personnels;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.location.name}")
	private String expected_location_name;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.location.type}")
	private String expected_location_type;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.location.begindt}")
	private String expected_location_begindt;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.location.enddt}")
	private String expected_location_enddt;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.devices}")
	private String expected_devices;
	
	@Autowired
	AppointmentDetailRepository repository;

	@Test 
	@Ignore("Test does not compile")
	public void testGetDetailedAppointmentsByCompanyIdAndPatientUnitAndHospServicePid(){
		List<String> units = new ArrayList<>();
		units.add("44");
		List<String> services = new ArrayList<>();
		services.add("DIAL");
		String type = "operartion";
		List<String> rooms = Arrays.asList("0");
		List<String> locantionNames = Arrays.asList("OP4", "OP5");
		LocalDate from = LocalDate.of(2015, 10, 14);
		LocalDate to = LocalDate.of(2015, 10, 22);
		boolean includeActive = false;
		
		List<AppointmentDetail> appointments = repository.findByCriteriaShortedAndPaginated(12, (long) 373890, anyLong(), units, services, type, rooms, locantionNames, includeActive, from, to, "begindt", 1, 0);
		assertNotNull(appointments);
		assertTrue(appointments.size() > 0);
	    assertEquals(appointments.get(0).getAppointmentId(),expected_appoint_id);
	    assertEquals(appointments.get(0).getAppointmentId(),expected_appoint_id);
	}

}
