package com.lab11opt.lab11.repository;

import com.lab11opt.lab11.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game,Integer> {
    Game findByToken(String token);
    Game findByFirstPlayer(String firstPlayer);
    Game findBySecondPlayer(String secondPlayer);
}
