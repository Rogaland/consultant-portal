import { SpinnerService } from './services/spinner.service';
import { MyDatePickerModule } from 'mydatepicker';
import { InvitationService } from './services/invitation.service';
import { ConsultantService } from './services/consultant.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    MyDatePickerModule
  ],
  providers: [ConsultantService, InvitationService, SpinnerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
