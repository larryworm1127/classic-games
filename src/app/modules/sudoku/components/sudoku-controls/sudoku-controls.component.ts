import { Component, OnInit } from '@angular/core';
import { SudokuService } from '@modules/sudoku/services/sudoku.service';

@Component({
  selector: 'app-sudoku-controls',
  templateUrl: './sudoku-controls.component.html',
  styleUrls: ['./sudoku-controls.component.css']
})
export class SudokuControlsComponent implements OnInit {

  constructor(public boardService: SudokuService) { }

  ngOnInit(): void {
  }

  togglePencil(): void {
    this.boardService.pencilEnabled = (!this.boardService.pencilEnabled);
  }
}
