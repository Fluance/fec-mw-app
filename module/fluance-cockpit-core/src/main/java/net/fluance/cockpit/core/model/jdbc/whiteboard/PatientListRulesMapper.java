package net.fluance.cockpit.core.model.jdbc.whiteboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;

public class PatientListRulesMapper {

	private List<PatientRulesAndReplacement> patientRulesAndReplacements;
	private boolean hasRules;

	public static final String PATIENT_LIST_RULES_KEY = "patientListRules";
	public static final String RULES_KEY = "rules";
	public static final String COMPARISON_COLUMN_KEY = "comparisonColumn";
	public static final String VALUES_KEY = "values";
	public static final String REPLACEMENT_KEY = "result";
	public static final String WHITEBOARD = "whiteboard";

	public PatientListRulesMapper(Map<Long, String> patientRules, Long companyId) {
		this.hasRules = patientRules.containsKey(companyId);
		if (hasRules) {
			String json = patientRules.get(companyId);
			analyze(json);
		} else {
			patientRulesAndReplacements = new ArrayList<PatientRulesAndReplacement>();
		}
	}

	// We analyze the json, and insert the rules in the patientRules
	private void analyze(String jsonString) {
		JSONObject json = new JSONObject(jsonString);
		JSONArray patientListRules = json.getJSONArray(PATIENT_LIST_RULES_KEY);

		PatientRulesAndReplacement patientRuleWrapper;

		List<PatientRule> patientRulesList;

		patientRulesAndReplacements = new ArrayList<PatientRulesAndReplacement>();

		for (int i = 0; i < patientListRules.length(); i++) {
			patientRuleWrapper = new PatientRulesAndReplacement();

			JSONObject patientListRule = patientListRules.getJSONObject(i);
			
			String replacement = patientListRule.getString(REPLACEMENT_KEY);

			patientRuleWrapper.setReplacement(replacement);

			JSONArray patientListRulesRules = patientListRule.getJSONArray(RULES_KEY);

			patientRulesList = new ArrayList<>();
			for (int y = 0; y < patientListRulesRules.length(); y++) {
				PatientRule patientRule = new PatientRule();

				JSONObject patientListRulesRule = patientListRulesRules.getJSONObject(y);
				String field = patientListRulesRule.getString(COMPARISON_COLUMN_KEY);

				patientRule.setField(convertField(field));

				JSONArray patientListRulesRuleValues = patientListRulesRule.getJSONArray(VALUES_KEY);

				List<String> values = new ArrayList<>();

				for (int c = 0; c < patientListRulesRuleValues.length(); c++) {
					values.add(patientListRulesRuleValues.getString(c));
				}

				patientRule.setValue(values);
				patientRulesList.add(patientRule);
			}

			patientRuleWrapper.setPatientRules(patientRulesList);
			patientRulesAndReplacements.add(patientRuleWrapper);
		}
	}

	private Boolean testIfAllRulesApply(WhiteBoardViewDTO whiteBoardView, List<PatientRule> patientRules) {
		Boolean apply = false;

		for (PatientRule patientRule : patientRules) {
			if (patientRule.getField()!=null) {
				if (patientRule.getField().equals(PATIENT_FIELD.INSURANCE)) {
					if (patientRule.getValue().contains(whiteBoardView.getInsurance())) {
						apply = true;
					} else {
						apply = false;
						break;
					}
				} else if (patientRule.getField().equals(PATIENT_FIELD.PATIENT_CLASS)) {
					if (patientRule.getValue().contains(whiteBoardView.getPatientClass())) {
						apply = true;
					} else {
						apply = false;
						break;
					}
				} else {
					apply = false;
					break;
				}
			}
		}

		return apply;
	}

	public void updateInsuranceByRules(WhiteBoardViewDTO whiteBoardView) {
		for (PatientRulesAndReplacement patientRulesAndReplacement : patientRulesAndReplacements) {
			if(testIfAllRulesApply(whiteBoardView, patientRulesAndReplacement.getPatientRules())) {
				whiteBoardView.setInsurance(patientRulesAndReplacement.getReplacement());
			}
		}
	}

	public boolean hasRules() {
		return this.hasRules;
	}

	private PATIENT_FIELD convertField(String name) {
		switch (name) {
			case "insurance":
				return PATIENT_FIELD.INSURANCE;
			case "patientclass":
				return PATIENT_FIELD.PATIENT_CLASS;
			default:
				return null;
		}
	}

}
