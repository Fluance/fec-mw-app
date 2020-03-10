/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.physician;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class PhysicianList implements Persistable<Integer> {

	private Integer attendingPhysicianId;
    private String attendingPhysicianFirstname;
    private String attendingPhysicianLastname;
    private Integer attendingPhysicianStaffid;
    private Integer referringPhysicianId;
    private String referringPhysicianFirstname;
    private String referringPhysicianLastname;
    private Integer referringPhysicianStaffid;
    private Integer consultingPhysicianId;
    private String consultingPhysicianFirstname;
    private String consultingPhysicianLastname;
    private Integer consultingPhysicianStaffid;
    private Integer admittingPhysicianId;
    private String admittingPhysicianFirstname;
    private String admittingPhysicianLastname;
    private Integer admittingPhysicianStaffid;
	
    
    public PhysicianList(){}
    public PhysicianList(Integer attendingPhysicianId, String attendingPhysicianFirstname,
			String attendingPhysicianLastname, Integer attendingPhysicianStaffid, Integer referringPhysicianId,
			String referringPhysicianFirstname, String referringPhysicianLastname, Integer referringPhysicianStaffid,
			Integer consultingPhysicianId, String consultingPhysicianFirstname, String consultingPhysicianLastname,
			Integer consultingPhysicianStaffid, Integer admittingPhysicianId, String admittingPhysicianFirstname,
			String admittingPhysicianLastname, Integer admittingPhysicianStaffid) {
		super();
		this.attendingPhysicianId = attendingPhysicianId;
		this.attendingPhysicianFirstname = attendingPhysicianFirstname;
		this.attendingPhysicianLastname = attendingPhysicianLastname;
		this.attendingPhysicianStaffid = attendingPhysicianStaffid;
		this.referringPhysicianId = referringPhysicianId;
		this.referringPhysicianFirstname = referringPhysicianFirstname;
		this.referringPhysicianLastname = referringPhysicianLastname;
		this.referringPhysicianStaffid = referringPhysicianStaffid;
		this.consultingPhysicianId = consultingPhysicianId;
		this.consultingPhysicianFirstname = consultingPhysicianFirstname;
		this.consultingPhysicianLastname = consultingPhysicianLastname;
		this.consultingPhysicianStaffid = consultingPhysicianStaffid;
		this.admittingPhysicianId = admittingPhysicianId;
		this.admittingPhysicianFirstname = admittingPhysicianFirstname;
		this.admittingPhysicianLastname = admittingPhysicianLastname;
		this.admittingPhysicianStaffid = admittingPhysicianStaffid;
	}


	public Integer getAttendingPhysicianId() {
		return attendingPhysicianId;
	}


	public void setAttendingPhysicianId(Integer attendingPhysicianId) {
		this.attendingPhysicianId = attendingPhysicianId;
	}


	public String getAttendingPhysicianFirstname() {
		return attendingPhysicianFirstname;
	}


	public void setAttendingPhysicianFirstname(String attendingPhysicianFirstname) {
		this.attendingPhysicianFirstname = attendingPhysicianFirstname;
	}


	public String getAttendingPhysicianLastname() {
		return attendingPhysicianLastname;
	}


	public void setAttendingPhysicianLastname(String attendingPhysicianLastname) {
		this.attendingPhysicianLastname = attendingPhysicianLastname;
	}


	public Integer getAttendingPhysicianStaffid() {
		return attendingPhysicianStaffid;
	}


	public void setAttendingPhysicianStaffid(Integer attendingPhysicianStaffid) {
		this.attendingPhysicianStaffid = attendingPhysicianStaffid;
	}


	public Integer getReferringPhysicianId() {
		return referringPhysicianId;
	}


	public void setReferringPhysicianId(Integer referringPhysicianId) {
		this.referringPhysicianId = referringPhysicianId;
	}


	public String getReferringPhysicianFirstname() {
		return referringPhysicianFirstname;
	}


	public void setReferringPhysicianFirstname(String referringPhysicianFirstname) {
		this.referringPhysicianFirstname = referringPhysicianFirstname;
	}


	public String getReferringPhysicianLastname() {
		return referringPhysicianLastname;
	}


	public void setReferringPhysicianLastname(String referringPhysicianLastname) {
		this.referringPhysicianLastname = referringPhysicianLastname;
	}


	public Integer getReferringPhysicianStaffid() {
		return referringPhysicianStaffid;
	}


	public void setReferringPhysicianStaffid(Integer referringPhysicianStaffid) {
		this.referringPhysicianStaffid = referringPhysicianStaffid;
	}


	public Integer getConsultingPhysicianId() {
		return consultingPhysicianId;
	}


	public void setConsultingPhysicianId(Integer consultingPhysicianId) {
		this.consultingPhysicianId = consultingPhysicianId;
	}


	public String getConsultingPhysicianFirstname() {
		return consultingPhysicianFirstname;
	}


	public void setConsultingPhysicianFirstname(String consultingPhysicianFirstname) {
		this.consultingPhysicianFirstname = consultingPhysicianFirstname;
	}


	public String getConsultingPhysicianLastname() {
		return consultingPhysicianLastname;
	}


	public void setConsultingPhysicianLastname(String consultingPhysicianLastname) {
		this.consultingPhysicianLastname = consultingPhysicianLastname;
	}


	public Integer getConsultingPhysicianStaffid() {
		return consultingPhysicianStaffid;
	}


	public void setConsultingPhysicianStaffid(Integer consultingPhysicianStaffid) {
		this.consultingPhysicianStaffid = consultingPhysicianStaffid;
	}


	public Integer getAdmittingPhysicianId() {
		return admittingPhysicianId;
	}


	public void setAdmittingPhysicianId(Integer admittingPhysicianId) {
		this.admittingPhysicianId = admittingPhysicianId;
	}


	public String getAdmittingPhysicianFirstname() {
		return admittingPhysicianFirstname;
	}


	public void setAdmittingPhysicianFirstname(String admittingPhysicianFirstname) {
		this.admittingPhysicianFirstname = admittingPhysicianFirstname;
	}


	public String getAdmittingPhysicianLastname() {
		return admittingPhysicianLastname;
	}


	public void setAdmittingPhysicianLastname(String admittingPhysicianLastname) {
		this.admittingPhysicianLastname = admittingPhysicianLastname;
	}


	public Integer getAdmittingPhysicianStaffid() {
		return admittingPhysicianStaffid;
	}


	public void setAdmittingPhysicianStaffid(Integer admittingPhysicianStaffid) {
		this.admittingPhysicianStaffid = admittingPhysicianStaffid;
	}


	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
	
	@JsonIgnore
	@Override
	public Integer getId() {
		return null;
	}
}