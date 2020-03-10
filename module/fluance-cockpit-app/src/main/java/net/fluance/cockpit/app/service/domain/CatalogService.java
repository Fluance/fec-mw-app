package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.repository.jdbc.catalog.CatalogRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;

@Service
public class CatalogService {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	CatalogRepository catalogRepository;
	
	@Autowired
	CompanyDetailsRepository companyDetailsRepository;
	
	public List<CatalogDTO> getCatalogs(Long companyId, String type, String language) {
		List<CatalogDTO> catalogs = new ArrayList<>();
		CompanyDetails companyDetails = companyDetailsRepository.findOne(companyId.intValue());
		if (companyDetails != null && !StringUtils.isEmpty(companyDetails.getCode())) {
			catalogs = catalogRepository.getCatalogs(companyDetails.getCode(), type, language);
		}
		return catalogs;
	}

	public CatalogDTO getCatalogByPK(Long companyId, String code, String language, String type) {
		CatalogDTO catalog = null;
		if (!StringUtils.isEmpty(code)) {
			CompanyDetails companyDetails = companyDetailsRepository.findOne(companyId.intValue());
			if (companyDetails != null && !StringUtils.isEmpty(companyDetails.getCode())) {
				if (StringUtils.isEmpty(language)) {
					language = "EN";
				}
				return catalogRepository.getCatalogByPK(companyDetails.getCode(), code, language, type);
			}
		}
		return catalog;
	}

}
