/**
 * 
 */
package net.fluance.cockpit.core.model.wrap.patient;


public class PatientInListAddress {
	
	private String address;
	private String locality;
	private String postCode;
	
	
	/**
	 * 
	 */
	public PatientInListAddress() {}
	
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
	public PatientInListAddress(String address, String locality, String postCode) {
		super();
		this.address = address;
		this.locality = locality;
		this.postCode = postCode;
	}
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	
}
