package net.fluance.cockpit.app.service.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.opensagres.odfdom.converter.core.IODFConverter;
import fr.opensagres.odfdom.converter.pdf.PdfConverter;
import fr.opensagres.odfdom.converter.pdf.PdfOptions;
import net.fluance.cockpit.app.service.domain.model.WhiteBoardViewPdfDTO;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.model.jdbc.patient.PatientBed;
import net.fluance.cockpit.core.model.jdbc.patient.PatientListHeaders;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.repository.jdbc.catalog.CatalogRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jdbc.company.ServiceListRepository;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;

@Service
public class WhiteBoardFilesService {
	
	private static final Logger LOGGER = LogManager.getLogger();

	@Value("${whiteboard.file.templates.folder.resource}")
	private String whiteBoardTemplatesFolder;

	@Value("${whiteboard.patientlist.template}")
	private String listTemplateName;

	@Value("${whiteboard.patientlist.tmp}")
	private String listTemplateTmpFolder;
	
	@Value("#{${whiteboard.config.specialRooms}}")
	private List<String> specialRooms;
	private static final String WINDOW_BED = "window_bed";
	private static final String DOOR_BED = "door_bed";
	
	@Value("#{${whiteboard.patientList.maxRowsFirsPage}}")
	private Integer MAX_ROWS_FIRST_PAGE;
		
	@Value("#{${whiteboard.patientList.maxRowsByPage}}")
	private Integer MAX_ROWS_BY_PAGE;
	
	//Carriage return amount of a line
	private Double CARRIAGE_RETURN_HEIGHT = 0.5d;
	
	//Number of carriages returns for a splited field
	private Double LINE_CARRIAGE_RETURNS_SPLITED = 3d;
	
	//Number of carriages return for normal line
	private Double LINE_CARRIAGE_RETURNS = 2d;
	
	@Value("#{${whiteboard.patientList.maxCharactersByRowForNurse}}")
	private Integer MAX_CHARACTERS_BY_ROW_FOR_NURSE;
	
	@Value("#{${whiteboard.patientList.maxCharactersByRowForReason}}")
	private Integer MAX_CHARACTERS_BY_ROW_FOR_REASON;
	
	@Value("#{${whiteboard.patientList.maxCharactersByRowForComment}}")
	private Integer MAX_CHARACTERS_BY_ROW_FOR_COMMENT;
	
	@Value("#{${whiteboard.patientList.maxCharactersByRowForPhysician}}")
	private Integer MAX_CHARACTERS_BY_ROW_FOR_PHYSICIAN;

	@Value("#{${whiteboard.patientList.maxCharactersByRowForDiet}}")
	private Integer MAX_CHARACTERS_BY_ROW_FOR_DIET;
	
	//Convenience value for room property for a row that should be print empty
	private static final String EMPTY_ROW_ROOM = "EMPTY";
	
	private static final String NEW_LINE = "\r\n";

	private static final String PHYSICIAN_PREFIX = "DR ";

	private static final String DATE_KEY = "date";
	
	private static final String DATE_OBJECT_KEY = "date_object";

	private static final String SERVICE_KEY = "service";

	private static final String COMPANY_KEY = "company";

	private static final String WHITEBOARDS_KEY = "whiteboards";

	private static final String ODT_EXTENSION = ".odt";

	private static final String PDF_EXTENSION = ".pdf";

	private static final String FILE_NAME_SEPARATOR = "_";

	private static final String DATE_FORMAT = "EEEE, dd MMMM yyyy";

	private static final String MESSAGES = "messages";
	
	private static final String WHITEBOARD_PREFIX = "whiteboard_";

	private static final String DIET_TYPE = "Diet";
	
	private static final Integer LIMIT = 1000;
	
	private static final Integer OFFSET = 0;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	CompanyDetailsRepository companyDetailsRepository;
	
	@Autowired
	ServiceListRepository serviceListRepository;

	@Autowired
	CatalogRepository catalogRepository;
	
	@Autowired
	WhiteBoardService whiteBoardService;

