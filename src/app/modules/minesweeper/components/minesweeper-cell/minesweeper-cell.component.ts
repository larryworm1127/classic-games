import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { Subscription } from "rxjs";
import { CellModel } from "@modules/minesweeper/interfaces/cell-model";
import { MinesweeperService } from "@modules/minesweeper/services/minesweeper.service";
import { CellContent } from "@modules/minesweeper/enums/cell-content";
import { GameStates } from "@modules/minesweeper/enums/game-states";
import { filter } from "rxjs/operators";
import { Resources } from "@modules/minesweeper/enums/resources";

@Component({
  selector: 'app-minesweeper-cell',
  templateUrl: './minesweeper-cell.component.html',
  styleUrls: ['./minesweeper-cell.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MinesweeperCellComponent implements OnChanges {

  constructor(private gameService: MinesweeperService) {
  }

  @Input() cell: CellModel | any;
  @Output() open = new EventEmitter<number[]>();

  private gameStatus$: Subscription | undefined;

  ngOnChanges(changes: SimpleChanges): void {
    if (this.gameStatus$) {
      this.gameStatus$.unsubscribe();
    }
    if (changes && changes.cell && !changes.cell.isFirstChange() && this.cell.type === CellContent.Mine) {
      this.gameStatusSubscription();
    }
  }

  onClick() {
    if (this.isUnavailableToOpen()) {
      return;
    } else {
      this.open.emit([this.cell.y, this.cell.x]);
    }
  }

  onContextMenu(event: Event | any) {
    event.preventDefault();
    this.insertFlag(event);
    return true;
  }

  private insertFlag(event?: Event): void {
    if (event) {
      event.preventDefault();
    }

    if (this.isUnavailableToFlag()) {
      return;
    }

    if (this.cell.label === CellContent.Flag) {
      this.cell.label = '';
      this.gameService.flagsAvailable += 1;
      if (this.cell.type !== CellContent.Mine && this.gameStatus$) {
        this.gameStatus$.unsubscribe();
      }
    } else if (this.gameService.flagsAvailable > 0) {
      this.cell.label = CellContent.Flag;
      this.gameService.flagsAvailable -= 1;
      if (this.cell.type !== CellContent.Mine) {
        this.gameStatusSubscription();
      }
    }

    this.gameService.emojiFace = Resources.GrinningFace;
  }

  private isUnavailableToFlag(): boolean {
    return this.gameService.gameStateValue === GameStates.Won
        || this.gameService.gameStateValue === GameStates.Lost
        || this.gameService.isFirstCellClick
        || this.cell.isOpened;
  }

  private gameStatusSubscription(): void {
    this.gameStatus$ = this.gameService.gameState$
        .pipe(filter(status => status === GameStates.Lost || status === GameStates.Won))
        .subscribe((status: GameStates) => {
          if (status === GameStates.Lost) {
            if (this.cell.label === CellContent.Flag) {
              if (this.cell.type !== CellContent.Mine) {
                this.cell.isWrongFlag = true;
              }
            } else {
              this.cell.isOpened = true;
              this.cell.isMine = true;
              this.cell.label = this.cell.type.toString();
            }
          } else {
            this.cell.label = CellContent.Flag;
          }

          if (this.gameStatus$) {
            this.gameStatus$.unsubscribe();
          }
        });
  }

  private isUnavailableToOpen(): boolean {
    return this.cell.label === CellContent.Flag ||
        this.gameService.gameStateValue === GameStates.Lost ||
        this.gameService.gameStateValue === GameStates.Won;
  }
}
