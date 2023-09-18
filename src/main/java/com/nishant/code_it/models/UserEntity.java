package com.nishant.code_it.models;

import com.nishant.code_it.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseModel{
   private String username;
   private String name;
   private String password;
   @Enumerated(EnumType.STRING)
   private Role role;
}
