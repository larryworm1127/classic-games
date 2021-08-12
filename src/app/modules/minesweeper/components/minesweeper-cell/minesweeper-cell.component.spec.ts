import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinesweeperCellComponent } from './minesweeper-cell.component';

describe('MinesweeperCellComponent', () => {
  let component: MinesweeperCellComponent;
  let fixture: ComponentFixture<MinesweeperCellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MinesweeperCellComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MinesweeperCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
