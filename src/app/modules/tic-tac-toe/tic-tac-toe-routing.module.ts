import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { TicTacToeComponent } from './pages/tic-tac-toe.page';

const routes: Routes = [{ path: '', component: TicTacToeComponent }]


@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
    CommonModule
  ],
  exports: [RouterModule]
})
export class TicTacToeRoutingModule { }
