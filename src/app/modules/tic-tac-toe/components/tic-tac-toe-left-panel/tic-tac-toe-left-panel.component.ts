import { Component, OnInit } from '@angular/core';
import { GameModes } from '@modules/tic-tac-toe/enums/game-modes';
import { TicTacToeService } from '@modules/tic-tac-toe/services/tic-tac-toe.service';

@Component({
  selector: 'app-tic-tac-toe-left-panel',
  templateUrl: './tic-tac-toe-left-panel.component.html',
  styleUrls: ['./tic-tac-toe-left-panel.component.scss']
})
export class TicTacToeLeftPanelComponent implements OnInit {

  constructor(public boardService: TicTacToeService) { }

  ngOnInit(): void {
  }

  onClick(type: GameModes): void {
    this.boardService.startGame(type);
  }
}
