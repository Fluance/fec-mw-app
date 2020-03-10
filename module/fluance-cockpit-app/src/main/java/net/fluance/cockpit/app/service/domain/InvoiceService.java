package net.fluance.cockpit.app.service.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.jdbc.invoice.InvoiceList;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceListRepository;
import net.fluance.cockpit.core.util.sql.InvoiceListOrderByEnum;

@Service
public class InvoiceService {

	@Autowired
	private InvoiceListRepository invoiceListRepo;

	public List<InvoiceList> getInvoiceList(Long visitnb, Long guarantorId, String orderBy, String sortorder, Integer limit, Integer offset){
		InvoiceListOrderByEnum orderByEnum=InvoiceListOrderByEnum.permissiveValueOf(orderBy);
		orderBy=orderByEnum.getValue();
		if(guarantorId == null){
			return invoiceListRepo.findByVisitNb(visitnb, orderBy, sortorder, limit, offset);
		} else {
			return invoiceListRepo.findByVisitNbAndGuarantorId(visitnb, guarantorId, orderBy, sortorder, limit, offset);
		}
	}

	public Integer getInvoiceListCount(Long visitnb, Long guarantorId) {
		if(guarantorId == null){
			return invoiceListRepo.findByVisitNbCount(visitnb);
		} else {
			return invoiceListRepo.findByVisitNbAndGuarantorIdCount(visitnb, guarantorId);
		}
	}
}
