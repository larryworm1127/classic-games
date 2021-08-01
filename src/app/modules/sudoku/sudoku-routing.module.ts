import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SudokuComponent } from '@modules/sudoku/pages/sudoku.page';

const routes: Routes = [{ path: '', component: SudokuComponent }]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
    CommonModule
  ],
  exports: [RouterModule]
})
export class SudokuRoutingModule { }
