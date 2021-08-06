package com.larryworm.boardgame.controller;

import com.larryworm.boardgame.sudoku.Assignment;
import com.larryworm.boardgame.sudoku.Sudoku;
import com.larryworm.boardgame.sudoku.SudokuVariable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    Optional<List<Assignment<Integer>>> getHintsFromCurrBoard(@RequestParam List<Integer> board) {
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < Sudoku.DIM * Sudoku.DIM; i++) {
            if (board.get(i) == 0) {
                emptyCells.add(i);
            }
        }

        var result = Sudoku.solveSudoku(board);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
            result.get().stream().filter(assignment -> {
                SudokuVariable var = (SudokuVariable) assignment.variable();
                return emptyCells.contains(var.getRow() * Sudoku.DIM + var.getCol());
            }).toList()
        );
    }
}
