package in.ashokit.service;

import in.ashokit.binding.EligResponse;

public interface EDService {

	public EligResponse determineEligibility(Long caseNum);

}
