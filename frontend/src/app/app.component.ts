import { Component } from '@angular/core';
import { EventBusService } from '../shared/event-bus.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: [ './app.component.css' ]
})
export class AppComponent {

  constructor(private eb: EventBusService) {
    eb.connect('http://localhost:8080/eventbus');
    eb.registerHandler('random', (err, msg) => {
      console.log('Location: ' + msg.body.lat + ', ' + msg.body.lng);
      console.log(msg);
    });
  }
}
