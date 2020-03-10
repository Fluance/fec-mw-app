package net.fluance.cockpit.core.repository.jpa.visit;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.visit.VisitDetailedEntity;

@Repository
public interface VisitDetailedRepository extends PagingAndSortingRepository<VisitDetailedEntity, Long>{
	@Query(
		value=" SELECT vd"
		+ " FROM visit_detailed vd"
		+ " WHERE "
		+ " vd.admitDate <= CURRENT_TIMESTAMP "
		+ " AND (vd.dischargeDate IS NULL OR vd.dischargeDate >= CURRENT_TIMESTAMP) "
		+ " AND vd.companyId = :companyId "
		+ " AND vd.fullBed IN :beds ")
	public List<VisitDetailedEntity> findAdmitedNowByCompanyIdAndFullBedIn(
		@Param("companyId") Integer companyId,
		@Param("beds") List<String> beds,
		Pageable pageable
	);
	
	@Query(
		value=" SELECT count(vd)"
		+ " FROM visit_detailed vd"
		+ " WHERE "
		+ " vd.admitDate <= CURRENT_TIMESTAMP "
		+ " AND (vd.dischargeDate IS NULL OR vd.dischargeDate >= CURRENT_TIMESTAMP) "
		+ " AND vd.companyId = :companyId "
		+ " AND vd.fullBed IN :beds ")
	public Long countAdmitedNowByCompanyIdAndFullBedIn(
		@Param("companyId") Integer companyId,
		@Param("beds") List<String> beds
	);
}
