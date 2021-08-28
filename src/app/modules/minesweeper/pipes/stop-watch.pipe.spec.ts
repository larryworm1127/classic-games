import { StopWatchPipe } from './stop-watch.pipe';

describe('StopWatchPipe', () => {
  let stopwatchPipe: StopWatchPipe;

  beforeEach(() => {
    stopwatchPipe = new StopWatchPipe();
  });

  it('should transform value correctly', () => {
    const value1 = stopwatchPipe.transform(3, '0');
    const value2 = stopwatchPipe.transform(9, '00');
    const value3 = stopwatchPipe.transform(89, '000');
    const value4 = stopwatchPipe.transform(892, '00');

    expect(value1).toEqual('3');
    expect(value2).toEqual('09');
    expect(value3).toEqual('089');
    expect(value4).toEqual('892');
  });
});
