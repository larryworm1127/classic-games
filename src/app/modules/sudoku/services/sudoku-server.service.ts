import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Difficulty } from "@modules/sudoku/enums/difficulty";
import { SudokuCell } from "@modules/sudoku/logic/sudoku-cell";

@Injectable({
  providedIn: 'root'
})
export class SudokuServerService {

  private readonly sudokuUrl: string;

  constructor(private http: HttpClient) {
    this.sudokuUrl = 'sudoku';
  }

  public getNewBoard(difficulty: Difficulty): Observable<number[]> {
    let url = `${this.sudokuUrl}/${difficulty.toUpperCase()}`;
    return this.http.get<number[]>(url);
  }

  public getHints(board: SudokuCell[]): Observable<any[]> {
    let boardCleaned = board.map(cell => (cell.isPencil || cell.value === null) ? 0 : cell.value);
    let url = `${this.sudokuUrl}/hints`;
    return this.http.get<any[]>(url, { params: new HttpParams().set('board', boardCleaned.join(',')) })
  }
}
