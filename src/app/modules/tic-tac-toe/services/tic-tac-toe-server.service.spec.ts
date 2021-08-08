import { TestBed } from '@angular/core/testing';

import { TicTacToeServerService } from './tic-tac-toe-server.service';

describe('TicTacToeServerService', () => {
  let service: TicTacToeServerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicTacToeServerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
