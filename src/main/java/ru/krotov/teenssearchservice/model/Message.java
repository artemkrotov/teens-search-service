package ru.krotov.teenssearchservice.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "USER")
public class Message {

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "TEXT")
	private String text;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private User user;

	@Column(name = "CREATED")
	private LocalDateTime created;

}
