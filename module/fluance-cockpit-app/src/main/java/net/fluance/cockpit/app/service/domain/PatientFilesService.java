package net.fluance.cockpit.app.service.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import fr.opensagres.odfdom.converter.core.IODFConverter;
import fr.opensagres.odfdom.converter.pdf.PdfConverter;
import fr.opensagres.odfdom.converter.pdf.PdfOptions;
import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.FileTemplate;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.patientfiles.PatientFile;
import net.fluance.cockpit.core.model.jdbc.patientfiles.PatientFilesOrderEnum;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianContact;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyDetail;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.model.wrap.patient.PersonalInfo;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jdbc.patientfiles.PatientFileRepository;
import net.fluance.cockpit.core.util.PatientClassIOU;
import net.fluance.cockpit.core.util.Sex;
import net.fluance.cockpit.core.util.TemplateField;
import net.fluance.commons.io.barcode.FluanceBarcodeGenerator;
import net.fluance.commons.io.barcode.datamatrix.DataMatrixGenerator;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;
import net.sf.jooreports.templates.image.FileImageSource;

@Service
public class PatientFilesService {

	private static Logger LOGGER = LogManager.getLogger();
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy_MM_dd_HH_mm");

	private static final String EMPTY_VALUE = "";
	private static final String POLICY_SEPARATOR = " / ";
	private static final String WHITE_SPACE = " ";
	private static final String ADMITING_PHYSICIAN_PREFIX = "Med.: ";
	private static final String PHYSICIAN_TELEPHONE_PREFIX = "Tel.: ";
	private static final String BIRTHDATE_PREFIX = "Data N. ";
	private static final String VISIT_ADMIT_DATE_PREFIX = "Ent. ";

	@Autowired
	private PatientFileRepository patientFileRepository;
	@Autowired
	private CompanyDetailsRepository companyDetailsRepository;
	@Autowired
	private ResourceLoader resourceLoader;

	@Value("#{${patientfiles.companies}}")
	private List<Integer> patientfilesCompaniesIds;
	@Value("${patientfiles.datamatrix.workDirectory}")
	private String datamatrixTmpDirectory;
	@Value("${patientenliste.config.workDirectory}")
	private String patientenlisteWorkDirectory;
	@Value("#{${patientfiles.template.realNames}}")
	private Map<Integer, String> templates;
	@Value("${patientfiles.template.folder}")
	private String templatesFolder;
	@Value("#{${patientfiles.template}}")
	private HashMap<Long, String> availableFileTemplates;
	@Value("#{${patientfiles.template.company}}")
	private HashMap<Long, List<Long>> availableFileTemplatesByCompany;

	@Value("${patientfiles.image.checkName}")
	private String imageCheckName;
	@Value("${patientfiles.image.uncheckName}")
	private String imageUncheckName;

	@Value("${patientfiles.image.ticinoLogo}")
	private String ticinoLogoFileName;
	@Value("${patientfiles.image.ticinoShield}")
	private String ticinoShieldFileName;

	@Value("${patientfiles.image.ticinoHeaderUp}")
	private String headerRowUpFileName;
	@Value("${patientfiles.image.ticinoHeaderDown}")
	private String headerRowDownFileName;
	@Value("${patientfiles.image.ticinoHeaderUpTemplate4}")
	private String headerRowUpFileNameTemplate4;
	@Value("${patientfiles.image.ticinoHeaderDownTemplate4}")
	private String headerRowDownFileNameTemplate4;

	@Value("${patientfiles.image.white}")
	private String whiteFileName;

	@Value("${patientfiles.image.lineName}")
	private String lineNameFileName;

	private FileImageSource imageCheckedBallot;
	private FileImageSource imageUncheckedBallot;
	private FileImageSource imageTicinoLogo;
	private FileImageSource imageTicinoShield;
	private FileImageSource imageHeaderRowUp;
	private FileImageSource imageHeaderRowDown;
	private FileImageSource imageWhite;
	private FileImageSource imageLineName;

