import { Consultant } from './models/consultant';
import { ActivatedRoute } from '@angular/router';
import { ConsultantService } from './serivces/consultant.service';
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
  private sub: any;
  constructor(private consultantService: ConsultantService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.consultantService.getConsultants().subscribe(result => {
      this.consultants = result;
      this.filterConsultants();
    });
    this.sub = this.route.queryParams.subscribe(params => {
      this.consultantState = params['type'];
      if (this.consultantState === undefined) {
        this.consultantState = 'confirmed';
      }
      this.filterConsultants();
    });
  }

  filterConsultants() {
    this.visibleConsultants = this.consultants[this.consultantState.toUpperCase()];
  }
}

