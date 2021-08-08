import { TicTacToeBoard } from './tic-tac-toe-board';
import { Players } from '@modules/tic-tac-toe/enums/players';
import { GameStates } from '@modules/tic-tac-toe/enums/game-states';

describe('TicTacToeBoard', () => {
  it('should create an instance', () => {
    expect(new TicTacToeBoard()).toBeTruthy();
  });
});


describe('TicTacToeBoard.checkWin()', () => {
  /**
   * Detect win for following boards:
   *
   *   x x x | x o   | x o
   *     o   | x   o | o x
   *   o     | x   o |   o x
   */
  it('should detect win for player X.', () => {
    let boardInstance = new TicTacToeBoard()

    let boards = [
      [
        Players.PlayerX, Players.PlayerX, Players.PlayerX,
        Players.Empty, Players.PlayerO, Players.Empty,
        Players.PlayerO, Players.Empty, Players.Empty
      ],
      [
        Players.PlayerX, Players.PlayerO, Players.Empty,
        Players.PlayerX, Players.Empty, Players.PlayerO,
        Players.PlayerX, Players.Empty, Players.PlayerO
      ],
      [
        Players.PlayerX, Players.PlayerO, Players.Empty,
        Players.PlayerO, Players.PlayerX, Players.Empty,
        Players.Empty, Players.PlayerO, Players.PlayerX
      ]
    ]

    for (let board of boards) {
      boardInstance.boardContent = board.map((item, index) => ({ 'id': index, 'state': item }))
      expect(boardInstance.checkWin()).toEqual(GameStates.XWin);
    }
  });

  /**
   * Detect win for following boards:
   *
   *   o o o | o x   | o x
   *     x   | o   x | x o
   *   x     | o   x |   x o
   */
  it('should detect win for player O.', () => {
    let boardInstance = new TicTacToeBoard()

    let boards = [
      [
        Players.PlayerO, Players.PlayerO, Players.PlayerO,
        Players.Empty, Players.PlayerX, Players.Empty,
        Players.PlayerX, Players.Empty, Players.Empty
      ],
      [
        Players.PlayerO, Players.PlayerX, Players.Empty,
        Players.PlayerO, Players.Empty, Players.PlayerX,
        Players.PlayerO, Players.Empty, Players.PlayerX
      ],
      [
        Players.PlayerO, Players.PlayerX, Players.Empty,
        Players.PlayerX, Players.PlayerO, Players.Empty,
        Players.Empty, Players.PlayerX, Players.PlayerO
      ]
    ]

    for (let board of boards) {
      boardInstance.boardContent = board.map((item, index) => ({ 'id': index, 'state': item }))
      expect(boardInstance.checkWin()).toEqual(GameStates.OWin);
    }
  });

  /**
   * Detect draw for following boards:
   *
   *   x o x | x o x
   *   o o x | x o x
   *   x x o | o x o
   */
  it('should detect draw.', () => {
    let boardInstance = new TicTacToeBoard()

    let boards = [
      [
        Players.PlayerX, Players.PlayerO, Players.PlayerX,
        Players.PlayerO, Players.PlayerO, Players.PlayerX,
        Players.PlayerX, Players.PlayerX, Players.PlayerO
      ],
      [
        Players.PlayerX, Players.PlayerO, Players.PlayerX,
        Players.PlayerX, Players.PlayerO, Players.PlayerX,
        Players.PlayerO, Players.PlayerX, Players.PlayerO
      ]
    ]

    for (let board of boards) {
      boardInstance.boardContent = board.map((item, index) => ({ 'id': index, 'state': item }))
      expect(boardInstance.checkWin()).toEqual(GameStates.Draw);
    }
  });

  /**
   * Detect still playing for following boards:
   *
   *   x o   | x o x
   *   o   x | o x
   *   o     | o   o
   */
  it('should detect game is still playing.', () => {
    let boardInstance = new TicTacToeBoard()

    let boards = [
      [
        Players.PlayerX, Players.PlayerO, Players.Empty,
        Players.PlayerO, Players.Empty, Players.PlayerX,
        Players.PlayerO, Players.Empty, Players.Empty
      ],
      [
        Players.PlayerX, Players.PlayerO, Players.PlayerX,
        Players.PlayerO, Players.PlayerX, Players.Empty,
        Players.PlayerO, Players.Empty, Players.PlayerO
      ]
    ]

    for (let board of boards) {
      boardInstance.boardContent = board.map((item, index) => ({ 'id': index, 'state': item }))
      expect(boardInstance.checkWin()).toEqual(GameStates.Running);
    }
  });
})
