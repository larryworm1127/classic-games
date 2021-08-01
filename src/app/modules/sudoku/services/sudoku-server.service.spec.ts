import { TestBed } from '@angular/core/testing';

import { SudokuServerService } from './sudoku-server.service';

describe('SudokuServerService', () => {
  let service: SudokuServerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SudokuServerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
