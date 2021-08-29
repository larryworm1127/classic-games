import { Component, OnInit } from '@angular/core';
import { SudokuService } from '@modules/sudoku/services/sudoku.service';

@Component({
  selector: 'app-sudoku-board',
  templateUrl: './sudoku-board.component.html',
  styleUrls: ['./sudoku-board.component.scss']
})
export class SudokuBoardComponent implements OnInit {

  constructor(public boardService: SudokuService) { }

  ngOnInit(): void {
  }

}
