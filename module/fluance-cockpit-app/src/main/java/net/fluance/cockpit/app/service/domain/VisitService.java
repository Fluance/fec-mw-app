package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.dao.VisitDetailedDao;
import net.fluance.cockpit.core.model.VisitDetailed;
import net.fluance.cockpit.core.model.jdbc.visit.GuarantorList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyList;
import net.fluance.cockpit.core.repository.jdbc.visit.GuarantorListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyListRepository;
import net.fluance.cockpit.core.util.sql.GuarantorListOrderByEnum;
import net.fluance.cockpit.core.util.sql.PolicyListOrderByEnum;
import net.fluance.cockpit.core.util.sql.VisitListOrderByEnum;

@Service
public class VisitService {

	private static final String VISIT_SPLIT_SEPERATOR = ",";
	@Autowired
	private GuarantorListRepository guarantorListRepo;
	@Autowired
	private VisitPolicyListRepository policyListRepo;
	@Autowired
	private VisitListRepository visitListRepo;
	@Autowired
	private VisitDetailRepository visitDetailRepository;
	@Autowired
	private VisitDetailedDao visitDetailedDao;

	public List<GuarantorList> findGuarantorsByVisitNb(Integer visitnb, String orderBy, String sortorder, Integer limit, Integer offset) {
		GuarantorListOrderByEnum orderByEnum = GuarantorListOrderByEnum.permissiveValueOf(orderBy);
		orderBy = orderByEnum.getValue();
		return guarantorListRepo.findByVisitNb(visitnb, orderBy, sortorder, limit, offset);
	}

	public List<VisitPolicyList> findPoliciesByVisitNb(Integer visitnb, String orderBy, String sortorder, Integer limit, Integer offset) {
		PolicyListOrderByEnum orderByEnum = PolicyListOrderByEnum.permissiveValueOf(orderBy);
		orderBy = orderByEnum.getValue();
		return policyListRepo.findByVisitNb(visitnb, orderBy, sortorder, limit, offset);
	}

	public List<VisitList> findVisitList(Integer companyid, Integer pid, boolean openvisits, String orderby, String sortorder, Integer limit, Integer offset) {
		VisitListOrderByEnum orderByEnum = VisitListOrderByEnum.permissiveValueOf(orderby);
		orderby = orderByEnum.getValue();
		if (pid == null) {
			throw new IllegalArgumentException("pid cannot be null");
		}
		if (companyid == null) {
			return visitListRepo.findByPatientId(pid, openvisits, orderby, sortorder, limit, offset);
		} else {
			return visitListRepo.findByCompanyIdAndPatientId(companyid, pid, openvisits, orderby, sortorder, limit, offset);
		}
	}

	public Integer findGuarantorsByVisitNbCount(Long visitnb) {
		return guarantorListRepo.findByVisitNbCount(visitnb);
	}

	public Integer findPoliciesByVisitNbCount(Long visitnb) {
		return policyListRepo.findByVisitNbCount(visitnb);
	}

	public List<VisitDetail> getMultipleVisits(String visits) {
		List<VisitDetail> visitDetails = new ArrayList<>();
		List<Long> visitList = getVisitList(visits);
		for (Long visitId : visitList) {
			VisitDetail visitDetail = findVisitDetail(visitId);
			visitDetails.add(visitDetail);
		}
		return visitDetails;
	}

	protected List<Long> getVisitList(String visits) {
		if (visits == null || visits.isEmpty()) {
			return new ArrayList<>();
		}
		List<Long> visitIds = new ArrayList<>();
		for (String visit : visits.split(VISIT_SPLIT_SEPERATOR)) {
			try {
				Long visitId = Long.parseLong(visit);
				if (!visitIds.contains(visitId)) {
					visitIds.add(visitId);
				}
			} catch (NumberFormatException e) {
				Logger.getLogger(this.getClass().getName()).warning("Input is not a number [" + visit + "]");
			}
		}
		return visitIds;
	}

	/**
	 * Gets a {@link VisitDetail} identified by its Visit Number
	 * 
	 * @param visitNb
	 *            Identifier of the Visit
	 * @return
	 *         <ul>
	 *         <li>If exists a Visit with that number, returns the pertinent {@link VisitDetail}.</li>
	 *         <li>A {@link IllegalArgumentException} if the argument is null or the Visit Number does not exist in the
	 *         database.</li>
	 *         </ul>
	 */
	public VisitDetail findVisitDetail(Long visitNb) throws EmptyResultDataAccessException {
		if (visitNb == null) {
			throw new IllegalArgumentException("Visit Number is required");
		}
		VisitDetail visitDetail = visitDetailRepository.findByNb(visitNb);
		return visitDetail;
	}

	/**
	 * Amount of visits on a specific date
	 * @param companyid	Clinic that receives visits
	 * @param date	Date of Visits
	 * @param isHosp	Return only those that are hospitalized
	 * @param isAmb	Return only those that are not hospitalized
	 * @return
	 */
	public Integer visitCountByDate(Integer companyid, Date date, Boolean isHosp, Boolean isAmb) {
		return visitListRepo.visitCountByDate(companyid, date, isHosp, isAmb);
	}

	public List<VisitDetailed> getVisitByCriteria(Integer companyId, List<String> beds, Integer limit, Integer offset) {
		return visitDetailedDao.findAdmitedNowByCompanyIdAndBeds(companyId, beds, limit, offset);
	}

	public Long getVisitByCriteriaCount(Integer companyId, List<String> beds) {
		return visitDetailedDao.countAdmitedNowByCompanyIdAndBeds(companyId, beds);
	}
}
