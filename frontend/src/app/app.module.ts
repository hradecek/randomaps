import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AgmCoreModule } from '@agm/core';

import {MatButtonModule, MatIconModule, MatMenuModule, MatSidenavModule, MatToolbarModule} from '@angular/material';

import { AppComponent } from './app.component';
import { MapComponent } from './map/map.component';
import { EventBusService } from '../shared/event-bus.service';
import { MapService } from './map/map.service';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule,
    MatToolbarModule,
    HttpClientModule,
    MatSidenavModule,
    AgmCoreModule.forRoot()
  ],
  providers: [ MapService, EventBusService ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
