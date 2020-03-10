package net.fluance.cockpit.app.service.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.app.config.ProviderConfig;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.VisitReference;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDoctorRepository;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentPatientRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jpa.refdata.RefdataRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppointmentServiceTest {
	
	private static final String DEFAULT_ORDER = "begindt DESC";
	private static final Integer DEFAULT_LIMIT = null;
	private static final Integer DEFAULT_OFFSET = 0;
	
	@TestConfiguration
	static class AppointmentServiceTestConfiguration {
		@Bean
		public AppointmentService appointmentService() {
			return new AppointmentService();
		}
		
		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
			
			Properties properties = new Properties();

			properties.put("net.fluance.cockpit.core.model.repository.defaultResultOffset", "0");
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultAppointmentListOrderBy", "begindt");
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultAppointmentListSortOrder", "DESC");
			properties.put("appointment.config.enableTranslationByCompany", "{'CAM'}");

			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
	}
	
	@Before
	public void setUp() {
		Mockito.reset(appointmentListRepository);
		Mockito.reset(appointmentDoctorRepository);
		Mockito.reset(appointmentDetailRepository);
		Mockito.reset(companyDetailsRepository);
		Mockito.reset(refDataRepository);
		Mockito.reset(providerConfig);
	}
	
	@Autowired
	AppointmentService appointmentService;
	
	@MockBean
	AppointmentPatientRepository appointmentListRepository;

	@MockBean
	AppointmentDoctorRepository appointmentDoctorRepository;

	@MockBean
	AppointmentDetailRepository appointmentDetailRepository;
	
	@MockBean
	CompanyDetailsRepository companyDetailsRepository;

	@MockBean
	RefdataRepository refDataRepository;

	@MockBean
	private ProviderConfig providerConfig;

	//getAppointmentDetailsByCriteriaShortedAndPaginated
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byPid_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, DEFAULT_ORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByPid(100000L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, null, null, null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byCompany_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(4, 100000L, null, null, null, null, null, null, false, null, null, DEFAULT_ORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByCompany(4));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(4, 100000L, null, null, null, null, null, null, false, null, null, null, null, null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byVisitNumber_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, null, 100L, null, null, null, null, null, false, null, null, DEFAULT_ORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByVisitNumber(100L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, null, 100L, null, null, null, null, null, false, null, null, null, null, null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byPidAndFrom_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), null, DEFAULT_ORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByPid(100000L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), null, null, null, null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byPidAndFromAndTo_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), LocalDate.now(), DEFAULT_ORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByPid(100000L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), LocalDate.now(), null, null, null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byPidAndBadOrderBy_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, DEFAULT_ORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByPid(100000L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, "foo", null, null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byPidAndOrderByAndShortOrder_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, "nb, appointment_id DESC", DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByPid(100000L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, "nb", "DESC", null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byPidAndLimitAndOffset_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, DEFAULT_ORDER, 20, 1)).thenReturn(AppointmentDetailListByPid(100000L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, null, false, null, null, null, null, 20, 1, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_byPid_and_locanionNames_should_return() {
		Mockito.when(appointmentDetailRepository.findByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, Arrays.asList("OP4", "OP5"), false, null, null, DEFAULT_ORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(AppointmentDetailListByPid(100000L));
		
		List<AppointmentDetail> appointmentDetails = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, 100000L, null, null, null, null, null, Arrays.asList("OP4", "OP5"), false, null, null, null, null, null, null, null);
		
		assertNotNull("should return a list", appointmentDetails);
		assertTrue("should contain appointments", appointmentDetails.size() > 0);
		assertEquals("Anesthesia value must match", "anesthesia", appointmentDetails.get(0).getAnesthesia());
		assertEquals("Patient Position value must match", "patientPosition", appointmentDetails.get(0).getPatientPosition());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_notFilter_throw() {
		appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getAppointmentDetailsByCriteriaShortedAndPaginated_badDates_throw() {
		appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(null, null, null, null, null, null, null, null, false, LocalDate.now().minusDays(1), LocalDate.now(), null, null, null, null, null);
	}
	
	//getAppointmentDetailsByCriteria
	@Test
	public void getAppointmentDetailsByCriteriaCount_byPid_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaCount(null, 100000L, null, null, null, null, null, null, false, null, null)).thenReturn(1);
		
		Integer count = appointmentService.getAppointmentDetailsByCriteriaCount(null, 100000L, null, null, null, null, null, null, false, null, null);
		
		assertNotNull("should return a list", count);
		assertTrue("should contain appointments", count > 0);
	}
	
	@Test
	public void getAppointmentDetailsCount_byCompany_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaCount(4, 100000L, null, null, null, null, null, null, false, null, null)).thenReturn(4);
		
		Integer count = appointmentService.getAppointmentDetailsByCriteriaCount(4, 100000L, null, null, null, null, null, null, false, null, null);
		
		assertNotNull("should return a list", count);
		assertTrue("should contain appointments", count > 0);
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaCount_byVisitNumber_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaCount(null, null, 100L, null, null, null, null, null, false, null, null)).thenReturn(1);
		
		Integer count = appointmentService.getAppointmentDetailsByCriteriaCount(null, null, 100L, null, null, null, null, null, false, null, null);
		
		assertNotNull("should return a list", count);
		assertTrue("should contain appointments", count > 0);
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaCount_byPidAndFrom_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaCount(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), null)).thenReturn(1);
		
		Integer count = appointmentService.getAppointmentDetailsByCriteriaCount(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), null);
		
		assertNotNull("should return a list", count);
		assertTrue("should contain appointments", count > 0);
	}
	
	@Test
	public void getAppointmentDetailsCount_byPidAndFromAndTo_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaCount(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), LocalDate.now())).thenReturn(1);
		
		Integer count = appointmentService.getAppointmentDetailsByCriteriaCount(null, 100000L, null, null, null, null, null, null, false, LocalDate.parse("1970-01-01"), LocalDate.now());
		
		assertNotNull("should return a list", count);
		assertTrue("should contain appointments", count > 0);
	}
	
	@Test
	public void getAppointmentDetailsByCriteriaCount_byPid_and_locanionNames_should_return() {		
		Mockito.when(appointmentDetailRepository.findByCriteriaCount(null, 100000L, null, null, null, null, null, Arrays.asList("OP4", "OP5"), false, null, null)).thenReturn(1);
		
		Integer count = appointmentService.getAppointmentDetailsByCriteriaCount(null, 100000L, null, null, null, null, null, Arrays.asList("OP4", "OP5"), false, null, null);
		
		assertNotNull("should return a list", count);
		assertTrue("should contain appointments", count > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAppointmentDetailsByCriteriaCount_notFilter_throw() {
		appointmentService.getAppointmentDetailsByCriteriaCount(null, null, null, null, null, null, null, null, false, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getAppointmentDetailsByCriteriaCount_badDates_throw() {
		appointmentService.getAppointmentDetailsByCriteriaCount(null, null, null, null, null, null, null, null, false, LocalDate.now().minusDays(1), LocalDate.now());
	}

	@Test
	public void getAppointmentDetailsByCriteriaCount_byPid_and_language_should_return() {
		Mockito.when(appointmentDetailRepository.findByAppointmentId(100000L)).thenReturn(Mockito.mock(AppointmentDetail.class));
		AppointmentDetail detail = appointmentService.getAppointmentDetail(100000L, "fr");
		assertNotNull("should return a AppointmentDetail", detail);
	}

	private List<AppointmentDetail> AppointmentDetailListByPid(@SuppressWarnings("SameParameterValue") Long pid) {
		return AppointmentDetailList(pid, 4, 1L);
	}
	
	private List<AppointmentDetail> AppointmentDetailListByCompany(@SuppressWarnings("SameParameterValue") int company) {
		return AppointmentDetailList(1000L, company, 1L);
	}
	
	private List<AppointmentDetail> AppointmentDetailListByVisitNumber(@SuppressWarnings("SameParameterValue") Long visitNumber) {
		return AppointmentDetailList(1000L, 4, visitNumber);
	}
	
	private List<AppointmentDetail> AppointmentDetailList(Long pid, int company, Long visitNumber) {
		List<AppointmentDetail> result = new ArrayList<>();
		PatientReference patientReference = new PatientReference(pid, "test", "test", "test", new Date());
		VisitReference visitReference = new VisitReference(visitNumber, "", "GOD", "100");
		AppointmentDetail appointmentDetail = new AppointmentDetail(company, 1L, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plusDays(1)), "operation", null, "test", null, "test", 4, "anesthesia", "patientPosition", patientReference, visitReference,
				new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		result.add(appointmentDetail);
		
		return result;
	}
}
