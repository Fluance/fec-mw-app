package net.fluance.cockpit.core.test.repository.jdbc.visit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.visit.Treatment;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.repository.jdbc.visit.TreatmentRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class VisitDetailRepositoryTest extends AbstractTest {

	@Autowired
	private VisitDetailRepository visitReponseRepository;

	// INPUTS PARAMS
	@Value("${net.fluance.cockpit.core.model.repository.treatment.visitNbAssociatedWithSomeTreatments}")
	private Integer visitNb;

	// EXPECTED VALUES
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientId}")
	private Integer expectedPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedCompanyId}")
	private Integer expectedCompanyId;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedCompanyCode}")
	private Integer expectedCompanyCode;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedCompanyName}")
	private Integer expectedCompanyName;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientClass}")
	private String expectedPatientClass;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientUnit}")
	private String expectedPatientUnit;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientRoom}")
	private String expectedPatientRoom;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientBed}")
	private String expectedPatientBed;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientCase}")
	private String expectedPatientCase;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedAdmissionType}")
	private String expectedAdmissionType;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedFinancialClass}")
	private String expectedFinancialClass;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedHospService}")
	private String expectedHospService;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedAdmitSource}")
	private String expectedAdmitSource;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientType}")
	private String expectedPatientType;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedAdmitDt}")
	private String expectedAdmitDt;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedDischargeDt}")
	private String expectedDischargeDt;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientClassDesc}")
	private String expectedPatientClassDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientTypeDesc}")
	private String expectedPatientTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientCaseDesc}")
	private String expectedPatientCaseDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedAdmissionTypeDesc}")
	private String expectedAdmissionTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedFinancialClassDesc}")
	private String expectedFinancialClassDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedPatientUnitDesc}")
	private String expectedPatientUnitDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitResponse.expectedHospServiceDesc}")
	private String expectedHospServiceDesc;

	@Test
	@Ignore("Test does not compile")
	public void testFindByNb() {
		VisitDetail actualVisitReponse = visitReponseRepository.findByNb((long)visitNb);
		assertNotNull(actualVisitReponse);
		assertEquals(visitNb, actualVisitReponse.getNumber());
		assertEquals(expectedPatientId, actualVisitReponse.getPatientId());
		assertEquals(expectedCompanyId, actualVisitReponse.getCompany().getCompanyId());
		assertEquals(expectedCompanyCode, actualVisitReponse.getCompany().getCode());
		assertEquals(expectedCompanyName, actualVisitReponse.getCompany().getName());
		assertEquals(expectedPatientClass, actualVisitReponse.getPatientClass());
		assertEquals(expectedPatientUnit, actualVisitReponse.getPatientUnit());
		assertEquals(expectedPatientRoom, actualVisitReponse.getPatientRoom());
		assertEquals(expectedPatientBed, actualVisitReponse.getPatientBed());
		assertEquals(expectedPatientCase, actualVisitReponse.getPatientCase());
		assertEquals(expectedAdmissionType, actualVisitReponse.getAdmissionType());
		assertEquals(expectedFinancialClass, actualVisitReponse.getFinancialClass());
		assertEquals(expectedHospService, actualVisitReponse.getHospService());
		assertEquals(expectedAdmitSource, actualVisitReponse.getAdmitSource());
		assertEquals(expectedPatientType, actualVisitReponse.getPatientType());
		assertEquals(expectedAdmitDt, actualVisitReponse.getAdmitDate());
		assertEquals(expectedDischargeDt, actualVisitReponse.getDischargeDate());
		assertEquals(expectedPatientClassDesc, actualVisitReponse.getPatientClassDesc());
		assertEquals(expectedPatientTypeDesc, actualVisitReponse.getPatientTypeDesc());
		assertEquals(expectedPatientCaseDesc, actualVisitReponse.getPatientCaseDesc());
		assertEquals(expectedAdmissionTypeDesc, actualVisitReponse.getAdmissionTypeDesc());
		assertEquals(expectedFinancialClassDesc, actualVisitReponse.getFinancialClassDesc());
		assertEquals(expectedPatientUnitDesc, actualVisitReponse.getPatientUnitDesc());
		assertEquals(expectedHospServiceDesc, actualVisitReponse.getHospServiceDesc());
	}
}
