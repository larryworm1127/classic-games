import { TestBed } from '@angular/core/testing';

import { SudokuService } from './sudoku.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('SudokuServiceService', () => {
  let service: SudokuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(SudokuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
