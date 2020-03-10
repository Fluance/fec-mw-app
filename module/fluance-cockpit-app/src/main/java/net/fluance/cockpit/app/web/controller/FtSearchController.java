package net.fluance.cockpit.app.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.FtSearchService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.search.FtSearchModel;
import net.fluance.cockpit.core.model.search.FtSearchModelOld;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_SEARCH)
public class FtSearchController extends AbstractRestController{
	
	Logger logger = LogManager.getLogger();
	
	@Autowired
	FtSearchService searchService;

	@ApiOperation(value = "Search", response = FtSearchModelOld.class, tags="FT Search API")
	@RequestMapping(value = "/old", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> search(@RequestParam String field, @RequestParam(required = false, defaultValue="50") Integer limit,
			@RequestParam(required = false, defaultValue="0") Integer offset, @RequestParam(required = false) String entityType, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("Processing FullText Search, Keyword : " + field);
		try {
			FtSearchModelOld result = searchService.search(field, offset, limit, entityType);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Search", response = FtSearchModel.class, tags="Full Text Search API", notes="Use the Solr to Search by a keyword. One group for each entyType in result, totalCount is the number of results that match. Limit and offset are applied for each group of result.")
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> search2(@RequestParam String field, @RequestParam(required = false, defaultValue="50") Integer limit,
			@RequestParam(required = false, defaultValue="0") Integer offset, @RequestParam(required = false) String entityType, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("Processing FullText Search, Keyword : " + field);
		try {
			FtSearchModel ftSearchModel = searchService.search2(field, offset, limit, entityType);
			super.systemLog(request, MwAppResourceType.FtSearch, field);
			return new ResponseEntity<>(ftSearchModel, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Search Telephone", response = FtSearchModel.class, tags="Full Text Search API", notes="Search Patients based in its telephone. It can be used more than one at the same time but the telephones must be separated by whitespaces.<br>The Entity Type is optional and by default is 'patient' ")
	@RequestMapping(value = "/telephone", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> searchTelephone(@RequestParam String telephone, @RequestParam(required = false, defaultValue="50") Integer limit,
			@RequestParam(required = false, defaultValue="0") Integer offset, @RequestParam(required = false) String entityType, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("Processing FullText Search, Keyword : " + telephone);
		try {
			FtSearchModel ftSearchModel = searchService.searchTelephone(telephone, offset, limit, entityType);
			super.systemLog(request, MwAppResourceType.FtSearch, telephone);
			return new ResponseEntity<>(ftSearchModel, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}


	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public Object handleDataException(DataException exc) {
		return null;
	}

}
