package id.test.springboottesting.service;

import id.test.springboottesting.exception.UserRegistrationException;
import id.test.springboottesting.model.User;
import id.test.springboottesting.model.UserRoleDto;
import id.test.springboottesting.repository.UserRepository;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
//import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains();
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/***
 * Project Name : spring-boot-testing Username : Teten Nugraha Date Time :
 * 12/18/2019 Telegram : @tennugraha
 */

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserService(UserRepository userRepository, EntityManager em) {
		this(userRepository);
		this.em = em;
	}

	public Optional<User> login(String email, String password) {
		return userRepository.login(email, password);
	}

	public User createUser(User user) {
		Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
		if (userOptional.isPresent()) {
			throw new UserRegistrationException("User with email " + user.getEmail() + " already exists");
		}

		return userRepository.save(user);
	}

	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public List<String> findDistinctUsers() {
		return userRepository.getDistinctNames();
	}

	public List<UserRoleDto> findFilteredusers() {

//    	ExampleMatcher matcher = ExampleMatcher
//    		    .matchingAll()
//    		    .withMatcher("name", contains().ignoreCase
//    		    .withMatcher("email", contains().ignoreCase());
//    		User example = User
//    		    .builder()
//    		    .name("raju") 
//    		    .email("email") 
//    		    .build();
//    		return userRepository.findAll(Example.of(example, matcher));
		// TypedQuery<UserRoleDto> query = (TypedQuery<UserRoleDto>)
		// em.createNativeQuery(sql).unwrap(org.hibernate.query.NativeQuery.class)
//    			.setResultTransformer(Transformers.aliasToBean(UserRoleDto.class));
//    	
//    	List<UserRoleDto> users =  query.getResultList();

		String name = "raju";
		String id = "4";
		String sql = "SELECT u.name, r.name as rolename FROM users u, roles r  where 1=1 ";

		if (name != null) {
			sql += " and u.name=:name";

		}

		if (id != null) {
			sql += " and u.id=:id";

		}

		Query query = this.em.createNativeQuery(sql);

		if (name != null) {

			query.setParameter("name", name);

		}

		if (id != null) {

			query.setParameter("id", id);

		}

		List<Object[]> results = query.getResultList();

		List<UserRoleDto> users = toDTO(results);

		// TypedQuery<UserRoleDto> query = (TypedQuery<UserRoleDto>)
		// em.createNativeQuery(sql).unwrap(org.hibernate.query.NativeQuery.class)
		/// .setResultTransformer(Transformers.aliasToBean(UserRoleDto.class));

		// List<UserRoleDto> users = query.getResultList();

//    	
		System.out.println("results:" + users.toString());

		return users;

	}

	public List<UserRoleDto> toDTO(List<Object[]> results) {
		List<UserRoleDto> users = new ArrayList<>();
		results.stream().forEach((record) -> {
			UserRoleDto dto = new UserRoleDto();
			String name1 = (String) record[0];
			String name2 = (String) record[1];
			dto.setName(name1);
			dto.setRolename(name2);
			users.add(dto);
		});

		return users;
	}

	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}
}
