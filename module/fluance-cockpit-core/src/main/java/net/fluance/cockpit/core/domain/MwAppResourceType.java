package net.fluance.cockpit.core.domain;

import net.fluance.app.log.ResourceType;

public enum MwAppResourceType implements ResourceType{

	UNKNOWN(""),
	PATIENT_DETAIL("patient"),
	PATIENT_LIST("patientlist"),
	PATIENTS_COUNT("patientsCount"),
	PATIENTS_COUNT_BY_ADMIN_DATE("patientsAdmitDate"),
	PATIENT_FILE("patientfile"),
	PATIENT_FILES_LIST("patientfileslist"),
	GUARANTOR("guarantor"),
	GUARANTOR_LIST("guarantorList"),
	INVOICE_LIST_BY_GUARANTOR("invoiceListByGuarantor"),
	RADIOLOGY_EXAMS("imagingList"),
	LAB("labList"),
	VISIT_LIST("visitList"),
	VISIT_COUNT("countVisits"),
	VISIT("visit"),
	POLICIES("policyList"),
	INVOICE("invoice"),
	INTERVENTION("intervention"),
	POLICY_DETAIL("policy"),
	DIAGNOSIS_TREATMENTS("icdChopList"),
	LOGS_PATIENT("patientLogs"),
	AppointmentList("appointmentList"),
	AppointmentDetail("appointment"),
	CompanyList("CompanyList"),
	CompanyDetail("CompanyDetail"),
	UnitList("UnitList"),
	ServiceList("ServiceList"),
	RoomList("RoomList"),
	BedList("BedList"),
	GuarantorDetail("GuarantorDetail"),
	NoksByPid("NoksByPid"),
	NoksContacts("NoksContacts"),
	LabGroupNames("LabGroupNames"),
	Oxygen("Oxygen"),
	FtSearch("FtSearch"),
	NOTE("note"),
	NOTE_LIST("noteList"),
	PICTURE("picture"),
	PICTURE_LIST("pictureList"),
	PICTURE_FILE("pictureFile"),
	Synlab("Synlab"),
	DoctorListByVisitid("DoctorListByVisitid"),
	WHITEBOARD("whiteboard"),
	WHITEBOARD_ENTRY("whiteboardEntry"),
	WHITEBOARD_LIST("whiteboardList"),
	WHITEBOARD_NURSES("whiteboardNurses"),
	WHITEBOARD_PHYSICIANS("whiteboardPhysicians"),
	WHITEBOARD_FILES("whiteboardFiles"),
	WHITEBOARD_ROOMS_CONFIGURATION("whiteboardRoomsConfiguration"),
	CAPACITIES("capacityList"),
	VISIT_DETAILED_LIST("visitDetailedList"),
	PATIENT_EXERCISE("exercisePatient"),
	PATIENT_WEIGHT("weightPatient");
	
	private String resourceType;
	
	private MwAppResourceType(String resourceType){
		this.resourceType = resourceType;
	}
	
	public String getResourceType() {
		return resourceType;
	}
	
	/**
	 * 
	 * @param value
	 * @return AdmissionStatusEnum corresponding to the parameter value, if not found returns AdmissionStatusEnum.UNKNOWN
	 */
	public static MwAppResourceType permissiveValueOf(String resourceType){
		if (resourceType == null){
			return MwAppResourceType.UNKNOWN;
		} else {
			MwAppResourceType[] mwAppResourceTypes = MwAppResourceType.values();
			for (int i=0;i<mwAppResourceTypes.length;i++){
				if(mwAppResourceTypes[i].getResourceType().equalsIgnoreCase(resourceType))
					return mwAppResourceTypes[i];
			}
			return MwAppResourceType.UNKNOWN;
		}
	}

}
