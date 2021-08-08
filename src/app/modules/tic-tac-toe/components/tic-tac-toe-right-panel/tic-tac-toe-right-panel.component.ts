import { Component, OnInit } from '@angular/core';
import { TicTacToeService } from '@modules/tic-tac-toe/services/tic-tac-toe.service';

@Component({
  selector: 'app-tic-tac-toe-right-panel',
  templateUrl: './tic-tac-toe-right-panel.component.html',
  styleUrls: ['./tic-tac-toe-right-panel.component.css']
})
export class TicTacToeRightPanelComponent implements OnInit {

  constructor(public boardService: TicTacToeService) { }

  ngOnInit(): void {
  }

  onSurrender(): void {
    this.boardService.resetGame();
  }
}
