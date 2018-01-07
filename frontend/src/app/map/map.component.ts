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

  defaultFocus = new LatLng(48.733333,  18.916667);

  @Input() focusedRoute: LatLng;
  @Input() routes: Array<Route> = [];

  constructor(private mapService: MapService) {
    this.focusedRoute = this.defaultFocus;
  }

  addRandomRoute() {
    this.mapService.randomJourney().subscribe(encoded => {
      const decoded = EncodedPolyline.decode(encoded.points);
      const route = new Route(decoded);
      route.color = this.getRandomColor();

      this.focusedRoute = route.center;
      this.routes.push(route);
    });
  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }


  refocus(index) {
    if (this.routes.length !== 0) {
      const next = (index === 0) ? 0 : index - 1;
      this.focusedRoute = this.routes[next].center;
    } else {
      this.focusedRoute = this.defaultFocus;
    }
  }
}
