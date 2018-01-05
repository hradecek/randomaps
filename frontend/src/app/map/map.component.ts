import { Component } from '@angular/core';
import { EncodedPolyline } from './EncodedPolyline';
import { MapService } from './map.service';
import {LatLng} from "./LatLng";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: [ './map.component.css' ]
})
export class MapComponent {

  focusedRoute: LatLng;
  routes: Array<Array<LatLng>> = [];

  constructor(private mapService: MapService) {
    this.focusedRoute = new LatLng(48.733333,  18.916667);
  }

  addRandomRoute() {
    this.mapService.randomJourney().subscribe(route => {
      const decoded = EncodedPolyline.decode(route.points);

      this.focusedRoute = decoded[0];
      this.routes.push(decoded);
    });
  }
}
