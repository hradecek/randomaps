import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: [ './map.component.css' ]
})
export class MapComponent {

  lat: number;
  lng: number;

  polylines: Array<any> = [];

  constructor(private client: HttpClient) { }

  randomJourney() {
    this.client.get('http://localhost:8080/random').subscribe(data => {
      // Center
      const start = data['polyline'][0];
      this.lat = start.lat;
      this.lng = start.lng;

      this.polylines.push(data);
    });
  }
}
