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
import tech.buildrun.agregadorinvestimentos.controller.UpdateUserDTO;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

            //Arrange
                // Joga uma exceção quando o método chama o save
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

    @Nested
    class listUsers{
        @Test
        @DisplayName("should Return All Users With Success")
        void shouldReturnAllUsersWithSuccess() {
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
                var userList = List.of(user);
                doReturn(userList)
                        .when(userRepository)
                        .findAll();


            // Act
            var output = userService.listUsers();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteById{
        @Test
        @DisplayName("should Delete User With Success When User Exists")
        void shouldDeleteUserWithSuccessWhenUserExists() {
            //Arrage
                /*
                Retorna true quando o método chama o existById e captura o que está sendo passado
                para dentro desse método
                 */
                doReturn(true)
                        .when(userRepository)
                        .existsById(uuidArgumentCaptor.capture());
                /*
                Não há retorno quando o método chama o deleteById e captura o que está sendo passado
                para dentro desse método
                 */
                doNothing()
                        .when(userRepository)
                        .deleteById(uuidArgumentCaptor.capture());
                var userId = UUID.randomUUID();
            // Act
            userService.deleteById(userId.toString());

            // Assert
                /*
                Se utiliza o getAllValues quando uuidArgumentCaptor.capture já tiver sido
                utilizado mais de uma vez, além disso ele cria uma lista com os valores
                que irá retornar na ordem conforme capture é chamado
                 */
                var idList = uuidArgumentCaptor.getAllValues();
                assertEquals(userId,idList.get(0));
                assertEquals(userId,idList.get(1));

            /*
            Verifica as chamadas dos métodos existsById e deleteById
             */
            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("should Not Delete User When User Not Exists")
        void shouldNotDeleteUserWhenUserNotExists() {
            //Arrage
                /*
                Retorna false quando o método chama o existById e captura o que está sendo passado
                para dentro desse método
                 */
                doReturn(false)
                        .when(userRepository)
                        .existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert

                // Não há necessidade de utilizar getAllValues
                assertEquals(userId,uuidArgumentCaptor.getValue());

                // Verifica as chamadas dos métodos existsById e deleteById

                verify(userRepository, times(1))
                        .existsById(uuidArgumentCaptor.getValue());

                verify(userRepository, times(0))
                        .deleteById(any());
        }
    }

    @Nested
    class updateUserById{
        @Test
        @DisplayName("should Update User By Id when user exists and username and password is filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordIsFilled() {

            //Arrage
            var updateUserDTO = new UpdateUserDTO(
                    "newUsername",
                    "newPassword"
            );
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
                /*
                Retorna um objeto quando o método chama o save e captura o que está sendo passado
                para dentro desse método
                 */
            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            //Act
            userService.updateUserById(user.getUserId().toString(), updateUserDTO);

            //Assert
                //Verificação do que foi passado
                assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

                var userCaptured= userArgumentCaptor.getValue();

                assertEquals(updateUserDTO.username(), userCaptured.getUsername());
                assertEquals(updateUserDTO.password(), userCaptured.getPassword());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .save(user);
        }
    }

}