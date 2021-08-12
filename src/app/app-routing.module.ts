import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('@modules/home/home.module').then((m) => m.HomeModule)
  },
  {
    path: 'ttt',
    loadChildren: () => import('@modules/tic-tac-toe/tic-tac-toe.module').then((m) => m.TicTacToeModule)
  },
  {
    path: 'sudoku',
    loadChildren: () => import('@modules/sudoku/sudoku.module').then((m) => m.SudokuModule)
  },
  {
    path: 'minesweeper',
    loadChildren: () => import('@modules/minesweeper/minesweeper.module').then((m) => m.MinesweeperModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
