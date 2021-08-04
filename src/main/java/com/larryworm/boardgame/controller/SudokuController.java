package com.larryworm.boardgame.controller;

import com.larryworm.boardgame.sudoku.Assignment;
import com.larryworm.boardgame.sudoku.Sudoku;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SudokuController {

    @GetMapping("/sudoku/{difficulty}")
    List<Integer> getNewBoard(@PathVariable Sudoku.Difficulty difficulty) {
        return Sudoku.generateBoard(difficulty);
    }

    @GetMapping("/sudoku/hints")
    Optional<List<Assignment>> getHintsFromCurrBoard(@RequestBody List<Integer> board) {
        return Sudoku.solveSudoku(board);
    }
}
