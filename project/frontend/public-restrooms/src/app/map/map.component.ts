import { Component, OnInit } from '@angular/core';
import { MapService } from './map.service';
import { RestRoom } from '../code/restRoom';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
  providers: [MapService]
})
export class MapComponent{
  userPosition = [44.063638, 12.563860999999974];
  restRooms: RestRoom[];

  constructor(private map: MapService) {
    map.getNearbyRestrooms().subscribe((restRooms:RestRoom[]) => this.restRooms = restRooms);
    
    navigator.geolocation.getCurrentPosition((position) => {
      this.userPosition[0] = position.coords.latitude;
      this.userPosition[1] = position.coords.longitude;
    });
  }
}
