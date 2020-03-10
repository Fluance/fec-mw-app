package net.fluance.cockpit.core.sql;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;

/**
 * 
 * SQL Statements for getting Access to the Whiteboard
 *         informations
 */
public class WhiteBoardSQLStatements {
	private static final String SELECT_ALL_FROM = "SELECT * FROM ";

	private static final String WHITEBOARD = "whiteboard";

	private static final String WHITEBOARD_VIEW = "whiteboard_view";
	private static final String COMPANY = "Company";
	private static final String ROOM_CONFIGURATION = "room_configuration";
	private static final String WHITEBOARD_CATALOG = "whiteboard_catalog";
	private static final String CAPACITY = "capacity";
	
	public static final String LIMIT_OFFSET = " ORDER BY ? LIMIT ? OFFSET ? ";

	private WhiteBoardSQLStatements() {
		throw new IllegalStateException("Utility class");
	}
	
	private static final String COMMON_SELECT_AND_FROM_PARTS = "SELECT DISTINCT(wbd.id), wbd.*, rc.room_type as room_type, "
			+ "wc.extra_1->>'room_capacity' as conf_capacity, cap.nbbed as original_capacity "
			+ " FROM " + MappingsConfig.TABLE_NAMES.get(WHITEBOARD_VIEW) + " wbd "
				+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get(COMPANY) + " c on c.id = wbd.company_id "
				+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get(ROOM_CONFIGURATION) + " rc on wbd.patientroom = rc.room " 
					+ " and c.code = rc.company_code "
					+ " and wbd.hospservice = rc.hospservice "
				+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get(WHITEBOARD_CATALOG) + " wc on wc.code = rc.room_type "
					+ "and wc.company_code = rc.company_code and wc.type = 'room_type' "
				+ " LEFT JOIN " + MappingsConfig.TABLE_NAMES.get(CAPACITY) + " cap ON cap.company_id = wbd.company_id "
						+ "and cap.service = wbd.hospservice and cap.roomnumber = wbd.patientroom ";

	public static final String SELECT_BY_COMPANY_ID_AND_SERVICE_ID = COMMON_SELECT_AND_FROM_PARTS 
			+ " WHERE wbd." + WhiteBoardMapper.COMPANY_ID + " = ?"
			+ " AND wbd." + WhiteBoardMapper.SERVICE_ID + " = ?";
	
	public static final String SELECT_BY_COMPANY_ID = COMMON_SELECT_AND_FROM_PARTS
			+ " WHERE wbd." + WhiteBoardMapper.ID + "=?";
		
	public static final String AND_BY_DATE = " AND (" + WhiteBoardMapper.EXP_ENTREE_DATE + "::date = ? OR (" + WhiteBoardMapper.ENTREE_DATE + "::date <= ? AND coalesce(" + WhiteBoardMapper.EDITED_EXPIRED_DATE + ", " + WhiteBoardMapper.EXPIRE_DATE + ", 'infinity')::timestamp >= ?))";
	public static final String AND_BY_DATE_RANGE = " AND " + WhiteBoardMapper.ENTREE_DATE + " <= LOCALTIMESTAMP AND coalesce(" + WhiteBoardMapper.EDITED_EXPIRED_DATE + ", " + WhiteBoardMapper.EXPIRE_DATE + ", 'infinity') >= LOCALTIMESTAMP";	
	public static final String AND_DISPLAY = " AND display IS NOT FALSE ";
	public static final String AND_CONF_CAPACITY = " AND CAST(wc.extra_1->>'room_capacity' AS INTEGER) = ? ";
	public static final String AND_ORIGINAL_CAPACITY = " AND cap.nbbed = ? ";

	public static final String UPDATE = "UPDATE " + MappingsConfig.TABLE_NAMES.get(WHITEBOARD) + "SET "
			+ WhiteBoardMapper.COMMENT + "= ?" + WhiteBoardMapper.DIET + "= ?"
			+ WhiteBoardMapper.ISOLATION_TYPE + "= ?" + WhiteBoardMapper.NURSE + "= ?"
			+ WhiteBoardMapper.OPERATION_DATE + "= ?" + WhiteBoardMapper.REASON + "= ?" + "WHERE "
			+ WhiteBoardMapper.ID + "=?";

	public static final String INSERT = "INSERT INTO " + MappingsConfig.TABLE_NAMES.get(WHITEBOARD) + " ("+ WhiteBoardMapper.COMMENT + "," + WhiteBoardMapper.DIET + ","
			+ WhiteBoardMapper.ISOLATION_TYPE + "," + WhiteBoardMapper.NURSE + ","
			+ WhiteBoardMapper.OPERATION_DATE + "," + WhiteBoardMapper.REASON
			+ ") values (?,?,?,?,?,?)";

	public static final String SELECT_BY_ID = COMMON_SELECT_AND_FROM_PARTS + " "
			+ "WHERE wbd." + WhiteBoardMapper.ID + "=?";

	public static final String SELECT_NURSES = "SELECT DISTINCT " + WhiteBoardMapper.NURSE + " FROM "
			+ MappingsConfig.TABLE_NAMES.get(WHITEBOARD_VIEW) + " WHERE " + WhiteBoardMapper.COMPANY_ID
			+ " = ? AND " + WhiteBoardMapper.SERVICE_ID + "=? AND " + WhiteBoardMapper.NURSE
			+ " IS NOT NULL LIMIT ? OFFSET ?";
	
	public static final String SELECT_PHYSICIANS = "SELECT DISTINCT " + WhiteBoardMapper.DOCTOR + " FROM "
			+ MappingsConfig.TABLE_NAMES.get(WHITEBOARD_VIEW) + " WHERE " + WhiteBoardMapper.COMPANY_ID
			+ " = ? AND " + WhiteBoardMapper.SERVICE_ID + "=? AND " + WhiteBoardMapper.DOCTOR
			+ " IS NOT NULL LIMIT ? OFFSET ?";

	public static final String COUNT_BY_VISIT_NUMBER = "SELECT COUNT(*) as c FROM " + MappingsConfig.TABLE_NAMES.get(WHITEBOARD) + " WHERE "+WhiteBoardMapper.VISIT_NUMBER+"=?";
	public static final String COUNT_BY_VISIT_NUMBER_VIEW = "SELECT COUNT(*) as c FROM " + MappingsConfig.TABLE_NAMES.get(WHITEBOARD_VIEW) + " WHERE "+WhiteBoardMapper.VISIT_NUMBER+"=? AND "+ WhiteBoardMapper.SERVICE_ID+"=? AND "+WhiteBoardMapper.COMPANY_ID+"=?";
}
