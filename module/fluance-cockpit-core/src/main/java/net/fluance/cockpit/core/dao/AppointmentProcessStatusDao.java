package net.fluance.cockpit.core.dao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusDto;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusMapper;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.model.jpa.appointment.AppointmentProcessStatus;
import net.fluance.cockpit.core.model.jpa.appointment.ProcessStatus;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDetailRepository;
import net.fluance.cockpit.core.repository.jpa.appointment.AppointmentProcessStatusRepository;
import net.fluance.cockpit.core.repository.jpa.appointment.ProcessStatusRepository;

/**
 * Data access service for getting the list of {@link AppointmentProcessStatusDto} of appointments that should be of type "operation".<br>
 * The logic to manage the live flag and operationLive flag for the status is included and applied.
 */
@Service
public class AppointmentProcessStatusDao {
	
	@Value("#{${whiteboardsurgery.config.operationLifeStatusByCompany}}")
	private Map<String, List<String>> lifeStatusByCompany;
	
//	@Value("#{${whiteboardsurgery.config.operationLifeStatusByCompany}}")
//	private Map<Long, List<String>> lifeStatusByCompany2;
	
	@Autowired
	private AppointmentProcessStatusRepository appointmentProcessStatusRepository;

	@Autowired
	private ProcessStatusRepository processStatusRepository;
	
	@Autowired
	private AppointmentProcessStatusMapper appointmentProcessStatusMapper;
	
	@Autowired
	private AppointmentDetailRepository appointmentDetailRepository; 

	/**
	 * Gets all the {@link AppointmentProcessStatusDto} for the given appointment with the correct live and operationLive flags
	 * 
	 * @param appointmentId
	 * @return list of {@link AppointmentProcessStatusDto} for the given appointment id with the correct live and operatiinLive flags
	 */
	public List<AppointmentProcessStatusDto> getProcessStatusByAppointmentId(Long appointmentId) {
		if (appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a value");
		}
		
		AppointmentDetail appointmentDetail = appointmentDetailRepository.findByAppointmentId(appointmentId);
		
		if(appointmentDetail == null) {
			throw new IllegalArgumentException("The given appointment don't exist");
		}
		
		LocalDateTime dateToTest = LocalDateTime.now();
		Map<Integer, List<ProcessStatus>> processSatusByCompany = new HashMap<Integer, List<ProcessStatus>>();
		
		return getProcessStatusForAppointment(appointmentDetail, addOrGetProcessSatusByCompany(processSatusByCompany, appointmentDetail.getCompanyId()), dateToTest);
	}
	
	/**
	 * Will get the {@link AppointmentProcessStatusDto} for the given list of {@link AppointmentDetail} with the correct live and operationLive flags
	 * 
	 * @param appointmentsDetail
	 * @return map with every appointmentId and the list of {@link AppointmentProcessStatusDto} for it with the correct live and operatiinLive flags
	 */
	public Map<Long, List<AppointmentProcessStatusDto>> getProcessStatusFromAppointmentList(List<AppointmentDetail> appointmentsDetail) {	
		LocalDateTime dateToTest = LocalDateTime.now();
		
		Map<Long, List<AppointmentProcessStatusDto>> appointmentProcessStatusByAppointment = new HashMap<Long, List<AppointmentProcessStatusDto>>();
		
		Map<Integer, List<ProcessStatus>> processSatusByCompany = new HashMap<Integer, List<ProcessStatus>>();
		
		for (AppointmentDetail appointmentDetail : appointmentsDetail) {
			appointmentProcessStatusByAppointment.put(appointmentDetail.getAppointmentId(), getProcessStatusForAppointment(appointmentDetail, addOrGetProcessSatusByCompany(processSatusByCompany, appointmentDetail.getCompanyId()), dateToTest));	
		}
		
		return appointmentProcessStatusByAppointment;
	}
	
	/**
	 * Gets the status for the company of the given appointment<br>
	 * Gets the data for the given appointment for every status, if there is data.<br>
	 * Finally it joins all in instances of {@link AppointmentProcessStatusDto}
	 * 
	 * @param appointmentDetail
	 * @param statusByCompany
	 * @param dateToTest
	 * @return
	 */
	private List<AppointmentProcessStatusDto> getProcessStatusForAppointment(AppointmentDetail appointmentDetail, List<ProcessStatus> statusByCompany, LocalDateTime dateToTest) {		
		//get the data
		List<AppointmentProcessStatusDto> appointmentProcessStatusDtos = setDataForAppointmentStatus(appointmentDetail, statusByCompany);
		
		//set the live flag
		setLiveStatusToAppointmentProcessStatus(appointmentProcessStatusDtos, appointmentDetail, dateToTest);
		
		return appointmentProcessStatusDtos;
	}
	
	/**
	 * Gets the list of {@link ProcessStatus} for the given comany id
	 * 
	 * @param processSatusByCompany
	 * @param companyId
	 * @return
	 */
	private List<ProcessStatus> addOrGetProcessSatusByCompany(Map<Integer, List<ProcessStatus>> processSatusByCompany, Integer companyId) {
		if(processSatusByCompany.get(companyId) == null) {
			processSatusByCompany.put(companyId, processStatusRepository.findByIdCompanyIdOrderByStep(companyId));
		}
		return processSatusByCompany.get(companyId);
	}

