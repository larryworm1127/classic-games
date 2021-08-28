import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MinesweeperRoutingModule } from "@modules/minesweeper/minesweeper-routing.module";
import { MinesweeperCellComponent } from './components/minesweeper-cell/minesweeper-cell.component';
import { MinesweeperBoardComponent } from './components/minesweeper-board/minesweeper-board.component';
import { MinesweeperComponent } from './pages/minesweeper.page';
import { FormsModule } from "@angular/forms";
import { StopWatchPipe } from './pipes/stop-watch.pipe';



@NgModule({
  declarations: [
    MinesweeperCellComponent,
    MinesweeperBoardComponent,
    MinesweeperComponent,
    StopWatchPipe
  ],
  imports: [
    CommonModule,
    MinesweeperRoutingModule,
    FormsModule
  ]
})
export class MinesweeperModule { }
