package net.fluance.cockpit.core.model.jdbc.appointment;

public class AppointmentSpecific {
    private String procedure;
    private String description;
   
    public String getProcedure() {
        return procedure;
    }
   
    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }
   
    public String getDescription() {
        return description;
    }
   
    public void setDescription(String description) {
        this.description = description;
    }
}
