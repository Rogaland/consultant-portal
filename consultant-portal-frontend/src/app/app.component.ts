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

  private showError: boolean;
  errorMessage: string;

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

  inviteConsultant(mobile: number){
    this.consultantNumber = null;
    this.invitationService.inviteConsultant(mobile).subscribe(result => {
      this.findConsultants();
    }, err => {
      this.showError = true;
      this.errorMessage = "Invitasjon ikke fullført. Allerede invitert?";
      setTimeout(() => {
        this.showError = false;
       }, 5000);
     });
  }

  filterConsultants() {
    this.visibleConsultants = this.consultants[this.consultantState.toUpperCase()];
  }

  confirm(consultant: Consultant) {
    console.log(consultant);
    this.consultantService.update(consultant).subscribe(r => this.findConsultants());
  }
}

