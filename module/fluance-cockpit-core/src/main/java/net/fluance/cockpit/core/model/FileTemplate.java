package net.fluance.cockpit.core.model;

public class FileTemplate {
	
	private Long templateId;
	
	private String templateName;

	
	public FileTemplate(Long templateId, String templateName) {
		this.templateId = templateId;
		this.templateName = templateName;
	}


	public Long getTemplateId() {
		return templateId;
	}

	
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	
	public String getTemplateName() {
		return templateName;
	}

	
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	
	
}
