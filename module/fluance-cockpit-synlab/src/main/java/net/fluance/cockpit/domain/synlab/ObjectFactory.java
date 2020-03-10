//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.04.06 at 03:32:48 PM CEST 
//


package net.fluance.cockpit.domain.synlab;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.fluance.cockpit.domain.synlab package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Patient_QNAME = new QName("http://tempuri.org/", "Patient");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.fluance.cockpit.domain.synlab
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OpenCnxImeds }
     * 
     */
    public OpenCnxImeds createOpenCnxImeds() {
        return new OpenCnxImeds();
    }

    /**
     * Create an instance of {@link PatientComplet }
     * 
     */
    public PatientComplet createPatientComplet() {
        return new PatientComplet();
    }

    /**
     * Create an instance of {@link OpenCnxImedsResponse }
     * 
     */
    public OpenCnxImedsResponse createOpenCnxImedsResponse() {
        return new OpenCnxImedsResponse();
    }

    /**
     * Create an instance of {@link OpenCnxImeds2 }
     * 
     */
    public OpenCnxImeds2 createOpenCnxImeds2() {
        return new OpenCnxImeds2();
    }

    /**
     * Create an instance of {@link OpenCnxImeds2Response }
     * 
     */
    public OpenCnxImeds2Response createOpenCnxImeds2Response() {
        return new OpenCnxImeds2Response();
    }

    /**
     * Create an instance of {@link OpenCnxImeds3 }
     * 
     */
    public OpenCnxImeds3 createOpenCnxImeds3() {
        return new OpenCnxImeds3();
    }

    /**
     * Create an instance of {@link OpenCnxImeds3Response }
     * 
     */
    public OpenCnxImeds3Response createOpenCnxImeds3Response() {
        return new OpenCnxImeds3Response();
    }

    /**
     * Create an instance of {@link OpenDeeplink }
     * 
     */
    public OpenDeeplink createOpenDeeplink() {
        return new OpenDeeplink();
    }

    /**
     * Create an instance of {@link OpenDeeplinkResponse }
     * 
     */
    public OpenDeeplinkResponse createOpenDeeplinkResponse() {
        return new OpenDeeplinkResponse();
    }

    /**
     * Create an instance of {@link GetPatient }
     * 
     */
    public GetPatient createGetPatient() {
        return new GetPatient();
    }

    /**
     * Create an instance of {@link GetPatientResponse }
     * 
     */
    public GetPatientResponse createGetPatientResponse() {
        return new GetPatientResponse();
    }

    /**
     * Create an instance of {@link Patient }
     * 
     */
    public Patient createPatient() {
        return new Patient();
    }

    /**
     * Create an instance of {@link GetStatutDemande }
     * 
     */
    public GetStatutDemande createGetStatutDemande() {
        return new GetStatutDemande();
    }

    /**
     * Create an instance of {@link GetStatutDemandeResponse }
     * 
     */
    public GetStatutDemandeResponse createGetStatutDemandeResponse() {
        return new GetStatutDemandeResponse();
    }

    /**
     * Create an instance of {@link TraceDemande }
     * 
     */
    public TraceDemande createTraceDemande() {
        return new TraceDemande();
    }

    /**
     * Create an instance of {@link GetStatutDemandes }
     * 
     */
    public GetStatutDemandes createGetStatutDemandes() {
        return new GetStatutDemandes();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link GetStatutDemandesResponse }
     * 
     */
    public GetStatutDemandesResponse createGetStatutDemandesResponse() {
        return new GetStatutDemandesResponse();
    }

    /**
     * Create an instance of {@link ArrayOfTraceDemande }
     * 
     */
    public ArrayOfTraceDemande createArrayOfTraceDemande() {
        return new ArrayOfTraceDemande();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Patient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "Patient")
    public JAXBElement<Patient> createPatient(Patient value) {
        return new JAXBElement<Patient>(_Patient_QNAME, Patient.class, null, value);
    }

}
