import {Component, EventEmitter, Input, Output} from '@angular/core';
import { Route } from '../map/Route';
import {LatLng} from '../map/LatLng';

@Component({
  selector: 'app-route-list',
  templateUrl: './route-list.component.html',
  styleUrls: [ './route-list.component.css' ]
})
export class RouteListComponent {

  @Input() routes: Array<Route> = [];
  @Input() focusedRoute: LatLng;
  @Output() removedRoute = new EventEmitter();

  removeRoute(event, route: Route) {
    const index = this.routes.indexOf(route, 0);
    this.routes.splice(index, 1);
    this.removedRoute.emit(index);
  }

  selectRoute(route: Route) {
    this.focusedRoute = route.center;
  }
}
