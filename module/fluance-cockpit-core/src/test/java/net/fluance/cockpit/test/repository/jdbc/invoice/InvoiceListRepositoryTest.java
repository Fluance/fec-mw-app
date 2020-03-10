package net.fluance.cockpit.test.repository.jdbc.invoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.invoice.InvoiceList;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class InvoiceListRepositoryTest extends AbstractTest {

	@Autowired
	private InvoiceListRepository invoiceRepo;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.id}")
	private Long id;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.nb_records}")
	private int nb_records;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.invdt}")
	private String invdt;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.total}")
	private Double total;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.visitnb}")
	private Long visitnb;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.balance}")
	private Double balance;

	@Test
	@Ignore("Test does not compile")
	public void testFindByVisitNb() throws ParseException {
		List<InvoiceList> invoiceLists = invoiceRepo.findByVisitNb(visitnb, null, null, null, null);
		assertNotNull(invoiceLists);
		assertTrue(invoiceLists.size()>0);
		assertTrue(id == invoiceLists.get(0).getId());
		assertEquals(nb_records, invoiceLists.get(0).getNb_records());
		assertEquals(invdt, invoiceLists.get(0).getInvdt());
		assertTrue(total == invoiceLists.get(0).getTotal());
		assertTrue(balance == invoiceLists.get(0).getBalance());
	}

	@Test
	@Ignore("Test does not compile")
	public void testFindByVisitNbAndGuarantorId() throws ParseException {
		List<InvoiceList> invoiceLists = invoiceRepo.findByVisitNbAndGuarantorId(visitnb, (long)1 , null, null, null, null);
		assertNotNull(invoiceLists);
		assertTrue(invoiceLists.size()>0);
		assertTrue(id == invoiceLists.get(0).getId());
		assertEquals(nb_records, invoiceLists.get(0).getNb_records());
		assertEquals(invdt, invoiceLists.get(0).getInvdt());
		assertTrue(total == invoiceLists.get(0).getTotal());
		assertTrue(balance == invoiceLists.get(0).getBalance());
	}

}