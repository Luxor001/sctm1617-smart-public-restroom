import { Component, OnInit } from '@angular/core';
import { MapService } from './map.service';
import { RestRoom } from '../code/restRoom';
import { timer } from 'rxjs';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
  providers: [MapService]
})
export class MapComponent implements OnInit {
  userPosition = [44.063638, 12.563860999999974];
  restRooms: RestRoom[];
  lastRestRoomSelected: RestRoom;

  constructor(private mapService: MapService) {

    navigator.geolocation.getCurrentPosition((position) => {
      this.userPosition[0] = position.coords.latitude;
      this.userPosition[1] = position.coords.longitude;
    });
  }

  ngOnInit() {
    timer(0, 15000).subscribe(() => {
      this.mapService.getNearbyRestrooms().subscribe((restRooms: RestRoom[]) => {
        this.restRooms = restRooms;
      });
    });
  }

  restRoomSelected(restRoomSelected: RestRoom) {
    this.lastRestRoomSelected = restRoomSelected;
  }
}