	/**
	 * Creates the pdf and save it to disk.
	 * 
	 * @param companyId
	 * @param serviceId
	 * @param sortOrder 
	 * @param orderBy 
	 * @return Resource of the generated document
	 * @throws Exception
	 */
	public Resource generatePdfList(Long companyId, String serviceId, LocalDate admitDate, Boolean display, Integer confCapacity, Integer originalCapacity, String occupancy, String orderBy, String sortOrder, String language) throws Exception {

		validatePdfListParams(companyId, serviceId);

		DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		
		//Get data from services and repositories
		CompanyDetails companyDetails = companyDetailsRepository.findOne(companyId.intValue());
		WhiteBoardDTO whiteBoardDTO = whiteBoardService.getAWhiteBoard(companyId, serviceId, admitDate, display, confCapacity, originalCapacity, occupancy, LIMIT, OFFSET, orderBy, sortOrder);
		
		//Resolve default locale
		Locale locale = Locale.ENGLISH;
		if(language != null && StringUtils.hasText(language)) {
			locale = new Locale(language);
		}

		Map<String, Object> data = generateDataMap(companyDetails, whiteBoardDTO, getServiceName(companyId, serviceId), orderBy, sortOrder, locale, admitDate);

		String tmpOdtFolder = listTemplateTmpFolder + generateFileListName(companyId, serviceId) + ODT_EXTENSION;

		String finalFilePath = listTemplateTmpFolder + generatePdfListName(companyId, serviceId);

		File inputFile = new File(tmpOdtFolder);
		File outputFile = new File(finalFilePath);

		Resource pdf = null;

		OdfTextDocument document = null;
		PdfOptions options = null;

		// create odt template for this document
		Resource fileResource = resourceLoader.getResource(whiteBoardTemplatesFolder + listTemplateName);
		DocumentTemplate template = documentTemplateFactory.getTemplate(fileResource.getInputStream());

		try (OutputStream outTmpOdtFolder = new FileOutputStream(tmpOdtFolder); OutputStream out = new FileOutputStream(outputFile); InputStream in = new FileInputStream(inputFile);) {

			// create the odt document
			template.createDocument(data, outTmpOdtFolder);

			document = OdfTextDocument.loadDocument(in);
			options = PdfOptions.create();

			// convert to PDF
			IODFConverter<PdfOptions> converter = PdfConverter.getInstance();
			converter.convert(document, out, options);

			out.flush();

			pdf = resourceLoader.getResource(outputFile.toURI().toString());
		}

		return pdf;
	}
	
	/**
	 * Generate the correct name for the PDF file using the company and the service
	 * 
	 * @param companyId
	 * @param serviceId
	 * @return
	 */
	public String generatePdfListName(Long companyId, String serviceId) {
		validatePdfListParams(companyId, serviceId);
		return generateFileListName(companyId, serviceId).concat(PDF_EXTENSION);
	}

	private Map<String, Object> generateDataMap(CompanyDetails companyDetails, WhiteBoardDTO whiteBoardDTO, String serviceName, String orderBy, String sortOrder, Locale locale, LocalDate admitDate) {
		Map<String, Object> data = new HashMap<>();
		
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, locale);
		
