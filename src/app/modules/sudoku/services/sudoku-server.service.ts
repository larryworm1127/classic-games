import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Difficulty } from "@modules/sudoku/enums/difficulty";

@Injectable({
  providedIn: 'root'
})
export class SudokuServerService {

  private readonly sudokuUrl: string;

  constructor(private http: HttpClient) {
    this.sudokuUrl = 'http://localhost:8080/sudoku';
  }

  public getNewBoard(difficulty: Difficulty): Observable<number[]> {
    return this.http.get<number[]>(`${this.sudokuUrl}/${difficulty.toUpperCase()}`);
  }
}
