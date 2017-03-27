import { SpinnerService } from './spinner.service';
import { ConsultantDto } from '../models/consultantDto';
import { Consultant } from '../models/consultant';
import { Headers, Http } from '@angular/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class ConsultantService {

  private baseUrl: string = '/api/consultants';

  constructor(private http: Http, private spinnerService: SpinnerService) { }

  getConsultants() {
    this.spinnerService.start();
    return this.http
      .get(this.baseUrl)
      .map(res => {
        let a = Object.assign(new ConsultantDto(), res.json());
        if(a.CONFIRMED){
          a.CONFIRMED = a.CONFIRMED.map(c => Object.assign(new Consultant(), c));
        }
        if(a.PENDING){
          a.PENDING = a.PENDING.map(c => Object.assign(new Consultant(), c));
        }
        this.spinnerService.stop();
        return a;
      },
      err => {
        this.spinnerService.stop();
      });
  }

  update(consultant: Consultant) {
    this.spinnerService.start();
    return this.http.put(this.baseUrl, consultant).map(res => {
      this.spinnerService.stop();
      return res.json();
    }, err => {
      this.spinnerService.stop();
    });
  }

  progressState(consultant: Consultant) {
    this.spinnerService.start();
    return this.http.put(this.baseUrl + '/progressstate', consultant).map(res => {
      this.spinnerService.stop();
      return res.json();
    }, err => {
      this.spinnerService.stop();
    });
  }

  delete(consultant: Consultant) {
    this.spinnerService.start();
    return this.http.delete(this.baseUrl + "/" + consultant.cn).map(res => {
      this.spinnerService.stop();
      return res.json();
    }, err => {
      this.spinnerService.stop();
    });
  }
}
