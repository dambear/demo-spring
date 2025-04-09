package com.danbear.demo.user;

import com.danbear.demo.common.exception.BusinessException;
import com.danbear.demo.user.dto.UserDto;
import com.danbear.demo.user.dto.UserMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
  private final UserRepository repository;
  private final UserMapper mapper;

  public UserService(UserRepository repository, UserMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public CompletableFuture<UserDto> createUserAsync(UserDto userDto) {

    if (repository.existsByUsername(userDto.username())) {
      throw new BusinessException(
              UserErrorCode.USERNAME_EXISTS.getCode(),
              UserErrorCode.USERNAME_EXISTS.getMessage(),
              UserErrorCode.USERNAME_EXISTS.getHttpStatus(),
              Map.of("username", userDto.username())
      );
    }

    if (repository.existsByEmail(userDto.email())) {
      throw new BusinessException(
              UserErrorCode.EMAIL_EXISTS.getCode(),
              UserErrorCode.EMAIL_EXISTS.getMessage(),
              UserErrorCode.EMAIL_EXISTS.getHttpStatus(),
              Map.of("email", userDto.email())
      );
    }

    User user = mapper.toEntity(userDto);
    User savedUser = repository.save(user);
    return CompletableFuture.completedFuture(mapper.toDto(savedUser));
  }

  @Async
  @Transactional(readOnly = true)
  public CompletableFuture<UserDto> getUserByIdAsync(Long id) {
    return CompletableFuture.completedFuture(
            repository.findById(id)
                    .map(mapper::toDto)
                    .orElseThrow(() -> new BusinessException(
                            UserErrorCode.USER_NOT_FOUND.getCode(),
                            UserErrorCode.USER_NOT_FOUND.getMessage(),
                            UserErrorCode.USER_NOT_FOUND.getHttpStatus(),
                            Map.of("userId", id)
                    ))
    );
  }

  @Async
  @Transactional(readOnly = true)
  public CompletableFuture<UserDto> getUserAsync(Long id) {
    return CompletableFuture.completedFuture(
        repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new RuntimeException("User not found"))
    );
  }

  @Async
  @Transactional(readOnly = true)
  public CompletableFuture<List<UserDto>> getAllUsersAsync() {
    return CompletableFuture.supplyAsync(() ->
        repository.findAll()
            .stream()
            .map(mapper::toDto)
            .toList()
    );
  }

  @Async
  @Transactional(readOnly = true)
  public CompletableFuture<Page<UserDto>> getAllUsersAsync(Pageable pageable) {
    return CompletableFuture.supplyAsync(() ->
        repository.findAll(pageable)
            .map(mapper::toDto)
    );
  }

}