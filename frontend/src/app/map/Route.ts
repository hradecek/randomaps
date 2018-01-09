import {LatLng} from './LatLng';
import {EncodedPolyline} from './EncodedPolyline';

export class Route {
  id: string;
  // encoded: string;
  points: Array<LatLng>;
  center: LatLng;
  start: LatLng;
  end: LatLng;
  color: string;
  weight: number;

  constructor(encoded: EncodedPolyline) {
    const points = EncodedPolyline.decode(encoded.points);
    this.points = points;
    this.start = points[0];
    this.end = points[points.length - 1];
    this.center = points[Math.floor(points.length / 2)];
    this.weight = 1;
    // this.encoded = encoded.points;
    this.id = encoded.points;
  }
}
