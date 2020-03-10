package net.fluance.cockpit.core.sql;

import net.fluance.cockpit.core.model.MappingsConfig;

public class PhysicianSQLStatements {
	public static final String PHYSICIAN_BY_ID_PERSONNEL = "SELECT * FROM "+MappingsConfig.TABLE_NAMES.get("ResourcePersonnel")+" WHERE staffid=? AND company_id=?";
	public static final String PHYSICIAN_BY_ID_PHYSICIAN = "SELECT * FROM "+MappingsConfig.TABLE_NAMES.get("PhysicianDetail")+" WHERE staffid=? AND company_id=?";
}
