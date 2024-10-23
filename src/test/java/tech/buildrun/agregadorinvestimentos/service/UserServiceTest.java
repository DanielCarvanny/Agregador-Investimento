package tech.buildrun.agregadorinvestimentos.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.buildrun.agregadorinvestimentos.controller.CreateUserDTO;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    /*
    Captura o argumento que estamos passando dentro de um parâmetro
     */
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser{

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUSer() {

            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );

            /*
            Retorna um objeto quando o método chama o save e captura o que está sendo passado
            para dentro desse método
             */
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDTO("username",
                    "email@email.com",
                    "password");
            // Act
            var output = userService.createUser(input);

            // Assert
            assertNotNull(output);


                //Verificação do que foi passado
            var userCaptured = userArgumentCaptor.getValue();

                // Verificação do username
            assertEquals(input.username(),userCaptured.getUsername());

                // Verificação do email
            assertEquals(input.email(),userCaptured.getEmail());

                // Verificação da password
            assertEquals(input.password(),userCaptured.getPassword());

        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            /* Arrange
            * Joga uma exceção quando o método chama o save
            */
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDTO("username",
                    "email@email.com",
                    "password");

            // Act & assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    @Nested
    class getUserbById{
        @Test
        @DisplayName("should Get User By Id With Success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {

            //Arrage
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );

            /*
            Retorna um objeto quando o método chama o findById e captura o que está sendo passado
            para dentro desse método
             */
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            //Act
            var output = userService.getUserbById(user.getUserId().toString());

            //Assert
            assertTrue(output.isPresent());

                //Verificação do que foi passado
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("should Get User By Id With Success when optional is empty")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {

            //Arrage
            var userId = UUID.randomUUID();

            /*
            Retorna um objeto quando o método chama o findById e captura o que está sendo passado
            para dentro desse método
             */
            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            //Act
            var output = userService.getUserbById(userId.toString());

            //Assert
            assertTrue(output.isEmpty());

            //Verificação do que foi passado
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }



}