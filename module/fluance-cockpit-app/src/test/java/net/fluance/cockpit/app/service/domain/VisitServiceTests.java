package net.fluance.cockpit.app.service.domain;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VisitServiceTests {
	
	VisitService visitService;
	
	@Before
	public void init() {
		visitService = new VisitService();
	}
	
	@Test
	public void getVisitList() {
		List<Long> visits = visitService.getVisitList("1,2");
		Assert.assertTrue("Is not null?", visits != null); 
		Assert.assertTrue("Has 2 entries?", visits.size() == 2); 
	}
	
	@Test
	public void getVisitList_wrong_input() {
		List<Long> visits = visitService.getVisitList("aaaaa");
		Assert.assertTrue("Is not null?", visits != null); 
		Assert.assertTrue("Has 2 entries?", visits.size() == 0); 
	}
	
	@Test
	public void getVisitList_one_wrong_input() {
		List<Long> visits = visitService.getVisitList("1,2,aaaaa");
		Assert.assertTrue("Is not null?", visits != null); 
		Assert.assertTrue("Has 2 entries?", visits.size() == 2); 
	}
	
	@Test
	public void getVisitList_empty() {
		List<Long> visits = visitService.getVisitList("");
		Assert.assertTrue("Is not null?", visits != null); 
		Assert.assertTrue("Has 0 entries?", visits.size() == 0); 
	}
	
	@Test
	public void getVisitList_null() {
		List<Long> visits = visitService.getVisitList(null);
		Assert.assertTrue("Is not null?", visits != null); 
		Assert.assertTrue("Has 0 entries?", visits.size() == 0); 
	}
	
	@Test
	public void getVisitList_no_multiple_visit_ids() {
		List<Long> visits = visitService.getVisitList("1,2,2,3,1");
		Assert.assertTrue("Is not null?", visits != null); 
		Assert.assertTrue("Has 0 entries?", visits.size() == 3); 
	}
}
