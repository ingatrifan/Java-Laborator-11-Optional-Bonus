package com.lab11opt.lab11.controllers;

import com.lab11opt.lab11.models.Game;
import com.lab11opt.lab11.models.User;
import com.lab11opt.lab11.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@Controller
@RequestMapping(value = "/game")
public class GameController {
    @Autowired
    private GameRepository gameRepository;
    BufferedReader reader;
    PrintWriter writer;
    GameController(){
        try  {
            InetAddress host = InetAddress.getLocalHost();
            Socket socket = new Socket(host.getHostName(), 6868);
            System.out.println(socket.getInetAddress());
            OutputStream output = socket.getOutputStream();
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/create")
    public @ResponseBody
    Game createGame(){
        writer.println("create game");
        try {
            String response = reader.readLine();
            Game game = new Game();
            game.setFirstPlayer(getAuthenticatedUsername());
            game.setToken(response);
            gameRepository.save(game);
            return game;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/join")
    public @ResponseBody String
    joinGame(@RequestBody String token){
        writer.println("join game " +token);
        try {
            String response = reader.readLine();
            if (response.contains("Successfully entered the game")) {
                Game game = gameRepository.findByToken(token);
                game.setSecondPlayer(getAuthenticatedUsername());
                gameRepository.save(game);
                return response;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Somthing bad happend";
    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(path = "/make-move")
    public @ResponseBody String
    makeMoveInGame(@RequestBody String positions){
        writer.println("make move " +positions);
        try {
            String response = reader.readLine();
            if (response.contains("Done")) {
                Game game = gameRepository.findByFirstPlayer(getAuthenticatedUsername());
                if (game == null )game = gameRepository.findBySecondPlayer(getAuthenticatedUsername());
                String content = game.getContent();
                content+= getAuthenticatedUsername() + "make move " + positions + "\n";
                game.setContent(content);
                gameRepository.save(game);
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Somthing bad happend";
    }

    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
