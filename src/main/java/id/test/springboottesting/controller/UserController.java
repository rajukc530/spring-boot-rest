package id.test.springboottesting.controller;

import id.test.springboottesting.model.User;
import id.test.springboottesting.model.UserDto;
import id.test.springboottesting.model.UserRoleDto;
import id.test.springboottesting.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/***
 * Project Name     : spring-boot-testing
 * Username         : Teten Nugraha
 * Date Time        : 12/18/2019
 * Telegram         : @tennugraha
 */

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    
    //@Autowired
    //private FileStorageProperties location;

    @Value("${file.location}")
	 private String location;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserDto> dtos = new ArrayList<UserDto>();
        for (User user : users) {
        	dtos.add(modelMapper.map(user, UserDto.class));
			
		}
        System.out.println("dtos:"+dtos.toString());
        return dtos;
    }

    
    @GetMapping("/distinct")
    public List<String> getDistinctUsers() {
        List<String> users = userService.findDistinctUsers();
        return users;
    }
    
    @GetMapping("/filter")
    public List<UserRoleDto> getFilteredUsers(@RequestParam(required = false)  String name) throws UnsupportedEncodingException {
    	
    	System.out.println("name:"+name);
    	
    	//String decoded = URLDecoder.decode(name,StandardCharsets.UTF_8.toString());
    	
    	System.out.println("name:"+name);
    	
        List<UserRoleDto> users = userService.findFilteredusers();
        return users;
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findUserById(id)
                .map(userObj -> {
                    userObj.setId(id);
                    return ResponseEntity.ok(userService.updateUser(userObj));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(user -> {
                    userService.deleteUserById(id);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @RequestMapping(value = "/upload/{id}", method = RequestMethod.POST)
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file,
    		@PathVariable Long id) throws IOException{
        
    	final String filepath = location + file.getOriginalFilename();
		byte[] bytes = file.getBytes();
		
		System.out.println("location:"+location);

		System.out.println("id:"+id);
		
		System.out.println("file:"+file.getContentType());

		try {

			FileOutputStream writer = new FileOutputStream(filepath);
			writer.write(bytes);
			writer.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
