package com.sda.travelagency.service;

import com.sda.travelagency.dtos.AccountDto;
import com.sda.travelagency.exception.SessionExpiredException;
import com.sda.travelagency.exception.UserAlreadyExistsException;
import com.sda.travelagency.util.Username;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final UserDetailsManager userDetailsManager;

    public AccountService(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }
    /**
     * This method  takes AccountDto object as a param.
     * If username already exists in database it throws UserAlreadyExistsException.
     * Password is encoded by BCryptPasswordEncoder.
     * It is using to User builder build UserDetails object with data from AccountDto. which is saved in UserDetailsManager
     * This method is strictly used to create USER role.
     * @param accountDto
     * @return void
     * @throws UserAlreadyExistsException "This username is already taken"
     **/
    public void createUser(AccountDto accountDto){
        if(userDetailsManager.userExists(accountDto.getName())){
            throw new UserAlreadyExistsException("This username is already taken");
        }
        String password = new BCryptPasswordEncoder().encode(accountDto.getPassword());
        UserDetails user = User
                .withUsername(accountDto.getName())
                .password(password)
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
    }
    /**
     * This method takes AccountDto object as a param.
     * If username already exists in database it thorws UserAlreadyExistsException.
     * Password is encoded by BCryptPasswordEncoder.
     * It is using to User builder build UserDetails object with data from AccountDto. which is saved in UserDetailsManager
     * This method is strictly used to create ADMIN user role.
     * @param accountDto
     * @return void
     * @throws UserAlreadyExistsException "This username is already taken"
     **/
    public void createAdmin(AccountDto accountDto){
        if(userDetailsManager.userExists(accountDto.getName())){
            throw new UserAlreadyExistsException("This username is already taken");
        }
        String password = new BCryptPasswordEncoder().encode(accountDto.getPassword());
        UserDetails admin = User
                .withUsername(accountDto.getName())
                .password(password)
                .roles("USER","ADMIN")
                .build();
        userDetailsManager.createUser(admin);
    }
    /**
     * This method is used to delete active account.
     * @return void
     **/
    public void deleteUser(){
        userDetailsManager.deleteUser(Username.getActive());
    }
    /**
     * This method takes new password as a param.
     * If present it gets active user username bu Username util class or else it throws SessionExpiredException
     * Instance of active user UserDetails object is loaded from UserDetailsManager,
     * New password is encoded by BCryptPasswordEncoder and changed.
     * @param password
     * @return void
     * @throws SessionExpiredException "Session expired"
     **/
    public void changePassword(String password){
        String username = Username.getActive();
        if(username == null) {
            throw new SessionExpiredException("Session expired");
        }
        UserDetails user = userDetailsManager.loadUserByUsername(username);
        String newPassword = new BCryptPasswordEncoder().encode(password);
        userDetailsManager.changePassword(user.getPassword(), newPassword);
    }
}
