import { TestBed } from '@angular/core/testing';

import { MinesweeperService } from './minesweeper.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('MinesweeperService', () => {
  let service: MinesweeperService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(MinesweeperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
