import {Component, OnChanges} from '@angular/core';
const EventBus = require('vertx3-eventbus-client/vertx-eventbus.js');

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html'
})
export class MapComponent {
  eventBus = new EventBus('http://localhost:8080/eventbus');
  messages = ["ahoj", "ahoj2", "ahoj3"];
}
