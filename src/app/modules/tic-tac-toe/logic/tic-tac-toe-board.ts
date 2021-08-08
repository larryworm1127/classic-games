import { Players } from '../enums/players';
import { GameStates } from '../enums/game-states';


export type Square = {
  id: number;
  state: Players;
};


export class TicTacToeBoard {

  private board: Square[];
  private boardDim: number = 9;

  constructor() {
    this.board = this.createBoard();
  }

  get boardContent() {
    return this.board;
  }

  set boardContent(value: Square[]) {
    this.board = [...value];
  }

  private createBoard(): Square[] {
    let board = [];
    for (let i = 0; i < this.boardDim; i++) {
      board.push({ id: i, state: Players.Empty });
    }
    return board;
  }

  getEmptySquares(): Square[] {
    return this.board.filter(square => square.state === Players.Empty);
  }

  update(square: Square): GameStates {
    this.board[square.id].state = square.state;
    return this.checkWin();
  }

  checkWin(): GameStates {
    // Initialize lines with diagonals
    let lines = [
      [this.board[0], this.board[4], this.board[8]],
      [this.board[2], this.board[4], this.board[6]],
    ];

    // Add rows and columns
    for (let i = 0; i < 3; i++) {
      // row
      lines.push([
        this.board[i * 3],
        this.board[i * 3 + 1],
        this.board[i * 3 + 2],
      ])

      // column
      lines.push([
        this.board[i],
        this.board[i + 3],
        this.board[i + 6],
      ])
    }

    // Check all lines
    for (let line of lines) {
      if (line.every((v, i, arr) => v.state === arr[0].state && v.state !== Players.Empty)) {
        return (line[0].state === Players.PlayerX) ? GameStates.XWin : GameStates.OWin;
      }
    }

    // No winner, check for draw
    if (this.getEmptySquares().length == 0) {
      return GameStates.Draw;
    }
    return GameStates.Running;
  }
}
