import { TestBed } from '@angular/core/testing';

import { MinesweeperServerService } from './minesweeper-server.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('MinesweeperServerService', () => {
  let service: MinesweeperServerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(MinesweeperServerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
