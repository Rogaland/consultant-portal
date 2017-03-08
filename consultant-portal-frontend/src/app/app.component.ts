import { IMyDayLabels } from 'mydatepicker/dist';
import { InvitationService } from './services/invitation.service';
import { ActivatedRoute } from '@angular/router';
import { ConsultantService } from './services/consultant.service';
import { Consultant } from './models/consultant';
import { ConsultantDto } from './models/consultantDto';
import { Component, OnInit } from '@angular/core';
import { IMyOptions } from "mydatepicker";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private consultants: ConsultantDto = new ConsultantDto();
  private visibleConsultants: Consultant[];

  private consultantState: string;
  private consultantNumber: number;

  private isMessageVisible: boolean;
  private message: string;
  private messageType: string;

  private expireDate: any;

  private selectedRow: number;

  private myDatePickerOptions: IMyOptions = {
    todayBtnTxt: 'I dag',
    dateFormat: 'dd.mm.yyyy',
  };

  private sub: any;
  constructor(private consultantService: ConsultantService,
    private route: ActivatedRoute,
    private invitationService: InvitationService) {
    this.findConsultants();

    let today = new Date();
    this.myDatePickerOptions.disableUntil = {
      year: today.getFullYear(),
      month: (today.getMonth() + 1),
      day: today.getDate()
    };

    this.myDatePickerOptions.disableSince = {
      year: today.getFullYear(),
      month: (today.getMonth() + 4),
      day: today.getDate()
    };
  }

  findConsultants() {
    this.consultantService.getConsultants().subscribe(result => {
      this.consultants = result;
      this.filterConsultants();
    });
  }

  ngOnInit(): void {
    this.sub = this.route.queryParams.subscribe(params => {
      this.consultantState = params['type'];
      if (this.consultantState === undefined) {
        this.consultantState = 'confirmed';
      }
      this.filterConsultants();
    });
  }

  showMessage(message: string, messageType: string): void {
    this.isMessageVisible = true;
    this.messageType = messageType;
    this.message = message;
    setTimeout(() => {
      this.isMessageVisible = false;
      this.messageType = '';
      this.message = '';
    }, 5000);
  }

  inviteConsultant(mobile: number) {
    this.consultantNumber = null;
    this.invitationService.inviteConsultant(mobile).subscribe(result => {
      this.showMessage(result.message, 'alert-success');
      this.findConsultants();
    }, err => {
      this.showMessage('Invitasjon ikke fullført. Allerede invitert?', 'alert-warning');
    });
  }

  filterConsultants() {
    this.visibleConsultants = this.consultants[this.consultantState.toUpperCase()];
  }

  confirm(consultant: Consultant) {
    this.consultantService.update(consultant).subscribe(result => {
      this.showMessage('Invitasjon godkjent.', 'alert-success');
      this.findConsultants();
    });
  }

  delete(consultant: Consultant) {
    this.consultantService.delete(consultant).subscribe(result => {
      this.showMessage(result.message, 'alert-success');
      this.findConsultants();
    }, err => {
      this.showMessage('Konsulenten ble ikke slettet!', 'alert-warning');
    });
  }

  rowSelected(idx: number) {
    this.selectedRow = idx;

    if (this.visibleConsultants[idx].expireDate) {
      let d = new Date(this.visibleConsultants[idx].expireDate);
      let a = {
        date: {
          year: d.getFullYear(),
          month: (d.getMonth() + 1),
          day: d.getDate()
        }
      };
      this.expireDate = a;
    } else {
      this.expireDate = null;
    }
  }

  saveExpireDate(idx: number) {
    let consultant = this.visibleConsultants[idx];
    consultant.setExpireDate(this.expireDate);
    this.consultantService.update(consultant).subscribe(res => this.showMessage('Oppdatert', 'alert-success'),
      err => {
        consultant.expireDate = null;
        this.showMessage('Feil ved lagring', 'alert-danger');
      });
  }

  refresh() {
    this.findConsultants();
  }
}
