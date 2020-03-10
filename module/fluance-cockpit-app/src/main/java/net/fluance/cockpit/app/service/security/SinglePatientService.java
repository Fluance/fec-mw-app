package net.fluance.cockpit.app.service.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.User;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.web.controller.LabController;
import net.fluance.cockpit.app.web.controller.RadiologyController;
import net.fluance.cockpit.core.model.jdbc.invoice.Invoice;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.repository.jdbc.guarantor.GuarantorRepository;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceRepository;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.commons.lang.RegexUtils;
import net.fluance.commons.net.UriUtils;

@Service
public class SinglePatientService {
	
	private static Logger LOGGER = LogManager.getLogger(SinglePatientService.class);

	private String contextPath;
	
	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private VisitDetailRepository visitDetailRepository;

	@Autowired
	private GuarantorRepository guarantorRepository;
	
	@Autowired
	ServiceFeesDetailRepository serviceFeesRepository;

	public SinglePatientService(){}
	
	public boolean isPatientPermitted(HttpServletRequest request, User authorizedUser){
		this.contextPath = request.getContextPath();
		
		boolean patientMatch = RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_PATIENTS + "/"  + authorizedUser.getPid() + ".*", request.getRequestURI())
				|| RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_ACCESSLOGGER + "/patient/"  + authorizedUser.getPid() + ".*", request.getRequestURI());
		if(patientMatch){
			return true;
		}

		if (urlWithPidParam(request.getRequestURI(), request) && authorizedUser.getPid().equals(Long.valueOf(request.getParameter("pid")))){
			return true;
		}

		Long visitid = UriUtils.getValueLongFromURI(request.getRequestURI(), "visits");
		boolean visitMatch = RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_VISITS + "/" + visitid + ".*", request.getRequestURI());
		if(visitMatch && checkVisitValid(authorizedUser.getPid(), visitid)){
			return true;
		}

		Long invoiceid = UriUtils.getValueLongFromURI(request.getRequestURI(), "invoices");
		boolean invoiceMatch = RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_INVOICES + "/" + invoiceid + ".*", request.getRequestURI());
		if(invoiceMatch && checkInvoiceValid(authorizedUser.getPid(), invoiceid)){
			return true;
		}

		Long guarantorId = UriUtils.getValueLongFromURI(request.getRequestURI(), "guarantor");
		boolean guarantorMatch = RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_GUARANTOR + "/" + guarantorId + ".*", request.getRequestURI());
		if(guarantorMatch && checkGuarantorValid(authorizedUser.getPid(), guarantorId)){
			return true;
		}

		Long serviceFeesId = UriUtils.getValueLongFromURI(request.getRequestURI(), "servicefees");
		boolean serviceFeesMatch = RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_SERVICEFEES + "/" + serviceFeesId + ".*", request.getRequestURI());
		String lang = request.getParameter("lang");
		if(serviceFeesMatch && checkServiceFeesValid(authorizedUser.getPid(), serviceFeesId, lang)){
			return true;
		}
		
		boolean invoiceListMatch = RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_INVOICES + ".*", request.getRequestURI());
		boolean serviceFeesMatchMatch = RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_SERVICEFEES + ".*", request.getRequestURI());
		
		boolean anyServiceWithVisitParamMatch = serviceFeesMatchMatch || invoiceListMatch;
		String visitnb = request.getParameter("visitnb");
		if(anyServiceWithVisitParamMatch && visitnb!= null && checkVisitValid(authorizedUser.getPid(), Long.valueOf(visitnb))){
			return true;
		}
		
		LOGGER.warn("PID : " + authorizedUser.getPid() + "doesn't have access to the resource :" + request.getRequestURI());
		return false;
	}

	private boolean checkServiceFeesValid(Long pid, Long serviceFeesId, String lang) {
		ServiceFeesDetail serviceFee = serviceFeesRepository.findBenefit(serviceFeesId, lang);
		if(serviceFee != null){
			Long visitNb = serviceFee.getVisitNb();
			return checkVisitValid(pid, visitNb);
		}
		return false;
	}

	private boolean checkVisitValid(Long pidJwt, long visitid){
		VisitDetail visitDetail = visitDetailRepository.findOne(visitid);
		if(visitDetail!=null && pidJwt!=null && visitDetail.getPatientId().equals(pidJwt)){
			return true;
		}
		return false;
	}

	private boolean checkInvoiceValid(Long pidJwt, long invoiceid){
		Invoice invoiceDetail = invoiceRepository.findOne(invoiceid);
		if(invoiceDetail!=null && pidJwt!=null){
			long visitid = invoiceDetail.getVisit_nb();
			VisitDetail visitDetail = visitDetailRepository.findOne(visitid);
			if(visitDetail!=null && visitDetail.getPatientId().equals(pidJwt)){
				return true;
			}
		}
		return false;
	}

	private boolean checkGuarantorValid(Long pidJwt, long guarantorId){
		return guarantorRepository.isGuarantorOfPatient(pidJwt, guarantorId) > 0;
	}

	private boolean urlWithPidParam(String uri, HttpServletRequest request) {
		String cleanUri = uri.indexOf("/count") > 0 ? uri.substring(0, uri.indexOf("/count")) : uri;
		return request.getParameter("pid") != null
				&&
				(RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_VISITS, cleanUri)
					|| RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_RADIOLOGY, cleanUri)
					|| RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_RADIOLOGY + "/reports", cleanUri)
					|| RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_LAB + LabController.PATH_GROUPNAME_LIST, cleanUri)
					|| RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_LAB + LabController.PATH_DATA, cleanUri)
					|| RegexUtils.matches(contextPath + WebConfig.API_MAIN_URI_APPOINTMENTS, cleanUri)
				);
	}

}
