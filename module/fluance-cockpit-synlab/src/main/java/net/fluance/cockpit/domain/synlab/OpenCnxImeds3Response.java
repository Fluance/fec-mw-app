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
 *         &lt;element name="OpenCnxImeds3Result" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="strID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "openCnxImeds3Result",
    "strID"
})
@XmlRootElement(name = "OpenCnxImeds3Response")
public class OpenCnxImeds3Response {

    @XmlElement(name = "OpenCnxImeds3Result")
    protected boolean openCnxImeds3Result;
    protected String strID;

    /**
     * Gets the value of the openCnxImeds3Result property.
     * 
     */
    public boolean isOpenCnxImeds3Result() {
        return openCnxImeds3Result;
    }

    /**
     * Sets the value of the openCnxImeds3Result property.
     * 
     */
    public void setOpenCnxImeds3Result(boolean value) {
        this.openCnxImeds3Result = value;
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

}
