package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesDetail;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesFilterList;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesList;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesFilterListRepository;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesListRepository;
import net.fluance.cockpit.core.util.sql.BenefitListOrderByEnum;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_SERVICEFEES)
public class BenefitController extends AbstractRestController{
	
	private static Logger LOGGER = LogManager.getLogger(BenefitController.class);
	
	@Autowired
	ServiceFeesDetailRepository detailRepository;
	
	@Autowired
	ServiceFeesListRepository listRepository;

	@Autowired
	ServiceFeesFilterListRepository filterListRepository;
	
	@ApiOperation(value = "Service Fees Detail", response = ServiceFeesDetail.class, tags="Service Fees API", notes="")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getServiceFeeDetail(@PathVariable Long id, 
			@RequestParam(required=false, name="lang") String language, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			ServiceFeesDetail serviceFee = detailRepository.findBenefit(id, language);
			return new ResponseEntity<>(serviceFee, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Service Fees List", response = ServiceFeesList.class, responseContainer = "List", tags="Service Fees API", notes="")
	@RequestMapping(value = "", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getServiceFeesList(@RequestParam(name = "visitnb") Long visitNumber, 
			@RequestParam(required=false, name = "lang") String language, 
			@RequestParam(required = false, name = "filterby") String filterBy, 
			@RequestParam(required = false, name = "filtervalue") String filterValue, 
			@RequestParam(required=false, name="orderby") String orderBy, 
			@RequestParam(required=false, name="sortorder") String sortOrder, 
			@RequestParam(required = false)Integer limit, 
			@RequestParam(required=false)Integer offset, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			BenefitListOrderByEnum orderByEnum = BenefitListOrderByEnum.permissiveValueOf(orderBy);
			orderBy = orderByEnum.getValue();
			
			List<ServiceFeesList> benefits;
			
			if (filterBy != null) {
				// retrieve a filtered list of fees
				benefits = listRepository.findServiceFeesUsingFilter(visitNumber, filterBy, filterValue, orderBy, sortOrder, limit, offset);
			} else {
				// retrieve a regular list of fees for a given visit number
				benefits = listRepository.findBenefits(visitNumber, language, orderBy, sortOrder, limit, offset);
			}
			
			return new ResponseEntity<>(benefits, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Service Fees List Count", response = Count.class, tags="Service Fees API", notes="")
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getServiceFeesListCount(@RequestParam(name = "visitnb") Long visitNumber, 
			@RequestParam(required=false, name="lang") String language, 
			@RequestParam(required=false, name="filterby") String filterBy, 
			@RequestParam(required=false, name="filtervalue") String filterValue, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			Integer count = listRepository.getBenefitListCount(visitNumber, language, filterBy, filterValue);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Service Fees Grouping", response = ServiceFeesList.class, responseContainer = "List", tags="Service Fees API", notes="Valid 'filterby' values are : department, date")
	@RequestMapping(value = "/filters", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getServiceFeesFilters(@RequestParam(name = "visitnb") Long visitNumber, 
			@RequestParam(required=false, name="filterby")String filterBy, 
			@RequestParam(required=false, name="sortorder")String sortOrder, 
			@RequestParam(required = false)Integer limit, 
			@RequestParam(required=false)Integer offset, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{		
			List<ServiceFeesFilterList> filters = filterListRepository.getFilterValues(visitNumber, filterBy, sortOrder, limit, offset);
			
			return new ResponseEntity<>(filters, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Service Fees Grouping Count", response = ServiceFeesList.class, responseContainer = "Integer", tags="Service Fees API", notes="Returns filters count for a given visit number and a filterby value. Valid 'filterby' values are : department, date")
	@RequestMapping(value = "/filters/count", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getServiceFeesFiltersCount(@RequestParam(name = "visitnb") Long visitNumber, 
			@RequestParam(required=false, name="filterby") String filterBy,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{	
			Integer filters = filterListRepository.getFilterValuesCount(visitNumber, filterBy);
			
			return new ResponseEntity<>(filters, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Object handleDataException(DataException exc) {
		return null;
	}
}
