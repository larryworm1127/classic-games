import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicTacToeBoardComponent } from './components/tic-tac-toe-board/tic-tac-toe-board.component';
import { TicTacToeSquareComponent } from './components/tic-tac-toe-square/tic-tac-toe-square.component';
import { TicTacToeLeftPanelComponent } from './components/tic-tac-toe-left-panel/tic-tac-toe-left-panel.component';
import { TicTacToeRightPanelComponent } from './components/tic-tac-toe-right-panel/tic-tac-toe-right-panel.component';
import { TicTacToeRoutingModule } from './tic-tac-toe-routing.module';
import { TicTacToeComponent } from './pages/tic-tac-toe.page';


@NgModule({
  declarations: [
    TicTacToeBoardComponent,
    TicTacToeSquareComponent,
    TicTacToeLeftPanelComponent,
    TicTacToeRightPanelComponent,
    TicTacToeComponent,
  ],
  imports: [
    CommonModule,
    TicTacToeRoutingModule
  ],
  exports: [
    TicTacToeComponent
  ]
})
export class TicTacToeModule { }
