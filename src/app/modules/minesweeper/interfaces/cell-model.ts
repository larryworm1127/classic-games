import { CellContent } from "@modules/minesweeper/enums/cell-content";

export interface CellModel {
  type: string | number;
  y: number;
  x: number;
  i: number;
  label: CellContent | string;
  isOpened: boolean;
  isMine: boolean;
  isMineExploded: boolean;
  isCenterZero?: boolean;
  openedIdClassName?: string;
  isWrongFlag?: boolean;
}
