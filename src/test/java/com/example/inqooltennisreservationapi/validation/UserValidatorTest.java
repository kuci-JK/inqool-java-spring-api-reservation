package com.example.inqooltennisreservationapi.validation;

import com.example.inqooltennisreservationapi.exceptions.InvalidRequestException;
import com.example.inqooltennisreservationapi.model.api.UserDTOs;
import com.example.inqooltennisreservationapi.model.entity.UserEntity;
import com.example.inqooltennisreservationapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class UserValidatorTest {

    @MockBean
    UserRepository repo;

    @Autowired
    UserValidator validator;

    @Test
    void acceptsNonExistingUser() {
        given(repo.getUserByPhone("777777777")).willReturn(Optional.empty());

        try {
            validator.validateUserDataMatchOrUserNotExists(new UserDTOs.UserModifyParams("John Doe", "777777777"));
        } catch (InvalidRequestException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    void rejectsUserWithDifferentName() {
        given(repo.getUserByPhone("777777777")).willReturn(Optional.of(new UserEntity(1, "John Doeee", "777777777")));

        try {
            validator.validateUserDataMatchOrUserNotExists(new UserDTOs.UserModifyParams("John Doe", "777777777"));
            fail("Should throw exception");
        } catch (InvalidRequestException e) {
            // pass
        }
    }
}
