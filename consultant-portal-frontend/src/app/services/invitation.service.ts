import { Headers, Http } from '@angular/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';


@Injectable()
export class InvitationService {

  private baseUrl: string = '/api/invited/consultants';

  constructor(private http: Http) {}

  inviteConsultant(mobile: number) {
      return this.http
        .post(this.baseUrl, {mobile: mobile})
        .map(res => {
          return res.json();
        });
    }
}
