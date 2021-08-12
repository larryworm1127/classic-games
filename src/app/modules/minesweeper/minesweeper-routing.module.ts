import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { SudokuComponent } from "@modules/sudoku/pages/sudoku.page";

const routes: Routes = [{ path: '' }]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
    CommonModule
  ],
  exports: [RouterModule]
})
export class MinesweeperRoutingModule { }
