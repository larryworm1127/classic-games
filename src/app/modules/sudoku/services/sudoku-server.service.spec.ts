import { TestBed } from '@angular/core/testing';

import { SudokuServerService } from './sudoku-server.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('SudokuServerService', () => {
  let service: SudokuServerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(SudokuServerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
