package br.com.lutfy.product_stock.controller;


import br.com.lutfy.product_stock.domain.user.AuthenticationData;
import br.com.lutfy.product_stock.domain.user.User;
import br.com.lutfy.product_stock.infra.security.JwtTokenData;
import br.com.lutfy.product_stock.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationData data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.tokenGenerate((User)authentication.getPrincipal());
        return ResponseEntity.ok(new JwtTokenData(tokenJWT));
    }
}
