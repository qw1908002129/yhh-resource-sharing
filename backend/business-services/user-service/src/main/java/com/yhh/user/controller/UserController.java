package com.yhh.user.controller;

import com.yhh.common.result.Result;
import com.yhh.user.dto.UserLoginDTO;
import com.yhh.user.dto.UserRegisterDTO;
import com.yhh.user.entity.User;
import com.yhh.user.service.UserService;
import com.yhh.user.utils.JwtUtils;
import com.yhh.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        UserVO userVO = userService.register(registerDTO);
        return Result.success(userVO);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        return Result.success(result);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info/{id}")
    public Result<UserVO> getUserInfo(@PathVariable Long id) {
        UserVO userVO = userService.getUserVOById(id);
        return Result.success(userVO);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/info/{id}")
    public Result<UserVO> updateUserInfo(@PathVariable Long id, @RequestBody User user) {
        UserVO userVO = userService.updateUserInfo(id, user);
        return Result.success(userVO);
    }

    @Operation(summary = "删除用户（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("用户删除成功");
    }

    @Operation(summary = "批量删除用户（逻辑删除）")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteUsers(@RequestBody Long[] ids) {
        userService.batchDeleteUsers(ids);
        return Result.success("批量删除用户成功");
    }

    @Operation(summary = "永久删除用户（物理删除）")
    @DeleteMapping("/permanent/{id}")
    public Result<String> permanentlyDeleteUser(@PathVariable Long id) {
        userService.permanentlyDeleteUser(id);
        return Result.success("用户永久删除成功");
    }

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    public Result<java.util.List<UserVO>> getUserList() {
        java.util.List<User> users = userService.list();
        java.util.List<UserVO> userVOList = users.stream()
                .map(user -> {
                    UserVO userVO = new UserVO();
                    org.springframework.beans.BeanUtils.copyProperties(user, userVO);
                    return userVO;
                })
                .collect(java.util.stream.Collectors.toList());
        return Result.success(userVOList);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<UserVO> getCurrentUserInfo() {
        // 从JWT中获取用户ID
        Long userId = JwtUtils.getCurrentUserId();
        UserVO userVO = userService.getUserVOById(userId);
        return Result.success(userVO);
    }

    // TODO: 这是一个临时接口，用于测试密码加密，测试完成后请删除
    @Operation(summary = "测试密码加密")
    @GetMapping("/test-password/{password}")
    public Result<String> testPasswordEncoding(@PathVariable String password) {
        return Result.success(userService.encodePassword(password));
    }
}
