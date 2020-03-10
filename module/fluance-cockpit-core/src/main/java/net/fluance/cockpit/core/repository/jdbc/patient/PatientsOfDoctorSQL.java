package net.fluance.cockpit.core.repository.jdbc.patient;

import net.fluance.cockpit.core.model.MappingsConfig;

public class PatientsOfDoctorSQL {
	public static final String FIND_PATIENTS_BY_ANY_PHYSICIAN = "SELECT COUNT(*) OVER() AS nb_records, bppl.* FROM"+
			" (SELECT DISTINCT ON (patient_id) * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_physician_visits_list")+" WHERE company_id = ? AND staffid = ? PATIENT_CLASS_CLAUSE "+
			"AND admitdt <= LOCALTIMESTAMP AND (dischargedt IS NULL OR dischargedt >= LOCALTIMESTAMP)"+
			"ORDER BY patient_id,nb DESC) bppl ORDER BY admitdt desc, UPPER(lastname) collate \"C\" , firstname ";

	public static final String FIND_PATIENTS_BY_ANY_PHYSICIAN_MULTIPLE_STAFFID_BASE = "SELECT COUNT(*) OVER() AS nb_records, bppl.* FROM"+
			" (SELECT DISTINCT ON (patient_id) * FROM " + MappingsConfig.TABLE_NAMES.get("bmv_physician_visits_list")+
			" WHERE admitdt <= LOCALTIMESTAMP AND (dischargedt IS NULL OR dischargedt >= LOCALTIMESTAMP)";
	
	public static final String FIND_PATIENTS_BY_ANY_PHYSICIAN_MULTIPLE_STAFFID_ORDER_BY	= " ORDER BY patient_id,nb DESC) bppl ORDER BY admitdt desc, UPPER(lastname) collate \"C\" , firstname ";
}
