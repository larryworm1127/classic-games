import { Injectable } from '@angular/core';
import { GameStates } from '../enums/game-states';
import { GameModes } from '../enums/game-modes';
import { Players } from '../enums/players';
import { Square, TicTacToeBoard } from '../logic/tic-tac-toe-board';
import { TicTacToeServerService } from "@modules/tic-tac-toe/services/tic-tac-toe-server.service";


@Injectable({
  providedIn: 'root'
})
export class TicTacToeService {

  private gameBoard: TicTacToeBoard;
  private gameType: GameModes = GameModes.Pvp;
  private computer: Players = Players.Empty;
  currentTurn: Players = Players.PlayerX;
  currentState: GameStates = GameStates.Stopped;
  message: string = 'Who starts?';

  constructor(private serverService: TicTacToeServerService) {
    this.gameBoard = new TicTacToeBoard();
  }

  startGame(type: GameModes): void {
    switch (this.currentState) {
      case GameStates.XWin:
      case GameStates.OWin:
      case GameStates.Draw:
        this.resetGame();
        break;
      case GameStates.Stopped:
        this.newGame(type);
        break;
    }
  }

  newGame(gameType: GameModes): void {
    switch (gameType) {
      case GameModes.ComputerStart:
        this.computer = Players.PlayerX;
        this.computerMove();  // Let computer make first move.
        break;
      case GameModes.HumanStart:
        this.computer = Players.PlayerO;
        break;
      case GameModes.Pvp:
        break;
    }
    this.gameType = gameType;
    this.currentState = GameStates.Running;
    this.currentTurn = Players.PlayerX;
    this.message = 'Player X turn!';
  }

  computerMove(): void {
    this.serverService.getNextMove(this.board, this.computer).subscribe(data => {
      let square = this.board[data.value0 * 3 + data.value1];
      if (this.currentState === GameStates.Running && square.state === Players.Empty &&
        this.currentTurn === this.computer) {
        square.state = this.computer;
        this.makeMove(square)
      }
    });
  }

  resetGame(): void {
    this.gameBoard = new TicTacToeBoard();
    this.message = 'Who starts?';
    this.currentState = GameStates.Stopped;
  }

  get board(): Square[] {
    return this.gameBoard.boardContent;
  }

  makeMove(square: Square): void {
    this.currentState = this.gameBoard.update(square);

    switch (this.currentState) {
      case GameStates.Draw:
        this.message = "It's a draw! Press any figure to restart.";
        break;
      case GameStates.OWin:
        this.message = "Player O wins! Press any figure to restart.";
        break;
      case GameStates.XWin:
        this.message = 'Player X wins! Press any figure to restart.';
        break;
      case GameStates.Running:
        if (this.currentTurn === Players.PlayerX) {
          this.currentTurn = Players.PlayerO;
          this.message = 'Player O turn!';
        } else {
          this.currentTurn = Players.PlayerX;
          this.message = 'Player X turn!';
        }

        if (this.currentTurn == this.computer && this.gameType != GameModes.Pvp) {
          this.computerMove();
        }
    }
  }
}
