import { Component, Input } from '@angular/core';
import { EncodedPolyline } from './EncodedPolyline';
import { MapService } from './map.service';
import { LatLng } from './LatLng';
import { Route } from './Route';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: [ './map.component.css' ]
})
export class MapComponent {

  focusedRoute: LatLng;
  @Input() routes: Array<Route> = [];

  constructor(private mapService: MapService) {
    this.focusedRoute = new LatLng(48.733333,  18.916667);
  }

  addRandomRoute() {
    this.mapService.randomJourney().subscribe(encoded => {
      const decoded = EncodedPolyline.decode(encoded.points);
      const route = new Route(decoded);

      this.focusedRoute = route.center;
      this.routes.push(route);
    });
  }
}
