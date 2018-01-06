import { Component, Input } from '@angular/core';
import { Route } from '../map/Route';

@Component({
  selector: 'app-route-list',
  templateUrl: './route-list.component.html'
})
export class RouteListComponent {

  @Input() routes: Array<Route> = [];

  removeRoute(event, route: Route) {
    const index = this.routes.indexOf(route, 0);
    this.routes.splice(index, 1);
  }
}
