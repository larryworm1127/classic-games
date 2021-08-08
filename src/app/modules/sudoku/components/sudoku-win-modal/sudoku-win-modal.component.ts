import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SudokuService } from '@modules/sudoku/services/sudoku.service';
import { GameStates } from '@modules/sudoku/enums/game-states';

@Component({
  selector: 'app-sudoku-win-modal',
  templateUrl: './sudoku-win-modal.component.html',
  styleUrls: ['./sudoku-win-modal.component.css']
})
export class SudokuWinModalComponent implements OnInit {

  @ViewChild('content', { static: false }) private content: any;

  constructor(private modalService: NgbModal, private boardService: SudokuService) { }

  ngOnInit(): void {
    this.boardService.modalListener = (gameState: GameStates) => {
      if (gameState === GameStates.GameWon) {
        this.modalService.open(this.content);
      }
    }
  }
}
