package com.agnasarp.mongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    //define Data Access Layer object
    private final UserDAL userDAL;
    private final UserRepository userRepository;

    //initialize DAL object via constructor autowiring
    public UserController(UserRepository userRepository, UserDAL userDAL) {
        this.userRepository = userRepository;
        this.userDAL = userDAL;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        LOG.info("Getting all users.");
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID: {}.", userId);
        return userDAL.getUserById(userId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User addNewUsers(@RequestBody User user) {
        LOG.info("Saving user.");
        return userRepository.save(user);
    }

    //change method implementation to use DAL and hence MongoTemplate
    @RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
    public Object getAllUserSettings(@PathVariable String userId) {
        User user = userDAL.getUserById(userId);
        if (user != null) {
            return userDAL.getAllUserSettings(userId);
        } else {
            return "User not found.";
        }
    }

    //change method implementation to use DAL and hence MongoTemplate
    @RequestMapping(value = "/settings/{userId}/{key}", method = RequestMethod.GET)
    public String getUserSetting(
            @PathVariable String userId, @PathVariable String key) {
        return userDAL.getUserSetting(userId, key);
    }

    @RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.GET)
    public String addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value) {
        User user = userDAL.getUserById(userId);
        if (user != null) {
            user.getUserSettings().put(key, value);
            userRepository.save(user);
            return "Key added";
        } else {
            return "User not found.";
        }
    }

}
