package net.fluance.cockpit.app.service.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import net.fluance.app.data.model.identity.EhProfile;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.cockpit.app.config.ProviderConfig;
import net.fluance.cockpit.app.domain.log.PatientAccessLogCreatorPatientDetail;
import net.fluance.cockpit.core.model.jdbc.visit.PatientClassEnum;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.model.wrap.patient.PatientCourtesyEnum;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.domain.synlab.ObjectFactory;
import net.fluance.cockpit.domain.synlab.OpenDeeplink;
import net.fluance.cockpit.domain.synlab.OpenDeeplinkResponse;
import net.fluance.cockpit.domain.synlab.PatientComplet;
import net.fluance.commons.net.SOAPUtils;

@Service
public class SynlabService {

	@Value("${app.synlab.server.token.url}")
	private String url;

	@Value("${app.synlab.username}")
	private String userName;

	@Value("${app.synlab.password}")
	private String password;

	@Value("${app.synlab.soap.endpoint}")
	private String soapEndpoint;

	@Value("${app.synlab.server.prescription.url}")
	private String prescriptionBaseURL;

	@Value("${app.synlab.server.prescription.page.type}")
	private String prescriptionPageType;
	
	@Value("#{${synlab.hospservices.code}}")
	private Map<String, String> hospServicesMap;

	@Autowired
	VisitDetailRepository visitRepo;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private ProviderConfig providerConfig;

	public String getSynlabEndpointURL(Long visitnb, UserProfile profile) throws SOAPException, JAXBException, ParserConfigurationException, DOMException, IOException {
		OutputStream os = new ByteArrayOutputStream();
		SOAPMessage soapRequest = createSOAPRequest(visitnb, profile);
		soapRequest.writeTo(System.out);
		SOAPMessage soapResponse = SOAPUtils.sendSOAPRequest(soapRequest, url);
		OpenDeeplinkResponse response = processSOAPResponse(soapResponse);
		String url = prescriptionBaseURL + "?type="+ prescriptionPageType + "&code="+ userName + "&id=" + response.getStrID();
		return url;
	}

	private SOAPMessage createSOAPRequest(Long visitnb, UserProfile profile) throws SOAPException, JAXBException, ParserConfigurationException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		// SOAP Envelope
		soapMessage = prepareSOAPEnvelope(soapMessage);
		// SOAP HEADER
		soapMessage = prepareSOAPHeaders(soapMessage);
		// SOAP Body
		soapMessage = prepareSOAPBody(soapMessage, visitnb, profile);
		soapMessage.saveChanges();
		return soapMessage;
	}

	private SOAPMessage prepareSOAPEnvelope(SOAPMessage soapMessage) throws SOAPException{
		SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
		envelope.removeNamespaceDeclaration("SOAP-ENV");
		envelope.addNamespaceDeclaration("soap", "http://www.w3.org/2003/05/soap-envelope");
		envelope.setPrefix("soap");
		return soapMessage;
	}

	private SOAPMessage prepareSOAPHeaders(SOAPMessage soapMessage) throws DOMException, SOAPException{
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", soapEndpoint);
		headers.addHeader("Content-Type", "text/xml");
		soapMessage.getSOAPHeader().setPrefix("soap");
		return soapMessage;
	}
	
	private SOAPMessage prepareSOAPBody(SOAPMessage soapMessage, Long visitnb, UserProfile profile) throws SOAPException, JAXBException, ParserConfigurationException{
		soapMessage = prepareRequestPayload(soapMessage, visitnb, profile);
		soapMessage.getSOAPBody().setPrefix("soap");
		return soapMessage;
	}

	private SOAPMessage prepareRequestPayload(SOAPMessage soapMessage, Long visitnb, UserProfile userProfile) throws SOAPException, JAXBException, ParserConfigurationException{
		EhProfile profile = userProfile.getProfile();
		ObjectFactory factory = new ObjectFactory();
		OpenDeeplink openDeepLink = factory.createOpenDeeplink();
		PatientComplet patient = factory.createPatientComplet();
		openDeepLink.setCode(userName);
		openDeepLink.setCode2(password);
		openDeepLink.setStrPage(prescriptionPageType);
		openDeepLink.setStrNST1(visitnb.toString());
		VisitDetail visit = visitRepo.findByNb(visitnb);
		if(visit != null){
			patient.setStrNST2(visit.getPatientId().toString());
			String staffId = profile.getStaffId(visit.getCompany().getCompanyId(), providerConfig.getProviderSynlabId());
			if(staffId != null && !staffId.isEmpty()){
				openDeepLink.setStrCodePresc(staffId);
			}
			String admissionType = visit.getPatientClassIOU();
			if(admissionType!=null && !admissionType.isEmpty()){
				openDeepLink.setStrAdmissionType(admissionType);
			}
			String companyCode = visit.getCompany().getCode();
			if(companyCode!=null && !companyCode.isEmpty()){
				// TODO: add to soap request
			}
			String hospService = visit.getHospService();
			if(hospService!=null && !hospService.isEmpty()){
				String hospSeviceCode = hospServicesMap.get(hospService);
				if(hospSeviceCode != null){
					openDeepLink.setStrCodeCor(hospSeviceCode);
				}
			}
			Patient patientDetail = patientService.patientDetail(visit.getPatientId());
			if(patientDetail != null){
				patient.setStrCTIT(PatientCourtesyEnum.permissiveValueOf(patientDetail.getPatientInfo().getCourtesy()));
				patient.setStrZNOM(patientDetail.getPatientInfo().getLastName());
				patient.setStrZPRE(patientDetail.getPatientInfo().getFirstName());
				patient.setStrZNJF(patientDetail.getPatientInfo().getMaidenName());
				patient.setStrDNAI(formatBirthDate(patientDetail.getPatientInfo().getBirthDate()));
				patient.setStrZAD1(patientDetail.getAddress().getAddressLine());
				patient.setStrZAD2(patientDetail.getAddress().getAddressLine2());
				patient.setStrZCPO(patientDetail.getAddress().getPostCode());
				patient.setStrZVIL(patientDetail.getAddress().getLocality());
				patient.setStrCPAY(patientDetail.getAddress().getCountry());
			}
		}
		openDeepLink.setCPat(patient);
		Document document = SOAPUtils.createXMLDocumentFromObject(openDeepLink, OpenDeeplink.class);
		soapMessage.getSOAPBody().addDocument(document);
		return soapMessage;
	}
	
	private OpenDeeplinkResponse processSOAPResponse(SOAPMessage soapMessage) throws JAXBException, SOAPException {
		Document document = soapMessage.getSOAPBody().extractContentAsDocument();
		OpenDeeplinkResponse response = (OpenDeeplinkResponse) SOAPUtils.createObjectFromXMLDocument(document, OpenDeeplinkResponse.class);
		return response;
	}
	
	private String formatBirthDate(Date birthdate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(birthdate);
		formattedDate = formattedDate.replaceAll("-", "");
		return formattedDate;
	}
}
