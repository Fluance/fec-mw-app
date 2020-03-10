/**
 * 
 */
package net.fluance.cockpit.core.model;

public class SQLStatements {

	// ------------------------------------ INSERT
	// -----------------------------------------------------

	// ------------------------------------ SELECT
	// -----------------------------------------------------
	public static final String FIND_PATIENT_ACCESS_LOGS_BY_PID = "SELECT res.* from " 
			+ "(SELECT DISTINCT ON (appuser, externaluser) appuser, externaluser, logdt, lastname, firstname, externalfirstname, externallastname, externalemail FROM " + MappingsConfig.TABLE_NAMES.get("UserAccess")
			+ " WHERE parentpid = ? GROUP BY appuser, externaluser, logdt, lastname, firstname, externalfirstname, externallastname, externalemail ORDER BY appuser, externaluser, logdt desc) res order by ? LIMIT ? OFFSET ?";
																
	public static final String FIND_PATIENT_ACCESS_LOGS_BY_PID_COUNT = "SELECT COUNT(res.*) from " 
			+ "(SELECT DISTINCT ON (appuser, externaluser) appuser, externaluser, logdt, lastname, firstname FROM " + MappingsConfig.TABLE_NAMES.get("UserAccess")
			+ " WHERE parentpid = ? GROUP BY appuser, externaluser, logdt, lastname, firstname ORDER BY appuser, externaluser, logdt desc) res";	

