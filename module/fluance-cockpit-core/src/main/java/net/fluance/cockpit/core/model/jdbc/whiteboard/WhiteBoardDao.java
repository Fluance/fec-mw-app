package net.fluance.cockpit.core.model.jdbc.whiteboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.repository.jdbc.whiteboard.WhiteBoardRepository;
import net.fluance.cockpit.core.repository.jpa.whiteboard.WhiteBoardJpaRepository;
import net.fluance.cockpit.core.sql.WhiteBoardSQLStatements;
import net.fluance.cockpit.core.util.sql.WhiteboardOrderByEnum;
import net.fluance.commons.lang.DateUtils;

@Service
public class WhiteBoardDao {
	
	@Value("${whiteboard.default.sortOrder}")
	private String defaultSortOrder;
	
	@Value("${whiteboard.default.orderBy}")
	private String defaultOrderBy;
	
	@Value("${whiteboard.default.limit}")
	private Integer defaultLimit;
	
	@Value("${whiteboard.default.offset}")
	private Integer defaultOffset;

	private static final String SEPERATOR = ",";

	@Autowired
	WhiteBoardRepository whiteBoardRepository;

	@Autowired
	WhiteBoardJpaRepository whiteBoardJpaRepository;
	
	public List<WhiteBoardViewEntity> getAllWhiteboardEntries(Long companyId, String serviceId, LocalDate admitDate, Boolean display, Integer confCapacity, 
			Integer originalCapacity, Integer limit, Integer offset, String orderBy, String sortOrder) {
		
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(WhiteBoardSQLStatements.SELECT_BY_COMPANY_ID_AND_SERVICE_ID);
		//company and service are mandatory
		params.add(companyId);
		params.add(serviceId);
		
		if(admitDate != null){
			sql.append(WhiteBoardSQLStatements.AND_BY_DATE);
			
			Date parsedDate = Date.from(admitDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());			
			params.add(parsedDate);
			params.add(parsedDate);
			
			//Timestamp with hour after 12:00:00 --> 12:00:01
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsedDate);
			cal.add(Calendar.HOUR, 12);
			cal.add(Calendar.SECOND, 1);
			Date limitDateHourToCheckout = cal.getTime();
			params.add(limitDateHourToCheckout);
		}
		else {
			sql.append(WhiteBoardSQLStatements.AND_BY_DATE_RANGE);
		}
		
		sql.append(WhiteBoardSQLStatements.AND_DISPLAY);

		if(confCapacity != null) {
			sql.append(WhiteBoardSQLStatements.AND_CONF_CAPACITY);
			params.add(confCapacity);
		}
		
		if(originalCapacity != null) {
			sql.append(WhiteBoardSQLStatements.AND_ORIGINAL_CAPACITY);
			params.add(originalCapacity);
		}
		
		sql.append(WhiteBoardSQLStatements.LIMIT_OFFSET);		
		if (limit == null || limit < 0) {
			limit = defaultLimit;
		}
		
		if (offset == null || offset < 0) {
			offset = defaultOffset;
		}
		
		StringBuilder orderByWithSortOrder = new StringBuilder();
		if(StringUtils.isEmpty(orderBy)) {
			orderBy = defaultOrderBy;
		}

		String[] orderColumns = orderBy.split(SEPERATOR);
		for (String orderColumn : orderColumns) {
			WhiteboardOrderByEnum orderByD = WhiteboardOrderByEnum.permissiveValueOf(orderColumn);

			if(StringUtils.isEmpty(orderByD.getValue())) {
				orderColumn = defaultOrderBy;
			}

			if(Sort.Direction.fromStringOrNull(sortOrder) == null) {
				sortOrder = Sort.Direction.fromStringOrNull(defaultSortOrder).name();
			}
		
			orderByWithSortOrder.append(orderColumn).append(" ").append(sortOrder).append(",");
		}
		
		params.add(limit);
		params.add(offset);
				
		return whiteBoardRepository.findEntries(sql.toString().replace("ORDER BY ?", "ORDER BY " + orderByWithSortOrder.substring(0, orderByWithSortOrder.length()-1)), params);
	}

	public WhiteBoardViewEntity getOneEntry(Long whiteboardId) {
		List<Object> params = new ArrayList<>();
		params.add(whiteboardId);
		
		List<WhiteBoardViewEntity> entries = whiteBoardRepository.findEntries(WhiteBoardSQLStatements.SELECT_BY_COMPANY_ID, params);
		if (!entries.isEmpty()) {
			return entries.get(0);
		}
		return null;
		
	}

	public WhiteBoardViewEntity update(WhiteBoardViewEntity whiteBoard) {
		return whiteBoardJpaRepository.save(whiteBoard);
	}

	public List<String> getNurses(Long companyId, String serviceId, Integer limit, Integer offset) {
		return whiteBoardRepository.getNurses(companyId, serviceId, limit, offset);
	}
	
	public List<String> getPhysicians(Long companyId, String serviceId, Integer limit, Integer offset) {
		return whiteBoardRepository.getPhysicians(companyId, serviceId, limit, offset);
	}

	public boolean exist(Long id) {
		WhiteBoardViewEntity entry = this.getOneEntry(id);
		return (entry != null);
	}

	public Boolean existVisitNumber(Long visitNumber) {
		return whiteBoardRepository.existEntryForVisitNumber(visitNumber);
	}

	public WhiteBoardViewEntity getOneEntryByVisitNummer(Long visitNumber) {
		return whiteBoardJpaRepository.findByVisitNumber(visitNumber);
	}

	public Boolean existVisitNumberOnView(Long visitNumber, Long companyId, String serviceId) {
		return whiteBoardRepository.existEntryForVisitNumberOnView(visitNumber,companyId,serviceId);
	}

}
