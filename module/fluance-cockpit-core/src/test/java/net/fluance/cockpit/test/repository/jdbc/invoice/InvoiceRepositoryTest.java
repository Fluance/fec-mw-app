package net.fluance.cockpit.test.repository.jdbc.invoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.invoice.Invoice;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class InvoiceRepositoryTest extends AbstractTest {

	@Autowired
	private InvoiceRepository invoiceRepo;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.id}")
	private long id;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.name}")
	private String name;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.invdt}")
	private String invdt;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.code}")
	private String code;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.total}")
	private Double total;

	@Test
	@Ignore("Test does not compile")
	public void testIfInvoiceExists() throws ParseException {
		Invoice lInvoices = invoiceRepo.findOne(id);
		assertNotNull(lInvoices);
		assertTrue(id == lInvoices.getId());
		assertEquals(name, lInvoices.getGuarantor_name());
		assertEquals(invdt, lInvoices.getInvdt());

		assertEquals(code, lInvoices.getGuarantor_code());
		assertEquals(total, lInvoices.getTotal());
		assertNotNull(lInvoices.getApdrg_code());
		assertNotNull(lInvoices.getApdrg_descr());
		assertNotNull(lInvoices.getMdc_code());
		assertNotNull(lInvoices.getMdc_descr());
	}

}