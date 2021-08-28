import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinesweeperBoardComponent } from './minesweeper-board.component';

describe('MinesweeperBoardComponent', () => {
  let component: MinesweeperBoardComponent;
  let fixture: ComponentFixture<MinesweeperBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MinesweeperBoardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MinesweeperBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
