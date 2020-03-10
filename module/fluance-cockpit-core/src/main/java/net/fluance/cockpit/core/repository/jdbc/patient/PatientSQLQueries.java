package net.fluance.cockpit.core.repository.jdbc.patient;

import net.fluance.cockpit.core.model.MappingsConfig;

/**
 * Queries related with {@code Patient}
 *
 */
public class PatientSQLQueries {
	
	/**
	 * NEXT OF KINS
	 */
	
	public static final String FIND_PATIENT_NEXTOFKINS =
		"SELECT COUNT(*) OVER() AS nb_records, nok.*, ncl.equipment, ncl.data "
		+ "FROM " + MappingsConfig.TABLE_NAMES.get("PatientNextOfKin") + " nok "
		+ "INNER JOIN " + MappingsConfig.TABLE_NAMES.get("PatientNextOfKinContact") + " ncl ON nok.id = ncl.id "
		+ "WHERE nok.patient_id = ?";
	
}

