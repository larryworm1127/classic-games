package com.larryworm.classicgames.controller;

import com.larryworm.classicgames.csp.Assignment;
import com.larryworm.classicgames.csp.variables.SudokuVariable;
import com.larryworm.classicgames.gamelogic.Sudoku;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin()
public class SudokuController {

    @GetMapping("/sudoku/{difficulty}")
    List<Integer> getNewBoard(@PathVariable Sudoku.Difficulty difficulty) {
        return Sudoku.generateBoard(difficulty);
    }

    @GetMapping("/sudoku/hints")
    List<Assignment<Integer>> getHintsFromCurrBoard(@RequestParam List<Integer> board) {
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < Sudoku.DIM * Sudoku.DIM; i++) {
            if (board.get(i) == 0) {
                emptyCells.add(i);
            }
        }

        var result = Sudoku.solveSudoku(board);
        return result.stream().filter(assignment -> {
            SudokuVariable var = (SudokuVariable) assignment.variable();
            return emptyCells.contains(var.getRow() * Sudoku.DIM + var.getCol());
        }).toList();
    }
}
