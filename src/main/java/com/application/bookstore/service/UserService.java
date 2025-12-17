package com.application.bookstore.service;

import com.application.bookstore.model.AppUser;
import com.application.bookstore.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final AppUser appUser = userRepository.findByUsername(username);
        if(appUser == null){
            throw new UsernameNotFoundException("User not found: " + username);
        }
        final UserDetails machedUser = User.withUsername(appUser.getUserName()).password(appUser.getPassword()).roles(appUser.getRoles().split(",")).build();


//        System.out.println("Mached user: " + machedUser);

        return machedUser;
    }

//        @PostConstruct // use to execute after dependency injection to initialization
//    public void initData(){
//        userRepository.save(new AppUser("admin",passwordEncoder.encode("admin"),"ADMIN,MANAGER,USER"));
//        userRepository.save(new AppUser("manager",passwordEncoder.encode("manager"),"MANAGER,USER"));
//        userRepository.save(new AppUser("user1",passwordEncoder.encode("user1"),"USER"));
//    }
}
