import { Component, OnInit } from '@angular/core';
import { SudokuService } from '@modules/sudoku/services/sudoku.service';
import { Difficulty } from '@modules/sudoku/enums/difficulty';

@Component({
  selector: 'app-sudoku-right-panel',
  templateUrl: './sudoku-right-panel.component.html',
  styleUrls: ['./sudoku-right-panel.component.css']
})
export class SudokuRightPanelComponent implements OnInit {

  constructor(public boardService: SudokuService) { }

  ngOnInit(): void {
  }

  selectDifficulty(difficulty: string): void {
    switch (difficulty) {
      case 'Easy':
        this.boardService.difficulty = Difficulty.Easy;
        break;
      case 'Medium':
        this.boardService.difficulty = Difficulty.Medium;
        break;
      case 'Hard':
        this.boardService.difficulty = Difficulty.Hard;
    }
  }
}
