package com.larryworm.classicgames.controller;

import com.larryworm.classicgames.gamelogic.Minesweeper;
import org.javatuples.Pair;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin()
public class MinesweeperController {

    @GetMapping("/minesweeper/board")
    List<Integer> getNewBoard(@RequestParam int width, @RequestParam int height, @RequestParam int numMines,
                              @RequestParam List<Integer> firstMove) {
        return Minesweeper.generateBoard(width, height, numMines, Pair.fromCollection(firstMove));
    }
}
