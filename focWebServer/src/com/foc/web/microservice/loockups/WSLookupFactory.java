package com.foc.web.microservice.loockups;

import java.util.HashMap;

import com.foc.admin.FocGroupDesc;
import com.foc.admin.FocUserDesc;
import com.foc.business.workflow.WFTitleDesc;

public class WSLookupFactory {

	private HashMap<String, WSSingleLookup> map = null;

	private WSLookupFactory() {
		map = new HashMap<String, WSSingleLookup>();

		putLookup(new WSSingleLookup("wf_title", WFTitleDesc.getInstance()));
		putLookup(new WSSingleLookup("fgroup", FocGroupDesc.getInstance()));
		putLookup(new WSSingleLookup("fuser", FocUserDesc.getInstance()));
		
//		putLookup(new WSSingleLookup("measure_type", MeasureType.getFocDesc()));
//		putLookup(new WSSingleLookup("measure_importance", MeasureImportance.getFocDesc()));
//
//		putLookup(new WSSingleLookup("incident_type", IncidentType.getFocDesc()));
//		putLookup(new WSSingleLookup("incident_place", IncidentPlace.getFocDesc()));
//
//		putLookup(new WSSingleLookup("need_types", NeedType.getFocDesc()));
//
//		putLookup(new WSSingleLookup("sectors", Sector.getFocDesc()) {
//			@Override
//			protected B01JsonBuilder newJsonBuiler() {
//				B01JsonBuilder builder = super.newJsonBuiler();
//				if (builder != null) {
//					JSONFieldFilter filter = new JSONFieldFilter();
//					filter.putField(Kadaa.DBNAME, Kadaa.FIELD_Mouhafaza);
//					builder.setAdditionalFieldFilter(filter);
//				}
//				return builder;
//			}
//		});
//
//		putLookup(new WSSingleLookup("municipalities", Municipality.getFocDesc()));
//
//		putLookup(new WSSingleLookup("mouhafaza", Mouhafaza.getFocDesc()));
//		putLookup(new WSSingleLookup("kadaa", Kadaa.getFocDesc()));
//
//		putLookup(new WSSingleLookup("social_statuses", SocialStatus.getFocDesc()));
//		putLookup(new WSSingleLookup("family_relations", FamilyRelation.getFocDesc()));
//		putLookup(new WSSingleLookup("education_levels", EducationLevel.getFocDesc()));
//		putLookup(new WSSingleLookup("genders", Gender.getFocDesc()));
//		putLookup(new WSSingleLookup("illness_types", IllnessType.getFocDesc()));
//		putLookup(new WSSingleLookup("mobility_types", MobilityType.getFocDesc()));
//		putLookup(new WSSingleLookup("ownership_types", OwnershipType.getFocDesc()));
//		putLookup(new WSSingleLookup("property_types", PropertyType.getFocDesc()));
//		putLookup(new WSSingleLookup("work_types", WorkType.getFocDesc()));
//		putLookup(new WSSingleLookup("syndicates", Syndicate.getFocDesc()));
//		putLookup(new WSSingleLookup("delivery_results", DeliveryResult.getFocDesc()));
//		putLookup(new WSSingleLookup("delivery_campaigns", DeliveryCampaign.getFocDesc()));
//		putLookup(new WSSingleLookup("assistance_type", AssistanceType.getFocDesc()));
//		putLookup(new WSSingleLookup("benefited_party", BenefitedParty.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_type", QuarantineType.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_sub_type", QuarantineSubType.getFocDesc()));
//
//		putLookup(new WSSingleLookup("water_source", QTWaterSource.getFocDesc()));
//		putLookup(new WSSingleLookup("electricity_type", QTElectricityType.getFocDesc()));
//		putLookup(new WSSingleLookup("entertainment_type", QTEntertainmentType.getFocDesc()));
//		putLookup(new WSSingleLookup("ventilation_type", QTVentilationType.getFocDesc()));
//
//		putLookup(new WSSingleLookup("hospitals", Hospital.getFocDesc()));
//		putLookup(new WSSingleLookup("health_care_centers", HealthCareCenter.getFocDesc()));
//
//		putLookup(new WSSingleLookup("quarantine_gender", QTGender.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_marital_status", QTMaritalStatus.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_accomodation_type", QTAccomodationType.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_family_member", QTFamilyMember.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_symptoms", QTSymptom.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_house_type", QTHouseType.getFocDesc()));
//		putLookup(new WSSingleLookup("covid_test_result", QTCovidTestResult.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_residence_approval", QTResidenceApproval.getFocDesc()));
//		
//		putLookup(new WSSingleLookup("quarantine_calling_party", QTCallingParty.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_prescription_state", QTPrescriptionState.getFocDesc()));
//		putLookup(new WSSingleLookup("quarantine_service_request", QTServiceRequest.getFocDesc()));
//		
//		putLookup(new WSSingleLookup("academic_certificate", AcademicCertificate.getFocDesc()));
//		putLookup(new WSSingleLookup("occupation_task_card", OccupationTaskCard.getFocDesc()));
//		putLookup(new WSSingleLookup("work_time_type", WorkTimeType.getFocDesc()));
//		putLookup(new WSSingleLookup("occupation_legal_status", OccupationLegalStatus.getFocDesc()));
//		putLookup(new WSSingleLookup("occupation_handling_type", OccupationHandlingType.getFocDesc()));
//		putLookup(new WSSingleLookup("human_resource_sociale_status", HumanResourceSocialeStatus.getFocDesc()));
//		putLookup(new WSSingleLookup("mandate_source", MandateSource.getFocDesc()));
//		putLookup(new WSSingleLookup("mandate_type", MandateType.getFocDesc()));
//		putLookup(new WSSingleLookup("occupation_line", OccupationLine.getFocDesc()));
//		putLookup(new WSSingleLookup("occupation_special_situation",OccupationSpecialSituation.getFocDesc()));
//		putLookup(new WSSingleLookup("public_sector_type",PublicSectorType.getFocDesc()));
//		putLookup(new WSSingleLookup("religion",Religion.getFocDesc()));
//		putLookup(new WSSingleLookup("occupation_category",OccupationCategory.getFocDesc()));
//		putLookup(new WSSingleLookup("public_administration",PublicAdministration.getFocDesc()));
//		putLookup(new WSSingleLookup("public_unit_status",PublicUnitStatus.getFocDesc()));
	}

	private void putLookup(WSSingleLookup lookup) {
		map.put(lookup.getKey(), lookup);
	}

	public WSSingleLookup getLookup(String key) {
		return map != null ? map.get(key) : null;
	}

	private static WSLookupFactory instance = null;

	public static synchronized WSLookupFactory getInstance() {
		if (instance == null) {
			instance = new WSLookupFactory();
		}
		return instance;
	}
}
