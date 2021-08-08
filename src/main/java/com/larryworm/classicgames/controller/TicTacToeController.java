package com.larryworm.classicgames.controller;

import com.larryworm.classicgames.gamelogic.TicTacToe;
import com.larryworm.classicgames.gamelogic.TicTacToeState;
import org.javatuples.Pair;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TicTacToeController {

    @GetMapping("/ttt/move")
    Pair<Integer, Integer> getMove(@RequestParam List<TicTacToeState> board, @RequestParam TicTacToeState computer) {
        return TicTacToe.getMove(board, computer);
    }
}