	public static final String FIND_TREATMENTS_BY_VISIT_NB = "SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, lang "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Treatment") + " bvt, " + "LATERAL "
			+ "		(SELECT preflang FROM " + MappingsConfig.TABLE_NAMES.get("Company") + " c, "
			+ MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ "			WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ "	(SELECT description, lang FROM " + MappingsConfig.TABLE_NAMES.get("OPA_CHOP") + " "
			+ "		WHERE bvt.data = m_cat_opa_chop.code AND lat.preflang = m_cat_opa_chop.lang) latjl ON true "
			+ "WHERE visit_nb=? ORDER BY rank limit ? offset ? ;";
	
	public static final String FIND_ALL_TREATMENTS_BY_VISIT_NB = "SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, lang "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Treatment") + " bvt, " + "LATERAL "
			+ "		(SELECT preflang FROM " + MappingsConfig.TABLE_NAMES.get("Company") + " c, "
			+ MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ "			WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ "	(SELECT description, lang FROM " + MappingsConfig.TABLE_NAMES.get("OPA_CHOP") + " "
			+ "		WHERE bvt.data = m_cat_opa_chop.code AND lat.preflang = m_cat_opa_chop.lang) latjl ON true "
			+ "WHERE visit_nb=? ORDER BY rank;";

	public static final String FIND_TREATMENTS_BY_VISIT_NB_AND_LANGUAGE = "WITH x AS (SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, lang FROM "+ 
			MappingsConfig.TABLE_NAMES.get("Treatment") + " bvt " +
			"INNER JOIN LATERAL (SELECT description, lang FROM "+ MappingsConfig.TABLE_NAMES.get("OPA_CHOP") +" WHERE bvt.data = m_cat_opa_chop.code AND m_cat_opa_chop.lang = ? ) latjl "+
			"ON true WHERE visit_nb = ?), y AS ( "+
			"SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, preflang "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Treatment") + " bvt, " + "LATERAL "
			+ "		(SELECT preflang FROM " + MappingsConfig.TABLE_NAMES.get("Company") + " c, "
			+ MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ "			WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ "	(SELECT description FROM " + MappingsConfig.TABLE_NAMES.get("OPA_CHOP") 
			+ "	WHERE bvt.data = m_cat_opa_chop.code AND lat.preflang = m_cat_opa_chop.lang) latjl ON true "
			+ "WHERE visit_nb=?)"+
			"SELECT * from X UNION ALL SELECT * FROM Y where rank NOT IN (SELECT rank from X ) ORDER BY rank limit ? offset ? ;";

	public static final String FIND_DIAGNOSIS_BY_VISIT_NB_AND_LANGUAGE = "WITH x AS (SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, lang FROM "+ 
			MappingsConfig.TABLE_NAMES.get("Diagnosis") + " bvt " +
			"INNER JOIN LATERAL (SELECT description, lang FROM "+ MappingsConfig.TABLE_NAMES.get("OPA_ICD") +" WHERE bvt.data = m_cat_opa_icd.code AND m_cat_opa_icd.lang = ? ) latjl "+
			"ON true WHERE visit_nb = ?), y AS ( "+
			"SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, preflang "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Diagnosis") + " bvt, " + "LATERAL "
			+ "		(SELECT preflang FROM " + MappingsConfig.TABLE_NAMES.get("Company") + " c, "
			+ MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ "			WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ "	(SELECT description FROM " + MappingsConfig.TABLE_NAMES.get("OPA_ICD") 
			+ "	WHERE bvt.data = m_cat_opa_icd.code AND lat.preflang = m_cat_opa_icd.lang) latjl ON true "
			+ "WHERE visit_nb=?)"+
			"SELECT * from X UNION ALL SELECT * FROM Y where rank NOT IN (SELECT rank from X ) ORDER BY rank limit ? offset ? ;";

	public static final String FIND_TREATMENTS_BY_VISIT_NB_COUNT = "SELECT COUNT(*) "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Treatment") + " bvt, " + "LATERAL "
			+ "		(SELECT preflang FROM " + MappingsConfig.TABLE_NAMES.get("Company") + " c, "
			+ MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ "			WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ "	(SELECT description FROM " + MappingsConfig.TABLE_NAMES.get("OPA_CHOP") + " "
			+ "		WHERE bvt.data = m_cat_opa_chop.code AND lat.preflang = m_cat_opa_chop.lang) latjl ON true "
			+ "WHERE visit_nb=?";

	public static final String FIND_DIAGNOSIS_BY_VISIT_NB = "SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, lang "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Diagnosis") + " bvt, " + "LATERAL " + "(SELECT preflang FROM "
			+ MappingsConfig.TABLE_NAMES.get("Company") + " c, " + MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ " WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ " (SELECT description, lang FROM " + MappingsConfig.TABLE_NAMES.get("OPA_ICD") + " "
			+ " WHERE bvt.data = m_cat_opa_icd.code AND lat.preflang = m_cat_opa_icd.lang) latjl ON true"
			+ " WHERE visit_nb = ? ORDER BY rank limit ? offset ? ;";
	
	public static final String FIND_ALL_DIAGNOSIS_BY_VISIT_NB = "SELECT COUNT(*) OVER() AS nb_records, bvt.rank, bvt.type, bvt.data, description, lang "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Diagnosis") + " bvt, " + "LATERAL " + "(SELECT preflang FROM "
			+ MappingsConfig.TABLE_NAMES.get("Company") + " c, " + MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ " WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ " (SELECT description, lang FROM " + MappingsConfig.TABLE_NAMES.get("OPA_ICD") + " "
			+ " WHERE bvt.data = m_cat_opa_icd.code AND lat.preflang = m_cat_opa_icd.lang) latjl ON true"
			+ " WHERE visit_nb = ? ORDER BY rank;";

	public static final String FIND_DIAGNOSIS_BY_VISIT_NB_COUNT = "SELECT COUNT(*) "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Diagnosis") + " bvt, " + "LATERAL " + "(SELECT preflang FROM "
			+ MappingsConfig.TABLE_NAMES.get("Company") + " c, " + MappingsConfig.TABLE_NAMES.get("Visit") + " v "
			+ " WHERE bvt.visit_nb = v.nb AND v.company_id = c.id) lat " + "LEFT JOIN LATERAL "
			+ " (SELECT description FROM " + MappingsConfig.TABLE_NAMES.get("OPA_ICD") + " "
			+ " WHERE bvt.data = m_cat_opa_icd.code AND lat.preflang = m_cat_opa_icd.lang) latjl ON true"
			+ " WHERE visit_nb = ?";

	public static final String VISIT_DETAIL = "SELECT bpvl.*, c.name, c.code FROM " + MappingsConfig.TABLE_NAMES.get("bmv_visit_detail") + " bpvl LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Company")
	+" c ON bpvl.company_id = c.id WHERE nb= ?";
	
	public static final String PATIENT_VISITS_BY_COMPANY_ID_AND_PID = "SELECT COUNT(*) OVER() AS nb_records, bpvl.*, c.name, c.code, "
			+ " vp.attending_physician_id as attendingPhysicianId, ph.lastname as attendingPhysicianLastname, ph.firstname as attendingPhysicianFirstname "
			+ " FROM " + MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list") + " bpvl "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Company") + " c ON bpvl.company_id = c.id "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("visit_physician") + " vp ON bpvl.nb = vp.visit_nb "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Physician") + " ph ON vp.attending_physician_id = ph.id "
			+ " WHERE bpvl.company_id = ? AND bpvl.patient_id = ? order by ? LIMIT ? OFFSET ?";

	public static final String PATIENT_OPEN_VISITS_BY_COMPANY_ID_AND_PID = "SELECT COUNT(*) OVER() AS nb_records, bpvl.*, c.name, c.code, "
			+ " vp.attending_physician_id as attendingPhysicianId, ph.lastname as attendingPhysicianLastname, ph.firstname as attendingPhysicianFirstname "
			+ " FROM " + MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list") + " bpvl "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Company") + " c ON bpvl.company_id = c.id "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("visit_physician") + " vp ON bpvl.nb = vp.visit_nb "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Physician") + " ph ON vp.attending_physician_id = ph.id "
			+ "WHERE bpvl.company_id = ? AND bpvl.patient_id = ? and bpvl.dischargedt is null order by ? LIMIT ? OFFSET ?";
	
	public static final String PATIENT_VISITS_BY_PID = "SELECT COUNT(*) OVER() AS nb_records, bpvl.*, c.name, c.code, "
			+ " vp.attending_physician_id as attendingPhysicianId, ph.lastname as attendingPhysicianLastname, ph.firstname as attendingPhysicianFirstname "
			+ " FROM " + MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list") + " bpvl "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Company") + " c ON bpvl.company_id = c.id "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("visit_physician") + " vp ON bpvl.nb = vp.visit_nb "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Physician") + " ph ON vp.attending_physician_id = ph.id "
			+ "WHERE bpvl.patient_id = ? order by ? LIMIT ? OFFSET ?";

	public static final String PATIENT_OPEN_VISITS_BY_PID_ = "SELECT COUNT(*) OVER() AS nb_records, " 
			+ " bpvl.*, c.name, c.code, vp.attending_physician_id as attendingPhysicianId, ph.lastname as attendingPhysicianLastname, ph.firstname as attendingPhysicianFirstname " 
			+ " FROM " + MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list") + " bpvl " 
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Company") + " c ON bpvl.company_id = c.id "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("visit_physician") + " vp ON bpvl.nb = vp.visit_nb "
			+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get("Physician") + " ph ON vp.attending_physician_id = ph.id "
			+ " WHERE bpvl.patient_id = ? and bpvl.dischargedt is null order by ? LIMIT ? OFFSET ?";
	
	public static final String VISIT_GUARANTORS = "SELECT bgd.*, nb_records FROM (SELECT distinct on (code) COUNT(*) OVER() As nb_records, * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantors_list") + " WHERE nb = ? ) bgl LEFT JOIN  "
			+ MappingsConfig.TABLE_NAMES.get("bmv_guarantor_detail")
			+ " bgd ON (bgl.id = bgd.id) order by ? limit ? offset ?";

	public static final String VISIT_GUARANTORS_COUNT = "SELECT COUNT(*) FROM (SELECT distinct on (code) COUNT(*) OVER() As nb_records, * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantors_list") + " WHERE nb = ? ) bgl LEFT JOIN  "
			+ MappingsConfig.TABLE_NAMES.get("bmv_guarantor_detail")
			+ " bgd ON (bgl.id = bgd.id)";

	public static final String FIND_COMPANIES = "select id,code,name,address,locality,postcode,canton,country,phone,fax,email,preflang"
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Company");

	public static final String FIND_COMPANIES_BY_ID = "select id,code,name,address,locality,postcode,canton,country,phone,fax,email,preflang "
			+ "FROM " + MappingsConfig.TABLE_NAMES.get("Company") + " WHERE id = ?";

	public static final String FIND_LIST_COMPANIES = "SELECT c.id, c.code, c.name, h.hospservices, u.units FROM "+ MappingsConfig.TABLE_NAMES.get("Company") + " c, LATERAL (SELECT json_agg(json_build_object('hospservice',hospservice,'hospservicedesc',hospservicedesc)) AS hospservices FROM "+ MappingsConfig.TABLE_NAMES.get("m_bmv_hospservice") + " WHERE company_id = c.id) AS h, LATERAL (SELECT json_agg(json_build_object('patientunit',patientunit,'patientunitdesc',patientunitdesc)) AS units FROM "+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientunit") + " WHERE company_id = c.id) AS u;";

	// Lab
	public static final String FIND_GROUPNAMES_BY_PATIENTID = "SELECT groupname FROM  "
			+ MappingsConfig.TABLE_NAMES.get("groupname_list")
			+ " WHERE patient_id = ?  GROUP BY groupname ORDER BY MIN(groupid)";
	public static final String FIND_LABDATA_BY_GROUPNAME_AND_PATIENTID = "SELECT patient_id,groupname,observationdt,analysisname,value,valuetype,unit,refrange,abnormalflag,abnormalflagdesc,resultstatus,resultstatusdesc,comments FROM "
			+ MappingsConfig.TABLE_NAMES.get("labdata_list")
			+ " WHERE patient_id = ? and groupname = ? ORDER BY analysisname, observationdt ASC";
	public static final String FIND_LABDATA_BY_PATIENTID_COUNT = "SELECT COUNT(*) FROM "
			+ MappingsConfig.TABLE_NAMES.get("labdata_list")
			+ " WHERE patient_id = ?";

	// ---------------------------------- Patient SELECT
	// -------------------------------------
	public static final String FIND_PATIENT_BY_PID = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("Patient")
	+ " p WHERE p.id = ?";
	public static final String FIND_PATIENTS_BY_PID = "SELECT bpl.* FROM (SELECT DISTINCT ON (patient_id) * FROM  WHERE company_id = ? AND patient_id = ? ORDER BY patient_id,nb DESC) bpl";
	public static final String FIND_PATIENTS_BY_VISIT_NB = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("PatientList") + " WHERE company_id= ? AND nb= ?";
	public static final String FIND_PATIENT_CONTACTS = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("PatientContact") + " WHERE id = ?";
	public static final String FIND_PATIENT_NEXTOFKIN_CONTACTS = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("PatientNextOfKinContact") + " WHERE id = ?";
	
	public static final String FIND_INHOUSE_PATIENTS_BY_ATTENDINGPHYSICIAN = "SELECT COUNT(*) OVER() AS nb_records, bppl.* FROM"+
			" (SELECT DISTINCT ON (patient_id) * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_physician_patients_list")+" WHERE company_id = ? AND staffid = ? "+
			"AND admitdt <= LOCALTIMESTAMP AND (dischargedt IS NULL OR dischargedt >= LOCALTIMESTAMP)"+
			"ORDER BY patient_id,nb DESC) bppl ORDER BY admitdt desc, UPPER(lastname) collate \"C\" , firstname ";
	public static final String FIND_INHOUSE_PATIENTS_BY_ATTENDINGPHYSICIAN_MULTIPLE_STAFFID_BASE = "SELECT COUNT(*) OVER() AS nb_records, bppl.* FROM"+
			" (SELECT DISTINCT ON (patient_id) * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_physician_patients_list")+
			" WHERE admitdt <= LOCALTIMESTAMP AND (dischargedt IS NULL OR dischargedt >= LOCALTIMESTAMP)";
	
	public static final String FIND_INHOUSE_PATIENTS_BY_ATTENDINGPHYSICIAN_MULTIPLE_STAFFID_ORDER_BY	= " ORDER BY patient_id,nb DESC) bppl ORDER BY admitdt desc, UPPER(lastname) collate \"C\" , firstname ";
	public static final String FIND_PATIENTS_BY_ATTENDINGPHYSICIAN_IN_NEXT_DAYS_MULTIPLE_STAFFID_BASE = "SELECT COUNT(*) OVER() AS nb_records, bppl.* FROM"+
			" (SELECT DISTINCT ON (patient_id) * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_physician_patients_list")+
			" WHERE admitdt BETWEEN current_date + INTERVAL '1 day' AND current_date + INTERVAL '6 days'";
	
