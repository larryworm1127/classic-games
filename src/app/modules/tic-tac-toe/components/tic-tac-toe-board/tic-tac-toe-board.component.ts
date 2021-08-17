import { Component, OnInit } from '@angular/core';
import { TicTacToeService } from '@modules/tic-tac-toe/services/tic-tac-toe.service';

@Component({
  selector: 'app-tic-tac-toe-board',
  templateUrl: './tic-tac-toe-board.component.html',
  styleUrls: ['./tic-tac-toe-board.component.scss']
})
export class TicTacToeBoardComponent implements OnInit {

  constructor(public boardService: TicTacToeService) { }

  ngOnInit(): void {
  }
}
