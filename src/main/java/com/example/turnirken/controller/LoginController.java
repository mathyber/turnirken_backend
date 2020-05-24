package com.example.turnirken.controller;

import com.example.turnirken.dto.LoginUserModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api")
public class LoginController {

    @ApiOperation("login")
    @PostMapping("/login")
    public void fakeLogin(@RequestBody LoginUserModel loginUserModel) {
        throw new IllegalStateException("This method shouldn't be called. It implemented by Spring Security filters.");
    }
}
