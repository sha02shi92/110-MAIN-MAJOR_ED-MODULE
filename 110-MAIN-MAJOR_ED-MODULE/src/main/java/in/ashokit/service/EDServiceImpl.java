package in.ashokit.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.binding.EligResponse;
import in.ashokit.entity.COTriggerEntity;
import in.ashokit.entity.CitizenAppEntity;
import in.ashokit.entity.CreateCaseEntity;
import in.ashokit.entity.EDEntity;
import in.ashokit.entity.EducationDetailsEntity;
import in.ashokit.entity.IncomeDetailsEntity;
import in.ashokit.entity.KidsDetailsEntity;
import in.ashokit.entity.PlanSelectionEntity;
import in.ashokit.repo.COTrigger;
import in.ashokit.repo.DataCollectionCitizenAppRepo;
import in.ashokit.repo.DataCollectionCreateCase;
import in.ashokit.repo.DataCollectionEducationRepo;
import in.ashokit.repo.DataCollectionIncomeRepo;
import in.ashokit.repo.DataCollectionKidsRepo;
import in.ashokit.repo.DataCollectionPlanSelectRepo;
import in.ashokit.repo.EDrepository;

@Service
public class EDServiceImpl implements EDService {

	@Autowired
	private DataCollectionCreateCase dcCaseRepo;

	@Autowired
	private DataCollectionPlanSelectRepo planRepo;

	@Autowired
	private DataCollectionIncomeRepo incomeRepo;

	@Autowired
	private DataCollectionKidsRepo kidRepo;

	@Autowired
	private DataCollectionCitizenAppRepo citizenRepo;

	@Autowired
	private DataCollectionEducationRepo educationRepo;

	@Autowired
	private EDrepository edRepo;

	@Autowired
	private COTrigger coTriggerRepo;

	boolean ageStatus = true;
	boolean noKids = false;

	@Override
	public EligResponse determineEligibility(Long caseNum) {

		EligResponse response = new EligResponse();

		Integer planId = null;
		String planName = null;
		Integer appId = null;

		Optional<CreateCaseEntity> caseEntity = dcCaseRepo.findById(caseNum);
		if (caseEntity.isPresent()) {
			planId = caseEntity.get().getPlanId();
			appId = caseEntity.get().getAppId();

		}
		Optional<PlanSelectionEntity> planEntity = planRepo.findById(planId);
		if (planEntity.isPresent()) {
			planName = planEntity.get().getPlanName();
		}

		Optional<CitizenAppEntity> citizenEntity = citizenRepo.findById(appId);
		CitizenAppEntity citizenApp = citizenEntity.get();

		IncomeDetailsEntity income = incomeRepo.findByCaseNum(caseNum);

		List<KidsDetailsEntity> kids = kidRepo.findByCaseNum(caseNum);

		if ("SNAP".equals(planName)) {

			if (income.getMonthlyIncome() > 300) {
				response.setPlanStatus("DENIED");
				response.setDenialReason("HIGH INCOME");
			}

		} else if ("CCAP".equals(planName)) {
			if (!kids.isEmpty()) {
				kids.forEach(kid -> {
					LocalDate dob = kid.getKidsAge();
					LocalDate today = LocalDate.now();
					Period p = Period.between(dob, today);
					int year = p.getYears();
					if (year > 16) {
//						ageStatus = false;
						response.setDenialReason("Age Greater than 16");
					}
				});
			} else {
				response.setDenialReason("No kids available");
//				noKids = true;
			}
			if (income.getMonthlyIncome() > 300) {
				response.setDenialReason("HIGH INCOME");
			}
			if (noKids && income.getMonthlyIncome() > 300) {
				response.setDenialReason("High income + no kids ");
			}
			if (!ageStatus) {
				response.setDenialReason("Kids Age > 16 ");
			}
			if (income.getMonthlyIncome() > 300 && !ageStatus) {
				response.setDenialReason("High income + Kids age > 16");
			}

		} else if ("Medicaid".equals(planName)) {
			Double monthlyIncome = income.getMonthlyIncome();
			Double propertyIncome = income.getPropertyIncome();
			Double rentIncome = income.getRentIncome();

			if (monthlyIncome > 300) {
				response.setDenialReason("High Monthly Income");
			}
			if (rentIncome > 0) {
				response.setDenialReason("Rental Income Available");
			}
			if (propertyIncome > 0) {
				response.setDenialReason("Property income Available");
			}
			if (rentIncome > 0 && propertyIncome > 0) {
				response.setDenialReason("Rental + Property Income Available");
			}
			if (monthlyIncome > 300 && rentIncome > 0 && propertyIncome > 0) {
				response.setDenialReason("High Income + Rental + Property Income Available");
			}

		} else if ("Medicare".equals(planName)) {
			LocalDate dob = citizenApp.getDob();
			LocalDate now = LocalDate.now();

			Period between = Period.between(dob, now);
			int year = between.getYears();
			if (year < 65) {
				response.setDenialReason("Age < 65 years");
			}

		} else if ("RIW".equals(planName)) {
			EducationDetailsEntity educationEntity = educationRepo.findByCaseNum(caseNum);
			Integer graduationYear = educationEntity.getGYear();
			if (graduationYear <= 0) {
				response.setDenialReason("Not Graduated");
			}
			if (income.getMonthlyIncome() > 0) {
				response.setDenialReason("Already Employeed");
			}
		}
		response.setPlanName(planName);
		if (response.getDenialReason() != null) {
			response.setDenialReason("DENIED");
		} else {
			response.setPlanStatus("APPROVED");
			response.setStartDate(LocalDate.now().plusDays(1));
			response.setEndDate(LocalDate.now().plusMonths(3));
			response.setBenefitAmount(350.00);
		}
		EDEntity edEntity = new EDEntity();
		BeanUtils.copyProperties(response, edEntity);
		edRepo.save(edEntity);

		COTriggerEntity coEntity = new COTriggerEntity();
		coEntity.setCaseNum(caseNum);
		coEntity.setTriggerStatus("Pending");

		coTriggerRepo.save(coEntity);

		return response;
	}

}
