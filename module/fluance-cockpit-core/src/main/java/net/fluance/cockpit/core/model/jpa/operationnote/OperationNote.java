package net.fluance.cockpit.core.model.jpa.operationnote;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

@Entity(name="OperationNote")
@Table(name = "operation_note", schema = "ehealth")
public class OperationNote implements Persistable<Integer>, Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_note_id_seq_generator")
    @SequenceGenerator(name="operation_note_id_seq_generator", sequenceName = "operation_note_id_seq")
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
	@Column(name="appoint_id")
	private Long appointmentId;
	@Column(name="note")
	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public boolean isNew() {
		return false;
	}

}
