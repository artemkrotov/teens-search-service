package ru.krotov.teenssearchservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(exclude = "user")
@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "text")
	private String text;

	@NotNull
	@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "created")
	private LocalDateTime created;

}
