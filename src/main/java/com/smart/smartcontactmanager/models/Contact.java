package com.smart.smartcontactmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cId;
	
	@NotBlank(message="yrrr empty mt rkho")
	@Size(max=20, message="enter between two to twenty characters")
	private String name;
	private String nickname;
	private String work;
	private String email;
	private String phone;
	private String image;
	@Column(length=5000)
	private String description;
	
	@JsonIgnore
	@ManyToOne
	private User user;
	
	
	public Contact() {

		// TODO Auto-generated constructor stub
	}
	public Contact(int cId, String name, String nickname, String work, String email, String phone, String image,
			String description) {
		this.cId = cId;
		this.name = name;
		this.nickname = nickname;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
	}
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", nickname=" + nickname + ", work=" + work + ", email="
				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + ", user=" + user
				+ "]";
	}
	
	//it is called by remove method internally 
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cId==((Contact)obj).getcId();
	} 
	
	
	
}
