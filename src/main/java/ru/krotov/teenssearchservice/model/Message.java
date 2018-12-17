package ru.krotov.teenssearchservice.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "text", length = 4096)
	private String text;

	@NotNull
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "created")
	private LocalDateTime created;

}
