import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Square } from "@modules/tic-tac-toe/logic/tic-tac-toe-board";
import { Players } from "@modules/tic-tac-toe/enums/players";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TicTacToeServerService {

  private readonly ticTacToeUrl: string;

  constructor(private http: HttpClient) {
    this.ticTacToeUrl = 'ttt';
  }

  public getNextMove(board: Square[], player: Players): Observable<any> {
    let boardCleaned = board.map(square => TicTacToeServerService.getState(square.state));
    let url = `${this.ticTacToeUrl}/move`;
    let params = new HttpParams()
      .append('board', boardCleaned.join(','))
      .append('computer', TicTacToeServerService.getState(player));

    return this.http.get<any>(url, { params: params })
  }

  private static getState(state: Players): string {
    switch (state) {
      case Players.PlayerO: return 'PLAYERO'
      case Players.PlayerX: return 'PLAYERX'
      case Players.Empty: return 'EMPTY'
    }
  }
}
