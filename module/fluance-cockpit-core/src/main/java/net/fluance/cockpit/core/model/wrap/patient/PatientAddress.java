/**
 * 
 */
package net.fluance.cockpit.core.model.wrap.patient;

public class PatientAddress {
	
	private String addressLine;
	private String addressLine2;
	private String locality;
	private String postCode;
	private String subPostCode;
	private String adressComplement;
	private String careOf;
	private String canton;
	private String country;
	
	/**
	 * 
	 */
	public PatientAddress() {}
	
	/**
	 * @param addressLine
	 * @param addressLine2
	 * @param locality
	 * @param postCode
	 * @param subPostCode
	 * @param adressComplement
	 * @param careOf
	 * @param canton
	 * @param country
	 */
	public PatientAddress(String address, String address2, String locality, String postCode, String subPostCode,
			String adressComplement, String careOf, String canton, String country) {
		super();
		this.addressLine = address;
		this.addressLine2 = address2;
		this.locality = locality;
		this.postCode = postCode;
		this.subPostCode = subPostCode;
		this.adressComplement = adressComplement;
		this.careOf = careOf;
		this.canton = canton;
		this.country = country;
	}
	
	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	
	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}
	
	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}
	
	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	/**
	 * @return the subPostCode
	 */
	public String getSubPostCode() {
		return subPostCode;
	}
	
	/**
	 * @param subPostCode the subPostCode to set
	 */
	public void setSubPostCode(String subPostCode) {
		this.subPostCode = subPostCode;
	}
	
	/**
	 * @return the adressComplement
	 */
	public String getAdressComplement() {
		return adressComplement;
	}

	/**
	 * @param adressComplement the adressComplement to set
	 */
	public void setAdressComplement(String adressComplement) {
		this.adressComplement = adressComplement;
	}
	
	/**
	 * @return the careOf
	 */
	public String getCareOf() {
		return careOf;
	}
	
	/**
	 * @param careOf the careOf to set
	 */
	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}
	
	/**
	 * @return the canton
	 */
	public String getCanton() {
		return canton;
	}
	
	/**
	 * @param canton the canton to set
	 */
	public void setCanton(String canton) {
		this.canton = canton;
	}
	
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

}
