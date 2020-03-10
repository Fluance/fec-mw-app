package net.fluance.cockpit.core.repository.jpa.patient;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.patient.Exercise;

@Repository
public interface PatientExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {

	Page<Exercise> findByPatientId(Long pid, Pageable pageable);
	
	@Query(value=
		" SELECT ex"
		+ " FROM Exercise ex"
		+ " WHERE"
		+ " ex.patientId =:pid"
		+ " AND ex.exerciseDate >= :from"
		+ " AND ex.exerciseDate <= :to")
	Page<Exercise> findByPatientIdFromTo(
		@Param("pid") Long pid,
		@Param("from") Timestamp from,
		@Param("to") Timestamp to,
		Pageable pageable
	);
	
	@Query(value=
		" SELECT ex"
		+ " FROM Exercise ex"
		+ " WHERE"
		+ " ex.patientId =:pid"
		+ " AND ex.exerciseDate >= :from")
	Page<Exercise> findByPatientIdFrom(
		@Param("pid") Long pid,
		@Param("from") Timestamp from,
		Pageable pageable
	);
	
	@Query(value=
		" SELECT ex"
		+ " FROM Exercise ex"
		+ " WHERE"
		+ " ex.patientId =:pid"
		+ " AND ex.exerciseDate <= :to")
	Page<Exercise> findByPatientIdTo(
		@Param("pid") Long pid,
		@Param("to") Timestamp to,
		Pageable pageable
	);
	
}
