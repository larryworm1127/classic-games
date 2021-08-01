package com.larryworm.boardgame.controller;

import com.larryworm.boardgame.Sudoku;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SudokuController {

    @GetMapping("/sudoku/{difficulty}")
    List<Integer> getNewBoard(@PathVariable Sudoku.Difficulty difficulty) {
        return Sudoku.generateBoard(difficulty);
    }
}
