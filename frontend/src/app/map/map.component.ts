import { Component } from '@angular/core';
import { LatLng } from '@agm/core';
import { EncodedPolyline } from './EncodedPolyline';
import { MapService } from './map.service';
import {selector} from "rxjs/operator/publish";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: [ './map.component.css' ]
})
export class MapComponent {

  selectedLocation: LatLng;
  routes: Array<EncodedPolyline> = [];

  constructor(private mapService: MapService) { }

  addRandomRoute() {
    this.mapService.randomJourney().subscribe(route => {
      this.selectedLocation = EncodedPolyline.decode(route.points)[0];
      this.routes.push(route);
    });
  }
}
