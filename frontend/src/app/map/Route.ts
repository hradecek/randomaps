import {LatLng} from './LatLng';

export class Route {
  points: Array<LatLng>;
  center: LatLng;
  start: LatLng;
  end: LatLng;
  color: string;
  weight: number;

  constructor(points: Array<LatLng>) {
    this.points = points;
    this.start = points[0];
    this.end = points[points.length - 1];
    this.center = points[Math.floor(points.length / 2)];
    this.weight = 1;
  }
}
