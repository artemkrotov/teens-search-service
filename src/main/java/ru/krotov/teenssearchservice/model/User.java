package ru.krotov.teenssearchservice.model;

import com.vk.api.sdk.objects.base.BaseObject;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "vk_id")
	private Integer vkId;

	@Column(name = "vk_domain")
	private String vkDomain;

	@Column(name = "instagram_id")
	private String instagramId;

	@Column(name = "photo_url")
	private String photoUrl;

	@Column(name = "city")
	private String city;

	@Column(name = "b_day")
	private String bDay;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
	private List<Message> messages = new ArrayList<>();

	public void setCityIfPresent (BaseObject city) {
		if (city != null) {
			this.setCity(city.getTitle());
		}
	}
}
