import { TestBed } from '@angular/core/testing';

import { MinesweeperServerService } from './minesweeper-server.service';

describe('MinesweeperServerService', () => {
  let service: MinesweeperServerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MinesweeperServerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
