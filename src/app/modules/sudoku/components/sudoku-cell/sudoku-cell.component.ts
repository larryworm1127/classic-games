import { Component, Input, OnInit } from '@angular/core';
import { SudokuService } from '@modules/sudoku/services/sudoku.service';
import { SudokuCell } from '@modules/sudoku/logic/sudoku-cell';

@Component({
  selector: 'app-sudoku-cell',
  templateUrl: './sudoku-cell.component.html',
  styleUrls: ['./sudoku-cell.component.css']
})
export class SudokuCellComponent implements OnInit {

  // @ts-ignore
  @Input() cell: SudokuCell;

  constructor(public boardService: SudokuService) { }

  ngOnInit(): void {
  }

  onSelect(): void {
    this.boardService.selectedNewCell(this.cell);
  }
}
