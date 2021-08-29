import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MinesweeperServerService {

  private readonly minesweeperUrl: string = 'minesweeper';

  constructor(private http: HttpClient) {
  }

  public getBoard(width: number, height: number, numMines: number, firstMove: number[]): Observable<number[]> {
    let url = `${this.minesweeperUrl}/board`;
    let params = new HttpParams()
        .append('width', width)
        .append('height', height)
        .append('numMines', numMines)
        .append('firstMove', firstMove.toString());

    return this.http.get<number[]>(url, { params: params })
  }
}