		// Generation date
		String date = dateFormat.format(new Date());
		if(admitDate != null) {
			date = dateFormat.format(Date.from(admitDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		}
		data.put(DATE_KEY, date.substring(0, 1).toUpperCase() + date.substring(1));
		
		Date dateObject = new Date();
		if(admitDate != null) {
			dateObject = Date.from(admitDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		data.put(DATE_OBJECT_KEY, dateObject);

		//Data for the company and service
		data.put(COMPANY_KEY, companyDetails.getName());
		data.put(SERVICE_KEY, serviceName);
		
		//Generate data for the table
		Double totalCalculatedRows = 0d;
		
		List<WhiteBoardViewPdfDTO> whiteBoardViewPdfDTOs = new ArrayList<>();

		WhiteBoardViewPdfDTO whiteBoardViewPdfDTO;
		//Parse to DTO for PDF and calculation of rows based on data from the service
		for (WhiteBoardViewDTO whiteBoardViewDTO : whiteBoardDTO.getEntries()) {			
			whiteBoardViewPdfDTO = parseToPdfDTO(companyDetails, locale, whiteBoardViewDTO);
			whiteBoardViewPdfDTOs.add(whiteBoardViewPdfDTO);
			
			//Calculate rows based on current data from the service.
			totalCalculatedRows += calculateRows(whiteBoardViewDTO);
		}
		
		//add rows to the end
		addEmptyRows(whiteBoardViewPdfDTOs, totalCalculatedRows);
		
		//add data for the table to the map
		data.put(WHITEBOARDS_KEY, whiteBoardViewPdfDTOs);
		
		// HEADERS 
		setHeadersTransalations(locale, data);

		return data;
	}

	private WhiteBoardViewPdfDTO parseToPdfDTO(CompanyDetails companyDetails, Locale locale, WhiteBoardViewDTO whiteBoardViewDTO) {
		WhiteBoardViewPdfDTO whiteBoardViewPdfDTO;
		List<String> physicians;

		whiteBoardViewPdfDTO = new WhiteBoardViewPdfDTO();
		
		whiteBoardViewPdfDTO.setId(whiteBoardViewDTO.getId());

		whiteBoardViewPdfDTO.setVisitNumber(whiteBoardViewDTO.getVisitNumber());

		whiteBoardViewPdfDTO.setRoom(whiteBoardViewDTO.getRoom());

		whiteBoardViewPdfDTO.setSex(whiteBoardViewDTO.getSex());

		whiteBoardViewPdfDTO.setFirstname(whiteBoardViewDTO.getFirstname());

		whiteBoardViewPdfDTO.setLastname(whiteBoardViewDTO.getLastname());

		whiteBoardViewPdfDTO.setEntreeDate(whiteBoardViewDTO.getEntreeDate());

		whiteBoardViewPdfDTO.setBirthDate(whiteBoardViewDTO.getBirthDate());

		whiteBoardViewPdfDTO.setInsurance(whiteBoardViewDTO.getInsurance());

		whiteBoardViewPdfDTO.setNurseName(whiteBoardViewDTO.getNurseName());	

		whiteBoardViewPdfDTO.setIsolationType(whiteBoardViewDTO.getIsolationType());

		whiteBoardViewPdfDTO.setComment(whiteBoardViewDTO.getComment());

		whiteBoardViewPdfDTO.setCompanyId(whiteBoardViewDTO.getCompanyId());

		whiteBoardViewPdfDTO.setServiceId(whiteBoardViewDTO.getServiceId());

		whiteBoardViewPdfDTO.setHl7Code(whiteBoardViewDTO.getHl7Code());

		whiteBoardViewPdfDTO.setAppointmentId(whiteBoardViewDTO.getAppointmentId());

		whiteBoardViewPdfDTO.setPatientId(whiteBoardViewDTO.getPatientId());

		whiteBoardViewPdfDTO.setPatientBed(whiteBoardViewDTO.getPatientBed());
		
		whiteBoardViewPdfDTO.setPatientClass(whiteBoardViewDTO.getPatientClass());
		
		whiteBoardViewPdfDTO.setCapacity(whiteBoardViewDTO.getCapacity());
		
		//Parse special attributes, preference for edited
		if(whiteBoardViewDTO.getEditedExpireDate() != null) {
			whiteBoardViewPdfDTO.setExpireDate(whiteBoardViewDTO.getEditedExpireDate());
		} else {
			whiteBoardViewPdfDTO.setExpireDate(whiteBoardViewDTO.getExpireDate());
		}
		
		if(whiteBoardViewDTO.getEditedOperationDate() != null) {
			whiteBoardViewPdfDTO.setOperationDate(whiteBoardViewDTO.getEditedOperationDate());
		} else {
			whiteBoardViewPdfDTO.setOperationDate(whiteBoardViewDTO.getOperationDate());
		}
		
		//Parse List of physicians to string with line breaks
		if(whiteBoardViewDTO.getEditedPhysician() != null && whiteBoardViewDTO.getEditedPhysician().size() > 0) {
			physicians = whiteBoardViewDTO.getEditedPhysician();
		} else if(whiteBoardViewDTO.getPhysician() != null && whiteBoardViewDTO.getPhysician().size() > 0) {
			physicians = whiteBoardViewDTO.getPhysician();
		} else {
			physicians = new ArrayList<>();
		}
		
		//Add prefix to physicians
		if(physicians.size() > 0) {
			physicians = physicians.stream().map(physician -> PHYSICIAN_PREFIX + physician).collect(Collectors.toList());	
			whiteBoardViewPdfDTO.setPhysician(String.join(NEW_LINE, physicians));
		} else {
			whiteBoardViewPdfDTO.setPhysician(null);
		}
		
		//Parse List of reasons  to string with line breaks
		if(whiteBoardViewDTO.getEditedReason() != null && whiteBoardViewDTO.getEditedReason().size() > 0) {
			whiteBoardViewPdfDTO.setReason(String.join(NEW_LINE, whiteBoardViewDTO.getEditedReason()));
		} else if(whiteBoardViewDTO.getReason() != null && whiteBoardViewDTO.getReason().size() > 0) {
			whiteBoardViewPdfDTO.setReason(String.join(NEW_LINE, whiteBoardViewDTO.getReason()));
		} else {
			whiteBoardViewPdfDTO.setReason(null);
		}
		
		//Parse diets code to String with the description with line breaks
		whiteBoardViewPdfDTO.setDiet(generateDietField(whiteBoardViewDTO.getDiet(), companyDetails, locale));
		
		//Set correct bed name
		whiteBoardViewPdfDTO.setPatientBed(generateRoomsBedName(whiteBoardViewDTO, locale));
		return whiteBoardViewPdfDTO;
	}
	
	private String generateRoomsBedName(WhiteBoardViewDTO whiteBoardViewDTO, Locale locale) {
		
		Map<String, String> data = mapPatientBedByLanguage(locale);
		
		if(!StringUtils.isEmpty(whiteBoardViewDTO.getPatientBed()) && !specialRooms.contains(whiteBoardViewDTO.getRoom())) {
			switch (whiteBoardViewDTO.getPatientBed()){
				case "1":
					return data.get(DOOR_BED);
				case "2":
					return data.get(WINDOW_BED);
				default:
					return null;
			}
		} else {
			return null;
		}
	}

	private Map<String, String> mapPatientBedByLanguage(Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
		
		// Wrap the property patientBed to Map
		Map<String, String> data = new HashMap<String, String>();
		if(PatientBed.values() != null && PatientBed.values().length > 0) {
			for (PatientBed patientBed : PatientBed.values()) {
				data.put(patientBed.getValue(), bundle.getString(WHITEBOARD_PREFIX + patientBed.getValue()));				
			}
		}
		return data;
	}
	
	private String generateDietField(List<String> dietCodes, CompanyDetails companyDetails, Locale locale) {
		String dietsTranslated = null;

		if (dietCodes != null && dietCodes.size() > 0) {

			dietsTranslated = String.join(NEW_LINE, dietCodes.stream().map(code -> {
				try {
					CatalogDTO catalog = catalogRepository.getCatalogByPK(companyDetails.getCode(), code, locale.getLanguage().toUpperCase(), DIET_TYPE);
					return catalog.getCodeDesc();
				} catch (EmptyResultDataAccessException exception) {
					LOGGER.error("Not existing diet in catalogs");
					return null;
				}
			}).filter(code -> code != null).collect(Collectors.toList()));

		}

		return dietsTranslated;
	}
	
	private void addEmptyRows(List<WhiteBoardViewPdfDTO> whiteBoards, double totalCalculatedRows) {
		//variables for the calculation
		Double rowsToAdd = 0d;
		
		//Convenience entry with specific ROOM
		WhiteBoardViewPdfDTO whiteBoardViewPdfDTOEmpty = new WhiteBoardViewPdfDTO();
		whiteBoardViewPdfDTOEmpty.setRoom(EMPTY_ROW_ROOM);

		if(totalCalculatedRows > MAX_ROWS_FIRST_PAGE) {		
			double modus = (totalCalculatedRows - MAX_ROWS_FIRST_PAGE) % MAX_ROWS_BY_PAGE;
			rowsToAdd = MAX_ROWS_BY_PAGE - modus;
		} else {
			rowsToAdd = MAX_ROWS_FIRST_PAGE - totalCalculatedRows;
		}
		
		for(double i = 0d; i<rowsToAdd; i++) {
			whiteBoards.add(whiteBoardViewPdfDTOEmpty);
		}
	}

	private Double calculateRows(WhiteBoardViewDTO whiteBoardViewDTO) {
		double[] values;
		Double calculatedRows = 0d;
		Double calculatedRowsReason = 0d;
		Double calculatedRowsComment = 0d;
		Double calculatedRowsNurse = 0d;
		Double calculatedRowsPhysician = 0d;
		Double calculatedRowsDiet = 0d;
		List<String> physicians;
		List<String> reasons;
		
		if(whiteBoardViewDTO.getEditedPhysician() != null) {
			physicians = whiteBoardViewDTO.getEditedPhysician();
		} else {
			physicians = whiteBoardViewDTO.getPhysician();
		}
		
		if(whiteBoardViewDTO.getEditedReason() != null) {
			reasons = whiteBoardViewDTO.getEditedReason();
		} else {
			reasons = whiteBoardViewDTO.getEditedReason();
		}
		
		calculatedRowsReason = calculateExtraRowsForSplitedFields(reasons, LINE_CARRIAGE_RETURNS_SPLITED, LINE_CARRIAGE_RETURNS, MAX_CHARACTERS_BY_ROW_FOR_REASON, CARRIAGE_RETURN_HEIGHT);
		
		calculatedRowsPhysician = calculateExtraRowsForSplitedFields(physicians, LINE_CARRIAGE_RETURNS_SPLITED, LINE_CARRIAGE_RETURNS, MAX_CHARACTERS_BY_ROW_FOR_PHYSICIAN, CARRIAGE_RETURN_HEIGHT);
		
		calculatedRowsDiet = calculateExtraRowsForSplitedFields(whiteBoardViewDTO.getDiet(), LINE_CARRIAGE_RETURNS_SPLITED, LINE_CARRIAGE_RETURNS, MAX_CHARACTERS_BY_ROW_FOR_DIET, CARRIAGE_RETURN_HEIGHT);
		
		calculatedRowsNurse = calculateExtraRowsLongFields(whiteBoardViewDTO.getNurseName(), MAX_CHARACTERS_BY_ROW_FOR_NURSE, CARRIAGE_RETURN_HEIGHT);
		
		calculatedRowsComment = calculateExtraRowsLongFields(whiteBoardViewDTO.getComment(), MAX_CHARACTERS_BY_ROW_FOR_COMMENT, CARRIAGE_RETURN_HEIGHT);
		
		//Get max value
		values = new double[] {calculatedRowsNurse, calculatedRowsReason, calculatedRowsComment, calculatedRowsPhysician, calculatedRowsDiet};
		Arrays.sort(values);
		calculatedRows = values[4];
		
		if(calculatedRows == 0d) {
			calculatedRows = CARRIAGE_RETURN_HEIGHT * LINE_CARRIAGE_RETURNS; 
		}
		
		return calculatedRows;
	}

	private Double calculateExtraRowsLongFields(String stringValue, Integer maxCharactersByRow, Double carriageReturnHeigh) {
		Double calculatedRows = 0d;
		if(!StringUtils.isEmpty(stringValue)) {
			//every line is a carriage return
			calculatedRows = ((stringValue.length() / maxCharactersByRow) * carriageReturnHeigh );
			
			//extra carriage return for the last line
			if(stringValue.length() % maxCharactersByRow > 0) {
				calculatedRows += carriageReturnHeigh;
			}
		}
		return calculatedRows;
	}

	private Double calculateExtraRowsForSplitedFields(List<String> StringValues, Double carriageReturnsForSplited, Double carriageReturnsLast, Integer maxCharactersByRow, Double carriageReturnHeigh) {
		Double calculatedRows = 0d;
		
		if(StringValues != null && StringValues.size()>0) {		
			if(StringValues.size() == 1) {
				calculatedRows += (carriageReturnsLast * carriageReturnHeigh);
			} else {
				
				for(int i = 0; i < StringValues.size(); i++) {
					String value = StringValues.get(i);
					
					if(i < (StringValues.size() -1)) {
						//no last value, extra carriage return
						calculatedRows += (carriageReturnsForSplited * carriageReturnHeigh);
					} else {
						//last value, no carriage return
						calculatedRows += (carriageReturnsLast * carriageReturnHeigh);	
					}

					//calculate carriage return for this splited value that overflows the max characters by row
					if(value.length() / maxCharactersByRow > 1) {						
						calculatedRows += calculateExtraRowsLongFields(value, maxCharactersByRow, carriageReturnHeigh);
					}
				}
			}
		}
		return calculatedRows;
	}

	private void setHeadersTransalations(Locale locale, Map<String, Object> data) {
		ResourceBundle bundle = ResourceBundle.getBundle(MESSAGES, locale);
		
		// Wrap the property headers to Map
		if(PatientListHeaders.values() != null && PatientListHeaders.values().length > 0) {
			for (PatientListHeaders header : PatientListHeaders.values()) {
				data.put(header.getValue(), bundle.getString(WHITEBOARD_PREFIX + header.getValue()));				
			}
		}
	}

	private String generateFileListName(Long companyId, String serviceId) {
		validatePdfListParams(companyId, serviceId);
		return listTemplateName.replace(ODT_EXTENSION, "") + FILE_NAME_SEPARATOR + companyId.toString() + FILE_NAME_SEPARATOR + serviceId;
	}

	private void validatePdfListParams(Long companyId, String serviceId) {
		if (companyId == null) {
			throw new IllegalArgumentException("CompanyId can't be null");
		} else if (serviceId == null || serviceId.length() == 0) {
			throw new IllegalArgumentException("ServiceId can't be null");
		}
	}
	
	private String getServiceName(Long companyId, String serviceId) {
		String name = "";
		
		List<ServiceList> services = serviceListRepository.findByCompanyId(companyId.intValue());
		
		for (ServiceList serviceList : services) {
			if(serviceList.getHospService().equals(serviceId)) {
				name = serviceList.getHospServiceDesc();
				break;
			}
		}
		
		return name;
	}

}
