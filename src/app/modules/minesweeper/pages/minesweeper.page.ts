import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-minesweeper',
  templateUrl: './minesweeper.page.html',
  styleUrls: ['./minesweeper.page.scss'],
})
export class MinesweeperComponent implements OnInit {

  gameName: string = "Minesweeper";

  constructor() {
  }

  ngOnInit(): void {
  }
}
