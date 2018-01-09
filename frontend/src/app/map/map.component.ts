import { Component, Input } from '@angular/core';
import { MapService } from './map.service';
import { LatLng } from './LatLng';
import { Route } from './Route';
import { EventBusService } from '../../shared/event-bus.service';
import { EncodedPolyline } from './EncodedPolyline';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: [ './map.component.css' ]
})
export class MapComponent {

  defaultFocus = new LatLng(48.733333,  18.916667);
  eventBusUrl = 'http://localhost:8080/eventbus';

  @Input() focusedRoute: LatLng;
  @Input() routes: Array<Route> = [];

  static getRandomRgbColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  constructor(private mapService: MapService, private eventBus: EventBusService) {
    this.focusedRoute = this.defaultFocus;
    this.eventBus.connect(this.eventBusUrl);
    this.eventBus.registerHandler('random', (err, msg) => {
      if (msg == null) {
        console.log(msg);
        return;
      }

      const routeToUpdate = this.routes.find(route => route.id === msg.body.id);
      const newPoint = new LatLng(msg.body.lat, msg.body.lng);
      if (routeToUpdate != null) {
        routeToUpdate.points.push(newPoint);
      } else {
        const newRoute = new Route(new EncodedPolyline([newPoint]));
        newRoute.color = MapComponent.getRandomRgbColor();
        newRoute.id = msg.body.id;
        this.focusedRoute = newRoute.start;
        this.routes.push(newRoute);
      }
    });
  }

  addRandomRoute() {
    this.mapService.randomJourney().subscribe(encoded => {
      const route = new Route(encoded);
      route.color = MapComponent.getRandomRgbColor();

      this.focusedRoute = route.center;
      this.routes.push(route);
    });
  }

  addRandomRealTimeRoute() {
    this.mapService.realtimeJourney();
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
