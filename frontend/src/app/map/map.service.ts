import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EncodedPolyline } from './EncodedPolyline';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';

@Injectable()
export class MapService {

  constructor(private client: HttpClient) { }

  randomJourney(): Observable<EncodedPolyline> {
    return this.client.get('http://localhost:8080/random').map(data => new EncodedPolyline(data['polyline']));
  }

  realtimeJourney() {
    return this.client.get('http://localhost:8080/realtime').subscribe();
  }
}