	public List<PatientFile> getPatientFiles(Long pid, Integer companyid, String orderby, String sortorder,
			Integer limit, Integer offset) {
		PatientFilesOrderEnum orderEnum = PatientFilesOrderEnum.permissiveValueOf(orderby);
		if (orderEnum == null) {
			throw new IllegalArgumentException("This OrderBy value is not handled : " + orderby);
		} else if (companyid != null) {
			return patientFileRepository.getPatientFilesByPidAndCompanyId(pid, companyid, orderEnum.name(), sortorder,
					limit, offset);
		} else {
			return patientFileRepository.getPatientFilesByPid(pid, orderEnum.name(), sortorder, limit, offset);
		}
	}

	public int getPatientFilesCount(Long pid, Integer companyid) {
		if (companyid != null) {
			return patientFileRepository.getPatientFilesByPidAndCompanyIdCount(pid, companyid);
		} else {
			return patientFileRepository.getPatientFilesByPidCount(pid);
		}
	}

	public List<CompanyReference> getPatientFilesCompanies(Long pid) {
		if (pid == null) {
			// Return All Clinics for those feature is available (from properties)
			List<CompanyReference> companies = new ArrayList<>();
			for (Integer companyId : patientfilesCompaniesIds) {
				CompanyDetails c = companyDetailsRepository.findOne(companyId);
				if (c != null) {
					companies.add(new CompanyReference(c.getId(), c.getName(), c.getCode()));
				}
			}
			return companies;
		} else {
			// Return All Clinics associated with patient files
			return patientFileRepository.getCompaniesPatientFiles(pid);
		}
	}

