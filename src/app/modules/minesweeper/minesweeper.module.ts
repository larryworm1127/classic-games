import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MinesweeperRoutingModule } from "@modules/minesweeper/minesweeper-routing.module";
import { MinesweeperCellComponent } from './components/minesweeper-cell/minesweeper-cell.component';
import { MinesweeperComponent } from './components/minesweeper/minesweeper.component';



@NgModule({
  declarations: [
    MinesweeperCellComponent,
    MinesweeperComponent
  ],
  imports: [
    CommonModule,
    MinesweeperRoutingModule
  ]
})
export class MinesweeperModule { }
