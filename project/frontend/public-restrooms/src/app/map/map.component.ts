import { Component, OnInit } from '@angular/core';
import { MapService } from './map.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
  providers: [MapService]
})
export class MapComponent{
  lat: number = 51.678418;
  lng: number = 7.809007;

  constructor(private map: MapService) {
    map.getNearbyRestrooms();
  }
}
