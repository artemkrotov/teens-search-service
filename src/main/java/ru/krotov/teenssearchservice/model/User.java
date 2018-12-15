package ru.krotov.teenssearchservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "VK_ID")
	private String vkId;

	@Column(name = "VK_DOMAIN")
	private String vkDomain;

	@Column(name = "INSTAGRAM_ID")
	private String instagramId;

	@Column(name = "PHOTO_URL")
	private String photoUrl;

	@Column(name = "CITY")
	private String city;

	@Column(name = "B_DAY")
	private String bDay;

	@OneToMany(mappedBy = "USER_ID")
	private List<Message> messages;
}
