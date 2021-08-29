import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sudoku',
  templateUrl: './sudoku.page.html',
  styleUrls: ['./sudoku.page.scss']
})
export class SudokuComponent implements OnInit {

  gameName: string = "Sudoku";

  constructor() { }

  ngOnInit(): void {
  }

}
