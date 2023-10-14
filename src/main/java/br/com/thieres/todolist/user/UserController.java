package br.com.thieres.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody UserViewModel ent){

        var verifyUser = this.userRepository.findByUsername(ent.getUsername());
        if(verifyUser != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario já existe.");
        }
        
        var passwordHashred = BCrypt.withDefaults().hashToString(12, ent.getPassword().toCharArray());
        ent.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(ent);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);

    }
}
