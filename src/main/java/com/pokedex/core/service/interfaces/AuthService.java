package com.pokedex.core.service.interfaces;

public interface AuthService {

    String register(String username, String email, String password);

    String login(String email, String password);
}