	public Resource getPatientFileBinary(PatientFile patientFile) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		String filePath = "file:" + patientFile.getPath() + "/" + patientFile.getFileName();
		LOGGER.info("Reading file : " + filePath);
		Resource file = resourceLoader.getResource(filePath);
		return file;
	}

	/**
	 * This will generate a Datamatrix and save it into the file system
	 * 
	 * @param companyId
	 * @param pid
	 * @return filePath of the generated DataMatrix
	 * @throws IOException
	 */
	public String generateDataMatrix(Integer companyId, Long pid) throws IOException {
		if (companyId == null || pid == null) {
			throw new IllegalArgumentException("CompanyID and PID must be provided");
		}
		FluanceBarcodeGenerator dataMatrixGenerator = new DataMatrixGenerator();
		CompanyDetails c = companyDetailsRepository.findOne(companyId);
		String code = c.getCode() + "-" + pid;
		String dataMatrixFilePath = this.datamatrixTmpDirectory + System.currentTimeMillis() + ".png";
		dataMatrixGenerator.generate(dataMatrixFilePath, code);
		return dataMatrixFilePath;
	}

	public String generatePdf(Patient patient, CompanyDetails company, VisitDetail visitDetail, PhysicianList physician,
			PhysicianContact physicianContact, List<VisitPolicyDetail> visitPoliciesDetail, Integer templateid,
			Boolean includeMatrix) throws Exception {

		/** Check if everything arrives with correct values */
		validateParams(patient, company, visitDetail, physician, visitPoliciesDetail, templateid, includeMatrix);

		Map<String, Object> data = new HashMap<String, Object>();

		/** Load the Static Media Resources for the PDF */
		loadResources(templateid);

		/** Header Images */
		initializeHeader(data);

		/** Patient */
		setPatientInformation(patient, data, DATE_FORMAT);

		/** Company */
		setCompanyInformation(company, data);

		/** Visit */
		setVisitInformation(visitDetail, physician, physicianContact, data, DATE_FORMAT);

		/** Policy */
		setPolicyInformation(visitPoliciesDetail, data);

		/** Matrix: Only retrieves Matrix when includeMatrix == true */
		setMatrixInformation(patient, company, includeMatrix, data);

		/** Initialization of empty checkboxes, images, etc. */
		initializeEmptyCheckboxes(data);
		initializeImageLines(data);
		initializeWhiteImages(data);

		String fileSuffix = createFileSuffix(patient.getPatientInfo());

		return convertToPDF(templateid, data, fileSuffix);
	}

	/**
	 * Generates the suffix to add to the file name
	 * 
	 * PID + Time in "yyyy_MM_dd_HH_mm"
	 * 
	 * @param patientInfo
	 * @return
	 */
	private String createFileSuffix(PersonalInfo patientInfo) {
		String suffix = patientInfo.getPid() + "_" + DATE_TIME_FORMAT.format(new Date());
		return suffix;
	}

	/**
	 * @param patient
	 * @param company
	 * @param visitDetail
	 * @param physician
	 * @param visitPoliciesDetail
	 * @param templateid
	 * @param includeMatrix
	 */
	private void validateParams(Patient patient, CompanyDetails company, VisitDetail visitDetail,
			PhysicianList physician, List<VisitPolicyDetail> visitPoliciesDetail, Integer templateid,
			Boolean includeMatrix) {
		if (patient == null) {
			throw new IllegalArgumentException("The patient must not be null or empty");
		} else if (company == null) {
			throw new IllegalArgumentException("The company must not be null or empty");
		} else if (visitDetail == null) {
			throw new IllegalArgumentException("The visitDetail must not be null or empty");
		} else if (physician == null) {
			throw new IllegalArgumentException("The physician must not be null or empty");
		} else if (visitPoliciesDetail == null || visitPoliciesDetail.isEmpty()) {
			throw new IllegalArgumentException("The visitPolicyDetail must not be null or empty");
		} else if (templateid == null) {
			throw new IllegalArgumentException("The templateid must not be null or empty");
		}

		if (includeMatrix == null) {
			// PatientFilesController -> defualtValue = true
			includeMatrix = true;
		}
	}

	/**
	 * @param templateid
	 * @param data
	 * @param patientName
	 * @return
	 * @throws IOException
	 * @throws DocumentTemplateException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private String convertToPDF(Integer templateid, Map<String, Object> data, String patientName)
			throws Exception {
		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();

		String templateName = templates.get(templateid);

		if (templateName == null || templateName.isEmpty()) {
			throw new IllegalArgumentException("Template Not found - ID : " + templateid);
		}

		Resource fileResource = resourceLoader.getResource("classpath:" + templatesFolder + templateName);
		DocumentTemplate template = documentTemplateFactory.getTemplate(fileResource.getInputStream());
		template.createDocument(data,
				new FileOutputStream(datamatrixTmpDirectory + templateid + "_" + patientName + ".odt"));

		// Conversion
		File inputFile = new File(datamatrixTmpDirectory + templateid + "_" + patientName + ".odt");
		String finalFileName = templateName.replaceAll(".odt", "") + "_" + patientName + ".pdf";
		File outputFile = new File(datamatrixTmpDirectory + finalFileName);
		InputStream in = new FileInputStream(inputFile);
		OdfTextDocument document = OdfTextDocument.loadDocument(in);
		PdfOptions options = PdfOptions.create();

		OutputStream out = new FileOutputStream(outputFile);

		IODFConverter<PdfOptions> converter = PdfConverter.getInstance();
		converter.convert(document, out, options);

		LOGGER.info("$> Returning " + finalFileName);
		return finalFileName;
	}

	/**
	 * @param visitPolicyDetail
	 * @param data
	 */
	private void setPolicyInformation(List<VisitPolicyDetail> visitPoliciesDetail, Map<String, Object> data) {

		StringBuilder policiesDetailData = new StringBuilder();
		boolean isFirst = true;

		for (VisitPolicyDetail visitPolicyDetail : visitPoliciesDetail) {
			if (!isFirst) {
				policiesDetailData.append(POLICY_SEPARATOR);
			}

			policiesDetailData.append(visitPolicyDetail.getCode());
			policiesDetailData.append(WHITE_SPACE);
			policiesDetailData.append(visitPolicyDetail.getName());

			isFirst = false;
		}

		data.put(TemplateField.VISIT_POLICIES, policiesDetailData.length() > 0 ? policiesDetailData : EMPTY_VALUE);
	}

	/**
	 * @param patient
	 * @param company
	 * @param includeMatrix
	 * @param data
	 * @throws IOException
	 */
	private void setMatrixInformation(Patient patient, CompanyDetails company, Boolean includeMatrix,
			Map<String, Object> data) throws IOException {
		if (includeMatrix) {
			String datamatrixPath = generateDataMatrix(company.getId(), patient.getPatientInfo().getPid());
			data.put(TemplateField.DATA_MATRIX, new FileImageSource(new File(datamatrixPath)));
		} else {
			data.put(TemplateField.DATA_MATRIX, imageWhite);
		}
	}

	/**
	 * @param visitDetail
	 * @param physician
	 * @param data
	 * @param dateFormat
	 * @throws IOException
	 */
	private void setVisitInformation(VisitDetail visitDetail, PhysicianList physician,
			PhysicianContact physicianContact, Map<String, Object> data, DateFormat dateFormat) throws IOException {
		data.put(TemplateField.VISIT_NUMBER,
				visitDetail.getNumber() != null ? visitDetail.getNumber().toString() : EMPTY_VALUE);
		data.put(TemplateField.VISIT_PATIENT_CLASS,
				visitDetail.getPatientClass() != null ? visitDetail.getPatientClass() : EMPTY_VALUE);
		data.put(TemplateField.VISIT_PATIENT_ROOM,
				visitDetail.getPatientRoom() != null ? visitDetail.getPatientRoom() : EMPTY_VALUE);
		data.put(TemplateField.VISIT_ADMISSION_TYPE_DESC,
				visitDetail.getAdmissionTypeDesc() != null ? visitDetail.getAdmissionTypeDesc() : EMPTY_VALUE);
		data.put(TemplateField.VISIT_HOSP_SERVICE_DESC,
				visitDetail.getHospServiceDesc() != null ? visitDetail.getHospServiceDesc() : EMPTY_VALUE);
		data.put(TemplateField.VISIT_PATIENT_CASE_DESC,
				visitDetail.getPatientCaseDesc() != null ? visitDetail.getPatientCaseDesc() : EMPTY_VALUE);
		data.put(TemplateField.VISIT_ADMIT_DATE,
				visitDetail.getAdmitDate() != null
						? VISIT_ADMIT_DATE_PREFIX + dateFormat.format(visitDetail.getAdmitDate())
						: EMPTY_VALUE);
		data.put(TemplateField.VISIT_FINANCIAL_CLASS_DESC,
				visitDetail.getFinancialClassDesc() != null ? visitDetail.getFinancialClassDesc() : EMPTY_VALUE);
		data.put(TemplateField.VISIT_DAP_LAST_NAME,
				physician.getAdmittingPhysicianLastname() != null
						&& !physician.getAdmittingPhysicianLastname().isEmpty()
								? ADMITING_PHYSICIAN_PREFIX + physician.getAdmittingPhysicianLastname()
								: EMPTY_VALUE);
		data.put(TemplateField.VISIT_DAP_FIRST_NAME,
				physician.getAdmittingPhysicianFirstname() != null ? physician.getAdmittingPhysicianFirstname()
						: EMPTY_VALUE);
		data.put(TemplateField.VISIT_DAP_TELEPHONE,
				physicianContact.getData() != null && !physicianContact.getData().isEmpty()
						? PHYSICIAN_TELEPHONE_PREFIX + replacePunctuation(physicianContact.getData())
						: EMPTY_VALUE);

		data.put(TemplateField.INV, EMPTY_VALUE);

		checkPatientClass(data, visitDetail.getPatientClassIOU());
	}

	/**
	 * @param company
	 * @param data
	 */
	private void setCompanyInformation(CompanyDetails company, Map<String, Object> data) {
		data.put(TemplateField.COMPANY_CODE, company.getCode() != null ? company.getCode() : EMPTY_VALUE);
	}

	/**
	 * @param patient
	 * @param data
	 * @param dateFormat
	 * @throws IOException
	 */
	private void setPatientInformation(Patient patient, Map<String, Object> data, DateFormat dateFormat)
			throws IOException {
		data.put(TemplateField.PATIENT_FIRST_NAME,
				patient.getPatientInfo().getFirstName() != null ? patient.getPatientInfo().getFirstName()
						: EMPTY_VALUE);
		data.put(TemplateField.PATIENT_LAST_NAME,
				patient.getPatientInfo().getLastName() != null ? patient.getPatientInfo().getLastName() : EMPTY_VALUE);
		data.put(TemplateField.PATIENT_BIRTH_DATE,
				patient.getPatientInfo().getBirthDate() != null
						? BIRTHDATE_PREFIX + dateFormat.format(patient.getPatientInfo().getBirthDate())
						: EMPTY_VALUE);
		data.put(TemplateField.PATIENT_BIRTH_DATE_TAG,
				patient.getPatientInfo().getBirthDate() != null
						? dateFormat.format(patient.getPatientInfo().getBirthDate())
						: EMPTY_VALUE);
		data.put(TemplateField.PATIENT_PID,
				patient.getPatientInfo().getPid() != null ? patient.getPatientInfo().getPid().toString() : EMPTY_VALUE);
		data.put(TemplateField.PATIENT_ADDRESS_LINE,
				patient.getAddress().getAddressLine() != null ? patient.getAddress().getAddressLine() : EMPTY_VALUE);
		data.put(TemplateField.PATIENT_LOCALITY,
				patient.getAddress().getLocality() != null ? patient.getAddress().getLocality() : EMPTY_VALUE);
		data.put(TemplateField.PATIENT_POST_CODE,
				patient.getAddress().getPostCode() != null ? patient.getAddress().getPostCode() : "");

		checkSex(data, patient.getPatientInfo().getSex());
	}

	/**
	 * @param data
	 */
	private void initializeHeader(Map<String, Object> data) {
		data.put(TemplateField.TICINO_LOGO, imageTicinoLogo);
		data.put(TemplateField.TICINO_SHIELD, imageTicinoShield);
		data.put(TemplateField.HEADER_ROW_UP, imageHeaderRowUp);
		data.put(TemplateField.HEADER_ROW_DOWN, imageHeaderRowDown);
	}

	/**
	 * Initialize the empty checkboxes in the Template
	 * 
	 * @param data
	 */
	private void initializeEmptyCheckboxes(Map<String, Object> data) {
		data.put(TemplateField.ANALISI_PREDENCITI_YES, imageUncheckedBallot);
		data.put(TemplateField.ANALISI_PREDENCITI_NO, imageUncheckedBallot);
		data.put(TemplateField.ESCREATO, imageUncheckedBallot);
		data.put(TemplateField.ESCREATO_I, imageUncheckedBallot);
		data.put(TemplateField.ESCREATO_II, imageUncheckedBallot);
		data.put(TemplateField.ESCREATO_III, imageUncheckedBallot);
		data.put(TemplateField.BRONCOASPIRATO, imageUncheckedBallot);
		data.put(TemplateField.BRUSHING, imageUncheckedBallot);
		data.put(TemplateField.LAVAGGIO_BRONCHIOLOALVEOLARE, imageUncheckedBallot);
		data.put(TemplateField.VOLUME_INSTILLATO, imageUncheckedBallot);
		data.put(TemplateField.VOLUME_RECUPERATO, imageUncheckedBallot);
		data.put(TemplateField.VOLUME_INVIATO, imageUncheckedBallot);
		data.put(TemplateField.LAVAGGIO_SPAZZOLA_BIOPSIA, imageUncheckedBallot);
		data.put(TemplateField.AAC_TRANSBRONCHIALE, imageUncheckedBallot);
		data.put(TemplateField.SECREZIONE, imageUncheckedBallot);
		data.put(TemplateField.VERSAMENTO, imageUncheckedBallot);
		data.put(TemplateField.LAVAGGIO, imageUncheckedBallot);
		data.put(TemplateField.URINA, imageUncheckedBallot);
		data.put(TemplateField.URINA_I, imageUncheckedBallot);
		data.put(TemplateField.URINA_II, imageUncheckedBallot);
		data.put(TemplateField.URINA_III, imageUncheckedBallot);
		data.put(TemplateField.LIQUOR, imageUncheckedBallot);
		data.put(TemplateField.ALTRO, imageUncheckedBallot);
		data.put(TemplateField.PER_FAVORE, imageUncheckedBallot);

		/** FEHC-2177: Template 3 */
		data.put(TemplateField.PREVENZIONE, imageUncheckedBallot);
		data.put(TemplateField.CONTROLLO, imageUncheckedBallot);
		data.put(TemplateField.ESOCERVICE, imageUncheckedBallot);
		data.put(TemplateField.VULVA, imageUncheckedBallot);
		data.put(TemplateField.ENDOCERVICE, imageUncheckedBallot);
		data.put(TemplateField.VAGINA, imageUncheckedBallot);
		data.put(TemplateField.ENDOMETRIO, imageUncheckedBallot);
		data.put(TemplateField.CONIZZAZIONE, imageUncheckedBallot);
		data.put(TemplateField.ISTERECTOMIA, imageUncheckedBallot);
		data.put(TemplateField.TRATTAMENTO_ORMONALE, imageUncheckedBallot);
		data.put(TemplateField.DIU, imageUncheckedBallot);
		data.put(TemplateField.GRAVIDANZA, imageUncheckedBallot);
		data.put(TemplateField.HPV, imageUncheckedBallot);
		data.put(TemplateField.CHLAMIDIA, imageUncheckedBallot);
		data.put(TemplateField.GONORRHOEAE, imageUncheckedBallot);
		data.put(TemplateField.SODDISFACENTE_1, imageUncheckedBallot);
		data.put(TemplateField.SODDISFACENTE_2, imageUncheckedBallot);
		data.put(TemplateField.SODDISFACENTE_3, imageUncheckedBallot);
		data.put(TemplateField.SODDISFACENTE_4, imageUncheckedBallot);
		data.put(TemplateField.NON_SODDISFACENTE_1, imageUncheckedBallot);
		data.put(TemplateField.NON_SODDISFACENTE_2, imageUncheckedBallot);
		data.put(TemplateField.NON_SODDISFACENTE_3, imageUncheckedBallot);
		data.put(TemplateField.NON_SODDISFACENTE_4, imageUncheckedBallot);
		data.put(TemplateField.NEGATIVO, imageUncheckedBallot);
		data.put(TemplateField.TRICHOMONAS, imageUncheckedBallot);
		data.put(TemplateField.MICETI, imageUncheckedBallot);
		data.put(TemplateField.VAGINOSI, imageUncheckedBallot);
		data.put(TemplateField.ACTINOMYCES, imageUncheckedBallot);
		data.put(TemplateField.HERPES, imageUncheckedBallot);
		data.put(TemplateField.MODIFICATION_1, imageUncheckedBallot);
		data.put(TemplateField.MODIFICATION_2, imageUncheckedBallot);
		data.put(TemplateField.MODIFICATION_3, imageUncheckedBallot);
		data.put(TemplateField.ATROFIA, imageUncheckedBallot);
		data.put(TemplateField.CELLULE, imageUncheckedBallot);
		data.put(TemplateField.ANOMALIE, imageUncheckedBallot);
		data.put(TemplateField.ASCUS, imageUncheckedBallot);
		data.put(TemplateField.ASCH, imageUncheckedBallot);
		data.put(TemplateField.LSIL, imageUncheckedBallot);
		data.put(TemplateField.HSIL, imageUncheckedBallot);
		data.put(TemplateField.CARCINOMA, imageUncheckedBallot);
		data.put(TemplateField.ATIPICHE_1, imageUncheckedBallot);
		data.put(TemplateField.ATIPICHE_2, imageUncheckedBallot);
		data.put(TemplateField.ADENOCARCIOMA_1, imageUncheckedBallot);
		data.put(TemplateField.ADENOCARCIOMA_2, imageUncheckedBallot);

	}

	/**
	 * Initialize the empty Image Lines in the Template
	 * 
	 * @param data
	 */
	private void initializeImageLines(Map<String, Object> data) {

		data.put(TemplateField.LINE_NAME, imageLineName);
		data.put(TemplateField.LINE_BIRTH_DATE, imageLineName);
		data.put(TemplateField.LINE_ADDRESS, imageLineName);
		data.put(TemplateField.LINE_LOCALITY, imageLineName);
		data.put(TemplateField.COPIA_1, imageLineName);
		data.put(TemplateField.COPIA_2, imageLineName);
		data.put(TemplateField.COPIA_3, imageLineName);

		/** FEHC-2176: Template 2 y 4 */
		data.put(TemplateField.MATERIAL_1, imageLineName);
		data.put(TemplateField.MATERIAL_2, imageLineName);
		data.put(TemplateField.MATERIAL_3, imageLineName);
		data.put(TemplateField.MATERIAL_4, imageLineName);
		data.put(TemplateField.MATERIAL_5, imageLineName);
		data.put(TemplateField.MATERIAL_6, imageLineName);
		data.put(TemplateField.MATERIAL_7, imageLineName);
		data.put(TemplateField.MATERIAL_8, imageLineName);

		/** FEHC-2177: Template 3 */
		data.put(TemplateField.COLPOSCOPICO_1, imageLineName);
		data.put(TemplateField.COLPOSCOPICO_2, imageLineName);

	}

	/**
	 * Initialize the white images in the Template
	 * 
	 * @param data
	 */
	private void initializeWhiteImages(Map<String, Object> data) {

		/** FEHC-2177: Template 3 */
		data.put(TemplateField.LIST_WHITE_1, imageWhite);
		data.put(TemplateField.LIST_WHITE_2, imageWhite);
		data.put(TemplateField.CELLULE_1, imageWhite);
		data.put(TemplateField.CELLULE_2, imageWhite);
	}

	/**
	 * Check the Patient Class in the Template
	 * 
	 * @param data
	 * @param patientClassIOU
	 * @throws IOException
	 */
	private void checkPatientClass(Map<String, Object> data, String patientClassIOU) throws IOException {

		Boolean isHosp = PatientClassIOU.HOSPITALIZED.getPatientClassIOU().contains(patientClassIOU);
		Boolean isNotHosp = PatientClassIOU.NOT_HOSPITALIZED_OVERNIGHT.getPatientClassIOU().contains(patientClassIOU);

		if (isHosp) {
			data.put(TemplateField.HOSPITALIZED, imageCheckedBallot);
			data.put(TemplateField.NOT_HOSPITALIZED_OVERNIGHT, imageUncheckedBallot);
		} else if (isNotHosp) {
			data.put(TemplateField.HOSPITALIZED, imageUncheckedBallot);
			data.put(TemplateField.NOT_HOSPITALIZED_OVERNIGHT, imageCheckedBallot);
		} else {
			data.put(TemplateField.HOSPITALIZED, imageUncheckedBallot);
			data.put(TemplateField.NOT_HOSPITALIZED_OVERNIGHT, imageUncheckedBallot);
		}
	}

	/**
	 * Check the Patient Sex in the Template
	 * 
	 * @param data
	 * @param sex
	 * @throws IOException
	 */
	private void checkSex(Map<String, Object> data, String sex) throws IOException {

		Boolean isMale = Sex.MALE.getSexNames().contains(sex);
		Boolean isFemale = Sex.FEMALE.getSexNames().contains(sex);

		if (isMale) {
			data.put(TemplateField.MALE, imageCheckedBallot);
			data.put(TemplateField.FEMALE, imageUncheckedBallot);
		} else if (isFemale) {
			data.put(TemplateField.MALE, imageUncheckedBallot);
			data.put(TemplateField.FEMALE, imageCheckedBallot);
		} else {
			data.put(TemplateField.MALE, imageUncheckedBallot);
			data.put(TemplateField.FEMALE, imageUncheckedBallot);
		}

	}

	/**
	 * Get a {@link List} with all the available Templates with are assigned to the
	 * Company ID
	 * 
	 * @param companyId
	 * 
	 * @return The {@link List} of available {@link FileTemplate}
	 */
	public List<FileTemplate> getFileTemplates(Long companyId) {

		List<FileTemplate> fileTemplates = new ArrayList<>();

		List<Long> templatesId = availableFileTemplatesByCompany.get(companyId);

		if (templatesId == null) {
			return fileTemplates;
		}

		for (Long id : templatesId) {
			fileTemplates.add(new FileTemplate(id, availableFileTemplates.get(id)));
		}

		return fileTemplates;
	}

	/**
	 * Load the Static Media Resources for the PDF
	 * 
	 * @throws IOException
	 */
	private void loadResources(Integer templateid) throws IOException{
		Resource ballotUnchecked = resourceLoader.getResource(imageUncheckName);
		Resource ballotChecked = resourceLoader.getResource(imageCheckName);
		
		Resource ticinoLogo = resourceLoader.getResource(ticinoLogoFileName);
		Resource ticinoShield = resourceLoader.getResource(ticinoShieldFileName);
		
		Resource headerRowUp = null;
		Resource headerRowDown = null;
		if(templateid != null && templateid == 4){
			headerRowUp = resourceLoader.getResource(headerRowUpFileNameTemplate4);
			headerRowDown = resourceLoader.getResource(headerRowDownFileNameTemplate4);
		} else { 
			headerRowUp = resourceLoader.getResource(headerRowUpFileName);
			headerRowDown = resourceLoader.getResource(headerRowDownFileName);
		}
		
		Resource white = resourceLoader.getResource(whiteFileName);
		
		Resource lineName = resourceLoader.getResource(lineNameFileName);
		
		imageCheckedBallot = new FileImageSource(ballotChecked.getFile());
		imageUncheckedBallot = new FileImageSource(ballotUnchecked.getFile());
		imageTicinoLogo = new FileImageSource(ticinoLogo.getFile());
		imageTicinoShield = new FileImageSource(ticinoShield.getFile());
		
		imageHeaderRowUp = new FileImageSource(headerRowUp.getFile());
		imageHeaderRowDown = new FileImageSource(headerRowDown.getFile());
		
		imageWhite = new FileImageSource(white.getFile());
		
		imageLineName = new FileImageSource(lineName.getFile());
	}

	/**
	 * <b>Provisional: Until we have a Telephone format in database, this method
	 * gives some properties to the telephone numbers</b> Replace all the . and ,
	 * characters by blank spaces
	 * 
	 * @param text
	 * @return
	 */
	private String replacePunctuation(String text) {
		String textWithoutDots = text.replace(".", " ");
		String textFormatted = textWithoutDots.replace(",", " ");
		return textFormatted;
	}

	public Logger getLogger() {
		return LogManager.getLogger();
	}
}