	/**
	 * Gets the status for the company of the given appointment<br>
	 * Gets the data for the given appointment for every the status, if there is data.<br>
	 * Finally it joins all in instances of {@link AppointmentProcessStatusDto}
	 * 
	 * @param appointmentDetail
	 * @return List of {@link AppointmentProcessStatusDto}
	 */
	private List<AppointmentProcessStatusDto> setDataForAppointmentStatus(AppointmentDetail appointmentDetail, List<ProcessStatus> statusByCompany) {
		List<AppointmentProcessStatusDto> appointmentProcessStatusDtos = new ArrayList<>();
		
		List<AppointmentProcessStatus> appointmentStatus = appointmentProcessStatusRepository.findByIdAppointmentId(appointmentDetail.getAppointmentId());
		
		statusByCompany.stream().forEach(processStatus -> {	
			AppointmentProcessStatusDto appointmentProcessStatusDto =  appointmentProcessStatusMapper.toModelFromProcessStatus(processStatus);
			appointmentProcessStatusDto.setAppointmentId(appointmentDetail.getAppointmentId());
			appointmentProcessStatusDto.setProcessStatusCode(processStatus.getProcessStatusCode());
			
			AppointmentProcessStatus appointmentProcessStatus = appointmentStatus.stream()
			  .filter(appointmentProcessStatusFind ->
			  		appointmentDetail.getAppointmentId().equals(appointmentProcessStatusFind.getId().getAppointmentId()) && 
			  		appointmentProcessStatusDto.getProcessStatusId().equals(appointmentProcessStatusFind.getId().getProcessStatusId()))
			  .findAny()
			  .orElse(null);
			
			if(appointmentProcessStatus != null) {
				if(appointmentProcessStatus.getEventDate()!=null) {
					appointmentProcessStatusDto.setEventDate(appointmentProcessStatus.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
				}
				//The company code is only set at the view "bmv_appointment_process_status"
				appointmentProcessStatusDto.setCompanyCode(appointmentProcessStatus.getCompanyCode());
			}
			
			appointmentProcessStatusDtos.add(appointmentProcessStatusDto);
		});
		return appointmentProcessStatusDtos;
	}

	/**
	 * It sets the live flag to true to the last status with eventDate only if there is no configuration for the Hospital or status is configured as one that can be life.<br>
	 * Configuration property: <b>whiteboardsurgery.config.lifeStatusByCompany</b><br>
	 * If last status of the list is the live the end date of the appointment and the given date to test will be use to determinate if the status is still alive.
	 * 
	 * @param appointmentProcessStatusDtos
	 */
	private void setLiveStatusToAppointmentProcessStatus(List<AppointmentProcessStatusDto> appointmentProcessStatusDtos, AppointmentDetail appointmentDetail, LocalDateTime dateToTest) {
		AppointmentProcessStatusDto liveStatus = null;
		for (AppointmentProcessStatusDto appointmentProcessStatusDto : appointmentProcessStatusDtos) {	
			if(appointmentProcessStatusDto.getEventDate() != null) {
				appointmentProcessStatusDto.setLive(true);
				if(liveStatus != null) {
					liveStatus.setLive(false);
				}				
				liveStatus = appointmentProcessStatusDto;
			} 
		}
		
		LocalDateTime endDate = null;
		if(appointmentProcessStatusDtos.size() > 0 && appointmentDetail.getEndDate() != null) {
			endDate = appointmentDetail.getEndDate().toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDateTime();
			AppointmentProcessStatusDto lastStatus = appointmentProcessStatusDtos.get(appointmentProcessStatusDtos.size() - 1);
			
			//The appointment is not alive because the end time is lower than the current time, so the status can't be live
			if(lastStatus.isLive() && endDate.compareTo(dateToTest) <= 0) {
				lastStatus.setLive(false);
				liveStatus = null;
			}
		}
		
		setOperationLive(liveStatus);
	}
	
	/**
	 * Sets the flag operationLive to true if the status has the flag live to true and it is in the property <b>whiteboardsurgery.config.operationLifeStatusByCompany</b>
	 * 
	 * @param appointmentProcessStatusDto
	 */
	private void setOperationLive(AppointmentProcessStatusDto appointmentProcessStatusDto) {
		boolean operationCanBeLife = true;
		
		if(appointmentProcessStatusDto != null && appointmentProcessStatusDto.isLive()) {
			List<String> lifeStatusProcessCodes = lifeStatusByCompany.get(appointmentProcessStatusDto.getCompanyCode());
			
			if(lifeStatusProcessCodes != null) {
				String processStatusCodeFound = lifeStatusProcessCodes.stream()
					.filter(processStatusId -> appointmentProcessStatusDto.getProcessStatusCode().equals(processStatusId))
					.findAny()
					.orElse(null);
				if(processStatusCodeFound == null) {
					operationCanBeLife = false;
				}
			}
			appointmentProcessStatusDto.setOperationLive(operationCanBeLife);
		}
	}
}
