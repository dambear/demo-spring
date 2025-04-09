package com.danbear.demo.user;

import com.danbear.demo.common.dto.ApiResponse;
import com.danbear.demo.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public CompletableFuture<ResponseEntity<ApiResponse<UserDto>>> createUser(
          @Valid @RequestBody UserDto userDto) {
    return userService.createUserAsync(userDto)
            .thenApply(user -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            user,
                            "/users",
                            HttpStatus.CREATED
                    ))
            );
  }

  @GetMapping("/{id}")
  public CompletableFuture<ResponseEntity<ApiResponse<UserDto>>> getUser(@PathVariable Long id) {
    return userService.getUserAsync(id)
            .thenApply(user -> ResponseEntity.ok(
                    ApiResponse.success(
                            user,
                            "/users/" + id,
                            HttpStatus.OK
                    )
            ));
  }

  @GetMapping
  public CompletableFuture<ResponseEntity<List<UserDto>>> getAllUsers() {
    return userService.getAllUsersAsync()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> {
//          log.error("Failed fetching users", ex);
          return ResponseEntity.internalServerError().build();
        });
  }

  // Paginated version
  @GetMapping("/paginated")
  public CompletableFuture<ResponseEntity<Page<UserDto>>> getAllUsersPaginated(
      @PageableDefault(size = 20) Pageable pageable) {
    return userService.getAllUsersAsync(pageable)
        .thenApply(ResponseEntity::ok);
  }

}