package net.fluance.cockpit.core.repository.jpa.patient;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.patient.Weight;

@Repository
public interface PatientWeightRepository extends PagingAndSortingRepository<Weight, Long> {
	
	Page<Weight> findByPatientId(Long pid, Pageable page);
	
	@Query(value=
			" SELECT we"
			+ " FROM Weight we"
			+ " WHERE"
			+ " we.patientId =:pid"
			+ " AND we.weightDate >= :from"
			+ " AND we.weightDate <= :to")
		Page<Weight> findByPatientIdFromTo(
			@Param("pid") Long pid,
			@Param("from") Timestamp from,
			@Param("to") Timestamp to,
			Pageable pageable
		);
		
		@Query(value=
			" SELECT we"
			+ " FROM Weight we"
			+ " WHERE"
			+ " we.patientId =:pid"
			+ " AND we.weightDate >= :from")
		Page<Weight> findByPatientIdFrom(
			@Param("pid") Long pid,
			@Param("from") Timestamp from,
			Pageable pageable
		);
		
		@Query(value=
			" SELECT we"
			+ " FROM Weight we"
			+ " WHERE"
			+ " we.patientId =:pid"
			+ " AND we.weightDate <= :to")
		Page<Weight> findByPatientIdTo(
			@Param("pid") Long pid,
			@Param("to") Timestamp to,
			Pageable pageable
		);
}
