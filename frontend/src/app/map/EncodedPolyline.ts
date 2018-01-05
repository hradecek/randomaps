import { LatLng } from '@agm/core';
import * as PolylineUtil from '../../../node_modules/polyline-encoded';

export class EncodedPolyline {

  points: string;

  static encode(points: Array<LatLng>): string {
    const latLngs = points.map(latLng => {
      return [latLng.lat, latLng.lng];
    });

    return PolylineUtil.encode(latLngs);
  }

  static decode(points: string): Array<LatLng> {
    return PolylineUtil.decode(points);
  }

  constructor(points: Array<LatLng>) {
    this.points = EncodedPolyline.encode(points);
  }
}
