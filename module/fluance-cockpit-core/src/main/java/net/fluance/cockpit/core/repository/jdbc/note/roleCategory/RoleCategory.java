package net.fluance.cockpit.core.repository.jdbc.note.roleCategory;

public class RoleCategory {
	private String role;
	private int category;
	
	public RoleCategory(String role, int category){
		this.role = role;
		this.category = category;
	}
	
	public String getRole(){
		return this.role;
	}
	
	public Integer getCategory(){
		return this.category;
	}
}
