import { TestBed } from '@angular/core/testing';

import { TicTacToeServerService } from './tic-tac-toe-server.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";

describe('TicTacToeServerService', () => {
  let service: TicTacToeServerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(TicTacToeServerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
