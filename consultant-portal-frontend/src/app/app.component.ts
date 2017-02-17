import { InvitationService } from './services/invitation.service';
import { Consultant } from './models/consultant';
import { ActivatedRoute } from '@angular/router';
import { ConsultantService } from './services/consultant.service';
import { ConsultantDto } from './models/consultantDto';
import { Component, OnInit } from '@angular/core';

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

  private sub: any;
  constructor(private consultantService: ConsultantService,
    private route: ActivatedRoute,
    private invitationService: InvitationService) {
      this.findConsultants();
  }

  findConsultants(){
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

  showMessage(message: string, messageType: string):void {
    this.isMessageVisible = true;
    this.messageType = messageType;
    this.message = message;
    setTimeout(() => {
      this.isMessageVisible = false;
      this.messageType = '';
      this.message = '';
    }, 5000);
  }

  inviteConsultant(mobile: number){
    this.consultantNumber = null;
    this.invitationService.inviteConsultant(mobile).subscribe(result => {
      this.showMessage('Invitasjon sendt.', 'alert-success');
      this.findConsultants();
    }, err => {
      this.showMessage('Invitasjon ikke fullført. Allerede invitert?', 'alert-warning');
     });
  }

  filterConsultants() {
    this.visibleConsultants = this.consultants[this.consultantState.toUpperCase()];
  }

  confirm(consultant: Consultant) {
    this.consultantService.update(consultant).subscribe(r => {
      this.showMessage('Invitasjon godkjent.', 'alert-success');
      this.findConsultants();
    });
  }
}

