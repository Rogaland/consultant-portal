import { Injectable } from '@angular/core';

@Injectable()
export class SpinnerService {

  private isSpinning: boolean;
  constructor() { }

  start() {
    this.isSpinning = true;
  }

  stop() {
    this.isSpinning = false;
  }

  status() {
    return this.isSpinning;
  }

}
