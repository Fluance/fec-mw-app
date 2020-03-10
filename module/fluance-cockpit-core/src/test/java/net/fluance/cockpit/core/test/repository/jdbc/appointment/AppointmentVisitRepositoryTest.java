package net.fluance.cockpit.core.test.repository.jdbc.appointment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.appointment.Appointment;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentNew;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentPatientRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class AppointmentVisitRepositoryTest extends AbstractTest {
	
	private static Logger LOGGER = LogManager.getLogger(AppointmentVisitRepositoryTest.class);

	@Autowired
	private AppointmentPatientRepository appointmentRepo;

	//EXPECTED DATA APPOINTMENTS BY SEARCH PARAMETERS
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.nb_records}")
	private Integer expected_nb_records;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.visitnb}")
	private Integer expected_visitnb;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.firstname}")
	private String expected_firstname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.lastname}")
	private String expected_lastname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.maidenname}")
	private String expected_maidenname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.begindt}")
	private String expected_begindt;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.description}")
	private String expected_description;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.pid}")
	private Integer expected_pid;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.patientroom}")
	private String expected_patientroom;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.new.appoint_id}")
	private Integer expected_appoint_id;
	
	//EXPECTED DATA
	@Value("${net.fluance.cockpit.core.model.repository.appointment.nb_records}")
	private Integer nb_records;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.appoint_id}")
	private Integer appoint_id;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.visit_nb}")
	private Integer visit_nb;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.duration}")
	private String duration;	
	@Value("${net.fluance.cockpit.core.model.repository.appointment.begindt}")
	private String begindt;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.endt}")
	private String endt;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.appointment_type}")
	private String appointment_type;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.description}")
	private String description;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.status}")
	private String status;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.staffid}")
	private Integer staffid;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.pid}")
	private Integer pid;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.firstname}")
	private String firstname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.lastname}")
	private String lastname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.maidenname}")
	private String maidenname;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.birthdate}")
	private String birthdate;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.hospservice}")
	private String hospservice;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.hospservicedesc}")
	private String hospservicedesc;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.patientunit}")
	private String patientunit;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.patientunitdesc}")
	private String patientunitdesc;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.patientroom}")
	private String patientroom;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.patientclass}")
	private String patientclass;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.patientclassdesc}")
	private String patientclassdesc;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.company_id}")
	private Integer company_id;
	private Integer nb_records2 = 10;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.nb_records3}")
	private Integer nb_records3;

	//INPUT DATA
	@Value("${net.fluance.cockpit.core.model.repository.appointment.visitnb}")
	private Integer visitnb;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.companyid}")
	private Integer comapnyid;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.comapnyidNotRelated}")
	private Integer comapnyidNotRelated;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.staffidRelated}")
	private String staffidRelated;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.staffidNotRelated}")
	private Integer staffidNotRelated;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.appointmentBeginddtRelated}")
	private String appointmentBeginddtRelated;
	@Value("${net.fluance.cockpit.core.model.repository.appointment.appointmentBeginddtNotRelated}")
	private String appointmentBeginddtNotRelated;

	@Test
	@Ignore("Test does not compile")
	public void testGetAppointmentsByCompanyIdAndPatientUnitAndHospService(){
		List units = new ArrayList<>();
		units.add("44");
		List services = new ArrayList<>();
		services.add("DIET");
		List<AppointmentNew> appointments = appointmentRepo.findByCompanyIdAndPatientUnitAndHospService(12, units, services ,"begindt", 1, 0);
		assertNotNull(appointments);
	    assertTrue(appointments.size() > 0);
	    assertEquals(appointments.get(0).getNb_records(),expected_nb_records);
	    assertEquals(appointments.get(0).getVisit_nb(),expected_visitnb);
	    assertEquals(appointments.get(0).getFirstname(),expected_firstname);
		assertEquals(appointments.get(0).getLastname(),expected_lastname);
		assertEquals(appointments.get(0).getMaidenname(),expected_maidenname);
		assertEquals(appointments.get(0).getDescription(),expected_description);
		assertEquals(appointments.get(0).getAppoint_id(),expected_appoint_id);
		assertEquals(appointments.get(0).getPatientroom(),expected_patientroom);
		assertEquals(appointments.get(0).getPid(),expected_pid);
		assertEquals(appointments.get(0).getBegindt().toString(), expected_begindt);
	}
	
	@Test
	@Ignore("Test does not compile")
	public void testGetAppointmentsByVisit() {

		// *** Find by visit ***
		List<Appointment> appointments = appointmentRepo.findByVisitnb(visitnb);
		assertNotNull(appointments);
		assertTrue(appointments.size() > 0);
		assertEquals(appointments.get(0).getNb_records(),nb_records);
		assertEquals(appointments.get(0).getAppoint_id(), appoint_id);
		assertEquals(appointments.get(0).getVisit_nb(),visit_nb);
		assertEquals(appointments.get(0).getDuration(),duration);
		assertEquals(appointments.get(0).getBegindt(),begindt);
		
		assertEquals(appointments.get(0).getEndt(),endt);
		assertEquals(appointments.get(0).getAppointment_type(),appointment_type);
		assertEquals(appointments.get(0).getDescription(),description);
		assertEquals(appointments.get(0).getStatus(),status);
		assertEquals(appointments.get(0).getStaffid(),staffid);
		assertEquals(appointments.get(0).getPid(),pid);
		assertEquals(appointments.get(0).getFirstname(),firstname);
		assertEquals(appointments.get(0).getLastname(),lastname);
		assertEquals(appointments.get(0).getMaidenname(),maidenname);
		assertEquals(appointments.get(0).getBirthdate(),birthdate);
		assertEquals(appointments.get(0).getHospservice(),hospservice);
		assertEquals(appointments.get(0).getHospservicedesc(),hospservicedesc);
		assertEquals(appointments.get(0).getPatientunit(),patientunit);
		assertEquals(appointments.get(0).getPatientunitdesc(),patientunitdesc);
		assertEquals(appointments.get(0).getPatientroom(),patientroom);
		assertEquals(appointments.get(0).getPatientclass(),patientclass);
		assertEquals(appointments.get(0).getPatientclassdesc(),patientclassdesc);
		assertEquals(appointments.get(0).getCompany_id(),company_id);

		// test appointments list not null when no appointments for visitNb
		appointments = appointmentRepo.findByVisitnb(2);
		assertNotNull(appointments);
		assertTrue(appointments.size() == 0);

	}

	@Test
	@Ignore("Test does not compile")
	public void testGetAppointmentsByStaffidAndCompanyidNextDay() {

		List<Appointment> appointments = appointmentRepo.findByStaffidAndCompanyid(staffidRelated, comapnyid, appointmentBeginddtRelated, appointmentBeginddtRelated);
		assertNotNull(appointments);
		assertTrue(appointments.size() > 0);
		assertEquals(appointments.get(0).getNb_records(),nb_records2);
		assertEquals(appointments.get(0).getAppoint_id(), appoint_id);
		assertEquals(appointments.get(0).getVisit_nb(),visit_nb);
		assertEquals(appointments.get(0).getDuration(),duration);
		assertEquals(appointments.get(0).getBegindt(),begindt);
		assertEquals(appointments.get(0).getEndt(),endt);
		assertEquals(appointments.get(0).getAppointment_type(),appointment_type);
		assertEquals(appointments.get(0).getDescription(),description);
		assertEquals(appointments.get(0).getStatus(),status);
		assertEquals(appointments.get(0).getStaffid(),staffid);
		assertEquals(appointments.get(0).getPid(),pid);
		assertEquals(appointments.get(0).getFirstname(),firstname);
		assertEquals(appointments.get(0).getLastname(),lastname);
		assertEquals(appointments.get(0).getMaidenname(),maidenname);
		assertEquals(appointments.get(0).getBirthdate(),birthdate);
		assertEquals(appointments.get(0).getHospservice(),hospservice);
		assertEquals(appointments.get(0).getHospservicedesc(),hospservicedesc);
		assertEquals(appointments.get(0).getPatientunit(),patientunit);
		assertEquals(appointments.get(0).getPatientunitdesc(),patientunitdesc);
		assertEquals(appointments.get(0).getPatientroom(),patientroom);
		assertEquals(appointments.get(0).getPatientclass(),patientclass);
		assertEquals(appointments.get(0).getPatientclassdesc(),patientclassdesc);
		assertEquals(appointments.get(0).getCompany_id(),company_id);

		appointments = appointmentRepo.findByStaffidAndCompanyid(staffidRelated, comapnyidNotRelated, appointmentBeginddtRelated, appointmentBeginddtRelated);
		assertNotNull(appointments);
		assertTrue(appointments.size() == 0);	
	}

	@Test
	@Ignore("Test does not compile")
	public void testGetAppointmentsByStaffidAndCompanyidNextWeek() {

		// *** Find by companyid and staffid next week
		List<Appointment> appointments = appointmentRepo.findByStaffidAndCompanyidAndBegindtAndEnddt(staffidRelated, comapnyid, appointmentBeginddtRelated, appointmentBeginddtRelated);
		assertNotNull(appointments);
		assertTrue(appointments.size() > 0);
		assertEquals(appointments.get(0).getNb_records(),nb_records3);
		assertEquals(appointments.get(0).getAppoint_id(), appoint_id);
		assertEquals(appointments.get(0).getVisit_nb(),visit_nb);
		assertEquals(appointments.get(0).getDuration(),duration);
		assertEquals(appointments.get(0).getBegindt(),begindt);
		assertEquals(appointments.get(0).getEndt(),endt);
		assertEquals(appointments.get(0).getAppointment_type(),appointment_type);
		assertEquals(appointments.get(0).getDescription(),description);
		assertEquals(appointments.get(0).getStatus(),status);
		assertEquals(appointments.get(0).getStaffid(),staffid);
		assertEquals(appointments.get(0).getPid(),pid);
		assertEquals(appointments.get(0).getFirstname(),firstname);
		assertEquals(appointments.get(0).getLastname(),lastname);
		assertEquals(appointments.get(0).getMaidenname(),maidenname);
		assertEquals(appointments.get(0).getBirthdate(),birthdate);
		assertEquals(appointments.get(0).getHospservice(),hospservice);
		assertEquals(appointments.get(0).getHospservicedesc(),hospservicedesc);
		assertEquals(appointments.get(0).getPatientunit(),patientunit);
		assertEquals(appointments.get(0).getPatientunitdesc(),patientunitdesc);
		assertEquals(appointments.get(0).getPatientroom(),patientroom);
		assertEquals(appointments.get(0).getPatientclass(),patientclass);
		assertEquals(appointments.get(0).getPatientclassdesc(),patientclassdesc);
		assertEquals(appointments.get(0).getCompany_id(),company_id);

		appointments = appointmentRepo.findByStaffidAndCompanyidAndBegindtAndEnddt(staffidRelated, comapnyid, appointmentBeginddtNotRelated, appointmentBeginddtNotRelated);
		assertNotNull(appointments);
		assertTrue(appointments.size() == 0);	

	}
	
	@Test
	@Ignore("Test does not compile")
	public void testSearchCriteria(){
		Map<String, Object> params = new HashMap<>();
		params.put("company_id", 123);
		params.put("status", "Deleted");
		params.put("limit", 50);
		params.put("offset", 0); 
		params.put("patientunit", Arrays.asList("AAA"));
		params.put("hospservice", Arrays.asList("BBB"));
		String actualQuery = appointmentRepo.searchCriteria(params);
		String expectedQuery = "WHERE company_id=123 AND status<>'Deleted'  AND ( patientunit IN ('AAA') OR hospservice IN ('BBB') ) AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("company_id", 123);
		params.put("status", "Deleted");
		params.put("limit", 50);
		params.put("offset", 0); 
		params.put("patientunit", Arrays.asList("AAA", "AAAAAAA"));
		params.put("hospservice", Arrays.asList("BBB", "BBBBBBB"));
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE company_id=123 AND status<>'Deleted'  AND ( patientunit IN ('AAA','AAAAAAA') OR hospservice IN ('BBB','BBBBBBB') ) AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);
		
		params = new HashMap<>();
		params.put("company_id", 123);
		params.put("status", "Deleted");
		params.put("limit", 50);
		params.put("offset", 0); 
		params.put("patientunit", Arrays.asList("AAA"));
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE company_id=123 AND status<>'Deleted' AND patientunit IN ('AAA') AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("company_id", 123);
		params.put("status", "Deleted");
		params.put("limit", 50);
		params.put("offset", 0); 
		params.put("hospservice", Arrays.asList("BBB"));
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE company_id=123 AND status<>'Deleted' AND hospservice IN ('BBB') AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("company_id", 123);
		params.put("status", "Deleted");
		params.put("limit", 50);
		params.put("offset", 0); 
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE company_id=123 AND status<>'Deleted' AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);
		
		params = new HashMap<>();
		params.put("company_id", 123);
		params.put("status", "Deleted");
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE company_id=123 AND status<>'Deleted' AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);
		
		params = new HashMap<>();
		params.put("aaa", "aaa");
		params.put("company_id", 123);
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE company_id=123 AND aaa='aaa' AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("aaa", "aaa");
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE aaa='aaa' AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("company_id", 123);
		params.put("status", "Deleted");
		params.put("orderby", "ANY_ORDER");
		params.put("sortorder", "ANY_SORT_ORDER");
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE company_id=123 AND status<>'Deleted' AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("status", "Deleted");
		params.put("orderby", "ANY_ORDER");
		params.put("sortorder", "ANY_SORT_ORDER");
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE status<>'Deleted' AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("patientunit", Arrays.asList("AAA"));
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE patientunit IN ('AAA') AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		params.put("hospservice", Arrays.asList("BBB"));
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE hospservice IN ('BBB') AND begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);

		params = new HashMap<>();
		actualQuery = appointmentRepo.searchCriteria(params);
		expectedQuery = "WHERE begindt>=current_date";
		assertEquals(expectedQuery, actualQuery);
	}
	
}