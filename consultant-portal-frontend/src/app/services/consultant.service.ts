import { Consultant } from '../models/consultant';
import { Headers, Http } from '@angular/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class ConsultantService {

  private baseUrl: string = '/api/consultants';

  constructor(private http: Http) { }

  getConsultants() {
    return this.http
      .get(this.baseUrl)
      .map(res => {
        return res.json();
      });
  }

  update(consultant: Consultant) {
    return this.http.put(this.baseUrl, consultant).map(res => res.json(), err => console.error(err));
  }

  delete(consultant: Consultant) {
    return this.http.delete(this.baseUrl + "/" + consultant.cn).map(res => res.json(), err => console.error(err));
  }
}
