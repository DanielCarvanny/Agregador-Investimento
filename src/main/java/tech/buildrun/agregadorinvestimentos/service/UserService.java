package tech.buildrun.agregadorinvestimentos.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.agregadorinvestimentos.controller.dto.*;
import tech.buildrun.agregadorinvestimentos.entity.Account;
import tech.buildrun.agregadorinvestimentos.entity.BillingAddress;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.repository.AccountRepository;
import tech.buildrun.agregadorinvestimentos.repository.AccountStockRepository;
import tech.buildrun.agregadorinvestimentos.repository.BillingAddressRepository;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;
    private AccountStockRepository accountStockRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository,
                       BillingAddressRepository billingAddressRepository, AccountStockRepository accountStockRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public List<UserResponseDTO> listUsersResponse() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
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
    public List<UserResponseDTO> listUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
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

    // Deleção de User e Account pelo ID
    public void deleteById(String userId) {
        var id = UUID.fromString(userId);

        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        // Buscar todas as contas associadas ao usuário
        List<Account> accounts = accountRepository.findByUserUserId(id);

        for (Account account : accounts) {
            // Excluir todos os registros de tb_billingaddress associados à conta
            billingAddressRepository.deleteAllByAccountId(account.getAccountId());

            // Excluir todos os registros de tb_account_stocks associados à conta
            accountStockRepository.deleteAllByAccountId(account.getAccountId());
        }

        // Excluir as contas do usuário
        accountRepository.deleteAllByUserId(id);

        // Agora podemos deletar o usuário
        userRepository.deleteById(id);
    }




    public void createAccount(String userId, CreateAccountDTO createAccountDTO) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Criar nova conta associada ao usuário
        var account = new Account(
                UUID.randomUUID(),
                user,
                createAccountDTO.description(),
                null,
                new ArrayList<>()
        );

        // Salvar a conta no banco de dados
        var accountCreated = accountRepository.save(account);

        // Adicionar a conta à lista de contas do usuário e salvar o usuário atualizado
        user.getAccounts().add(accountCreated);
        userRepository.save(user);  // Garante que a conta esteja associada corretamente no banco de dados

        // Criar e salvar endereço de cobrança
        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                accountCreated,
                createAccountDTO.street(),
                createAccountDTO.number()
        );

        billingAddressRepository.save(billingAddress);
    }


    public List<AccountResponseDTO> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return  user.getAccounts()
                .stream()
                .map(ac ->
                        new AccountResponseDTO(ac.getAccountId().toString(), ac.getDescription()))
                .toList();
    }
}