	public static final String FIND_PATIENTS_BY_ATTENDINGPHYSICIAN_IN_NEXT_DAYS = "SELECT COUNT(*) OVER() AS nb_records, bppl.* FROM"+
			" (SELECT DISTINCT ON (patient_id) * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_physician_patients_list")+" WHERE company_id = ? AND staffid = ? "+
			"AND admitdt BETWEEN current_date + INTERVAL '1 day' AND current_date + INTERVAL '6 days'"+
			"ORDER BY patient_id,nb DESC) bppl ORDER BY admitdt desc, UPPER(lastname) collate \"C\" , firstname ";
	
	public static final String FIND_PATIENTS_BY_COMPANYID_AND_PATIENTUNIT = "SELECT COUNT(*) OVER() AS nb_records, bpl.* FROM (SELECT DISTINCT ON (patient_id) * FROM "
			+ MappingsConfig.TABLE_NAMES.get("PatientList")
			+ " WHERE company_id = ? AND patientunit = ? AND admitdt < NOW() AND (dischargedt is null or dischargedt > NOW()) ORDER BY patient_id, admitdt DESC) bpl ORDER BY orderByParam sortOrderParam LIMIT ? OFFSET ?";

	public static final String FIND_PATIENTS_BY_CRITERIA_BASE = "SELECT COUNT(*) OVER() AS nb_records, bpl.* FROM "
			+ "(SELECT DISTINCT ON (pl.patient_id) pl.* FROM " + MappingsConfig.TABLE_NAMES.get("PatientList") + " pl ";
	public static final String PATIENTS_COUNT_BY_CRITERIA_BASE = "SELECT COUNT(*) AS nb_records, upper(unaccent(SUBSTR(lastname, 1, ? ))) AS substr FROM " 
			+ "(SELECT DISTINCT (patient_id), lastname FROM " + MappingsConfig.TABLE_NAMES.get("PatientList") + " pl ";

	public static final String INNER_JOIN_PATIENT_CONTACT_LIST = " INNER JOIN " + MappingsConfig.TABLE_NAMES.get("PatientContact") + " pcl on pl.patient_id = pcl.id ";
	
	public static final String PATIENTS_COUNT_BY_ADMIT_DATE_BASE =
		" SELECT count(*) OVER (PARTITION BY date_trunc('day', admitdt)) AS date_count, date_trunc('day', admitdt)::DATE AS admitdate " +
		" FROM ( SELECT DISTINCT ON (patient_id) * FROM " + MappingsConfig.TABLE_NAMES.get("PatientList"); 
	
	public static final String FIND_PATIENTS_BY_CRITERIA_BASE_ORDER = "ORDER BY patient_id,nb DESC) bpl ORDER BY ";

	public static final String FIND_PREADMITTED_PATIENTS_BY_CRITERIA = FIND_PATIENTS_BY_CRITERIA_BASE
			+ " AND admitdt > LOCALTIMESTAMP";
	public static final String FIND_INHOUSE_PATIENTS_BY_CRITERIA = FIND_PATIENTS_BY_CRITERIA_BASE
			+ " AND admitdt <= LOCALTIMESTAMP AND (dischargedt IS NULL OR dischargedt >= LOCALTIMESTAMP)";

	public static final String PREADMITTED_PATIENTS_CRITERIA = "admitdt > LOCALTIMESTAMP";
	public static final String INHOUSE_PATIENTS_CRITERIA = " admitdt <= LOCALTIMESTAMP AND coalesce(dischargedt, 'infinity') >= LOCALTIMESTAMP";
	public static final String MALE_PATIENTS_CRITERIA="lower(sex) IN ('m', 'masculin','männlich','maschile')";
	public static final String FEMALE_PATIENTS_CRITERIA="lower(sex) IN ('f', 'weiblich','féminin','femminile')";
	public static final String UNDETERMINED_GENDER_PATIENTS_CRITERIA="(sex = 'U' OR sex is null)";

	// COMPANY
	public static final String FIND_COMPANY_BY_ID = "SELECT c.id, c.code, c.name, h.hospservices, u.units FROM "+ MappingsConfig.TABLE_NAMES.get("Company") + " c, LATERAL (SELECT json_object_agg(hospservice, hospservicedesc ORDER BY hospservice) AS hospservices FROM m_bmv_hospservice WHERE company_id = c.id) AS h, LATERAL (SELECT json_object_agg(patientunit, patientunitdesc ORDER BY patientunit) AS units FROM "+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientunit") + " WHERE company_id = c.id) AS u;";
	public static final String FIND_LIST_UNITS_BY_ID = "SELECT * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientunit") + " WHERE company_id = ? ORDER BY patientunit asc";
		
	public static final String FIND_LIST_UNITS_BY_ID_WITH_COUNT = 
			"WITH userelements AS (SELECT * FROM json_array_elements_text(?::json)) " + 
			"SELECT value AS patientunit, null AS patientunitdesc, COUNT(sub.patient_id) as nb_patients FROM userelements " + 
			"LEFT JOIN (" + 
			"	SELECT DISTINCT patient_id, patientunit FROM bmv_patients_list " +
			"	WHERE admitdt <= LOCALTIMESTAMP AND coalesce(dischargedt, 'infinity') >= LOCALTIMESTAMP " + 
			"	AND patientunit IN (SELECT value FROM userelements) " + 
			"	AND company_id = ?) sub " + 
			"ON userelements.value = sub.patientunit " + 
			"GROUP BY value";
	
	public static final String FIND_SERVICE_LIST_BY_ID = "SELECT DISTINCT ON (hospservice) * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_hospservice") + " WHERE company_id = ? ORDER BY hospservice asc";
	
	public static final String FIND_SERVICE_LIST_BY_ID_WITH_COUNT = 
			"WITH userelements AS (SELECT * FROM json_array_elements_text(?::json)) " + 
			"SELECT value AS hospservice, null AS hospservicedesc, COUNT(sub.patient_id) AS nb_patients FROM userelements " + 
			"LEFT JOIN (" + 
			"	SELECT DISTINCT patient_id, hospservice FROM bmv_patients_list " + 
			"	WHERE admitdt <= LOCALTIMESTAMP AND coalesce(dischargedt, 'infinity') >= LOCALTIMESTAMP " + 
			"	AND hospservice IN (SELECT value FROM userelements) " + 
			"	AND company_id = ?) sub " + 
			"ON userelements.value = sub.hospservice " + 
			"GROUP BY value";
	
	public static final String FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_HOSPSERVICE = "SELECT * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ " WHERE company_id = ? AND patientunit = ? AND hospservice = ? ORDER BY patientroom asc";	
	public static final String FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_HOSPSERVICE_AND_EXCLUDEROOMS = "SELECT * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ " WHERE company_id = ? AND patientunit = ? AND hospservice = ? and patientroom not in (?) ORDER BY patientroom asc";
	public static final String FIND_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_HOSPSERVICE_AND_EXCLUDEROOMS = "SELECT * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ " WHERE company_id = ? AND patientunit = ? AND hospservice = ? and patientroom not in (?) ORDER BY patientroom asc";	
	public static final String FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT = "SELECT DISTINCT ON (patientroom) * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ "  WHERE company_id = ? AND patientunit = ? ORDER BY patientroom asc";	
	public static final String FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_EXCLUDEROOMS = "SELECT DISTINCT ON (patientroom) * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ "  WHERE company_id = ? AND patientunit = ? and patientroom not in (?) ORDER BY patientroom asc";
	public static final String FIND_ROOM_LIST_BY_COMPANYID_PATIENTUNIT_AND_EXCLUDEROOMS = "SELECT DISTINCT ON (patientroom) * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ "  WHERE company_id = ? AND patientunit = ? and patientroom not in (?) ORDER BY patientroom asc";	
	public static final String FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_HOSPSERVICE = "SELECT DISTINCT ON (patientroom) * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ " WHERE company_id = ? AND hospservice = ? ORDER BY patientroom asc";	
	public static final String FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_HOSPSERVICE_AND_EXCLUDEROOMS = "SELECT DISTINCT ON (patientroom) * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ " WHERE company_id = ? AND hospservice = ? and patientroom not in (?) ORDER BY patientroom asc";
	public static final String FIND_ROOM_LIST_BY_COMPANYID_HOSPSERVICE_AND_EXCLUDEROOMS = "SELECT DISTINCT ON (patientroom) * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom")
			+ " WHERE company_id = ? AND hospservice = ? and patientroom not in (?) ORDER BY patientroom asc";
	public static final String FIND_PATIENT_BED_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_PATIENTROOM = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientbed")
			+ " WHERE company_id = ? AND patientunit = ? AND patientroom = ? ORDER BY patientbed asc";
	public static final String FIND_PATIENT_BED_LIST_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTROOM = "SELECT * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientbed")
			+ " WHERE company_id = ? AND hospservice = ? AND patientroom = ? ORDER BY patientbed asc";
	public static final String FIND_PATIENT_BED_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_HOSPSERVICE_AND_PATIENTROOM = "SELECT * FROM  "
			+ MappingsConfig.TABLE_NAMES.get("m_bmv_patientbed")
			+ " WHERE company_id = ? AND patientunit = ? AND hospservice = ? AND patientroom = ? ORDER BY patientbed asc";

	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND patientunit = ? AND company_id = ? group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT_INOUT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("PatientList")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND patientunit = ? AND hl7code = ? AND company_id = ? group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT_AND_EXCLUDEROOMS = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND patientunit = ? AND company_id = ? and patientroom not in (?) group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT_AND_EXCLUDEROOMS_INOUT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("PatientList")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND patientunit = ? AND hl7code = ? AND company_id = ? and patientroom not in (?) group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND company_id = ? group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_INOUT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("PatientList")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND hl7code = ? AND company_id = ? group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_EXCLUDEROOMS = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND company_id = ? and patientroom not in (?) group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_EXCLUDEROOMS_INOUT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients from "
			+ MappingsConfig.TABLE_NAMES.get("PatientList")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND hl7code = ? AND company_id = ? and patientroom not in (?) group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND patientunit = ? AND company_id = ? group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT_INOUT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients FROM "
			+ MappingsConfig.TABLE_NAMES.get("PatientList")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND patientunit = ? AND hl7code = ? AND company_id = ? group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT_AND_EXCLUDEROOMS = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND patientunit = ? AND company_id = ? and patientroom not in (?) group by patientroom order by patientroom asc";
	public static final String FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT_AND_EXCLUDEROOMS_INOUT = "SELECT patientroom, COUNT(DISTINCT(patient_id)) AS nb_patients FROM "
			+ MappingsConfig.TABLE_NAMES.get("PatientList")
			+ " where admitdt < NOW() AND (dischargedt is null or dischargedt  > NOW()) AND hospservice = ? AND patientunit = ? AND hl7code = ? AND company_id = ? and patientroom not in (?) group by patientroom order by patientroom asc";
	public static final String VISIT_POLICIES = "SELECT COUNT(*) OVER() AS nb_records, * FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantors_list") + " WHERE nb = ? order by ? limit ? offset ?";

	public static final String FIND_VISIT_POLICIES = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantors_list") + " WHERE nb = ?";
	
	public static final String VISIT_POLICIES_COUNT = "SELECT COUNT(*) FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantors_list") + " WHERE nb = ?";
	
	public static final String VISIT_COUNT_BY_DATE = "SELECT COUNT(*) FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list") + " WHERE company_id  = ? and admitdt::date = to_date(?, 'YYYY-MM-DD')";

	public static final String IS_AMB = " AND patientroom like '0' ";
	public static final String IS_HOSP = " AND patientroom != '0' ";
	
	public static final String VISIT_POLICY_DETAIL = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantors_list")
			+ " WHERE nb = ? AND id = ? AND priority = ? AND subpriority = ?";
	
	// Capacity
	public static final String CAPACITY_BY_COMPANY = "SELECT roomnumber, nbbed FROM m_bmv_capacity where company_id = ? ";
	public static final String AND_BY_UNIT = " AND unit = ? ";
	public static final String AND_BY_SERVICE = " AND service = ? ";
	public static final String CAPACITY_ORDER_BY = " ORDER BY roomnumber ASC LIMIT ? OFFSET ? ";
	
	//company metadata
	public static final String FIND_COMPANY_METADATA_BY_COMPANY_ID_AND_TITLE = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("company_metadata") + " WHERE company_id = ? AND title = ?";

	// Radiology
	public static final String FIND_RADIOLOGY_BY_PATIENTID = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_radiological_serie")
			+ " WHERE patient_id = ? ORDER BY serieobsdt DESC";
	public static final String FIND_RADIOLOGY_BY_PATIENTID_COUNT = "SELECT COUNT(*) FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_radiological_serie")
			+ " WHERE patient_id = ?";

	// Radiology Report
	public static final String FIND_RADIOLOGY_REPORT_BY_PATIENTID = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_radiological_report")
			+ " WHERE patient_id = ?";
	public static final String FIND_RADIOLOGY_REPORT_BY_STUDYUID = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_radiological_report")
			+ " WHERE studyuid = ?";
	public static final String FIND_RADIOLOGY_REPORT_BY_PATIENTID_COUNT = "SELECT COUNT(*) FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_radiological_report")
			+ " WHERE patient_id = ?";

	// APPOINTMENT
	public static final String FIND_APPOINTMENTS_BY_COMPANYId_PATIENTUNIT_HOSPSERVICE_BASE="SELECT COUNT(*) OVER() AS nb_records,bpa.appoint_id,bpa.patientroom,bpa.pid,bpa.nb,bpa.lastname,bpa.firstname,bpa.maidenname,bpa.begindt,bpa.description FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_appointment_list") + " bpa";
	public static final String FIND_APPOINTMENTS_BY_COMPANYId = "SELECT COUNT(*) OVER() AS nb_records,bpa.appoint_id,bpa.patientroom,bpa.pid,bpa.nb,bpa.lastname,bpa.firstname,bpa.maidenname,bpa.begindt,bpa.description FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_appointment_list")
			+ " bpa WHERE company_id = ? AND status != 'Deleted' AND begindt >= current_date ORDER BY ? LIMIT ? OFFSET ?;";
	public static final String FIND_APPOINTMENTS_BY_COMPANYId_PATIENTUNIT = "SELECT COUNT(*) OVER() AS nb_records,bpa.appoint_id,bpa.patientroom,bpa.pid,bpa.nb,bpa.lastname,bpa.firstname,bpa.maidenname,bpa.begindt,bpa.description FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_appointment_list")
			+ " bpa WHERE company_id = ? AND patientunit = ? AND status != 'Deleted' AND begindt >= current_date ORDER BY ? LIMIT ? OFFSET ?;";
	public static final String FIND_APPOINTMENTS_BY_COMPANYId_PATIENTUNIT_HOSPSERVICE = "SELECT COUNT(*) OVER() AS nb_records,bpa.appoint_id,bpa.patientroom,bpa.pid,bpa.nb,bpa.lastname,bpa.firstname,bpa.maidenname,bpa.begindt,bpa.description FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_appointment_list")
			+ " bpa WHERE company_id = ? AND patientunit = ? AND hospservice= ? AND status != 'Deleted' AND begindt >= current_date ORDER BY ? LIMIT ? OFFSET ?;";
	public static final String FIND_APPOINTMENTS_BY_COMPANYId_HOSPSERVICE = "SELECT COUNT(*) OVER() AS nb_records,bpa.appoint_id,bpa.patientroom,bpa.pid,bpa.nb,bpa.lastname,bpa.firstname,bpa.maidenname,bpa.begindt,bpa.description FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_appointment_list")
			+ " bpa WHERE company_id = ? AND hospservice= ? AND status != 'Deleted' AND begindt >= current_date ORDER BY ? LIMIT ? OFFSET ?;";
	public static final String FIND_APPOINTMENTS_BY_VISIT = "SELECT COUNT(*) OVER() AS nb_records, bpa.* FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_personnel_appointment_list")
			+ " bpa WHERE visit_nb = ? ORDER BY bpa.begindt;";
	public static final String FIND_PHYSICIAN_NEXT_DAY_APPOINTMENT = "SELECT COUNT(*) OVER() AS nb_records, bpa.* FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_personnel_appointment_list")
			+ " bpa WHERE staffid= ? AND company_id= ? AND begindt > to_date(?, 'YYYY-MM-DD') AND enddt < to_date(?,'YYYY-MM-DD') + interval '1 day' ORDER BY begindt;";
	public static final String FIND_PHYSICIAN_NEXT_WEEK_APPOINTMENT = "SELECT COUNT(*) OVER() AS nb_records, bpa.* FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_personnel_appointment_list")
			+ " bpa WHERE staffid = ? AND company_id = ? AND begindt > to_date(?, 'YYYY-MM-DD') AND enddt < to_date(?, 'YYYY-MM-DD') + interval '5 day' ORDER BY begindt;";

	// doctor appointment
	public static final String FIND_DOCTOR_APPOINTMENTS_MULTIPLE_STAFFID_BASE = "SELECT COUNT(*) OVER() AS nb_records, bpal.* FROM bmv_personnel_appointment_list bpal WHERE bpal.status='Booked' ";
	public static final String FIND_DOCTOR_APPOINTMENTS = "SELECT COUNT(*) OVER() AS nb_records, bpal.* FROM bmv_personnel_appointment_list bpal WHERE bpal.staffid = ? AND bpal.status='Booked' ";
	public static final String FIND_DOCTOR_APPOINTMENTS_DATE_PARAM = " AND 'dateParam' BETWEEN bpal.begindt::DATE AND bpal.enddt::DATE ";
	public static final String FIND_DOCTOR_APPOINTMENTS_COMPANYID_PARAM = " AND bpal.company_id = ? ";
	public static final String FIND_DOCTOR_APPOINTMENTS_WITHOUT_DATE_PARAM = " AND current_date <= bpal.enddt ";
	public static final String FIND_DOCTOR_APPOINTMENTS_ORDER_BY = " ORDER BY ? LIMIT ? OFFSET ?";
	//Appointment Detail Count
	public static final String FIND_DETAILED_APPOINTMENTS_BASE_COUNT = "SELECT COUNT(*) FROM "+ MappingsConfig.TABLE_NAMES.get("appointment_detail") + " bpal WHERE";
	public static final String FIND_DETAILED_APPOINTMENTS_UNIT_SERVICE_PID_BASE_COUNT = "SELECT COUNT(*) FROM "+ MappingsConfig.TABLE_NAMES.get("appointment_detail");
	//Appointment Detail
	public static final String FIND_DETAILED_APPOINTMENTS_MULTIPLE_STAFFID_BASE = "SELECT COUNT(*) OVER() AS nb_records, bpal.* FROM "+ MappingsConfig.TABLE_NAMES.get("appointment_detail") + " bpal WHERE";
	public static final String FIND_DETAILED_APPOINTMENTS_DATE_PARAM = " AND 'dateParam' BETWEEN bpal.begindt::DATE AND bpal.enddt::DATE ";
	public static final String FIND_DETAILED_APPOINTMENTS_WITHOUT_DATE_PARAM = " AND current_date <= bpal.enddt ";
	public static final String FIND_DETAILED_APPOINTMENTS_SINGLE_STAFF_ID = "SELECT COUNT(*) OVER() AS nb_records, bpal.* FROM "+ MappingsConfig.TABLE_NAMES.get("appointment_detail") + " bpal WHERE ";
	public static final String FIND_DETAILED_APPOINTMENTS_PATIENTID = "SELECT COUNT(*) OVER() AS nb_records, bpal.* FROM "+ MappingsConfig.TABLE_NAMES.get("appointment_detail") + " bpal WHERE patient_id = ? ORDER BY ? LIMIT ? OFFSET ?";
	public static final String FIND_DETAILED_APPOINTMENTS_COMPANYID_PATIENTID = "SELECT COUNT(*) OVER() AS nb_records, bpal.* FROM "+ MappingsConfig.TABLE_NAMES.get("appointment_detail") + " bpal WHERE company_id = ? AND patient_id = ? ORDER BY ? LIMIT ? OFFSET ?";
	public static final String FIND_DETAILED_APPOINTMENTS_BY_COMPANYId_PATIENTUNIT_HOSPSERVICE_BASE = "SELECT COUNT(*) OVER() AS nb_records, bpa.* FROM "
			+ MappingsConfig.TABLE_NAMES.get("appointment_detail") + " bpa";
	public static final String FIND_BY_APPOINTMENT_ID = "SELECT COUNT(*) OVER() AS nb_records, bpal.* FROM "+ MappingsConfig.TABLE_NAMES.get("appointment_detail") + " bpal WHERE appointment_id = ? ";
	// Physician
	public static final String PHYSICIAN_BY_VISIT_NB = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_visit_physicians_list") + " WHERE visit_nb = ?";
	public static final String PHYSICIAN_BY_STAFFID = "SELECT * from bmv_physicians_list where staffid = ? AND company_id = ?";
	public static final String FIND_PHYSICIAN_CONTACTS_TLF = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("PhysicianContact") + " WHERE id = ? AND equipment like 'telephone'";
	public static final String PHYSICIAN_BY_ID = "SELECT * from bmv_physician_detail where id = ?";
	public static final String PHYSICIAN_FILTER_BY_FIELD = "SELECT pd.* FROM ehealth.bmv_physician_detail pd WHERE pd.firstname ILIKE CONCAT('%', ?, '%') OR pd.lastname ILIKE CONCAT('%', ?, '%') OR pd.physpecialitydesc ILIKE CONCAT('%', ?, '%')";
	public static final String PHYSICIAN_FILTER_BY_COMPANY_ID = "SELECT pd.* FROM ehealth.bmv_physician_detail pd WHERE pd.company_id = ?";

	//Resource_Personnel
	public static final String RESOURCE_PERSONNEL_BY_ID = "SELECT * from resource_personnel where id = ?";
	public static final String RESOURCE_PERSONNEL_FILTER_BY_FIELD = "SELECT rp.* FROM ehealth.resource_personnel rp WHERE rp.name ILIKE CONCAT('%', ?, '%') or rp.role ILIKE CONCAT('%', ?, '%')";
	public static final String RESOURCE_PERSONNEL_FILTER_BY_COMPANY_ID = "SELECT rp.* FROM ehealth.resource_personnel rp WHERE rp.company_id = ? ";


	public static final String FIND_INVOICE_LIST_BY_VISIT_NB = "SELECT COUNT(*) OVER() AS nb_records,* FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantor_invoices_list")
			+ " WHERE visit_nb = ? AND guarantor_id is null ORDER BY ? LIMIT ? OFFSET ?";

	public static final String FIND_INVOICE_LIST_BY_VISIT_NB_COUNT = "SELECT COUNT(*) FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantor_invoices_list")
			+ " WHERE visit_nb = ? AND guarantor_id is null";

	public static final String FIND_INVOICE_LIST_BY_VISIT_NB_AND_GUARANTOR_ID = "SELECT COUNT(*) OVER() AS nb_records,* FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantor_invoices_list")
			+ " WHERE visit_nb = ? AND guarantor_id = ? ORDER BY ? LIMIT ? OFFSET ?";

	public static final String FIND_INVOICE_LIST_BY_VISIT_NB_AND_GUARANTOR_ID_COUNT = "SELECT COUNT(*) FROM "
			+ MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantor_invoices_list")
			+ " WHERE visit_nb = ? AND guarantor_id = ? ";

	// ROLE_CATEGORY - NOTES
	public static final String ROLES_CATEGORIES_GET_ALL = "select role, category_id from notesandpictures.role_category ";

	public static final String ROLES_CATEGORIES_GET_CATEGORIES = "select distinct category_id from notesandpictures.role_category "
			+ "where role in (?)";

	public static final String ROLES_CATEGORIES_GET_CATEGORIES_DETAILS = "select distinct on (category.id) category.id, notesandpictures.role_category.priority, category.name from notesandpictures.role_category"
			+ " join notesandpictures.category on notesandpictures.role_category.category_id=notesandpictures.category.id"
			+ " where notesandpictures.role_category.role in (?)";

	//Shift Notes
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND shifted = true and p.username = ? and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND shifted = true and p.username = ? and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and editeddt between ? AND ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and categoryid in (%1$s) and deleted = ? and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and categoryid in (%1$s) and deleted = ? and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";

	// Shift Notes Count
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND shifted = true and p.username = ? and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and editeddt between ? AND ?";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND shifted = true and p.username = ? and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and editeddt between ? AND ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_PID_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and categoryid in (?) and deleted = ? and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_PID_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE shifted = true and patient_id = ? and categoryid in (?) and deleted = ? and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? ";
	public static final String FIND_SHIFT_NOTES_BY_VISITNB_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";

	//Notes
	public static final String FIND_ALL_NOTES_BY_PID_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s";
	public static final String FIND_ALL_NOTES_BY_PID_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s";
	public static final String FIND_ALL_NOTES_BY_PID_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s";
	public static final String FIND_ALL_NOTES_BY_PID_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s";
	
	public static final String FIND_NOTES_BY_PID = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = false and patient_id = ? and categoryid in (%1$s) and deleted = ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = false and patient_id = ? and categoryid in (%1$s) and deleted = ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = false and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE shifted = false and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = false where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND shifted = false and p.username = ? and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND shifted = false and p.username = ? and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (%1$s) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_PID_AND_READ_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (?) and deleted = ? and shifted = false where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false)";
	public static final String FIND_NOTES_BY_PID_AND_READ_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT= "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_NOTE_BY_ID = "SELECT * FROM notesandpictures.bmv_note_details WHERE noteid = ?";
	public static final String FIND_NOTE_BY_ID_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE noteid = ? AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	
	public static final String GET_HAS_READ_FOR_NOTE = "SELECT CAST(COUNT(1) AS BIT) AS read FROM notesandpictures.profile_note_track WHERE username = ? AND note_id = ?";
	

	public static final String FIND_NOTES_BY_PID_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE patient_id = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_PID_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE patient_id = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_NOTES_BY_PID_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";

	public static final String FIND_NOTES_BY_VISITNB = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = false where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_ALL_NOTES_BY_VISITNB_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false order by referencedt desc";
	public static final String FIND_ALL_NOTES_BY_VISITNB_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false order by referencedt desc";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false order by referencedt desc LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by referencedt desc LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (%1$s) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false) order by %2$s %3$s LIMIT ? OFFSET ?";
	public static final String FIND_ALL_NOTES_BY_VISITNB_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (%1$s) and deleted = ? and shifted = false order by %2$s %3$s";	
	public static final String FIND_ALL_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false order by referencedt desc";	
	public static final String FIND_NOTES_BY_VISITNB_AND_READ_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false)";
	public static final String FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false where EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = n.noteid and deleted = false)";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_NOTES_BY_VISITNB_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_VISITNB_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";
	public static final String FIND_NOTES_BY_VISITNB_AND_CREATOR_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false";
	public static final String FIND_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES_COUNT = "SELECT COUNT(*) FROM notesandpictures.bmv_note_details WHERE visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = false AND EXISTS (SELECT 1 FROM notesandpictures.picture pic WHERE pic.note_id = noteid and deleted = false)";

	public static final String FIND_NOTE_HISTORY = "select * from bmv_note_history where note_id = ? order by date desc limit ? offset ?";

	public static final String FIND_NOTE_HISTORY_COUNT = "select COUNT(*) from bmv_note_history where note_id = ?";

	// PICTURES 

	public static final String FIND_PICTURES_BY_NOTE_ID = "SELECT p.* FROM notesandpictures.picture p INNER JOIN notesandpictures.bmv_note_details n ON (n.noteid=p.note_id and p.note_id=? and p.deleted=?) ORDER BY sortorder asc LIMIT ? OFFSET ?";

	public static final String FIND_PICTURES_BY_NOTE_ID_COUNT = "SELECT COUNT(*) FROM notesandpictures.picture WHERE note_id = ? and deleted = ?";

	public static final String FIND_PICTURE_BY_ID = "SELECT * FROM notesandpictures.picture WHERE id = ? ";

	//Pictures for shift notes

	public static final String FIND_ALL_PICTURES_BY_SHIFT_NOTE_ID = "SELECT p.* FROM notesandpictures.picture p INNER JOIN notesandpictures.bmv_note_details n ON (n.noteid=p.note_id and n.shifted=true and p.note_id=? and p.deleted=?)";

	public static final String FIND_ALL_PICTURES_BY_SHIFT_NOTE_ID_COUNT = "SELECT COUNT(p.*) FROM notesandpictures.picture p INNER JOIN notesandpictures.bmv_note_details n ON (n.noteid=p.note_id and n.shifted=true and p.note_id=? and p.deleted=?)";

	public static final String FIND_ALL_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";

	public static final String FIND_ALL_SHIFT_NOTES_BY_PID_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and patient_id = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";

	public static final String FIND_ALL_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";

	public static final String FIND_ALL_SHIFT_NOTES_BY_PID_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and patient_id = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";

	public static final String FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";

	public static final String FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_READ = "SELECT * FROM notesandpictures.bmv_note_details n INNER JOIN notesandpictures.profile_note_track p ON n.noteid=p.note_id AND p.username = ? and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";

	public static final String FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and creator = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";

	public static final String FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_UNREAD = "SELECT * from notesandpictures.bmv_note_details where noteid not in (select note_id from notesandpictures.profile_note_track where username = ?) and visit_nb = ? and categoryid in (?) and deleted = ? and shifted = true and editeddt between ? AND ? order by referencedt desc";


	// Benefits 
	public static final String FIND_BENEFIT_LIST_BY_VISIT_NB_AND_LANG = "SELECT COUNT(*) OVER() AS nb_records,id,visit_nb,code,benefitdt,quantity,side,actingdptdesc,note,COALESCE(latu.lang, latpref.lang) as lang,COALESCE(latu.description, latpref.description) as description FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list")+" vbl, LATERAL (SELECT preflang FROM company c, visit v WHERE vbl.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbl.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latpref ON true LEFT JOIN LATERAL (SELECT description, lang FROM m_cat_opa_ben "
			+" WHERE vbl.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang = ? ) latu ON true WHERE visit_nb = ? ";
	public static final String FIND_BENEFIT_LIST_BY_VISIT_NB_AND_LANG_COUNT = "SELECT COUNT(*) FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list")+" vbl, LATERAL (SELECT preflang FROM company c, visit v WHERE vbl.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbl.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latpref ON true LEFT JOIN LATERAL (SELECT description, lang FROM m_cat_opa_ben "
			+" WHERE vbl.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang = ?) latu ON true WHERE visit_nb = ? ";
	
	public static final String FIND_BENEFIT_LIST_BY_VISIT_NB = "SELECT COUNT(*) OVER() AS nb_records,id,visit_nb,code,benefitdt,quantity,side,actingdptdesc,note,COALESCE(latu.lang, latpref.lang) as lang,COALESCE(latu.description, latpref.description) as description, case (select 1 where vbp.visit_benefit_id is null) when 1 then false else true end as hasPhysician FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list")+" vbl "
			+" left join visit_benefit_physician vbp on vbl.id = vbp.visit_benefit_id, "
			+" LATERAL (SELECT preflang FROM company c, visit v WHERE vbl.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbl.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latpref ON true LEFT JOIN LATERAL (SELECT description, lang FROM m_cat_opa_ben "
			+" WHERE vbl.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang = lat.preflang) latu ON true WHERE visit_nb = ? ";
	public static final String FIND_BENEFIT_LIST_BY_VISIT_NB_COUNT = "SELECT COUNT(*) FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list")+" vbl, LATERAL (SELECT preflang FROM company c, visit v WHERE vbl.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbl.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latpref ON true LEFT JOIN LATERAL (SELECT description, lang FROM m_cat_opa_ben "
			+" WHERE vbl.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang = lat.preflang) latu ON true WHERE visit_nb = ? ";

	public static final String FIND_BENEFIT_DETAIL_BY_ID = "SELECT id,visit_nb,code,benefitdt,quantity,side,actingdptdesc,note,pp_id,pp_firstname,pp_lastname,op_id,op_firstname,op_lastname,lp_id,lp_firstname,lp_lastname,COALESCE(latu.lang, latdef.lang) AS lang,COALESCE(latu.description, latdef.description) AS description FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_detail")+ " vbd,LATERAL (SELECT preflang FROM company c, visit v WHERE vbd.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbd.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latdef ON true LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbd.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang=lat.preflang) latu ON true WHERE id = ?";
	public static final String FIND_BENEFIT_DETAIL_BY_ID_AND_LANG = "SELECT id,visit_nb,code,benefitdt,quantity,side,actingdptdesc,note,pp_id,pp_firstname,pp_lastname,op_id,op_firstname,op_lastname,lp_id,lp_firstname,lp_lastname,COALESCE(latu.lang, latdef.lang) AS lang,COALESCE(latu.description, latdef.description) AS description FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_detail")+ " vbd,LATERAL (SELECT preflang FROM company c, visit v WHERE vbd.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbd.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latdef ON true LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbd.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang = ? ) latu ON true WHERE id = ?";

	public static final String FIND_BENEFIT_FILTERED_LIST_BY_VISIT_NB = "SELECT COUNT(*) OVER() AS nb_records,id,visit_nb,code,benefitdt,quantity,side,actingdptdesc,note,COALESCE(latu.lang, latpref.lang) as lang,COALESCE(latu.description, latpref.description) as description, case (select 1 where vbp.visit_benefit_id is null) when 1 then false else true end as hasPhysician FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list")+" vbl left join visit_benefit_physician vbp on vbl.id = vbp.visit_benefit_id, LATERAL (SELECT preflang FROM ehealth.company c, ehealth.visit v WHERE vbl.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbl.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latpref ON true LEFT JOIN LATERAL (SELECT description, lang FROM ehealth.m_cat_opa_ben "
			+" WHERE vbl.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang = lat.preflang) latu ON true WHERE visit_nb = ? AND %1$s = ? ORDER BY %2$s %3$s LIMIT %4$d OFFSET %5$d";
	
	public static final String FIND_BENEFIT_FILTERED_LIST_BY_VISIT_NB_COUNT = "SELECT COUNT(*) AS nb_records FROM "
			+MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list")+" vbl, LATERAL (SELECT preflang FROM ehealth.company c, ehealth.visit v WHERE vbl.visit_nb = v.nb AND v.company_id = c.id) lat LEFT JOIN LATERAL "
			+" (SELECT description, lang FROM m_cat_opa_ben WHERE vbl.code = m_cat_opa_ben.code AND lat.preflang = m_cat_opa_ben.lang) latpref ON true LEFT JOIN LATERAL (SELECT description, lang FROM ehealth.m_cat_opa_ben "
			+" WHERE vbl.code = m_cat_opa_ben.code AND m_cat_opa_ben.lang = lat.preflang) latu ON true WHERE visit_nb = ? AND %1$s = ?";
	
	
	public static final String FIND_BENEFIT_FILTER_GROUPED_LIST_BY_VISIT_NB = "SELECT DISTINCT %1$s AS filter FROM ehealth.bmv_visit_benefit_list WHERE visit_nb = ? ORDER BY filter %2$s LIMIT %3$d OFFSET %4$d";
	public static final String FIND_BENEFIT_FILTER_GROUPED_LIST_BY_VISIT_NB_COUNT = "SELECT COUNT(*) OVER() AS nb_records FROM ehealth.bmv_visit_benefit_list WHERE visit_nb = ? GROUP BY %1$s LIMIT 1";
	
	// Interventions

	public static final String FIND_INTERVENTION_BY_VISIT_NB = "SELECT * FROM "+MappingsConfig.TABLE_NAMES.get("bmv_visit_intervention_data")+" WHERE visit_nb = ? ORDER BY rank asc";

	// Visits PatientList 
	
	public static final String VISITS_PATIENTLIST_BASE_CRITERIA = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_patientslist_detail")+" WHERE company_id = ? AND patientunit = ? AND patientroom <> '0'";
	
	public static final String VISITS_PATIENTLIST_ALL_CRITERIA = "(admitdt > LOCALTIMESTAMP OR (admitdt <= LOCALTIMESTAMP AND (dischargedt IS NULL OR dischargedt >= LOCALTIMESTAMP)))";
	
	public static final String VISITS_PATIENTLIST_COUNT_BASE_CRITERIA = "SELECT COUNT(*) FROM " + MappingsConfig.TABLE_NAMES.get("bmv_patientslist_detail")+" WHERE company_id = ? AND patientunit = ? ";
	
	public static final String VISITS_PATIENTLIST_FOOD_MOBILITY_UPDATE = "UPDATE " + MappingsConfig.TABLE_NAMES.get("bmv_patientslist_detail") + " set values () where nb = ? ";
	
	// Get Actions made over a Patient by an user
	public static final String FIND_PATIENT_ACTIONS_LOG_BY_USERNAME = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("UserAccess") + " WHERE parentpid = ? AND appuser = ? AND (externaluser IS NULL OR externaluser='') ORDER BY ? LIMIT ? OFFSET ? ";
	
	// Get Actions made over a Patient by an user with exteraluser
	public static final String FIND_PATIENT_ACTIONS_LOG_BY_USERNAME_AND_EXTERNALUSER = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("UserAccess") + " WHERE parentpid = ? AND appuser = ? AND externaluser = ? ORDER BY ? LIMIT ? OFFSET ? ";
	
	// Get Number of  Actions made over a Patient by an user
	public static final String FIND_PATIENT_ACTIONS_LOG_BY_USERNAME_COUNT = "SELECT count(*) FROM " + MappingsConfig.TABLE_NAMES.get("UserAccess") + " WHERE parentpid = ? AND appuser = ? AND (externaluser IS NULL OR externaluser='')";
	
	// Get Number of  Actions made over a Patient by an user with exteraluser
	public static final String FIND_PATIENT_ACTIONS_LOG_BY_USERNAME_AND_EXTERNALUSER_COUNT = "SELECT count(*) FROM " + MappingsConfig.TABLE_NAMES.get("UserAccess") + " WHERE parentpid = ? AND appuser = ? AND externaluser = ?";
	
	// Get Patients references for Rooms of a Clinic/Service 
	public static final String FIND_ROOMS_WITH_PATIENTS_REFERENCES = "SELECT room_count, patientroom, patient_id, lastname, firstname, maidenname, birthdate" +
			" FROM ( SELECT row_number() OVER (PARTITION BY patientroom ORDER BY patientroom, lastname collate \"C\", firstname collate \"C\") AS row_count," + 
				" count(*) OVER (PARTITION BY patientroom) AS room_count," +
		    	" patientroom,  patient_id, lastname, firstname, maidenname, birthdate" + 
		    	" FROM ( SELECT *  FROM bmv_patients_list" +
		    		" WHERE company_id = ? %1$s AND admitdt <= LOCALTIMESTAMP AND coalesce(dischargedt, 'infinity') >= LOCALTIMESTAMP" + 
		    		" ORDER BY patient_id, admitdt DESC) sub1" +  
		    " ) sub2" +
		    " WHERE row_count <= ?" +
		    " ORDER BY patientroom::integer %2$s LIMIT %3$d OFFSET %4$d";
	
	//Get Rooms with number of patients of a Clinic/Service
	public static final String FIND_ROOMS_WITH_PATIENTS_REFERENCES_COUNT = "SELECT room_count, patientroom" +
			" FROM ( SELECT row_number() OVER (PARTITION BY patientroom ORDER BY patientroom, lastname collate \"C\", firstname collate \"C\") AS row_count, " +
				" count(*) OVER (PARTITION BY patientroom) AS room_count," +
				" patientroom FROM ( SELECT * FROM bmv_patients_list" +
					" WHERE company_id = ? %1$s AND admitdt <= LOCALTIMESTAMP AND coalesce(dischargedt, 'infinity') >= LOCALTIMESTAMP" + 
		      		" ORDER BY patient_id, admitdt DESC) sub1" +
		    " ) sub2"+
		    " GROUP BY room_count, patientroom" +
		    " ORDER BY patientroom::integer %2$s";
	
	// Get Patients references for Rooms of a Clinic/Service 
	public static final String FIND_ROOMS_WITH_PATIENTS_DETAILS = "SELECT room_count, patientroom, *" +
			" FROM ( SELECT row_number() OVER (PARTITION BY patientroom ORDER BY patientroom, lastname collate \"C\", firstname collate \"C\") AS row_count," + 
				" count(*) OVER (PARTITION BY patientroom) AS room_count," +
		    	" *" + 
		    	" FROM ( SELECT * FROM bmv_patients_list" +
		    		" WHERE company_id = ? %1$s AND admitdt <= LOCALTIMESTAMP AND coalesce(dischargedt, 'infinity') >= LOCALTIMESTAMP" + 
		    		" ORDER BY patient_id, admitdt DESC) sub1" +  
		    " ) sub2" +
		    " WHERE row_count <= ?" +
		    " ORDER BY patientroom::integer %2$s LIMIT %3$d OFFSET %4$d";
	
	public static final String FIND_CATALOG_LIST_BY_COMPANYID_AND_TYPE = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("whiteboard_catalog")
			+ " WHERE company_code = ? AND type = ?";
	
	public static final String FIND_VISIT_BY_CRITERIA = 
			" SELECT bpl.nb, bpl.patient_id, bpl.admitdt, bpl.dischargedt, bvd.expdischargedt, bpl.patientclass, bpl.patientclassdesc, bpl.patienttype, bpl.patienttypedesc, bpl.patientcase, bpl.patientcasedesc, bpl.hospservice, bpl.hospservicedesc, bpl.admissiontype, bpl.admissiontypedesc, bpl.patientunit, bpl.patientunitdesc, bpl.patientroom, bpl.patientbed, bvd.priorroom, bvd.priorbed, bvd.priorunit, bvd.priorunitdesc, bvd.admitsource, bvd.admitsourcedesc, bpl.financialclass, bpl.financialclassdesc, bpl.hl7code, bpl.company_id, com.code, com.\"name\", bpl.patient_id, bpl.firstname, bpl.lastname, bpl.maidenname,bpl.birthdate "
			+ " FROM BMV_PATIENTS_LIST bpl "
			+ " INNER JOIN company com on bpl.company_id = com.id "
			+ " INNER JOIN bmv_visit_detail bvd on bpl.nb = bvd.nb "
			+ " WHERE "
			+ " bpl.admitdt <= LOCALTIMESTAMP "
			+ " AND (bpl.dischargedt IS NULL OR bpl.dischargedt >= LOCALTIMESTAMP) "
			+ " AND bpl.company_id = ? "
			+ " AND CONCAT(bpl.patientroom,'.',bpl.patientbed) IN (?) "
			+ " ORDER BY ? LIMIT ? OFFSET ? ";
			
	public static final String FIND_VISIT_BY_CRITERIA_COUNT = 
			" SELECT count(bpl.nb) as count "
			+ " FROM BMV_PATIENTS_LIST bpl "
			+ " INNER JOIN company com on bpl.company_id = com.id "
			+ " INNER JOIN bmv_visit_detail bvd on bpl.nb = bvd.nb "
			+ " WHERE "
			+ " bpl.admitdt <= LOCALTIMESTAMP "
			+ " AND (bpl.dischargedt IS NULL OR bpl.dischargedt >= LOCALTIMESTAMP) "
			+ " AND bpl.company_id = ? "
			+ " AND CONCAT(bpl.patientroom,'.',bpl.patientbed) IN (?) ";
			
	public static final String AND_LANG = " AND lang = ?"; 

	public static final String CATALOG_BY_PK = "SELECT * FROM "
			+ MappingsConfig.TABLE_NAMES.get("whiteboard_catalog")
			+ " WHERE company_code = ? AND lang = ? AND type = ? AND code = ?";

}