import { LatLng } from '@agm/core';
import * as PolylineUtil from '../../../node_modules/polyline-encoded';

export class EncodedPolyline {
  points: string;

  constructor(points: Array<LatLng>) {
    this.points = this.encode(points);
  }

  encode(points: Array<LatLng>) {
    const latLngs = points.map(latLng => {
      return [latLng.lat, latLng.lng];
    });

    return PolylineUtil.encode(latLngs);
  }

  decode(): Array<LatLng> {
    return PolylineUtil.decode(this.points);
  }
}
