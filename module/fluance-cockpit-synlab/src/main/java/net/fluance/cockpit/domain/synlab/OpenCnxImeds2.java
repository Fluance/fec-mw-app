//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.04.06 at 03:32:48 PM CEST 
//


package net.fluance.cockpit.domain.synlab;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Code2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="strID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cPat" type="{http://tempuri.org/}patientComplet" minOccurs="0"/&gt;
 *         &lt;element name="strNST1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="strPage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="strIDEXEC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "code",
    "code2",
    "strID",
    "cPat",
    "strNST1",
    "strPage",
    "strIDEXEC"
})
@XmlRootElement(name = "OpenCnxImeds2")
public class OpenCnxImeds2 {

    @XmlElement(name = "Code")
    protected String code;
    @XmlElement(name = "Code2")
    protected String code2;
    protected String strID;
    protected PatientComplet cPat;
    protected String strNST1;
    protected String strPage;
    protected String strIDEXEC;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the code2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode2() {
        return code2;
    }

    /**
     * Sets the value of the code2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode2(String value) {
        this.code2 = value;
    }

    /**
     * Gets the value of the strID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrID() {
        return strID;
    }

    /**
     * Sets the value of the strID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrID(String value) {
        this.strID = value;
    }

    /**
     * Gets the value of the cPat property.
     * 
     * @return
     *     possible object is
     *     {@link PatientComplet }
     *     
     */
    public PatientComplet getCPat() {
        return cPat;
    }

    /**
     * Sets the value of the cPat property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientComplet }
     *     
     */
    public void setCPat(PatientComplet value) {
        this.cPat = value;
    }

    /**
     * Gets the value of the strNST1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrNST1() {
        return strNST1;
    }

    /**
     * Sets the value of the strNST1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrNST1(String value) {
        this.strNST1 = value;
    }

    /**
     * Gets the value of the strPage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrPage() {
        return strPage;
    }

    /**
     * Sets the value of the strPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrPage(String value) {
        this.strPage = value;
    }

    /**
     * Gets the value of the strIDEXEC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrIDEXEC() {
        return strIDEXEC;
    }

    /**
     * Sets the value of the strIDEXEC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrIDEXEC(String value) {
        this.strIDEXEC = value;
    }

}
