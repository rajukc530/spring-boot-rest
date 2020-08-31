package id.test.springboottesting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class UserDto {

	 @JsonIgnore
	 private Long id;
	 private String email;
	 private String password;
	 private String name;
	 
}
