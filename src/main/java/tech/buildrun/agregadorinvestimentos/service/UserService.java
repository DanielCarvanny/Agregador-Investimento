package tech.buildrun.agregadorinvestimentos.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.agregadorinvestimentos.controller.dto.CreateAccountDTO;
import tech.buildrun.agregadorinvestimentos.controller.dto.CreateUserDTO;
import tech.buildrun.agregadorinvestimentos.controller.dto.UpdateUserDTO;
import tech.buildrun.agregadorinvestimentos.entity.Account;
import tech.buildrun.agregadorinvestimentos.entity.BillingAddress;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.repository.AccountRepository;
import tech.buildrun.agregadorinvestimentos.repository.BillingAddressRepository;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository,
                       BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    // Criação de User
    public UUID createUser(CreateUserDTO createUserDTO){

        // DTO -> Entity
        var entity = new User(
                UUID.randomUUID(),
                createUserDTO.username(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    // Busca de User pelo ID
    public Optional<User> getUserbById(String userId){

        return userRepository.findById(UUID.fromString(userId));
    }

    // Listagem de Users
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    // Atualização do User pelo ID
    public void updateUserById(String userId,
                               UpdateUserDTO updateUserDTO) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if (updateUserDTO.username() != null) {
                user.setUsername(updateUserDTO.username());
            }

            if (updateUserDTO.password() != null) {
                user.setPassword(updateUserDTO.password());
            }
            userRepository.save(user);
        }
    }

    // Deleção de User pelo ID
    public void deleteById(String userId){
        var id = UUID.fromString(userId);
        var userExist = userRepository.existsById(id);

        if (userExist){
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDTO createAccountDTO) {
        var user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //DTO -> ENTITY
        var account = new Account(
                UUID.randomUUID(),
                user,
                createAccountDTO.description(),
                null,
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                createAccountDTO.street(),
                createAccountDTO.number()
        );

        billingAddressRepository.save(billingAddress);
    }
}
