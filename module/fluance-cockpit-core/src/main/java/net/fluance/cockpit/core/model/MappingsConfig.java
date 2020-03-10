/**
 * 
 */
package net.fluance.cockpit.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines common and re-usable mapping properties
 *
 *
 */
public class MappingsConfig {

  // Maps a domain class name with the correponding table name in the database
  public static final Map<String, String> TABLE_NAMES = new HashMap<>();
  static {
    TABLE_NAMES.put("bmv_patientslist_detail", "ehealth.bmv_patientslist_detail");
    TABLE_NAMES.put("bmv_visit_intervention_data", "ehealth.bmv_visit_intervention_data");
    TABLE_NAMES.put("bmv_visit_benefit_detail", "ehealth.bmv_visit_benefit_detail");
    TABLE_NAMES.put("bmv_visit_benefit_list", "ehealth.bmv_visit_benefit_list");
    TABLE_NAMES.put("UserAccess", "ehealth.bmv_user_access_list");
    TABLE_NAMES.put("appointment_detail", "ehealth.bmv_appointment_detail");
    TABLE_NAMES.put("Company", "ehealth.company");
    TABLE_NAMES.put("Physician", "ehealth.physician");
    TABLE_NAMES.put("PhysicianDetail", "ehealth.bmv_physician_detail");
    TABLE_NAMES.put("ResourcePersonnel", "ehealth.resource_personnel");
    TABLE_NAMES.put("Visit", "ehealth.visit");
    TABLE_NAMES.put("Treatment", "ehealth.bmv_visit_treatment_list");
    TABLE_NAMES.put("bmv_visit_appointment_list", "ehealth.bmv_visit_appointment_list");
    TABLE_NAMES.put("OPA_CHOP", "ehealth.m_cat_opa_chop");
    TABLE_NAMES.put("Diagnosis", "ehealth.bmv_visit_diagnosis_list");
    TABLE_NAMES.put("OPA_ICD", "ehealth.m_cat_opa_icd");
    TABLE_NAMES.put("m_bmv_patientunit", "ehealth.m_bmv_patientunit");
    TABLE_NAMES.put("m_bmv_hospservice", "ehealth.m_bmv_hospservice");
    TABLE_NAMES.put("m_bmv_patientroom", "ehealth.m_bmv_patientroom");
    TABLE_NAMES.put("m_bmv_patientbed", "ehealth.m_bmv_patientbed");
    TABLE_NAMES.put("bmv_patient_visits_list", "ehealth.bmv_patient_visits_list");
    TABLE_NAMES.put("groupname_list", "ehealth.bmv_lab_groupname_list");
    TABLE_NAMES.put("labdata_list", "ehealth.bmv_lab_data_list");
    TABLE_NAMES.put("Patient", "ehealth.bmv_patient_detail");
    TABLE_NAMES.put("PatientList", "ehealth.bmv_patients_list");
    TABLE_NAMES.put("PatientContact", "ehealth.bmv_patient_contacts_list");
    TABLE_NAMES.put("PatientNextOfKinContact", "ehealth.bmv_nextofkin_contacts_list");
    TABLE_NAMES.put("PatientNextOfKin", "ehealth.bmv_patient_nextofkins_list");
    TABLE_NAMES.put("bmv_physician_patients_list", "ehealth.bmv_physician_patients_list");
    TABLE_NAMES.put("bmv_physician_visits_list", "ehealth.bmv_physician_visits_list");
    TABLE_NAMES.put("PhysicianContact", "ehealth.bmv_physician_contacts_list");
    TABLE_NAMES.put("bmv_visit_detail", "ehealth.bmv_visit_detail");
    TABLE_NAMES.put("bmv_patient_visits_list", "ehealth.bmv_patient_visits_list");
    TABLE_NAMES.put("bmv_guarantor_detail", "ehealth.bmv_guarantor_detail");
    TABLE_NAMES.put("bmv_visit_guarantors_list", "ehealth.bmv_visit_guarantors_list");
    TABLE_NAMES.put("PatientList", "ehealth.bmv_patients_list");
    TABLE_NAMES.put("PatientContact", "ehealth.bmv_patient_contacts_list");
    TABLE_NAMES.put("PatientNextOfKinContact", "ehealth.bmv_nextofkin_contacts_list");
    TABLE_NAMES.put("PatientNextOfKin", "ehealth.bmv_patient_nextofkins_list");
    TABLE_NAMES.put("bmv_radiological_serie", "ehealth.bmv_radiological_serie");
    TABLE_NAMES.put("bmv_radiological_report", "ehealth.bmv_radiological_report");
    TABLE_NAMES.put("bmv_personnel_appointment_list", "ehealth.bmv_personnel_appointment_list");
    TABLE_NAMES.put("visit_physician", "ehealth.visit_physician");
    TABLE_NAMES.put("bmv_physicians_list", "ehealth.bmv_physicians_list");
    TABLE_NAMES.put("bmv_guarantor_detail", "ehealth.bmv_guarantor_detail");
    TABLE_NAMES.put("bmv_personnel_appointment_list", "ehealth.bmv_personnel_appointment_list");
    TABLE_NAMES.put("bmv_invoice_detail", "ehealth.bmv_invoice_detail");
    TABLE_NAMES.put("bmv_visit_guarantor_invoices_list",
        "ehealth.bmv_visit_guarantor_invoices_list");
    TABLE_NAMES.put("bmv_visit_physicians_list", "ehealth.bmv_visit_physicians_list");
    TABLE_NAMES.put("Note", "notesandpictures.bmv_note_details");
    TABLE_NAMES.put("History", "notesandpictures.bmv_note_history");
    TABLE_NAMES.put("picture", "notesandpictures.picture");
    TABLE_NAMES.put("patientslistconfig", "ehealth.patientslistconfig");
    TABLE_NAMES.put("company_metadata", "ehealth.company_metadata");
    TABLE_NAMES.put("patientfile", "files.bmv_icp_detail");
    // Table for the Whiteboard
    TABLE_NAMES.put("whiteboard", "ehealth.whiteboard");
    TABLE_NAMES.put("whiteboard_view", "ehealth.bmv_whiteboard_detail");
    TABLE_NAMES.put("m_bmv_capacity", "ehealth.m_bmv_capacity");
    TABLE_NAMES.put("whiteboard_catalog", "ehealth.whiteboard_catalog");
    TABLE_NAMES.put("room_configuration", "ehealth.room_configuration");
    TABLE_NAMES.put("capacity", "ehealth.capacity");
    TABLE_NAMES.put("BMV_PATIENTS_LIST", "ehealth.BMV_PATIENTS_LIST");
  }
}
