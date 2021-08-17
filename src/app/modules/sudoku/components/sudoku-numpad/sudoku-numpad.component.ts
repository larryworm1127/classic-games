import { Component, OnInit } from '@angular/core';
import { SudokuService } from '@modules/sudoku/services/sudoku.service';

@Component({
  selector: 'app-sudoku-numpad',
  templateUrl: './sudoku-numpad.component.html',
  styleUrls: ['./sudoku-numpad.component.scss']
})
export class SudokuNumpadComponent implements OnInit {

  numbers: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9];

  constructor(public boardService: SudokuService) { }

  ngOnInit(): void {
  }

  onClick(value: number): void {
    this.boardService.onNumpadClick(value);
  }
}
