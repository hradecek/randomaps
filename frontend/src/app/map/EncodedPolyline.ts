import * as PolylineUtil from '../../../node_modules/polyline-encoded';
import {LatLng} from "./LatLng";

export class EncodedPolyline {

  points: string;

  static encode(points: Array<LatLng>): string {
    const latLngs = points.map(latLng => [latLng.lat, latLng.lng]);

    return PolylineUtil.encode(latLngs);
  }

  static decode(points: string): Array<LatLng> {
    return PolylineUtil.decode(points).map(latLng => {
      return {lat: latLng[0], lng: latLng[1]};
    });
  }

  constructor(points: Array<LatLng>) {
    this.points = EncodedPolyline.encode(points);
  }
}
