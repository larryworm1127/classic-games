import { Component, Input, OnInit } from '@angular/core';
import { TicTacToeService } from '@modules/tic-tac-toe/services/tic-tac-toe.service';
import { Players } from '../../enums/players';
import { GameStates } from '../../enums/game-states';

@Component({
  selector: 'app-tic-tac-toe-square',
  templateUrl: './tic-tac-toe-square.component.html',
  styleUrls: ['./tic-tac-toe-square.component.css']
})
export class TicTacToeSquareComponent implements OnInit {

  @Input() square: any;

  constructor(public gameService: TicTacToeService) { }

  ngOnInit(): void {
  }

  makeMove(): void {
    if (
        this.gameService.currentState == GameStates.Running &&
        this.square.state === Players.Empty &&
        this.gameService.currentTurn != this.gameService.computer
    ) {
      this.square.state = this.gameService.currentTurn;
      this.gameService.makeMove(this.square);
    }
  }

}
